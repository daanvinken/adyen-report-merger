package com.adyen.reportMerger.builder;

import com.adyen.reportMerger.Util.ConnectionUtil;
import com.adyen.reportMerger.entities.HeaderCollection;
import com.adyen.reportMerger.entities.MergeTypes;
import com.adyen.reportMerger.entities.ReportLocation;
import com.adyen.reportMerger.gui.DownloadResultScreen;
import com.adyen.reportMerger.gui.ShowAvailableReportScreen;
import com.adyen.reportMerger.gui.StartScreen;
import com.adyen.reportMerger.runners.Controller;
import com.google.common.base.Stopwatch;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.List;

public class CsvMerger extends SwingWorker<String, String> {

    private StartScreen startScreen = StartScreen.getInstance();
    private List<ReportLocation> reportLocations;
    private MergeTypes mergeTypes;

    /**
     *         //download
     //For each report location
     //check if it's actual csv
     //check the header and add to HeaderCollection if this does not contain this header yet
     //check if all the headers are the same. If they are, merging can be done with appending all files to new file but strip headers from all files except first
     //if headers are not all the same, we need to start mapping stuff

     * @param reportLocations
     * @param mergeTypes
     */
    public CsvMerger(List<ReportLocation> reportLocations, MergeTypes mergeTypes){
        this.reportLocations = reportLocations;
        this.mergeTypes = mergeTypes;

    }

    private void downloadAndMerge(){

        Stopwatch stopwatch = Stopwatch.createStarted();
        if (reportLocations == null || reportLocations.size() == 0) {
            return;
        }

        if (mergeTypes == null) {
            return;
        }

        //get headers from last report and set: HeaderCollection.getInstance().setHeaders
        HeaderCollection.getInstance().setHeaders(ConnectionUtil.downloadHeader(reportLocations.get(reportLocations.size()-1)));

        try {
            //delete mergeFile if already exists
            Files.deleteIfExists(new File(setFileNameAndPath()).toPath());

            FileWriter mergeFileWriter = new FileWriter(setFileNameAndPath(),true);
            BufferedWriter bw = new BufferedWriter(mergeFileWriter);
            PrintWriter out = new PrintWriter(bw);

            //Add headers
            out.println(HeaderCollection.getInstance().getHeadersAsCsvLine());

            StringBuilder sb = new StringBuilder();

            int reportSizeEstimation = 0;

            for (ReportLocation reportLocation : reportLocations) {

                //write to disc when pasing maxMemoryBytes only;  to speed up the process
                if (reportSizeEstimation > Controller.maxMergeFileSizeBeforeWriteToDisk) {
                    out.println(sb.toString());
                    out.flush();
                    out.close();
                    bw.close();
                    mergeFileWriter.close();

                    mergeFileWriter = new FileWriter(setFileNameAndPath(),true);
                    bw = new BufferedWriter(mergeFileWriter);
                    out = new PrintWriter(bw);
                    sb = new StringBuilder();

                    reportSizeEstimation = 0;

                }

                sb.append(ConnectionUtil.dynamicDownloadFileAsString(reportLocation, false));
                sb.append("\n");

                if (Controller.deleteFiles) {
                    if (reportLocation.getReport() != null) {
                        Files.deleteIfExists(reportLocation.getReport().toPath());
                    }
                }

                if (reportLocation.getSize() != null) {
                    try {
                        //since the size indicated on the site is completly off (seems to be 4 times to small compared to actuall size)  I multiply the reportLocation by 4
                        reportSizeEstimation += Integer.parseInt(reportLocation.getSize().replaceAll("[^0-9]", ""))*4;
                    } catch (NumberFormatException e) {
                        System.out.println("error parsing size");
                        //todo add this to log screen
                    }
                }
            }
            out.println(sb.toString());

            out.flush();
            out.close();
            bw.close();
            mergeFileWriter.close();

            new DownloadResultScreen(ShowAvailableReportScreen.showAvailableReportFrame,  true, setFileNameAndPath());

            stopwatch.stop();
            System.out.println(stopwatch);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private String setFileNameAndPath(){
        //Set fileName based on mergeType
        String fileName = "";
        switch (mergeTypes) {
            case DATERANGE: {

                // (2) create a date "formatter" (the date format we want)
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

                // (3) create a new String using the date format we want //todo  simplify this
                fileName = reportLocations.get(0).getReportType().getReportCode() + "_" + formatter.format(reportLocations.get(0).getReportModificationDate() ) + "_" + formatter.format(reportLocations.get(reportLocations.size()-1).getReportModificationDate());
                break;
            }

            case BATCHRANGE: {
                fileName = reportLocations.get(0).getReportType().getReportCode() + "FROM" + reportLocations.get(0).getBatchNumber() + "TO" + reportLocations.get(reportLocations.size()-1).getBatchNumber();
            }
        }

        return startScreen.getPath() +File.separator + fileName + ".csv";
    }

    @Override
    protected String doInBackground() throws Exception {
        downloadAndMerge();
        return null;
    }

}
