package com.adyen.report.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.codec.EncoderException;

import com.adyen.report.entities.ReportLocation;
import com.adyen.report.entities.ReportTypes;
import com.adyen.report.gui.Controller;

public class ConnectionInfoTest {
	private static String merchant = "TestMerchant";
	private static String company = "TestCompany";
	private static String username = "report@Company.TestCompany";
	private static String password = "s5fruGN2bn2kGaDF4bN-FPP";
	private static String path = "/home/bjorn/Documents/Developing/Downloads-Reporting";

	private static boolean isMerchant = true;
	private static Date startDate;
	private static Date endDate;
	
	private static FileWriter write ;
	private static BufferedWriter bw ;
	private static File f;
	private static File f2;
	static ArrayList<ReportLocation> r;
	
	private static boolean settlementDetailReport = true;
	public static void main (String args []) throws EncoderException, MalformedURLException, IOException, ParseException{
//		ConnectionInfo connection = new ConnectionInfo(merchant,"", username, password, isMerchant,"/home/bjorn/Documents/Developing/Downloads-Reporting");
//		connection.adyenConnection(connection.getURLBase(),true);
//		ArrayList<ReportLocation> r = connection.getPossibleBatches("settlement");
//		System.out.println(r.toString());
//		
//
		Controller ctrl = new Controller(isMerchant, merchant, username, password.toCharArray(), ReportTypes.SETTLEMENT_DETAIL);
//		Controller ctrl2 = new Controller(isMerchant, merchant, username+"invalidUsername", password.toCharArray(), true);
//		Controller ctrl3 = new Controller(isMerchant, merchant, username, (password+"invalidPassword").toCharArray(), true);
//		Controller ctrl4 = new Controller(isMerchant, merchant+"invalidMerchant", username, password.toCharArray(), true);
//		Controller ctrl5 = new Controller(false, company+"invalidCompany", username, password.toCharArray(), false);

		
//		ArrayList<ReportLocation> r = ctrl.connect(path+"/");
//		ArrayList<ReportLocation> r2 = ctrl.connect(merchant+"error", username, password, isMerchant, path); //will not catch error on username
//		ArrayList<ReportLocation> r3 = ctrl.connect(merchant, username+"error", password, isMerchant, path);
//		ArrayList<ReportLocation> r4 = ctrl.connect(merchant, username, password+"error", isMerchant, path);

		//Creating a list of reports, because selected reports now are a list of reports directly
		ArrayList<ReportLocation> selectedReports = new ArrayList<ReportLocation>();
		
		ReportLocation reportLoc1 = null;
		ReportLocation reportLoc2 = null;
		ReportLocation reportLoc3 = null;
		try {
			reportLoc1 = new ReportLocation("settlement_detail_report_batch_121.csv",
					ReportTypes.SETTLEMENT_DETAIL, new URL("https://www"), "2012-12-05",
					"242 bytes");
			reportLoc2 = new ReportLocation("settlement_detail_report_batch_122.csv",
					ReportTypes.SETTLEMENT_DETAIL, new URL("https://www"), "2012-12-06",
					"542 bytes");
			reportLoc3 = new ReportLocation("settlement_detail_report_batch_123.csv",
					ReportTypes.SETTLEMENT_DETAIL, new URL("https://www"), "2012-12-07",
					"742 bytes");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		selectedReports = new ArrayList<ReportLocation>();
		selectedReports.add(reportLoc1);
		selectedReports.add(reportLoc2);
		selectedReports.add(reportLoc3);
		
		int[] selected = {1,2,3,4,46}; //list of the reports(name) that has been selected.

		
		//will merge
//		ctrl.getSelectedReports(selectedReports, true, "settlement");
		//will not merge
//		ctrl.getSelectedReports(selectedReports,false, false, "settlement");
//		ctrl.getSelectedDateRange(startDate, endDate, true, filename);
		DateFormat formatter = new SimpleDateFormat("dd/MM/yy");
		Controller cont = new Controller(true, "eDarling","report_347138@Company.Jade853","bZCIM89P=F[>}(JgyjJ6NP<dg".toCharArray(),ReportTypes.SETTLEMENT_DETAIL);
		ArrayList<ReportLocation> r2 = cont.connect(path+"/");
		
		
//		System.out.println("R: "+r.toString());
		System.out.println("R2: "+r2.toString());
		Date de = new SimpleDateFormat("yyyy-MM-dd").parse("2013-06-02");
		Date ds = new SimpleDateFormat("yyyy-MM-dd").parse("2013-03-29");
		
		System.out.println(cont.getSelectedDateRange(ds, de, false, false, "testfile_received"));
//		System.out.println("R2: "+ctrl2);
//		System.out.println("R3: "+ctrl3);
//		System.out.println("R4: "+ctrl4);
//		System.out.println("R5: "+ctrl5);
////		int[] selected = {1,2,3,4,46}; //list of the reports(name) that has been selected.
//		
//		//will merge
////		ctrl.getSelectedReports(selected, true, "settlement");
//		//will not merge
//		ctrl.getSelectedReports(selected, false, "settlement");
////		ctrl.getSelectedDateRange(startDate, endDate, true, filename);
//		DateFormat formatter = new SimpleDateFormat("dd/MM/yy");
//		
//		try {
//			endDate = formatter.parse("02/05/13");
//			startDate = formatter.parse("30/04/13");
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		System.out.println(startDate);
//		System.out.println(endDate);
//		
//		ctrl.getSelectedBatchNumber(2,5,true,"settlement_test");
		//test for date range
//		System.out.println(ctrl.getSelectedDateRange(startDate, endDate, true, "settlement"));
//		System.out.println(ctrl.getTotalSize(selected));
		
		
//		ctrl.getSelectedDateRange(startDate, endDate)
	
	}

}





















