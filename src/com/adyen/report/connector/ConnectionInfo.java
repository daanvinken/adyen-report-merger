package com.adyen.report.connector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Base64;
import com.adyen.report.entities.ReportLocation;
import com.adyen.report.entities.ReportTypes;
import com.adyen.report.gui.Controller;
import com.adyen.report.parser.HTMLLineReader;

/**
* This class is used to Connect to the Adyen subscription reports URL 
* creating the correspondent document 
*
* @author  adyen
* @since   2013-04-30 
*/
public class ConnectionInfo {
	private String merchantCode;
	private String companyCode;
	private String reportUsername;
	private String reportPassword;
	private boolean isMerchant;
	private String webpageStringURL;
	private String credentialsString;
	private String filename;
	private FileOutputStream writer;
	private String pathLocation; // Where we will save the downloaded documents,
									// it's needed for the final version?
	private URL urlBase; // This link will be created in base on the detailed
							// parameters (isMerchant and Company/Merchant code)
	private InputStream reader;
	private FileHandler fh = Controller.getFileHandler();
	private final static Logger LOGGER = Logger.getLogger(ConnectionInfo.class.getName());
	
	
	
	/**
	 *
	 *  
	 * @param merchantCode
	 * @param companyCode
	 * @param reportUsername
	 * @param reportPassword
	 * @param isMerchant
	 * @param pathLocation
	 */
	public ConnectionInfo(String merchantCode, String companyCode,
			String reportUsername, String reportPassword, boolean isMerchant,
			String pathLocation) {
		super();
		this.merchantCode = merchantCode;
		this.companyCode = companyCode;
		this.reportUsername = reportUsername;
		this.reportPassword = reportPassword;
		this.isMerchant = isMerchant;
		this.pathLocation = pathLocation;
		this.reader = null;
		this.filename = "";
		createURLBase();
	}

	/**
	 * 
	 * @param companyCode
	 * @param reportUsername
	 * @param reportPassword
	 * @param pathLocation
	 */
	public ConnectionInfo(String companyCode, String reportUsername,
			String reportPassword, String pathLocation) {
		super();
		this.merchantCode = "";
		this.isMerchant = false;
		this.companyCode = companyCode;
		this.reportUsername = reportUsername;
		this.reportPassword = reportPassword;
		this.pathLocation = pathLocation;
		this.reader = null;
		this.filename = "";
		createURLBase();
	
	}

	/**
	 * This method is used to get the base of the URL depending on if we will
	 * use the Merchant code or the Company code. The baseURL is appended to when specifying
	 * the download path
	 */
	private void createURLBase() {
		try {
			
			if (isMerchant) {
				this.urlBase = new URL(
						"https://ca-live.adyen.com/reports/download/MerchantAccount/"
								+ merchantCode + "/");
			} else {
				this.urlBase = new URL(
						"https://ca-live.adyen.com/reports/download/Company/"
								+ companyCode + "/");
				
				
			}
		} catch (MalformedURLException m) {
			m.printStackTrace();
			
			LOGGER.addHandler(fh);
			LOGGER.severe(m.getMessage());
			
		}
	}

	public URL getURLBase() {
		return urlBase;
	}
	/**
	 * Used to get the report. Since the file will always be found, the error handling is used mostly for internal debugging.
	 * However, this should be implemented in case a connection error occurs, that can cause the error to occur.
	 * 
	 * @param url
	 * @param isHTML
	 * @param rl
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws EncoderException
	 */
	// TODO test to see what happens if a report is not found..
	public void adyenConnection(URL url, boolean isHTML, ReportLocation rl)
			throws IOException, MalformedURLException, EncoderException {
		// /Creating credentials string
		filename = rl.getReportName();
		credentialsString = reportUsername + ":" + reportPassword;
		// Encoding
		byte[] encoding = Base64.encodeBase64(credentialsString.getBytes());
		credentialsString = new String(encoding);
		URL urlObject = url;
		// Creating the connection
		HttpsURLConnection urlConnection = null;
		try {
			urlConnection = ((HttpsURLConnection) urlObject.openConnection());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			LOGGER.addHandler(fh);
			LOGGER.severe(e1.getMessage());
		}
		urlConnection.setRequestProperty("Authorization", "Basic "
				+ credentialsString);
		urlConnection.setConnectTimeout(600000);
		// Establishing the connection

		int code = 0;
		try {
			urlConnection.connect();
			code = urlConnection.getResponseCode();
//			System.out.println(code);
			if (code == 200) {
				if (urlConnection.getInputStream() != null) {
					reader = urlConnection.getInputStream();
					// Creating the document
					createDocument(isHTML);
				} else {
//					System.out.println("EMPTY STREAM");
					filename = "401";

				}
			} else if (code == 401) {
//				System.out.println("INCORRECT LOGIN DETAILS");
			} else if (code == 404) {
//				System.out.println("PAGE NOT FOUND");
			}
		} catch (IOException e) {
			LOGGER.addHandler(fh);
			LOGGER.severe(e.getMessage());
		}

		// https://ca-live.adyen.com/reports/download/MerchantAccount/TestMerchant/exchange_rate_report_2013_05_01.csv

	}
/**
 *  Gets the HTML file where all reports are listed. Downloads and parses the file, and reports on errors that might occur.
 * @param url
 * @param isHTML
 * @return
 */
	public HttpsURLConnection adyenConnection(URL url, boolean isHTML) {
		// Creating credentials string
		credentialsString = reportUsername + ":" + reportPassword;
		// Encoding
		byte[] encoding = Base64.encodeBase64(credentialsString.getBytes());
		credentialsString = new String(encoding);
		URL urlObject = url;
		// Creating the connection
		HttpsURLConnection urlConnection = null;
		try {
			urlConnection = ((HttpsURLConnection) urlObject.openConnection());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			LOGGER.addHandler(fh);
			LOGGER.severe(e1.getMessage());
		}
		urlConnection.setRequestProperty("Authorization", "Basic "
				+ credentialsString);
		urlConnection.setConnectTimeout(600000);
		// Establishing the connection

		int code = 0;
		try {
			urlConnection.connect();
			code = urlConnection.getResponseCode();
//			System.out.println(code);
			if (code == 200) {
				if (urlConnection.getInputStream() != null) {
					reader = urlConnection.getInputStream();
					// Creating the document
					createDocument(isHTML);
				} else {
					System.out.println("EMPTY STREAM");

				}
			} else if (code == 401) {
//				System.out.println("INCORRECT LOGIN DETAILS");
				filename = "401";
			} else if (code == 404) {
//				System.out.println("PAGE NOT FOUND");
				filename = "404";
			}
		} catch (IOException e) {
			LOGGER.addHandler(fh);
			LOGGER.severe(e.getMessage());
		}

		// https://ca-live.adyen.com/reports/download/MerchantAccount/TestMerchant/exchange_rate_report_2013_05_01.csv
		return urlConnection;

	}

