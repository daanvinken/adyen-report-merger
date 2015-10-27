package com.adyen.report.gui;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import com.adyen.report.connector.ConnectionInfo;
import com.adyen.report.entities.ReportLocation;
import com.adyen.report.entities.ReportTypes;
import com.scc.calendar.datepicker.DatePicker;

public class SelectReportByDate {

	/** ATTRIBUTES **/

	private static JFrame interfaceFrame;
	private static JFrame frame;

	private static Container contentPane;

	private static JCheckBox chckbxDoDelete;
	private static JCheckBox chckbxDoMerge;

	private static Controller controller;

	private static ArrayList<ReportLocation> reports;

	private static JLabel sizeTimeAndErrorLabel;

	private static JButton endDateButton;
	private static JButton startDateButton;
	private static JButton confirmButton;

	private static JList<ReportLocation> listAvailableReports;
	private static DefaultListModel<ReportLocation> model;

	static ObservingTextField endDateTextField, startDateTextField;
	private static JProgressBar progressBar;

	private static boolean canDownload;
	private static FileHandler fh = Controller.getFileHandler();
	private final static Logger LOGGER = Logger.getLogger(ConnectionInfo.class.getName());


	/** CONSTRUCTOR **/
	public SelectReportByDate(JFrame interfaceFrame, Controller controller) {
		Rectangle possition = interfaceFrame.getBounds();
		SelectReportByDate.controller = controller;
		SelectReportByDate.reports = new ArrayList<ReportLocation>(
				controller.getR());
		// System.out.println(reports);
		SelectReportByDate.interfaceFrame = interfaceFrame;
		// System.out.println("I am going to initialize SelectReportByBatch");
		doStart();
		// System.out.println("I have initialized SelectReportByBatch");
		frame.setBounds(possition);
		interfaceFrame.setVisible(false);
		canDownload = false;
		// main(null);
	}

