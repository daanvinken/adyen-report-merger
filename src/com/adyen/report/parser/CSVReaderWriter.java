package com.adyen.report.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import com.adyen.report.connector.ConnectionInfo;
import com.adyen.report.gui.Controller;

/**
 * 
 * @author andrew
 * Class to read and write csv files
 */
public class CSVReaderWriter {
	
	private static FileHandler fh = Controller.getFileHandler();
	private final static Logger LOGGER = Logger.getLogger(ConnectionInfo.class.getName());
	
	
	/**
	 * 
	 * @param file
	 * @return
	 */
	public ArrayList<List<String>> csvValueReader(File file){
//		System.out.println("csvValueReader reads a file, skips the header, makes a list of all lines");
		BufferedReader br = null;
		String line ;
		
		ArrayList <List<String>> lineList = new ArrayList<List<String>>();
		try {

			br = new BufferedReader(new FileReader(file));
			
//			String headerLine = br.readLine();//used to not add first line(headers) to the lineList
			while ((line = br.readLine()) != null) {
				List<String> valList = new ArrayList<>(Arrays.asList(line.split("\\s*,\\s*")));
                lineList.add(valList);
				
            }
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			LOGGER.addHandler(fh);
			LOGGER.severe(e1.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.addHandler(fh);
			LOGGER.severe(e.getMessage());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
					LOGGER.addHandler(fh);
					LOGGER.severe(e.getMessage());
				}
			}
		}
		
		return lineList;
		
	}
	
	/**
	 * makes a list of all values in the first line of a csv file
	 * @param file
	 * @return
	 */
	public List<String> csvHeaderReader(File file){
//		System.out.println("csvHeaderReader reads the headers and makes a list of the headers");
		BufferedReader br = null;
		String line = "";
		List <String> headers = new ArrayList<String>();
		try {
	
			br = new BufferedReader(new FileReader(file));
			
			line = br.readLine();

			headers = Arrays.asList(line.split("\\s*,\\s*"));

			
			return 	headers;
	
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			LOGGER.addHandler(fh);
			LOGGER.severe(e1.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.addHandler(fh);
			LOGGER.severe(e.getMessage());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
					LOGGER.addHandler(fh);
					LOGGER.severe(e.getMessage());
				}
			}
		}
		
		return headers;
	
	}

	

	
/**
 * Method used to ensure the scv has same number of columns in each row
 * And convert the file to a list of lists
 * @param file
 */
	public ArrayList<List<String>> csvEvenColumns(ArrayList<List<String>> valList){
		
		//ensure has same amount of columns and rows troughout the file based on first line
		int nrOfColumns = valList.get(0).size();
		
		for (List<String> list : valList ){
			for (int col = list.size(); col < nrOfColumns ;col++ ){
				list.add("");
			}
		}
		
		return valList;
	}
	
/**
 * Method to convert csvFileList to a map
 * Headers are keys, and values in the list	
 * @param valList
 */
	public LinkedHashMap<String, List<String>> csvMapFile(ArrayList<List<String>> valList){
	
		int nrOfRows = valList.size();
		LinkedHashMap<String, List<String>> valMap = new LinkedHashMap<String, List<String>>();
		
		//add columns from file as Key for Map and an empty ArrayList as value
		int headerIndex = 0;
		for (String header : valList.get(0)){
			
			valMap.put(header, new ArrayList<String>());
			for(int row = 1; row < nrOfRows ; row++){
				valMap.get(header).add(valList.get(row).get(headerIndex));
			}
			
			
			headerIndex++;
		}
		
//		qSystem.out.println(valMap);
		return valMap;
		
	}
	
	/**
	 * assuming the last file is the latest version of the report, and includes all the column according to Adyen style, we use these columns to base the new report on
	 * @param fileList
	 * @return
	 */
	public List <String> headersfromLastFile(List<File> fileList){
		
		List <String> headerList = new ArrayList<String>();
		for (File file : fileList){
			headerList =csvHeaderReader(file);
		}
		
		return headerList;
	}
	
	/**
	 * create header in new scv file
	 * @param headerList
	 * @return
	 */
	public LinkedHashMap<String, List<String>> startCsvFileMerge(List <String> headerList){
		
		LinkedHashMap<String, List<String>> mergedCsvFileMapped = new LinkedHashMap<String, List<String>>();
		
		for (String header : headerList){
			mergedCsvFileMapped.put(header, new ArrayList<String>());
		}

		return mergedCsvFileMapped;
	}
	
	/**
	 * map values for new csv file
	 * @param mergeList
	 * @param valList
	 * @return
	 */
	public LinkedHashMap<String, List<String>> fillCsvFileMerge( LinkedHashMap<String, List<String>> mergeList, LinkedHashMap<String, List<String>> valList){
		
		int nrOfRows = 0;
		
		for (String key : mergeList.keySet()){
			if(valList.get(key) != null){
				nrOfRows = valList.get(key).size();
				for (String val :valList.get(key)){
					mergeList.get(key).add(val);
				}
				
			}else{
				for (int row = 0; row < nrOfRows ; row++){
					mergeList.get(key).add("");
				}
			}

		}
		
		return mergeList;
		
	}
	
	/**
	 * write the new scv file
	 * @param csvMap
	 * @return
	 */
	public StringBuilder writeCsv (LinkedHashMap<String, List<String>> csvMap){
		
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
//			System.out.println(nrOfRows);
			for (String header : csvMap.keySet()){
				
				sb.append(csvMap.get(header).get(row));
				sb.append(",");
			}
			
			sb.setLength(sb.length() - 1);
			sb.append("\n");
		}
		
		return sb;
	}
	
}
