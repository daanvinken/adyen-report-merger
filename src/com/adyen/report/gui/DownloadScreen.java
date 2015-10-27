package com.adyen.report.gui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.BackingStoreException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DownloadScreen {

	private static JFrame frameDownload;
	private static JFrame selectReportByBatchFrame;
	private static JFrame interfaceFrame;
	
	private static JLabel downloadStatus;
	
	private static JButton startAgainButton;
	private static JButton backButton;
	private static JButton exitButton;
	
	private static Controller cont;
	
	private static boolean reportSuccessfullyCreated;
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					createAndShowGUI();
//					frameDownload.setVisible(true);
//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the application.
	 */
	public DownloadScreen(JFrame selectReportByBatchFrame, Controller cont, JFrame interfaceFrame) {
		Rectangle possition = selectReportByBatchFrame.getBounds();
		DownloadScreen.selectReportByBatchFrame = selectReportByBatchFrame;
		DownloadScreen.interfaceFrame = interfaceFrame;
		this.cont = cont;
		reportSuccessfullyCreated = true;
		initialize();
		frameDownload.setBounds(possition);
		DownloadScreen.selectReportByBatchFrame.setVisible(false);
		frameDownload.setVisible(true);
		reportSuccessfullyCreated = true;
		//main(null);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
//		frameDownload = new JFrame();
//		frameDownload.setTitle("AdyenReportMerger");
//		frameDownload.setBounds(100, 100, 450, 300);
//		frameDownload.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createAndShowGUI();
	}

	private static void createAndShowGUI() {
		frameDownload = new JFrame("Adyen Report Merger");
		frameDownload.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameDownload.setSize(new Dimension(1200, 400));
		GridLayout myLayout = new GridLayout(3, 1);
		frameDownload.getContentPane().setLayout(myLayout);
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) panel_1.getLayout();
		flowLayout_4.setVgap(10);
		flowLayout_4.setHgap(10);
		flowLayout_4.setAlignment(FlowLayout.LEFT);
		frameDownload.getContentPane().add(panel_1);
        
		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panel_2.getLayout();
		flowLayout_3.setVgap(6);
		flowLayout_3.setHgap(10);
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		frameDownload.getContentPane().add(panel_2);

		JPanel panel_3 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_3.getLayout();
		flowLayout_2.setVgap(6);
		flowLayout_2.setHgap(10);
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		frameDownload.getContentPane().add(panel_3);
		
		String choosenMessage = "";
		if (reportSuccessfullyCreated){
			choosenMessage = "The report has been correctly created. It is placed in the folder "+cont.getPath();
		}else{
			choosenMessage = "The report has not been correctly created. Please try again.";
		}
					
		downloadStatus = new JLabel(choosenMessage);
		panel_1.add(downloadStatus);
		
		startAgainButton = new JButton("Start again");
		backButton = new JButton("Back");
		exitButton = new JButton("Exit");
		panel_3.add(startAgainButton);
		
		startAgainButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				// System.exit(0);
				frameDownload.setVisible(false);
				DownloadScreen.interfaceFrame.setVisible(true);
			}
		});

		panel_3.add(backButton);
		
		backButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				// System.exit(0);
				frameDownload.setVisible(false);
				Rectangle currentPossition = frameDownload.getBounds();
				DownloadScreen.selectReportByBatchFrame.setVisible(true);
				selectReportByBatchFrame.setBounds(currentPossition);
			}
		});
		
		panel_3.add(exitButton);
		
		exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
				//frameDownload.setVisible(false);
				//selectReportByBatchFrame.setVisible(true);

			}
		});
		
	}

}