	/** MAIN **/
	public static void main(final String argv[]) {
		// JFrame.setDefaultLookAndFeelDecorated(true);
		// JDialog.setDefaultLookAndFeelDecorated(true);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					doStart();
				} catch (Exception ex) {
					ex.printStackTrace();
					LOGGER.addHandler(fh);
					LOGGER.info(ex.getMessage());
				}
			}
		});

		// doStart(argv);
	}

	/** INITIALIZING THE FRAME **/
	public static void doStart() {
		frame = new JFrame("AdyenReportMerger");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		contentPane = frame.getContentPane();
		frame.getContentPane().setLayout(null);

		/** START DATE */

		JLabel startDateLabel = new JLabel("Start Date");
		frame.setSize(new Dimension(1200, 400));
		frame.getContentPane().add(startDateLabel);

		// Create a text-field that implements Observer to show the start date
		startDateTextField = new ObservingTextField();

		startDateTextField.setBounds(22, 82, 224, 19);
		startDateTextField.setEditable(false);
		startDateTextField.setColumns(20);
		startDateTextField.setText("");
		contentPane.add(startDateTextField);

		// Start date field listener
		startDateTextField.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent e) {
				// System.out.println("Caret listener - for startDateTextField");
				listUpdate();
			}
		});
		// startDateTextField.getDocument().addDocumentListener(
		// new DocumentListener() {
		// public void changedUpdate(DocumentEvent e) {
		// listUpdate();
		// }
		//
		// public void removeUpdate(DocumentEvent e) {
		// listUpdate();
		// }
		//
		// public void insertUpdate(DocumentEvent e) {
		// listUpdate();
		// }
		// });

		// create a button to access to the select date functionality for start
		// date
		String lang = "nl_NL";

		final Locale locale = getLocale(lang);
		startDateButton = new JButton("...");
		startDateButton.setBounds(268, 79, 49, 25);
		Dimension d = startDateButton.getPreferredSize();
		d.height = startDateTextField.getPreferredSize().height;
		d.width = d.height;
		startDateButton.setPreferredSize(d);
		contentPane.add(startDateButton);

		// start date button listener
		startDateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// instantiate the DatePicker
				DatePicker dp = new DatePicker(frame, startDateTextField,
						locale);
				// previously selected date
				Date selectedDate = dp.parseDate(startDateTextField.getText());
				dp.setSelectedDate(selectedDate);
				dp.start(startDateButton);

			};
		});

		/** END DATE */

		JLabel endDateLabel = new JLabel("End Date");
		endDateLabel.setBounds(22, 141, 64, 15);
		frame.getContentPane().add(endDateLabel);

		// Create a text-field that implements Observer to show the start date
		endDateTextField = new ObservingTextField();
		endDateTextField.setBounds(23, 168, 224, 19);
		endDateTextField.setText("");
		endDateTextField.setEditable(false);
		endDateTextField.setColumns(20);
		frame.getContentPane().add(endDateTextField);

		// End date field listener
		endDateTextField.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent e) {
				// System.out.println("Caret listener - for endDateTextField");
				listUpdate();
			}
		});
		// endDateTextField.getDocument().addDocumentListener(new
		// DocumentListener() {
		// public void changedUpdate(DocumentEvent e) {
		// listUpdate();
		// }
		//
		// public void removeUpdate(DocumentEvent e) {
		// listUpdate();
		// }
		//
		// public void insertUpdate(DocumentEvent e) {
		// listUpdate();
		// }
		//
		// });

		// create a button to access to the select date functionality for end
		// date
		endDateButton = new JButton("...");
		endDateButton.setBounds(268, 165, 49, 25);
		endDateButton.setPreferredSize(new Dimension(49, 25));
		frame.getContentPane().add(endDateButton);

		// end date button listener
		endDateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (startDateTextField.equals(null)
						|| startDateTextField.equals("")) {
					sizeTimeAndErrorLabel
							.setText("Please, select at first the start date");
				} else {

					// instantiate the DatePicker
					DatePicker dp = new DatePicker(frame, endDateTextField,
							locale);
					// previously selected date
					Date selectedDate = dp.parseDate(endDateTextField.getText());
					dp.setSelectedDate(selectedDate);
					dp.start(endDateButton);
					updateAppropiateSizeMessage();
				}
			};
		});

		/** CHECK BOXES */

		chckbxDoMerge = new JCheckBox("I would like to Merge these reports also in one report",true);
		chckbxDoMerge.setBounds(22, 218, 415, 23);
		frame.getContentPane().add(chckbxDoMerge);
				
		chckbxDoDelete = new JCheckBox("I would like deleting the individual downloaded reports");
		chckbxDoDelete.setBounds(27, 245, 426, 23);
		frame.getContentPane().add(chckbxDoDelete);
		
		/** CONFIRM */

		confirmButton = new JButton("Confirm");
		confirmButton.setEnabled(false);
		confirmButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (!canDownload) {
					// System.out.println("too big");
				} else {
					// progressBar.setVisible(true);
					canDownload = true;
					String filename = "";
					filename = reports.get(0).getReportType().getReportFileNameOrErrorDescription()+"_Merged";
					
//					frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
//					
//					frame.setCursor(null); //turn off the wait cursor
//					new DownloadScreen(frame, controller, interfaceFrame);
					
					try {
						frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
						controller.getSelectedDateRange(
								Controller.getDate(startDateTextField.getText(), 0),
								Controller.getDate(endDateTextField.getText(), 1),
								chckbxDoMerge.isSelected(),
								chckbxDoDelete.isSelected(), filename);
					} finally {
						frame.setCursor(Cursor.getDefaultCursor());
						new DownloadScreen(frame, controller, interfaceFrame);
					}

				}
			}

		}

		);
		confirmButton.setBounds(419, 336, 117, 25);
		frame.getContentPane().add(confirmButton);

		/** BACK */

		final JButton backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (interfaceFrame != null) {
					// go back to previous screen
					frame.setVisible(false);
					// frame.dispose();
					Rectangle currentPossition = frame.getBounds();
					interfaceFrame.setVisible(true);
					interfaceFrame.setBounds(currentPossition);
				}
				// show error
				backButton.setEnabled(false);
				backButton.setText("Please exit the programm");
			}
		});
		backButton.setBounds(565, 336, 117, 25);
		frame.getContentPane().add(backButton);

		model = new DefaultListModel<ReportLocation>();

		for (ReportLocation r : reports) {
			model.addElement(r);
		}

		/** LIST WITH AVAILABLE REPORTS */

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(804, 37, 315, 263);
		frame.getContentPane().add(scrollPane);
		
				listAvailableReports = new JList<ReportLocation>();
				scrollPane.setViewportView(listAvailableReports);
				listAvailableReports.setEnabled(false);
				
						listAvailableReports.setModel(model);
		
		
		
		/** SIZE INFORMATION */

		sizeTimeAndErrorLabel = new JLabel("");
		sizeTimeAndErrorLabel.setBounds(12, 276, 765, 33);
		frame.getContentPane().add(sizeTimeAndErrorLabel);

		/** CLEAR BUTTONS */
		JButton startDateClearButton = new JButton("Clear");
		startDateClearButton.setBounds(329, 79, 74, 25);
		frame.getContentPane().add(startDateClearButton);
		startDateClearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startDateTextField.setText("");
			}
		});

		JButton endDateClearButton = new JButton("Clear");
		endDateClearButton.setBounds(329, 165, 74, 25);
		frame.getContentPane().add(endDateClearButton);
		
		JLabel lblDescriptionNew = new JLabel("<html><body>Merging by date range. Please select the date generation range you would like to use to choose the reports would be included in the new report:</body></html>");
		lblDescriptionNew.setBounds(22, 12, 791, 33);
		frame.getContentPane().add(lblDescriptionNew);
		
