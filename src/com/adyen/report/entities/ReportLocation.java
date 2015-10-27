package com.adyen.report.entities;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import com.adyen.report.connector.ConnectionInfo;
import com.adyen.report.gui.Controller;


/**
 * @author cristina
 * This class is used to save the report details - Name, URL and creation date
 */
public class ReportLocation {

	
	private ReportTypes reportType;// The enum defines the values it can take
	private String reportName;
	private URL reportDownloadURL;
	private Date creationDate;
	private String size;
	private int batchNumber; // under eval.
	private FileHandler fh = Controller.getFileHandler();
	private final static Logger LOGGER = Logger.getLogger(ReportLocation.class.getName());
	
	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        try {
			this.creationDate =  df.parse(creationDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.addHandler(fh);
			LOGGER.severe(e.getMessage());
		}
	}

	/**This constructor is used to control errors, the rest of the parameters are not required*/
	public ReportLocation (ReportTypes reportType){
		this.reportType = reportType;
		this.reportName = "Error";
	}
	
	public ReportLocation(String reportName, ReportTypes reportType, URL reportDownloadURL) {
		this.reportName = reportName;
		this.reportType = reportType;
		this.reportDownloadURL = reportDownloadURL;
		this.creationDate = new Date();
		this.batchNumber = (int) Double.parseDouble(reportName.replaceAll("[^\\d.,]", ""));
	
	}
	
	public ReportLocation(String reportName, ReportTypes reportType, URL reportDownloadURL, String creationDate, String size) {
		this.reportName = reportName;
		this.reportType = reportType;
		this.reportDownloadURL = reportDownloadURL;
//		this.batchNumber = (int) Double.parseDouble(reportName.replaceAll("[^\\d.,]", "")); //not needed as this is only used for revieved?
		//2013-05-21 07:59:54 CEST
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        try {
			this.creationDate =  df.parse(creationDate+" 00:00:00");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.addHandler(fh);
			LOGGER.severe(e.getMessage());
		}
		this.size = size;
		
	}

	public ReportTypes getReportType() {
		return reportType;
	}

	public void setReportType(ReportTypes reportType) {
		this.reportType = reportType;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public URL getReportDownloadURL() {
		return reportDownloadURL;
	}

	public void setReportDownloadURL(URL reportDownloadURL) {
		this.reportDownloadURL = reportDownloadURL;
	}
	
	public int getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(int batchNumber) {
		this.batchNumber = batchNumber;
	}

	public String toString(){	
		//return "The report name is: "+reportName+", the type is: "+reportType+", was created on: "+creationDate+", the size is: "+size+" and the URL to download it is: "+reportDownloadURL;
		return reportName;
	}
	
}
