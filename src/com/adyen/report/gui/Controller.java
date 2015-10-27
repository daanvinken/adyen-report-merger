package com.adyen.report.gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.commons.codec.EncoderException;







import com.adyen.report.connector.ConnectionInfo;
import com.adyen.report.entities.ReportLocation;
import com.adyen.report.entities.ReportTypes;
import com.adyen.report.parser.CSVReaderWriter;


public class Controller {
	private String merchant;
	private String company;
	private String username;
	private String password; // do not store this one?
	private int startBatch;
	private int endBatch;

	private boolean isMerchant = true;

	private double depositCorrection = 0;
	private double totalFee = 0;
	private String startDate;
	private String endDate;

	private FileWriter write;
	private BufferedWriter bw;
	private File f;
	static LogFileHandler log = new LogFileHandler();
	
	private static String path = Interface.getPath();
	public static FileHandler fileHandler = log.fileHandler(path);
	private final static Logger LOGGER = Logger.getLogger(Controller.class.getName());
	
	

	private ArrayList<ReportLocation> r;
	CSVReaderWriter csv = new CSVReaderWriter();
	ArrayList<File> tmpList = new ArrayList<File>();
	LinkedHashMap<String, List<String>> mergedCsv = new LinkedHashMap<String, List<String>>();
	 
	
	
	public ArrayList<ReportLocation> getR() {
		return r;
	}