	/**
	 * This method create the document depending on your needs (csv or html)
	 */
	public void createDocument(boolean isHTML) throws IOException {

		if (!isHTML) {
			File tmpHTML = new File(pathLocation + "/" + filename);
			writer = new FileOutputStream(tmpHTML);
		}
		if (isHTML) {
			File tmpHTML = new File(pathLocation + "/reports.html");
			writer = new FileOutputStream(tmpHTML);
		}
		// Downloading the different required documents. Note that this will
		// only download a single file, from a single URL. Will have multiple
		// calls instead
		byte[] buffer = new byte[153600];
		int bytesRead = 0;
		while ((bytesRead = reader.read(buffer)) > 0) {
			writer.write(buffer, 0, bytesRead);
			buffer = new byte[153600];
		}
		writer.close();
		reader.close();
	}

	

	// throws the exception if file not found.
	// TODO add check to if its a company account or not
	/**
	 * Tool to generate a list of URLs needed for making the connection to
	 * download all the files. At this moment is merging at the same time the
	 * download is done. UNDER CONSTRUCTION. NOT USED.
	 * 
	 * @param webpageStringURL
	 * @param startBatch
	 * @param endBatch
	 * @param reportName
	 * @return
	 */
	public ArrayList<URL> getBatches(String merchant, int startBatch,
			int endBatch, String reportName) {
		ArrayList<URL> batches = new ArrayList<URL>();
		// as this report is only available on merchant level, we don't need
		// check for merchant/company
		webpageStringURL = "https://ca-live.adyen.com/reports/download/MerchantAccount/"
				+ merchantCode + "/";

		for (int i = startBatch; i < endBatch + 1; i++) {
			try {
				batches.add(new URL(webpageStringURL + reportName + "_" + i
						+ ".csv"));
			} catch (MalformedURLException e) {
				// TODO Handle the exceptions
				e.printStackTrace();
				LOGGER.addHandler(fh);
				LOGGER.severe(e.getMessage());
			}
		}
		filename = "settlement_batch_" + startBatch + "-" + endBatch;
		doesFileExist(filename);
		return batches;
	}

	/**
	 * Checks if a file exists, if it does, deletes the file. If not, do nothing
	 * as the lines will be added.
	 * 
	 * @throws FileNotFoundException
	 */
	private void doesFileExist(String filename) {
		File f = new File(pathLocation + filename + ".csv");

		if (f.exists()) {
			f.delete();
		}

	}

	/**
	 * Tool to get the possible batches if the Merge of Reports has been chosen
	 * by "batches".
	 * 
	 * @param kindOfReport
	 *            : can be "settlement" or "received"
	 * @return
	 */
	public ArrayList<ReportLocation> getPossibleBatches(ReportTypes selectedReportType) {
		ArrayList<ReportLocation> reportDetails = new ArrayList<ReportLocation>();
		if (filename.equals("401")){
			reportDetails.add(new ReportLocation(ReportTypes.ERROR_INVALID_CREDENTIALS));
//			System.out.println("I am in line 290 of ConnectionInfo creating the error report");
		}else if (filename.equals("404")){
			reportDetails.add(new ReportLocation(ReportTypes.ERROR_PAGE_NOT_FOUND));
		}else{
		
			HTMLLineReader htmlReader = new HTMLLineReader(reader, selectedReportType,urlBase);
			// Reading the content of the downloaded document when the connection
			// was done
	
			// File f = new File(pathLocation+filename+".csv");
			// if(f.exists()){
			reportDetails = htmlReader.getReportDetails(pathLocation + "reports.html");
	
			// }
		}
		return reportDetails;
	}

}
