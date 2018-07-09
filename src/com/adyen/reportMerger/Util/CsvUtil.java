package com.adyen.reportMerger.Util;

import com.adyen.reportMerger.entities.HeaderCollection;
import com.adyen.reportMerger.gui.ProgressIndicatorScreen;

import java.io.*;
import java.util.*;

/**
 * Created by andrew on 9/21/16.
 */
public final class CsvUtil {


    public static boolean validateCSV (File file) {

        //todo   actual validation
        return true;
    }

    public static File csvToJavaFile (String fileLocation) {

        File file = new File(fileLocation);

        return file;

    }

    public static List<List<String>> csvValuesToListStringList (File file) {
        BufferedReader br = null;
        String line ;

        List<List<String>> lineList = new ArrayList<>();
        try {

            br = new BufferedReader(new FileReader(file));

            while ((line = br.readLine()) != null) {
                lineList.add(Arrays.asList(line.split("\\s*,\\s*")));

            }
        } catch (FileNotFoundException e1) {
            ProgressIndicatorScreen.getInstance().addInfoToTextArea(file.getName() + "could not be found while parsing to StringList :" +e1.getMessage());

        } catch (IOException e) {
            ProgressIndicatorScreen.getInstance().addInfoToTextArea(file.getName() + "caused an IOException while parsing to StringList :" + e.getMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    ProgressIndicatorScreen.getInstance().addInfoToTextArea("An error orcurred while closing BufferedReader" + e.getMessage());
                }
            }
        }

