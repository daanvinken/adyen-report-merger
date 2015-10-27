package com.adyen.report.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.adyen.report.entities.ReportLocation;
import com.adyen.report.entities.ReportTypes;

public class ErrorScreen{

	private JFrame errorFrame;
	private static JFrame previousInterface;
	
	private JPanel contentPane;
	private JPanel panel;
	private JPanel panel_1;
	private JPanel panel_2;
	private JPanel panel_3;
	private JPanel panel_3_1;
	private JPanel panel_3_2;
	
	private JButton backButton;
	private JButton exitButton;
	
	private String error = "";
	private ReportTypes selectedReportType;
	private boolean invalidJavaVer;
	
	private ArrayList<ReportLocation> reports;
	
	private JLabel errorHeader;
	private JLabel errorDescription;


	/**
	 * Create the application.
	 * @wbp.parser.entryPoint
	 */
	public ErrorScreen(JFrame previousInterface, Controller controller) {

		this.invalidJavaVer = false;
		Rectangle possition = previousInterface.getBounds();
//		System.out.println("Rectangle is: "+possition.getHeight());
		reports = new ArrayList<ReportLocation>(controller.getR());
//		System.out.println(reports);
		ErrorScreen.previousInterface = previousInterface;
//		System.out.println("I am going to initialize SelectReportByBatch");
		selectedReportType = ReportTypes.SETTLEMENT_DETAIL;
		
		initialize();
		errorFrame.setBounds(possition);
//		System.out.println("I have initialized SelectReportByBatch");
		previousInterface.setVisible(false);
		errorFrame.setVisible(true);
		//I assign a default value
		
	}
	
	//Creates an error page if the version is lower that the required version.
	public ErrorScreen(String error) {
		this.error=error;
		this.invalidJavaVer = true;
//		System.out.println("I am going to initialize SelectReportByBatch");
		selectedReportType = ReportTypes.SETTLEMENT_DETAIL;
		initialize();
//		System.out.println("I have initialized SelectReportByBatch");
		errorFrame.setVisible(true);
		//I assign a default value
		
	}
	
	public ErrorScreen(JFrame previousInterface, Controller controller, ReportTypes selectedReportType) {

		this.invalidJavaVer = false;
		Rectangle possition = previousInterface.getBounds();
//		System.out.println("Rectangle is: "+possition.getHeight());
		reports = new ArrayList<ReportLocation>(controller.getR());
		ErrorScreen.previousInterface = previousInterface;
		this.selectedReportType= selectedReportType;
		initialize();
		errorFrame.setBounds(possition);
		previousInterface.setVisible(false);
		errorFrame.setVisible(true);
	}

	
	private void initialize (){
		errorFrame = new JFrame();
		errorFrame.setTitle("AdyenReportMerger");
		errorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		errorFrame.setSize(new Dimension(1200, 400));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		errorFrame.setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(4, 1, 0, 0));
		
		
		
		/*Creating the panels*/		
		panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel);
		
		panel_1 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel_1);
		
		panel_2 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel_2);
		
		panel_3 = new JPanel();
		contentPane.add(panel_3);
		panel_3.setLayout(new GridLayout(1, 2, 0, 0));
		
		panel_3_1 = new JPanel();
		panel_3.add(panel_3_1);
		
		panel_3_2 = new JPanel();
		panel_3.add(panel_3_2);
		
		
		/*Adding content to the panels*/
		errorHeader = new JLabel("Unfortunately we have not been able to process your request. See the errors bellow and try again:");
		errorHeader.setVerticalAlignment(SwingConstants.TOP);
		panel.add(errorHeader);
		if(!invalidJavaVer){
			errorDescription = new JLabel(getError());
		}
		else{
			errorDescription = new JLabel(error);
		}
		
		panel_1.add(errorDescription);
		
		backButton = new JButton("Back");
		panel_3_1.add(backButton);
		backButton.setVisible(!invalidJavaVer);
		
		backButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				// System.exit(0);
				errorFrame.setVisible(false);
				Rectangle currentPossition = errorFrame.getBounds();
				previousInterface.setVisible(true);
				previousInterface.setBounds(currentPossition);
			}
		});
		
		
		exitButton = new JButton("Exit");
		panel_3_2.add(exitButton);
		
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}
	
	/**This method should return the error description*/
	
	private String getError(){
		this.error = "";
		ReportLocation rep = reports.get(0);
//		System.out.println(reports.size()+ rep.getReportName());
//		System.out.println(selectedReportType.getHtmlFormatedReportName());
		this.error = rep.getReportType().toStringHTMLFormat(selectedReportType.getHtmlFormatedReportName());
		return this.error;
	}
}