	public void setR(ArrayList<ReportLocation> r) {
		this.r = new ArrayList<ReportLocation>(r);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	
	private ReportTypes selectedReportType;
	private ArrayList<ReportLocation> selectedReports;
	private ConnectionInfo connection;

	public Controller(boolean isMerchant, String companyMerchantName,
			String username, char[] password, ReportTypes reportType) {
		this.isMerchant = isMerchant;
		if (isMerchant) {
			this.merchant = companyMerchantName;
			this.company = "";
		} else {
			this.company = companyMerchantName;
			this.merchant = "";
		}
		this.username = username;
		this.password = new String(password);
		selectedReportType = reportType;

		
//		System.out.println("controller created with data "
//				+ companyMerchantName + " " + username + "  " + this.password
//				+ "  ");
	}

	
	
	/**
	 * Generates the footer for the report. Boolean to decide which report to be
	 * generated. If settlementDetailReport is selected, the deposit correction
	 * will be generated. Total fee is also calculated.
	 * This function is deprecated.
	 * @param settlementDetailReport
	 * @return
	 */
	private String generateEndLine(boolean settlementDetailReport) {
		String fee = "";
		// "TestCompany,TestMerchant,,,,2012-06-05 12:46:55,CEST,DepositCorrection,Deposit Correction,EUR,,,,EUR,1.02,,,,,,,,,,,,,,,";
		Date date = new Date();
		if (totalFee > 0) {
			fee = company + "," + merchant + "," + ",,," + date.toString()
					+ ",CEST,Fee,Total Fee,EUR,,,,EUR," + totalFee
					+ ",,,,,,,,,,,,,,,\n";
		}
		if (settlementDetailReport) {
			return fee + company + "," + merchant + "," + ",,,"
					+ date.toString()
					+ ",CEST,DepositCorrection,Deposit Correction,EUR,,,,EUR,"
					+ depositCorrection + ",,,,,,,,,,,,,,,";
		}

		else
			return "";

	}

	/**
	 * Checks if file exists and deletes if it exists and establishes a
	 * filewriter and bufferedwriter.
	 */
	private void openBW(String filename) {
		try {
			//
			f = new File(path + filename + ".csv");

			if (f.exists()) {
				f.delete();
			}
			write = new FileWriter(f, true);
			bw = new BufferedWriter(write);

		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.addHandler(fileHandler);
			LOGGER.severe(e.getMessage());

		}
		
		;
	}


	/**
	 * closes the bufferedwriter and filewriter.
	 */
	private void closeBW() {
		try {
			bw.close();
			write.close();
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.addHandler(fileHandler);
			LOGGER.severe(e.getMessage());
		}

	}
	
	
	/**
	 * It downloads the selected reports and merges them into one file. This
	 * list is created from the selected batches list. The downloaded file is
	 * stored temp. and is deleted when the file has been parsed. If the doMerge
	 * is true the reports will be merged, and reports will be deleted
	 * afterwards. If not, reports will be downloaded. filename specifies the
	 * filename without fileformat.
	 * 
	 * i.e filename should be 'settlement' and NOT 'settlement.csv'
	 * 
	 * @param selected
	 * @return
	 */
	/*
	 * TODO We can merge these report generations into one. I do not know why we
	 * have 3 of them, when we should have only two? In theory we can merge them
	 * into one. I will look into this.
	 */
	public ArrayList<ReportLocation> getSelectedReports(
			ArrayList<ReportLocation> selectedRep, boolean deleteLocalFiles,
			boolean doMerge, String filename) {
		selectedReports = new ArrayList<ReportLocation>(selectedRep);
//		System.out.println("getSelectedReports");
		
		if (doMerge) {
			openBW(filename);
			//test generating headers generic//
//			System.out.println(selectedRep.size());
			for (ReportLocation rl : selectedRep) {
				try {
					connection.adyenConnection(new URL(rl.getReportDownloadURL()
							+ rl.getReportName()), false, rl);
//					System.out.println(rl.getReportName());
				} catch (MalformedURLException e) {
					LOGGER.addHandler(fileHandler);
					LOGGER.severe(e.getMessage());
				} catch (IOException e) {
					LOGGER.addHandler(fileHandler);
					LOGGER.severe(e.getMessage());
				} catch (EncoderException e) {
					LOGGER.addHandler(fileHandler);
					LOGGER.severe(e.getMessage());
				}
				File tmp = new File(path + rl.getReportName());
				
				tmpList.add(tmp);
			}
			
			List<String> headerList = csv.headersfromLastFile(tmpList);
			LinkedHashMap<String, List<String>> mergeList = csv.startCsvFileMerge(headerList);
			
			for (File file : tmpList){
//				
				ArrayList<List<String>> valList = csv.csvValueReader(file);
				ArrayList<List<String>> valListEven = csv.csvEvenColumns(valList);
				LinkedHashMap<String, List<String>> valListEvenMapped =csv.csvMapFile(valListEven);
				mergedCsv = csv.fillCsvFileMerge(mergeList, valListEvenMapped);

				if (deleteLocalFiles) {
					file.delete();
				
				}
			}
			
//			System.out.println(csv.writeCsv(mergedCsv).toString());
			
				try {
					bw.write(csv.writeCsv(mergedCsv).toString());

					
				} catch (IOException e) {
					LOGGER.addHandler(fileHandler);
					LOGGER.severe(e.getMessage());
				}

		}
		


		if (doMerge) {
			closeBW();
			mergedCsv = new LinkedHashMap<String, List<String>>();
			tmpList = new ArrayList<File>();
		}
		return selectedReports;

	}

	/**
	 * getAvailableReportsForDateRange try to parse the Strings as dates and
	 * check if is possible getting reports or there is any error
	 */
	public ArrayList<ReportLocation> getAvailableReportsForDateRange(
			
			Date startDate, Date endDate) {
//		System.out.println("getAvailableReportsForDateRange");
		ArrayList<ReportLocation> reportsAvailableReturn = new ArrayList<ReportLocation>();

		if (!endDate.before(startDate)) {
			for (ReportLocation rl : r) {
				if (isWithinRange(rl.getCreationDate(), startDate, endDate)) {
					reportsAvailableReturn.add(rl);
				}
			}
			if (reportsAvailableReturn.isEmpty()) {
				ReportLocation errorReport = new ReportLocation(ReportTypes.ERROR_NO_REPORTS_PERIOD);
				reportsAvailableReturn.add(errorReport);
			}
		} else {
			ReportLocation errorReport = new ReportLocation(ReportTypes.ERROR_ENDDATE_BEFORE_STARTDATE);
			reportsAvailableReturn.add(errorReport);
		}
		// } catch (ParseException e1) {
		// System.out.println("Check Date. Error parsing-I AM IN CONTROLLER.");
		// ReportLocation errorReport = new
		// ReportLocation("Check Date. Error parsing.","error");
		// reportsAvailableReturn.add(errorReport);
		// }

		return reportsAvailableReturn;
	}

	public ArrayList<ReportLocation> getSelectedBatchNumber(int start, int end,
			boolean doMerge, boolean deleteLocalFiles, String filename) {
		
		selectedReports = new ArrayList<ReportLocation>();
//		System.out.println("getSelectedBatchNumber");
		if (doMerge) {
			openBW(filename);
		}
		
		for (ReportLocation rl : r) {
			if (rl.getBatchNumber() >= start && end >= rl.getBatchNumber()) {
				selectedReports.add(rl);
				try {
					connection.adyenConnection(
							new URL(rl.getReportDownloadURL()
									+ rl.getReportName()), false, rl);
					File tmp = new File(path + rl.getReportName());
					
					tmpList.add(tmp);
					
					List<String> headerList = csv.headersfromLastFile(tmpList);
					LinkedHashMap<String, List<String>> mergeList = csv.startCsvFileMerge(headerList);
					
					for (File file : tmpList){
//						
						ArrayList<List<String>> valList = csv.csvValueReader(file);
						ArrayList<List<String>> valListEven = csv.csvEvenColumns(valList);
						LinkedHashMap<String, List<String>> valListEvenMapped =csv.csvMapFile(valListEven);
						mergedCsv = csv.fillCsvFileMerge(mergeList, valListEvenMapped);
						
						if (deleteLocalFiles) {
							if(file.delete()){
							}
						}
					}
				
					
//					System.out.println(csv.writeCsv(mergedCsv).toString());
					
						try {
							bw.write(csv.writeCsv(mergedCsv).toString());
							
						} catch (IOException e) {
							LOGGER.addHandler(fileHandler);
							LOGGER.severe(e.getMessage());
						}
						
						
					if (deleteLocalFiles) {
						tmp.delete();
					}

				} catch (MalformedURLException e) {
					LOGGER.addHandler(fileHandler);
					LOGGER.severe(e.getMessage());
				} catch (IOException e) {
					LOGGER.addHandler(fileHandler);
					LOGGER.severe(e.getMessage());
				} catch (EncoderException e) {
					LOGGER.addHandler(fileHandler);
					LOGGER.severe(e.getMessage());
				}

			}
		}
		if (doMerge) {
			closeBW();
			mergedCsv = new LinkedHashMap<String, List<String>>();
			tmpList = new ArrayList<File>();
		}
		return selectedReports;
	}

	/**
	 * Downloads and merges the reports based on a date range.
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public ArrayList<ReportLocation> getSelectedDateRange(Date startDate,
			Date endDate, boolean doMerge, boolean deleteLocalFiles,
			String filename) {
		selectedReports = new ArrayList<ReportLocation>();
		
//		System.out.println("getSelectedDateRange");
		if (doMerge) {
			openBW(filename);

		}
		for (ReportLocation rl : r) {
			if (isWithinRange(rl.getCreationDate(), startDate, endDate)) {
				selectedReports.add(rl);
				try {
					connection.adyenConnection(
							new URL(rl.getReportDownloadURL()
									+ rl.getReportName()), false, rl);
					File tmp = new File(path + rl.getReportName());
					
					tmpList.add(tmp);
					
					
					if (deleteLocalFiles) {
						tmp.delete();
//						System.out.println("delteLocaLFiles in getSelectedDateRange ");
					}

				} catch (MalformedURLException e) {
					LOGGER.addHandler(fileHandler);
					LOGGER.severe(e.getMessage());
				} catch (IOException e) {
					LOGGER.addHandler(fileHandler);
					LOGGER.severe(e.getMessage());
				} catch (EncoderException e) {
					LOGGER.addHandler(fileHandler);
					LOGGER.severe(e.getMessage());
				}
				
			}
			
		}
		
		List<String> headerList = csv.headersfromLastFile(tmpList);
		LinkedHashMap<String, List<String>> mergeList = csv.startCsvFileMerge(headerList);
		
		for (File file : tmpList){
		
			ArrayList<List<String>> valList = csv.csvValueReader(file);
			ArrayList<List<String>> valListEven = csv.csvEvenColumns(valList);
			LinkedHashMap<String, List<String>> valListEvenMapped =csv.csvMapFile(valListEven);
			mergedCsv = csv.fillCsvFileMerge(mergeList, valListEvenMapped);

		}
		
		// if true, merges into one file, and deletes the old files.
		if (doMerge) {
			
			try {
				bw.write(csv.writeCsv(mergedCsv).toString());
				
			} catch (IOException e) {
				LOGGER.addHandler(fileHandler);
				LOGGER.severe(e.getMessage());
			}

		}

		
		if (doMerge) {

			closeBW();
			mergedCsv = new LinkedHashMap<String, List<String>>();
			tmpList = new ArrayList<File>();
		}
		return selectedReports;
	}

	/** 
	 * isWithinRange returns true if the first date is within the range 
	 * 
	 */
	public boolean isWithinRange(Date reportCreationDate, Date startDate,
			Date endDate) {
		boolean afterStartDate = !(reportCreationDate.before(startDate));
		boolean beforeEndDate = !(reportCreationDate.after(endDate));
//		System.out.println("Is " + reportCreationDate + " after " + startDate
//				+ " and before " + endDate + " ?");
		// If both true means it is in range
		return afterStartDate && beforeEndDate;
	}

	/**
	 * establishes a connection to the server. This is used to download the
	 * files, so they are available offline to be merged.
	 * 
	 * @param merchant
	 * @param username
	 * @param password
	 * @param isMerchant
	 * @param path
	 * @return
	 */
	public ArrayList<ReportLocation> connect(String path) {
		this.path = path;
		connection = new ConnectionInfo(merchant, "", username, password,
				isMerchant, path);

		connection.adyenConnection(connection.getURLBase(), true);

		r = connection.getPossibleBatches(selectedReportType);
//		System.out.println("I am in Controller and getting possible batches");

		return r;
	}

	/**
	 * Calculates the total size of the documents to be downloaded. Returned in
	 * bytes.
	 * 
	 * @param selected
	 * @return
	 */
	public static double getTotalSize(List<ReportLocation> selected) {
		double sum = 0;
		double value = 0;
		for (ReportLocation rl : selected) {
			if (!rl.getReportType().isError()) {
				// removes the non numerical characters from the string and gets
				// the
				// value of the string.
				value = adaptReportSize( Double.parseDouble(rl.getSize()
						.replaceAll("[^\\d.]", "")));
				sum += value;
			}

		}
		return sum;

	}
	
	private static double adaptReportSize(double reportSize){
		double reportSizeAdapted = reportSize;
		
		reportSizeAdapted = reportSizeAdapted * 4.8;
		
		return reportSizeAdapted;
	}

	/**
	 * getDate parse a string to a day or if it is empty assign a value
	 * depending if it is startDate,0, or endDate, 1.
	 */
	public static Date getDate(String dateString, int startOrEnd) {
		Date date = null;
		try {
			if (dateString.trim().compareToIgnoreCase("") == 0
					|| dateString.equals(null)) {
				if (startOrEnd == 0) {
					// date = new
					// SimpleDateFormat("MM/dd/yyyy kk:mm:ss").parse("01/01/2000"
					// + " 00:00:01");
					date = new SimpleDateFormat("MMMMMMMM dd, yyyy HH:mm:ss")
							.parse("January 1, 2000 00:00:00");
//					System.out.println("Start date is 01/01/2000");
				} else {
					// date = new
					// SimpleDateFormat("MM/dd/yyyy kk:mm:ss").parse("12/31/2040"
					// + " 23:59:59");
					date = new SimpleDateFormat("MMMMMMMM dd, yyyy HH:mm:ss")
							.parse("December 31, 2040 23:59:59");
//					System.out.println("End date is 12/31/2040");
				}
			} else {
				if (startOrEnd == 0) {
					// date = new
					// SimpleDateFormat("MM/dd/yyyy kk:mm:ss").parse(dateString
					// + " 00:00:01");
					date = new SimpleDateFormat("MMMMMMMM dd, yyyy HH:mm:ss")
							.parse(dateString + " 00:00:00");
//					System.out.println("Start date is " + dateString);
				} else {
					// date = new
					// SimpleDateFormat("MM/dd/yyyy kk:mm:ss").parse(dateString
					// + " 23:59:59");
					date = new SimpleDateFormat("MMMMMMMM dd, yyyy HH:mm:ss")
							.parse(dateString + " 23:59:59");
//					System.out.println("End date is " + dateString);
				}
			}

		} catch (ParseException e1) {
			LOGGER.addHandler(fileHandler);
			LOGGER.severe(e1.getMessage());
			
		}

		return date;
	}

	public static FileHandler getFileHandler() {
		return fileHandler;
	}

	public void setFileHandler(FileHandler fileHandler) {
		this.fileHandler = fileHandler;
	}

	
}
