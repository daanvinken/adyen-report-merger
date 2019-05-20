package com.adyen.reportMerger.Util;

import com.adyen.reportMerger.entities.*;
import com.adyen.reportMerger.gui.ProgressIndicatorScreen;
import com.adyen.reportMerger.gui.StartScreen;
import com.adyen.reportMerger.runners.Controller;
import com.google.common.base.Objects;
import org.apache.commons.codec.binary.Base64;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by andrew on 9/21/16.
 */
public final class ConnectionUtil {


    private static final int BUFFER_SIZE = 4096;
    private static String userName = StartScreen.getInstance().getReportUserName().getText() + "@Company."+StartScreen.getInstance().getCompanyCode().getText();
    private static char [] passWord = StartScreen.getInstance().getPasswordField().getPassword();
    private static String savePath = StartScreen.getInstance().getPath();


    public static List<URL> getUrlListBasedOnReportLevel (ReportLevels rl, String accountCode) {

        List<URL> urlList = new ArrayList<>();
        URL url;

        switch (rl) {

            case MERCHANT: {

                urlList = new ArrayList<>();

                try {
                    url = new URL(new URL(
                            "https://ca-live.adyen.com/reports/download/MerchantAccount/"
                                    + accountCode + "/").toString());

                    urlList.add(url);

                } catch (MalformedURLException e) {
                    e.printStackTrace();

                }
            }

            break;

            case COMPANY: {

                urlList = new ArrayList<>();

                try {
                    url = new URL(new URL(
                            "https://ca-live.adyen.com/reports/download/Company/"
                                    + accountCode + "/").toString());

                    urlList.add(url);

                } catch (MalformedURLException e) {
                    e.printStackTrace();

                }
            }
            break;

            case MULTIMERCHANT: {
                urlList = new ArrayList<>();

//                List<String>multipleAccountCodes = Arrays.asList(accountCode.toString().trim().split(","));
//                List<String> multipleAccountCodes = Lists.newArrayList(Splitter.on(",").trimResults().split(accountCode));

                List<String> multipleAccountCodes = CsvUtil.csvLineToList(accountCode);
                for (String account : multipleAccountCodes) {
                    try {

                        url = new URL(new URL(
                                "https://ca-live.adyen.com/reports/download/MerchantAccount/"
                                        + account.trim() + "/").toString());

                        urlList.add(url);

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }

        return  urlList;
    }

    //base64 Encoded credentialString
    public static String getCredentialString () {
        String pass = new String (passWord);
        String credentialString = userName + ":" + pass;

        // Encoding
        byte[] encoding = Base64.encodeBase64(credentialString.getBytes());
        credentialString = new String(encoding);

        return credentialString;
    }

    //create Http connection
    public static HttpsURLConnection httpConnection(URL url, String credentialString) {
        HttpsURLConnection urlConnection = null;

        try {
            urlConnection = ((HttpsURLConnection) url.openConnection());

        } catch (IOException e1) {
            e1.printStackTrace();
        }
        urlConnection.setRequestProperty("Authorization", "Basic "
                + credentialString);
        urlConnection.setConnectTimeout(600000);

        // Establishing the connection
        int code = 0;
        try {
            urlConnection.connect();
            code = urlConnection.getResponseCode();
            if (code == 200) {

                return urlConnection;

            }
            else if (code == 401) {
                //Send to contoller
                Controller.setErrorType(ErrorTypes.ERROR_INCORRECT_DETAILS) ;

            } else if (code == 404 ) {
                Controller.setErrorType(ErrorTypes.ERROR_INCORRECT_ACCOUNT) ;


            } else if (code == 403 ) {
                Controller.setErrorType(ErrorTypes.ERROR_INCORRECT_ACCOUNT) ;


            }

        } catch (IOException e) {
            //uh oh
        }

        return urlConnection;

    }

    //Find available Reports
    public static List<ReportLocation> getAllReportsForType (HttpsURLConnection http, ReportTypes rt, String path) {
        List<ReportLocation> reportDetails = new ArrayList<>();

        try {
            File tmpHTML = new File(path + "/reports.html");
            FileOutputStream writer = new FileOutputStream(tmpHTML);
            InputStream reader = http.getInputStream();

            byte[] buffer = new byte[153600];
            int bytesRead = 0;
            while ((bytesRead = reader.read(buffer)) > 0) {
                writer.write(buffer, 0, bytesRead);
                buffer = new byte[153600];
            }
            writer.close();
            reportDetails = ReadHtml.getReportDetails(tmpHTML, http.getURL(), rt );

        } catch (IOException e) {
            e.printStackTrace();
        }

        return reportDetails;
    }


    public static boolean downloadFile(URL url, String credentialString, String newFileName, String savePath) {
        HttpURLConnection http;
        boolean success = false;
//        LOGGER.addHandler(fileHandler);

        try {
            http = (HttpURLConnection) url.openConnection();
            http.setRequestProperty("Authorization", "Basic "
                    + credentialString);
            int responseCode = http.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = http.getInputStream();
                String saveFilePath = savePath + File.separator + newFileName;
                FileOutputStream outputStream = new FileOutputStream(saveFilePath);

                int bytesRead;
                byte[] buffer = new byte[BUFFER_SIZE];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                inputStream.close();

                success = true;
            } else {
                Controller.errorType = ErrorTypes.ERROR_DOWNLOAD_FILE;
            }
            http.disconnect();

            } catch (IOException e1) {
                e1.printStackTrace();
        }

        return success;
    }

    public static ReportLocation downloadFile(ReportLocation reportLocation) {
        HttpURLConnection http;
        try {
            http = (HttpURLConnection) reportLocation.reportDownloadURL.openConnection();
            http.setRequestProperty("Authorization", "Basic "
                    + getCredentialString());
            int responseCode = http.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = http.getInputStream();
                String saveFilePath = savePath + File.separator + reportLocation.newReportName;
                FileOutputStream outputStream = new FileOutputStream(saveFilePath);

                int bytesRead;
                byte[] buffer = new byte[BUFFER_SIZE];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                inputStream.close();

                reportLocation.setDownloadSucceeded(true);
                reportLocation.setReport(new File(saveFilePath));
                reportLocation.setReportHeaders(CsvUtil.csvHeaderToStringList(reportLocation.getReport()));
                return reportLocation;

            } else {
                Controller.errorType = ErrorTypes.ERROR_DOWNLOAD_FILE;
            }
            http.disconnect();

        } catch (IOException e1) {
            e1.printStackTrace();
        }

        reportLocation.setDownloadSucceeded(false);


        return reportLocation;
    }


    public static List<String> downloadHeader(ReportLocation reportLocation) {
        HttpURLConnection http;
        try {
            http = (HttpURLConnection) reportLocation.reportDownloadURL.openConnection();
            http.setRequestProperty("Authorization", "Basic "
                    + getCredentialString());
            int responseCode = http.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = http.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line = reader.readLine();
                http.disconnect();

//                return Lists.newArrayList(Splitter.on(",").split(line));
                return CsvUtil.csvLineToList(line);

            } else {
                Controller.errorType = ErrorTypes.ERROR_DOWNLOAD_FILE;
                return null;
            }

        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return null;
    }


    public static String dynamicDownloadFileAsString(ReportLocation reportLocation, boolean includeHeader) {

        HttpURLConnection http;
        try {
            http = (HttpURLConnection) reportLocation.reportDownloadURL.openConnection();
            http.setRequestProperty("Authorization", "Basic "
                    + getCredentialString());
            int responseCode = http.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = http.getInputStream();
                ProgressIndicatorScreen.getInstance().addInfoToTextArea("Downloading report: " + reportLocation.reportName);

                // create files if deleteFiles is switched off
                if (! Controller.deleteFiles) {
                    String saveFilePath = savePath + File.separator + reportLocation.newReportName;
                    FileOutputStream outputStream = new FileOutputStream(saveFilePath);

                    int bytesRead;
                    byte[] buffer = new byte[BUFFER_SIZE];
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }

                    outputStream.close();
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                //check if headers match
                StringBuilder sb = new StringBuilder();
                String header = reader.readLine();

//                if (Objects.equal(Lists.newArrayList(Splitter.on(",").trimResults().split(header)), HeaderCollection.getInstance().getHeaders())) {
                if (Objects.equal(CsvUtil.csvLineToList(header), HeaderCollection.getInstance().getHeaders())) {

                    if (includeHeader) {
                        sb.append(header);
                        sb.append("\n");
                    }
                    String line;

                    while((line = reader.readLine()) != null){
                        sb.append(line);
                        sb.append("\n");
                    }

                } else {
                    List<String> headerListCurrentReport = CsvUtil.csvLineToList(header);
                    sb.append(CsvUtil.fixCsvWithWrongColumns(headerListCurrentReport , reader));
                    sb.append("\n");
                }

                http.disconnect();

                return sb.toString();

            } else {
                Controller.errorType = ErrorTypes.ERROR_DOWNLOAD_FILE;
                return null;
            }

        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return "";
    }


}
