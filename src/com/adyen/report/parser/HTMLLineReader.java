package com.adyen.report.parser;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.adyen.report.connector.ConnectionInfo;
import com.adyen.report.entities.ReportLocation;
import com.adyen.report.entities.ReportTypes;
import com.adyen.report.gui.Controller;

/**
 * @author cristina
 *
 */
/**
 * @author cristina
 * 
 */
public class HTMLLineReader {

	private InputStreamReader reader;
	private ReportTypes selectedReportType;
	private URL urlBase;
	private static FileHandler fh = Controller.getFileHandler();
	private final static Logger LOGGER = Logger.getLogger(HTMLLineReader.class.getName());

	/**
	 * @param reader
	 * @param reportType
	 *            : "settlement" or "received_payments"
	 */
	public HTMLLineReader(InputStream inputStream, ReportTypes selectedReportType,
			URL urlBase) {
		
		if (reader != null) {
			this.reader = new InputStreamReader(inputStream);

		}
		this.selectedReportType = selectedReportType;
		this.urlBase = urlBase;
	}

	/**
	 * This method will read the reports web page and will create the
	 * ReportLocation objects related with the attribute reportType
	 * 
	 * @return All the reports related with the report type desired
	 */
	public ArrayList<ReportLocation> getReportDetails(String tmpHTML) {
		// TODO Auto-generated method stub

		// Create the ArrayList to include all the objects
		ArrayList<ReportLocation> reportsLocationInfo = new ArrayList<ReportLocation>();

		// Open the file
		FileInputStream fileStream;
		File f;
		try {
			int reportNumber = 0;
			boolean previousWasReportName = false;
			boolean previousWasDate = false;

			f = new File(tmpHTML);
			if (!f.exists()) {
				return reportsLocationInfo;
			}
			fileStream = new FileInputStream(f);

			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fileStream);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(in));

			String strLine;

			// Line to line it's checked if there is related information with
			// the reporType
			while ((strLine = bufferedReader.readLine()) != null) {
				// Print the content on the console

				// The report name it's extracted if it's in this line
				String info = getBatchInformation(strLine);

				if (strLine.contains("Error")) {
					ReportTypes errorReport = ReportTypes.ERROR_INVALID_CREDENTIALS;
					ReportLocation report = new ReportLocation(errorReport);
					reportsLocationInfo.add(report);
				}else{
				
					// If the line has information related with the reports we are
					// looking for we create a report object to add in the list
					if (!info.equals("")) {
						reportNumber++;
						ReportLocation report = new ReportLocation(info, selectedReportType, urlBase);
						reportsLocationInfo.add(report);
						// System.out.println(reportNumber+". - "+"\nReport name: "+report.getReportName()+"\nReport type: "+report.getReportType());
						previousWasReportName = true;
	
					} else{ 
						
						// Is checked if the previous line was report information
						// (and the report was created)
						if (previousWasReportName) {
							String date = getReportDate(strLine);
							ReportLocation tmpReport = reportsLocationInfo
									.get(reportNumber - 1);
							tmpReport.setCreationDate(date);
							reportsLocationInfo.set(reportNumber - 1, tmpReport);
							previousWasReportName = false;
							previousWasDate = true;
						} else {
							// If the previous was date the following information
							// has to be got it's the size (useful to provide an
							// overview to the
							// Merchant regarding the time they will be required to
							// generate the merged report)
							if (previousWasDate) {
								String size = getReportSize(strLine);
								ReportLocation tmpReport = reportsLocationInfo
										.get(reportNumber - 1);
								tmpReport.setSize(size);
								reportsLocationInfo
										.set(reportNumber - 1, tmpReport);
								previousWasDate = false;
							}
						}
					
					}
				}
			}

			// Close the input stream

			in.close();
			f.delete();
			// System.out.println("I can provissionary stop removing the file reports.html in: HTMLLineReader - getReportDetails - f.delete()");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.addHandler(fh);
			LOGGER.severe(e.getMessage());
		}

		return getReversed(reportsLocationInfo);
	}
	/**
	 * Used to reverse the table, so that it's show in correct order in the GUI.
	 * @param original
	 * @return
	 */
	public ArrayList<ReportLocation> getReversed(
			ArrayList<ReportLocation> original) {
		ArrayList<ReportLocation> copy = new ArrayList<ReportLocation>(original);
		Collections.reverse(copy);
		return copy;
	}
