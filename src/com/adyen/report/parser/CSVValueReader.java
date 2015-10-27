package com.adyen.report.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import com.adyen.report.connector.ConnectionInfo;
import com.adyen.report.gui.Controller;

/**
 * class used to read all values of scv expect header lines
 * @author andrew
 *
 */
public class CSVValueReader {
	
	private static FileHandler fh = Controller.getFileHandler();
	private final static Logger LOGGER = Logger.getLogger(CSVValueReader.class.getName());
	
	public ArrayList<List<String>> csvValueReader(File file){
		BufferedReader br = null;
		String line ;
		
		ArrayList <List<String>> lineList = new ArrayList<List<String>>();
		try {
			
			br = new BufferedReader(new FileReader(file));
			
			String headerLine = br.readLine();//used to not add first line(headers) to the lineList
			while ((line = br.readLine()) != null) {
                lineList.add(Arrays.asList(line.split("\\s*,\\s*")));
				
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
//		System.out.println(lineList);
		return lineList;
		
	}

}
