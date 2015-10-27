package com.adyen.report.test;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.adyen.report.entities.ReportLocation;
import com.adyen.report.gui.Controller;

public class TestingClass {

	private JFrame frame;
	
	
	private JFrame selectingReportByDate;
	private static ArrayList<ReportLocation> reports;
	private static JFrame interfaceFrame;
	private Controller controller;
	private ArrayList<ReportLocation> selectedReports;
	private boolean deleteLocalFiles;
	private static JList<ReportLocation> listAvailableReports;
	private static DefaultListModel<ReportLocation> model;
	private static JProgressBar progressBar;

	private static boolean canDownload;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestingClass window = new TestingClass();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TestingClass() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		//frame.setBounds(100, 100, 450, 300);
		frame.setSize(new Dimension(1200, 300));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(10, 2, 0, 0));
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		
		JPanel panel_2 = new JPanel();
		frame.getContentPane().add(panel_2);
		
		JPanel panel_3 = new JPanel();
		frame.getContentPane().add(panel_3);
		
		JPanel panel_4 = new JPanel();
		frame.getContentPane().add(panel_4);
		
		JPanel panel_5 = new JPanel();
		frame.getContentPane().add(panel_5);
		
		JPanel panel_6 = new JPanel();
		frame.getContentPane().add(panel_6);
		
		JList<ReportLocation> list = new JList<ReportLocation>();
		list.setEnabled(false);
		panel_6.add(list);
		
		JPanel panel_7 = new JPanel();
		frame.getContentPane().add(panel_7);
		
		JPanel panel_8 = new JPanel();
		frame.getContentPane().add(panel_8);
		
		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1);
		
		JPanel panel_9 = new JPanel();
		frame.getContentPane().add(panel_9);
		
		JPanel panel_10 = new JPanel();
		frame.getContentPane().add(panel_10);
		
		JPanel panel_11 = new JPanel();
		frame.getContentPane().add(panel_11);
		
		JPanel panel_12 = new JPanel();
		frame.getContentPane().add(panel_12);
		
		JPanel panel_13 = new JPanel();
		frame.getContentPane().add(panel_13);
		
		JPanel panel_14 = new JPanel();
		frame.getContentPane().add(panel_14);
		
		JPanel panel_15 = new JPanel();
		frame.getContentPane().add(panel_15);
		
		JPanel panel_16 = new JPanel();
		frame.getContentPane().add(panel_16);
		
		JPanel panel_17 = new JPanel();
		frame.getContentPane().add(panel_17);
		
		JPanel panel_18 = new JPanel();
		frame.getContentPane().add(panel_18);
		
		JPanel panel_19 = new JPanel();
		frame.getContentPane().add(panel_19);
		GridLayout myLayout = new GridLayout(10, 2);
		selectingReportByDate.getContentPane().setLayout(myLayout);
		
		selectingReportByDate.setVisible(true);
		
	}
	
	
}
