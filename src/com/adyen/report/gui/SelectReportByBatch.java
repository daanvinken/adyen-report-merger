package com.adyen.report.gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.adyen.report.connector.ConnectionInfo;
import com.adyen.report.entities.ReportLocation;

import javax.swing.SwingConstants;

public class SelectReportByBatch {

	private JFrame selectingByBatchFrame;
	private JComboBox<ReportLocation> olderBatchCombo;
	private JComboBox<ReportLocation> lastBatchCombo;
	private JButton confirmButton;
	private JButton backButton;
	private static ArrayList<ReportLocation> reports;
	private static JFrame interfaceFrame;
	private JCheckBox mergeDesired;
	private JCheckBox deleteDLocalFilesCheckBox;
	private JLabel sizeTime;
	private Controller controller;
	private ArrayList<ReportLocation> selectedReports;
	private boolean deleteLocalFiles;
	private static FileHandler fh = Controller.getFileHandler();
	private final static Logger LOGGER = Logger.getLogger(ConnectionInfo.class.getName());

	/**
	 * Create the application.
	 * @wbp.parser.entryPoint
	 */
	public SelectReportByBatch(JFrame interfaceFrame, Controller controller) {
		Rectangle possition = interfaceFrame.getBounds();
//		System.out.println("Rectangle is: "+possition.getHeight());
		this.controller = controller;
		reports = new ArrayList<ReportLocation>(controller.getR());
//		System.out.println(reports);
		this.interfaceFrame = interfaceFrame;
//		System.out.println("I am going to initialize SelectReportByBatch");
		initialize();
		selectingByBatchFrame.setBounds(possition);
//		System.out.println("I have initialized SelectReportByBatch");
		interfaceFrame.setVisible(false);
		// main(null);
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		// creatingProvissionaryReports();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// SelectReportByBatch window = new SelectReportByBatch(new
					// JFrame(),new Controller(true, "TestMerchant",
					// "report@Company.TestCompany",
					// "s5fruGN2bn2kGaDF4bN-FPP".toCharArray(), true));
//					System.out.println("I am in main of SelectReportByBatch");
					// window.selectingByDateFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
					LOGGER.addHandler(fh);
					LOGGER.severe(e.getMessage());
				}
			}
		});
	}

	/** I create provisionally the list of reports (I have to get it) **/
	public static void creatingProvissionaryReports() {

		// ReportLocation reportLoc1 = null;
		// ReportLocation reportLoc2 = null;
		// ReportLocation reportLoc3 = null;
		// try {
		// reportLoc1 = new ReportLocation("settlemen_report_batch_121.csv",
		// "settlement1", new URL("https://www"), "2012-12-05",
		// "242 bytes");
		// reportLoc2 = new ReportLocation("settlemen_report_batch_122.csv",
		// "settlement2", new URL("https://www"), "2012-12-06",
		// "542 bytes");
		// reportLoc3 = new ReportLocation("settlemen_report_batch_123.csv",
		// "settlement3", new URL("https://www"), "2012-12-07",
		// "742 bytes");
		// } catch (MalformedURLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// reports = new ArrayList<ReportLocation>();
		// reports.add(reportLoc1);
		// reports.add(reportLoc2);
		// reports.add(reportLoc3);

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		creatingProvissionaryReports();
		selectingByBatchFrame = new JFrame();
		// selectingByDateFrame.pack();c
		selectingByBatchFrame.setTitle("AdyenReportMerger");
		// selectingByDateFrame.setBounds(200, 400, 638, 345);
		selectingByBatchFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		selectingByBatchFrame.setSize(new Dimension(1200, 400));

		GridLayout myLayout = new GridLayout(8, 1);
		selectingByBatchFrame.getContentPane().setLayout(myLayout);

		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) panel_1.getLayout();
		flowLayout_4.setVgap(10);
		flowLayout_4.setHgap(10);
		flowLayout_4.setAlignment(FlowLayout.LEFT);
		selectingByBatchFrame.getContentPane().add(panel_1);

		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panel_2.getLayout();
		flowLayout_3.setVgap(6);
		flowLayout_3.setHgap(10);
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		selectingByBatchFrame.getContentPane().add(panel_2);

		JPanel panel_3 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_3.getLayout();
		flowLayout_2.setVgap(6);
		flowLayout_2.setHgap(10);
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		selectingByBatchFrame.getContentPane().add(panel_3);

		JPanel panel_4 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_4.getLayout();
		flowLayout_1.setVgap(6);
		flowLayout_1.setHgap(10);
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		selectingByBatchFrame.getContentPane().add(panel_4);

		JPanel panel_5 = new JPanel();
		FlowLayout flowLayout_8 = (FlowLayout) panel_5.getLayout();
		flowLayout_8.setAlignment(FlowLayout.LEFT);
		selectingByBatchFrame.getContentPane().add(panel_5);
		
		
		JPanel panel_6 = new JPanel();
		selectingByBatchFrame.getContentPane().add(panel_6);
		
		
		JPanel panel_7 = new JPanel();
		selectingByBatchFrame.getContentPane().add(panel_7);
		//flowLayout_6.setAlignment(FlowLayout.LEFT);
		

		JLabel description = new JLabel(
				"Merging by batch range. Please select range of batches you would like to include in the new report:");
		panel_1.add(description);

		JLabel olderBatch = new JLabel("Start batch:");
		panel_2.add(olderBatch);

		olderBatchCombo = new JComboBox<ReportLocation>();
		panel_2.add(olderBatchCombo);
		for (ReportLocation report : reports) {
			olderBatchCombo.addItem(report);
		}
		olderBatchCombo.setSelectedItem((ReportLocation) reports.get(0));

		JLabel lastBatch = new JLabel("End batch:  ");
		panel_3.add(lastBatch);

		lastBatchCombo = new JComboBox<ReportLocation>();
		panel_3.add(lastBatchCombo);
		for (ReportLocation report : reports) {
			lastBatchCombo.addItem(report);
		}
		lastBatchCombo.setSelectedIndex(0);
	
		lastBatchCombo.addItemListener(new ItemChangeListener());
		
		olderBatchCombo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				ReportLocation selectedReport = (ReportLocation) olderBatchCombo
						.getSelectedItem();
				boolean chargingSecondCombo = false;
				lastBatchCombo.removeAllItems();

				for (ReportLocation report : reports) {
					if (report.equals(selectedReport)) {
						chargingSecondCombo = true;
						lastBatchCombo.addItem(report);
					} else if (chargingSecondCombo)
						lastBatchCombo.addItem(report);
				}
				lastBatchCombo.setSelectedIndex(0);
				selectedList();
				updateAppropiateSizeMessage();
			}
		});

		mergeDesired = new JCheckBox(
				"I would like to Merge these reports in to one report", true);
		panel_4.add(mergeDesired);
		deleteDLocalFilesCheckBox = new JCheckBox("I would like to delete the individual downloaded reports", false);
		if (reports.size() == 0 || reports.size() == 1) {
			mergeDesired.setEnabled(false);
			mergeDesired.setSelected(false);
			deleteDLocalFilesCheckBox.setSelected(false);
			deleteDLocalFilesCheckBox.setEnabled(false);
		}
		
		
		panel_5.add(deleteDLocalFilesCheckBox);

		sizeTime = new JLabel ("");
		sizeTime.setHorizontalAlignment(SwingConstants.LEFT);
		panel_6.add(sizeTime);
		
		confirmButton = new JButton("Confirm");
		panel_7.add(confirmButton);
		confirmButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				// confirmButton.setEnabled(false);
				// ReportLocation srtB = (ReportLocation) olderBatchCombo
				// .getSelectedItem();
				// ReportLocation endB = (ReportLocation) lastBatchCombo
				// .getSelectedItem();
				//
				// System.out.println(srtB.getBatchNumber());
				// System.out.println(endB.getBatchNumber());
				//
				// (firstWindow.getController()).getSelectedBatchNumber(
				// srtB.getBatchNumber(), endB.getBatchNumber(), false,
				// "settlement_detail_report");
				selectedList();
				if (selectedReports.size() == 1){
					mergeDesired.setSelected(false);
					deleteDLocalFilesCheckBox.setSelected(false);					
				}
				selectingByBatchFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				ArrayList<ReportLocation> auxSelectedReports = controller.getSelectedReports(selectedReports, deleteDLocalFilesCheckBox.isSelected(),mergeDesired.isSelected(),selectedReports.get(0).getReportType().getReportFileNameOrErrorDescription()+"_merged");
				if (auxSelectedReports.get(0).getReportType().isError()){
					new ErrorScreen(selectingByBatchFrame,controller);
				}else {
					selectingByBatchFrame.setCursor(Cursor.getDefaultCursor());
					new DownloadScreen(selectingByBatchFrame, controller, interfaceFrame);
				}
			}
			
			});

		backButton = new JButton("Back");
		backButton.setBounds(315, 118, 89, 23);
		panel_7.add(backButton, BorderLayout.SOUTH);
				
						JPanel panel = new JPanel();
						selectingByBatchFrame.getContentPane().add(panel);
						FlowLayout flowLayout = (FlowLayout) panel.getLayout();
						flowLayout.setVgap(10);
						flowLayout.setHgap(10);
						flowLayout.setAlignment(FlowLayout.LEFT);
						FlowLayout flowLayout_5 = (FlowLayout) panel.getLayout();
						flowLayout_5.setVgap(10);
						flowLayout_5.setHgap(10);
						flowLayout_5.setAlignment(FlowLayout.LEFT);
						FlowLayout flowLayout_6 = (FlowLayout) panel.getLayout();
						flowLayout_6.setVgap(10);
						flowLayout_6.setHgap(10);
						flowLayout_6.setAlignment(FlowLayout.LEFT);
						FlowLayout flowLayout_7 = (FlowLayout) panel.getLayout();
						flowLayout_7.setVgap(10);
						flowLayout_7.setHgap(10);

		backButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				// System.exit(0);
				selectingByBatchFrame.setVisible(false);
				Rectangle currentPossition = selectingByBatchFrame.getBounds();
				interfaceFrame.setVisible(true);
				interfaceFrame.setBounds(currentPossition);
			}
		});
		
		selectingByBatchFrame.setVisible(true);		
	}
	
	/**
	 * SHOW A MESSAGE ACCORDING WITH THE SELECTED REPORTS - WILL ALLOW OR NOT
	 * CONTINUE
	 */
	public void updateAppropiateSizeMessage() {
		//ArrayList<ReportLocation> reportsArrayList = new ArrayList<ReportLocation>();

		StringBuilder stringBuilder = new StringBuilder(100);

		
		double totalSize = Controller.getTotalSize(selectedReports);
		String totalSizeString = "";

		// I convert to MB or GB or leave it as Byte depending on how big the
		// number is
		if (totalSize > 1024){
			if (totalSize > 1048576) {
				if (totalSize < 1073741824) {
					totalSizeString = Math.round(totalSize / 1048576) + "MB";
				} else {
					totalSizeString = Math.round(totalSize / 1073741824) + "GB";
				}
			} else {
				totalSizeString = Math.round(totalSize / 1024) + "KB";
			}
		}else {
			totalSizeString = totalSize + "Bytes";
		}	
		if (totalSize > 209715200) {
			stringBuilder
					.append("<html><body>The size of the requested report would have a size bigger than 200 MB, it's not possible generating that. Please try again with an smaller range.</body></html>");
		} else if (totalSize == 0) {
			stringBuilder.append("");

		} else {
			stringBuilder
					.append("<html><body>The size of the new file will be around "
							+ totalSizeString
							+ ". Would you like to continue?</body></html>");

		}
		sizeTime.setText(stringBuilder.toString());

	}
	
	

	public void selectedList() {
		int olderIndex = olderBatchCombo.getSelectedIndex();
		int lastIndex = lastBatchCombo.getSelectedIndex();
		
		selectedReports = new ArrayList<ReportLocation>(reports.subList(
				olderIndex, olderIndex + lastIndex + 1));
	}
	private String calculateTotalTime (){
		int connectionDelay =selectedReports.size() * 2;//extra seconds pr connection
		
		 int TotalTime = (int) ((50 * Controller.getTotalSize(selectedReports) * 1) / 100000000)+connectionDelay;
	     int totalHours = (int) Math.floor((TotalTime / 3600));
	     int totalHoursMod = (TotalTime % 3600);
	     int totalMin = (int) Math.floor(totalHoursMod / 60);
	     int totalMinMod = (totalHoursMod % 60);
	     int totalSec = (int) Math.floor((totalMinMod));
	     		
		return totalHours+":"+totalMin+":"+totalSec;
	}

	class ItemChangeListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent event) {
			if (event.getStateChange() == ItemEvent.SELECTED) {
				// ReportLocation item = (ReportLocation) event.getItem();
				selectedList();
				updateAppropiateSizeMessage();
				// do something with object
			}
		}
	}
}
