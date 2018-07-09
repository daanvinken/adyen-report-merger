package com.adyen.reportMerger.runners;

import com.adyen.reportMerger.Util.ConnectionUtil;
import com.adyen.reportMerger.entities.ErrorTypes;
import com.adyen.reportMerger.entities.ReportLocation;
import com.adyen.reportMerger.gui.ErrorScreen;
import com.adyen.reportMerger.gui.ProgressIndicatorScreen;
import com.adyen.reportMerger.gui.ShowAvailableReportScreen;
import com.adyen.reportMerger.gui.StartScreen;

import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;




/**
 * Created by andrew on 9/21/16.
 */
public class Controller {


    //initiate startScreen
    ShowAvailableReportScreen showAvailableReportScreen;
    private static List<URL> urlList;
    private static List<HttpsURLConnection> httpsURLConnectionList;
    private List<ReportLocation> reportLocations;
    private static String accountCode;
    private static String credentialString;
    public static boolean newStyleMerge = true;
    public static boolean deleteFiles = true;
    public static boolean debugMode = true;

    private static String path ;
    public static ErrorTypes errorType;

    public static int maxMergeFileSizeBeforeWriteToDisk = 10485760; //10MB     increasing this does not improve the speed


    public Controller(){
        StartScreen.getInstance();
    }

    public void setStartScreenDataToController() {

        path = StartScreen.getInstance().getPath();

        if (StartScreen.getInstance().getReportLevel() != null) {
            switch (StartScreen.getInstance().getReportLevel()) {

                case MERCHANT: {
                    accountCode = StartScreen.getInstance().getMerchantCode().getText();
                }
                break;

                case COMPANY: {
                    accountCode = StartScreen.getInstance().getCompanyCode().getText();
                }
                break;

                case MULTIMERCHANT: {
                    accountCode = StartScreen.getInstance().getMerchantCode().getText();
                }
                break;
            }
        }


        urlList = ConnectionUtil.getUrlListBasedOnReportLevel( StartScreen.getInstance().getReportLevel(), accountCode );
        ProgressIndicatorScreen.getInstance().addInfoToTextArea("Create URL List..");
        for (URL url : urlList) {
            ProgressIndicatorScreen.getInstance().addInfoToTextArea(url.toString());

        }


        //Set Login Credentials for httpUrlConnection
//        userName = StartScreen.getInstance().getReportUserName().getText() + "@Company."+screen.getCompanyCode().getText();
//        passWord = StartScreen.getInstance().getPasswordField().getPassword();
        credentialString = ConnectionUtil.getCredentialString();

        //Create 1 or more Connections
        httpsURLConnectionList = new ArrayList<>();
        for (URL url : urlList) httpsURLConnectionList.add( ConnectionUtil.httpConnection(url, credentialString ) );
        ProgressIndicatorScreen.getInstance().addInfoToTextArea("Connect to URL..");

        validateForError();


        ProgressIndicatorScreen.getInstance().addInfoToTextArea("Fetching report locations..");


        //Get report Details from the connections
        reportLocations = new ArrayList<>();
        for (HttpsURLConnection http : httpsURLConnectionList) {
            ProgressIndicatorScreen.getInstance().addInfoToTextArea(http.getURL().toString());

            reportLocations.addAll( ConnectionUtil.getAllReportsForType( http, StartScreen.getInstance().getReportType(), StartScreen.getInstance().getPath() ) );
        }

        //Show new Window with reports to DownLoad?
        //Check if there is any report of this type for this Merchant/Company account

        if (reportLocations.isEmpty()){
            ProgressIndicatorScreen.getInstance().addInfoToTextArea("** No Reports Available for type " + StartScreen.getInstance().getReportType() + " **");
        } else {
            StartScreen.getInstance().getFrame().setVisible(false);
            showAvailableReportScreen = new ShowAvailableReportScreen( StartScreen.getInstance().getFrame(), reportLocations );
        }

    }

    public void validateForError () {
        if (errorType != null) {
            new ErrorScreen( StartScreen.getInstance().getFrame(), errorType );
        }
    }

    public static void setErrorType(ErrorTypes errorType) {
        new ErrorScreen( StartScreen.getInstance().getFrame(), errorType );

    }


    //TODO more info fields in selected reports ScrollBar
    //TODO TextFile in location with merchant codes to use
    //TODO Look into static mess...
    //TODO Allow users to use selection in selectedReportResult
    //TODO received payment detail order by date
    //TODO date in dispute report seems to not be working
    //TODO disable date / batch based on type of report
}