//		progressBar = new JProgressBar(0,reports.size());
//		progressBar.setBounds(22, 335, 148, 14);
//		
//		progressBar.setValue(0);
//		progressBar.setStringPainted(true);
		
//		frame.getContentPane().add(progressBar);
		
		
		
		endDateClearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				endDateTextField.setText("");
			}
		});
		
		frame.setVisible(true);
		
		updateAppropiateSizeMessage();
	}

	/** GET DATE ON GERMANY - THE SAME THAN IN AMSTERDAM */

	private static Locale getLocale(String loc) {
		if (loc != null && loc.length() > 0)
			return new Locale(loc);
		else
			return Locale.GERMANY;
	}

	/** UPDATE THE LIST OF REPORTS */
	private static void listUpdate() {

		String startDateText = startDateTextField.getText();
		String endDateText = endDateTextField.getText();
		ArrayList<ReportLocation> reportsAvailableTmp = new ArrayList<ReportLocation>();

		// Creating the dates with the values the user chose or assign a default
		// value

		// System.out.println("The end date is:" + endDateText
		// + ". And the startDate is: " + startDateText + ".");
		// For every cases it has to be done
		// System.out.println("I call getAvailableReportsForDateRange method");
		reportsAvailableTmp = controller.getAvailableReportsForDateRange(
				Controller.getDate(startDateText, 0),
				Controller.getDate(endDateText, 1));
		// System.out.println("getAvailableReportsForDateRange method finished with "
		// + reportsAvailableTmp.size() + " reports available in this range.");
		showErrorOrUpdateList(reportsAvailableTmp);

	}

	/** UPDATE THE LIST OF REPORTS AVAILABLE OR SHOW THE ERROR */

	private static void showErrorOrUpdateList(
			ArrayList<ReportLocation> gottenReports) {

		ReportLocation errorReport = gottenReports.get(0);
		ReportTypes reportType = errorReport.getReportType();
		String reportName = reportType.getReportFileNameOrErrorDescription();

		if (reportType.isError()) {
			model.removeAllElements();
			listAvailableReports.setModel(model);
			sizeTimeAndErrorLabel.setText(reportName);

		} else {
			model.removeAllElements();
			for (ReportLocation r : gottenReports) {
				model.addElement(r);
			}

			listAvailableReports.setModel(model);
			updateAppropiateSizeMessage();
		}

		// System.out.println("Updating reports list with available reports");

	}

	/**
	 * SHOW A MESSAGE ACCORDING WITH THE SELECTED REPORTS - WILL ALLOW OR NOT
	 * CONTINUE
	 */
	private static void updateAppropiateSizeMessage() {
		ArrayList<ReportLocation> reportsArrayList = new ArrayList<ReportLocation>();

		StringBuilder stringBuilder = new StringBuilder(100);

		for (int i = 0; i < model.size(); i++) {
			ReportLocation report_i = model.get(i);
			reportsArrayList.add(report_i);
		}
		double totalSize = Controller.getTotalSize(reportsArrayList);
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
					canDownload = true;

				}

		sizeTimeAndErrorLabel.setText(stringBuilder.toString());
		if (canDownload) {
			confirmButton.setEnabled(true);
		} else {
			confirmButton.setEnabled(false);
		}
	}
	
	private static void clearTextField(JTextField textField) {
		textField.setText("");
	}

	private String calculateTotalTime() {
		int connectionDelay = model.size() * 3;// extra seconds pr connection

		int TotalTime = (int) ((50 * Controller
				.getTotalSize((List<ReportLocation>) model) * 1) / 1024000)
				+ connectionDelay;
		int totalHours = (int) Math.floor((TotalTime / 3600));
		int totalHoursMod = (TotalTime % 3600);
		int totalMin = (int) Math.floor(totalHoursMod / 60);
		int totalMinMod = (totalHoursMod % 60);
		int totalSec = (int) Math.floor((totalMinMod));

		return totalHours + ":" + totalMin + ":" + totalSec;
	}
	

}

class ObservingTextField extends JTextField implements Observer {
	private static final long serialVersionUID = -9121215994812342536L;

	public void update(Observable o, Object arg) {
		DatePicker dp = (DatePicker) o;
		Calendar calendar = (Calendar) arg;
		// System.out.println("picked=" + dp.formatDate(calendar));
		setText(dp.formatDate(calendar));

	}
}

