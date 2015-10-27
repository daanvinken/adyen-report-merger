package com.adyen.report.gui;


import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogFileHandler {
	
	
	
	public FileHandler fileHandler(String path){
		
		
			try {
				FileHandler fh = new FileHandler(path +"/logFile.txt");
				SimpleFormatter formatter = new SimpleFormatter();  
		        fh.setFormatter(formatter);  
				return fh;
			} catch (SecurityException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
			
		
		
		
		
	}

	
}