/**
 * Gets the size of the reports.
 * @param htmlLine
 * @return
 */
	private String getReportSize(String htmlLine) {
		String reportSize = "";
		String aux = "";

		Pattern p = Pattern.compile("<td (.*)</td>", Pattern.DOTALL);

		Matcher matcher = p.matcher(htmlLine);

		if (matcher.find()) {
			// I will split the string in several parts
			Pattern spliting1 = Pattern.compile(">", Pattern.LITERAL);
			String[] htmlWords1 = spliting1.split(htmlLine);
			aux = htmlWords1[1];
			spliting1 = Pattern.compile("<", Pattern.LITERAL);
			String[] htmlWords2 = spliting1.split(aux);
			reportSize = htmlWords2[0];

			// System.out.println("This batch size is: "+reportSize);
		}
		return reportSize;
	}

	/**
	 * @param htmlLine
	 *            This is one of the lines of the html file that contains the
	 *            date
	 * @return the date contained on the line
	 */
	private String getReportDate(String htmlLine) {
		String reportDate = "";
		String aux = "";

		Pattern p = Pattern.compile("<td>(.*)</td>", Pattern.DOTALL);

		Matcher matcher = p.matcher(htmlLine);

		if (matcher.find()) {
			// I will split the string in several parts
			Pattern spliting1 = Pattern.compile(">", Pattern.LITERAL);
			String[] htmlWords1 = spliting1.split(htmlLine);
			aux = htmlWords1[1];
			spliting1 = Pattern.compile("<", Pattern.LITERAL);
			String[] htmlWords2 = spliting1.split(aux);
			reportDate = htmlWords2[0];

			// System.out.println("This batch date is: "+reportDate);
		}
		return reportDate;
	}

	/**
	 * @param htmlLine
	 *            This is one of the html file with all the report links
	 * @return the valid information of the line
	 */
	public String getBatchInformation(String htmlLine) {
		String reportInfo = "";

		Pattern p = Pattern.compile("<td><a href=\"" + selectedReportType.getReportFileNameOrErrorDescription() + "(.*)\">"
				+ selectedReportType.getReportFileNameOrErrorDescription() + "(.*)", Pattern.DOTALL);

		Matcher matcher = p.matcher(htmlLine);

		if (matcher.find()) {
			// I will split the string in several parts
			Pattern spliting = Pattern.compile("\"", Pattern.LITERAL);
			String[] htmlWords = spliting.split(htmlLine);
			reportInfo = htmlWords[1];

//			System.out.println("This batch name is: " + reportInfo);
		}
		return reportInfo;
	}

	/**
	 * @param line
	 *            This line has to be checked to see if it has a report name
	 *            related with the report type - NOT WILL BE DONE YET
	 * @return The batch number
	 */
	public Integer checkLineBatchNumber(String line) {
		Integer batchNumber = null;

		// Pattern p = Pattern.compile(
		// "<td><a href=\"settlement_(.*)\">settlement(.*)",
		// Pattern.DOTALL
		// );
		//
		// Matcher matcher = p.matcher(
		// line
		// );
		// int start = line.indexOf("batch_")+6;
		// System.out.println("The first batch number will be in the possition :"+start+"for the line:"+line);
		// int end = line.indexOf(".csv");
		// if (matcher.find()){
		// String batchString = line.substring(start, end);
		// for (int po=start;po<end;po++){
		//
		// }
		// }
		return batchNumber;
	}

	public void readingDocument() {

		// //reading file line by line in Java using BufferedReader
		// // FileInputStream fis = null;
		// BufferedReader bufReader = null;
		// ArrayList <Integer> batches = new ArrayList<Integer>();
		// try {
		// //fis = new FileInputStream("C:/sample.txt");
		// bufReader = new BufferedReader(reader);
		//
		// System.out.println("Reading File line by line using BufferedReader");
		//
		// String line = bufReader.readLine();
		// while(line != null){
		// System.out.println(line);
		// line = bufReader.readLine();
		// batches.add(checkLineBatchNumber(line));
		// }
		//
		//
		// // } catch (FileNotFoundException ex) {
		// //
		// Logger.getLogger(BufferedReaderExample.class.getName()).log(Level.SEVERE,
		// null, ex);
		// // } catch (IOException ex) {
		// //
		// Logger.getLogger(BufferedReaderExample.class.getName()).log(Level.SEVERE,
		// null, ex);
		// //
		// // } finally {
		// // try {
		// // reader.close();
		// // fis.close();
		// } catch (IOException ex) {
		// //
		// Logger.getLogger(BufferedReaderExample.class.getName()).log(Level.SEVERE,
		// null, ex);
		// // }
		// }
		//
		// //Read more:
		// http://javarevisited.blogspot.com/2012/07/read-file-line-by-line-java-example-scanner.html#ixzz2SW1D4cBn
	}
}