package com.adyen.reportMerger.entities;

import com.adyen.reportMerger.Util.ConnectionUtil;
import com.adyen.reportMerger.Util.CsvUtil;
import com.adyen.reportMerger.gui.DownloadResultScreen;
import com.adyen.reportMerger.gui.ProgressIndicatorScreen;
import com.adyen.reportMerger.gui.ShowAvailableReportScreen;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Created by andrew on 9/27/16.
 *
 */

//TODO  * This class does to many things:
// It should be refactored to smaller, single responsible classes *

public class CsvClass {

    private File tmpCsv;
    private List<File> tmpFileList;
    private List<String> tmpHeaderList;
    private File mergedCsv;
    private List<ReportLocation> filesNotDownloaded ;
    private String tmpFilePath;
    private List<LinkedHashMap<String, List<String>>> tmpMapList;
    private LinkedHashMap<String, List<String>> mergedCsvMap;
    private StringBuilder mergedCsvToString;


    public CsvClass ( List<ReportLocation> rl, String path, String credentialString, boolean deleteLocalFiles , MergeTypes mt) {

        filesNotDownloaded = new ArrayList<>();
        tmpFileList = new ArrayList<>();
        tmpMapList = new ArrayList<>();

        ProgressIndicatorScreen.getInstance().addInfoToTextArea("Start Download for the chosen reports..");

        for (ReportLocation r : rl) {

            ProgressIndicatorScreen.getInstance().addInfoToTextArea("Set tmp File Path..");

            tmpFilePath = path + File.separator + r.getNewReportName();
            ProgressIndicatorScreen.getInstance().addInfoToTextArea(tmpFilePath);


            //downloadCsv();
            ProgressIndicatorScreen.getInstance().addInfoToTextArea("DownLoad : " + r.getNewReportName());

            if (ConnectionUtil.downloadFile(r.getReportDownloadURL(), credentialString ,r.getNewReportName() , path) ) {
                r.setDownloadSucceeded(true);
                ProgressIndicatorScreen.getInstance().addInfoToTextArea("Download successful");

                //ReadCsv from Machine
                tmpCsv = CsvUtil.csvToJavaFile(tmpFilePath);

                //storeFileInList();
                tmpFileList.add(tmpCsv);

                //GetHeaderFromFile (actually only needed for Last ReportLocation)
                tmpHeaderList = CsvUtil.csvHeaderToStringList(tmpCsv);


            }else {
                filesNotDownloaded.add(r);
            }
        }

        LinkedHashMap<String, List<String>> mergeList = CsvUtil.startCsvFileMerge(tmpHeaderList);

        for (File file : tmpFileList){
            ProgressIndicatorScreen.getInstance().addInfoToTextArea("Merge file: "+ file.getName());

            List<List<String>> valList = CsvUtil.csvValuesToListStringList(file);
            List<List<String>> valListEven = CsvUtil.csvEvenColumns(valList);
            LinkedHashMap<String, List<String>> valListEvenMapped = CsvUtil.csvMapFile(valListEven);
            mergedCsvMap = CsvUtil.fillCsvFileMerge(mergeList, valListEvenMapped);

            if (deleteLocalFiles) {
                file.delete();
            }
        }

        //StringBuild the Csv
        mergedCsvToString = CsvUtil.writeCsv(mergedCsvMap);

        String filename = "";

        //Set fileName based on mergeType
        switch (mt) {
            case DATERANGE: {

                // (2) create a date "formatter" (the date format we want)
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

                // (3) create a new String using the date format we want
                String folderName = formatter.format(rl.get(rl.size()-1).getReportModificationDate());
                filename = rl.get(0).getReportType().getReportCode() + formatter.format(rl.get(0).getReportModificationDate() ) + "_" + formatter.format(rl.get(rl.size()-1).getReportModificationDate());
            }
            break;

            case BATCHRANGE: {
                filename = rl.get(0).getReportType().getReportCode() + "FROM" + rl.get(0).getBatchNumber() + "TO" + rl.get(rl.size()-1).getBatchNumber();
            }
        }

        //Create new file
        mergedCsv = new File(path +File.separator + filename + ".csv");

        if (mergedCsv.exists()) {
            mergedCsv.delete();
        }

        Scanner scan = new Scanner(mergedCsvToString.toString());
        try {
            mergedCsv.createNewFile();
            FileWriter writer = new FileWriter(mergedCsv);
            while (scan.hasNextLine() ){
                String oneLine = scan.nextLine();
                writer.write(oneLine);
                writer.flush();
            }
            writer.close();
            new DownloadResultScreen(ShowAvailableReportScreen.showAvailableReportFrame,  true, mergedCsv.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            new DownloadResultScreen(ShowAvailableReportScreen.showAvailableReportFrame, false, mergedCsv.getAbsolutePath());
            ProgressIndicatorScreen.getInstance().addInfoToTextArea("an exeption was thrown while creating the Merged CSV File :" + e.getMessage());
        }
    }
}