        return lineList;

    }

    public static List<String> csvHeaderToStringList(File file){
//		csvHeaderReader reads the headers and makes a list of the headers";
        BufferedReader br = null;
        String line ;
        List <String> headers = new ArrayList<>();
        try {

            br = new BufferedReader(new FileReader(file));

            line = br.readLine();

            headers = Arrays.asList(line.split("\\s*,\\s*"));

            return 	headers;

        } catch (IOException e1) {
            ProgressIndicatorScreen.getInstance().addInfoToTextArea(file.getName() + "could not be found while parsing to StringList :" +e1.getMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    ProgressIndicatorScreen.getInstance().addInfoToTextArea("An error orcurred while closing BufferedReader" + e.getMessage());
                }
            }
        }

        return headers;

    }

    public static List<List<String>> csvEvenColumns (List<List<String>> valList){

        //ensure has same amount of columns troughout the file based on headerLine line
        int nrOfColumns = valList.get(0).size();
        List<List<String>> valListNew = new ArrayList<>();
        List<String> newList;
        for (List<String> list : valList ){
            newList = new ArrayList<>();
            int addEmpty =  nrOfColumns - list.size();
            newList.addAll(list);
            for (int x = 0; x<addEmpty ; x++) {
                newList.add("");
            }
            valListNew.add(newList);
        }
        return valListNew;
    }

    public static LinkedHashMap<String, List<String>> csvMapFile(List<List<String>> valList){

        int nrOfRows = valList.size();
        LinkedHashMap<String, List<String>> valMap = new LinkedHashMap<>();

        //add columns from file as Key for Map and an empty ArrayList as value
        int headerIndex = 0;
        for (String header : valList.get(0)){

            valMap.put(header, new ArrayList<>());
            for(int row = 1; row < nrOfRows ; row++){
                valMap.get(header).add(valList.get(row).get(headerIndex));
            }

            headerIndex++;
        }

        return valMap;
    }



    /**
     * create header in new scv file
     * @param headerList
     * @return
     */
    public static LinkedHashMap<String, List<String>> startCsvFileMerge(List <String> headerList){

        LinkedHashMap<String, List<String>> mergedCsvFileMapped = new LinkedHashMap<String, List<String>>();

        for (String header : headerList){
            mergedCsvFileMapped.put(header, new ArrayList<String>());
        }

        return mergedCsvFileMapped;
    }

    /**
     * map values for new csv file
     * todo give more explanation on what im doing here and make it simpler
     * Merge Map is a map with the found CSV headers and empty string list
     * The value map contains all the data
     */
    public static LinkedHashMap<String, List<String>> fillCsvFileMerge( LinkedHashMap<String, List<String>> mergeMap, LinkedHashMap<String, List<String>> valMap){
        //Null pointer for JoeAndtheJuice JoeAndTheJuice_NiceAirport
//        at com.adyen.reportMerger.Util.CsvUtil.fillCsvFileMerge(CsvUtil.java:180
        //report 11 changed the structure of the report  (the key is still there though..

        int nrOfRows = 0;
        if (mergeMap == null) {
            return null;
        }
        if (mergeMap.isEmpty()) {
            return null;
        }
        if (valMap == null) {
            return null;
        }
        if (valMap.isEmpty()){
            return null;
        }
        for (String key : mergeMap.keySet()){
            if(valMap.get(key) != null){
                nrOfRows = valMap.get(key).size();

                for (String val :valMap.get(key)){
                    mergeMap.get(key).add(val);
                }

            }else{
                for (int row = 0; row < nrOfRows ; row++){
                    mergeMap.get(key).add("");
                }
            }

        }

        return mergeMap;

    }

    /**
     * write the new scv file
     */
    public static StringBuilder writeCsv (LinkedHashMap<String, List<String>> csvMap){

        StringBuilder sb = new StringBuilder();
        int nrOfRows = 0;

        for (String header : csvMap.keySet()){
            nrOfRows = csvMap.get(header).size();
            sb.append(header);
            sb.append(",");
        }

        sb.setLength(sb.length() - 1);
        sb.append("\n");

        for (int row = 0 ; row < nrOfRows ; row++){
            for (String header : csvMap.keySet()){

                sb.append(csvMap.get(header).get(row));
                sb.append(",");
            }

            sb.setLength(sb.length() - 1);
            sb.append("\n");
        }

        return sb;
    }

    //This only works if headers is passed and the bufferedReader already had the first line read (so no headers included)
    private static Map<String, List<String>> bufferedReaderCsvToHashMap(List<String> headers, BufferedReader bufferedReader) throws IOException {

        //initiate a map put the headers as key values
        Map<String, List<String>> map = new LinkedHashMap<>();
        for (String s : headers) {
            map.put(s, new ArrayList<>());
        }


        String line;
        while((line = bufferedReader.readLine()) != null){
//            List<String> list = Arrays.asList(line.split("\\s*,\\s*"));
//            List<String> list = Lists.newArrayList(Splitter.on(",").split(line));
            List<String> list = CsvUtil.csvLineToList(line);

            //why!?  out of bounds
            int x = 0;
            for (String s : list) {
                String mapKey = headers.get(x);
                map.get(mapKey).add(s);
                x++;
            }
        }

        return map;
    }


    public static String fixCsvWithWrongColumns(List<String> headers, BufferedReader bufferedReader) {
        StringBuilder sb = new StringBuilder();
        try {
            Map<String , List<String>> map = bufferedReaderCsvToHashMap(headers, bufferedReader);

            String key =  (String)map.keySet().toArray()[0];
            int nrOfRows = map.get(key).size();

            for (int x = 0; x < nrOfRows ; x++) {
                for (String header : HeaderCollection.getInstance().getHeaders()) {

                    if (null == map.get(header) || map.get(header).size() == 0 ) {
                        sb.append("");
                    } else{
                        sb.append(map.get(header).get(x));
                    }
                    if (! HeaderCollection.getInstance().isLastHeader(header)) {
                        sb.append(",");
                    }else {
                        sb.append("\n");
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static List<String> csvLineToList(String csvLine) {

        List<String> list = new ArrayList<>();
        if (csvLine == null) {
            return list;
        }

        StringBuilder sb = new StringBuilder();

        int n = 0;
        for (int i = 0; i < csvLine.length(); i++) {
            char c = csvLine.charAt(i);

            sb.append(c);

            if (n % 2 == 0) {
                if (c == ',') {
                    sb.deleteCharAt(sb.length() - 1);
                    list.add(sb.toString());
                    sb = new StringBuilder();
                }
            }

            if (c == '"') {
                n++;
            }
        }

        list.add(sb.toString());
        return list;
    }
}
