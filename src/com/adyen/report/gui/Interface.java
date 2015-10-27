package com.adyen.report.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import com.adyen.report.entities.ReportLocation;
import com.adyen.report.entities.ReportTypes;

public class Interface  {
	private static JFrame frame;

	private static JLabel levelLabel;
	private static JLabel reportTypeLabel;
	private static JLabel mergeByLabel;
	private static JLabel merchantCodeLabel;
	private static JLabel companyCodeLabel;
	private static JLabel passwordLabel;
	private static JLabel reportUserLabel;
	private static JLabel introduction;
	private static JLabel reportUserLabelEnd;

	private static JLabel merchantCodeError;
	private static JLabel companyCodeError;
	private static JLabel reportUserNameError;
	private static JLabel passwordError;
	private static JLabel pathError;

	private static JTextField merchantCode;
	private static JTextField companyCode;
	private static JTextField reportUserName;

	private static JPasswordField passwordField;

	// private static JComboBox<String> levelCombo;
	/*
	 * levelInfoLabel:necessary while just there is reports available for
	 * Merchant Level
	 */
	private static JLabel levelInfoLabel;
	private static JComboBox<String> reportTypeCombo;
	private static JComboBox<String> mergeByCombo;

	private static JButton pathButton;
	private static JButton continueButton;
	private static JButton exitButton;

	private static Controller cont;
	private static String path;
	private static JFileChooser chooser;

	private static ReportTypes selectedReportType;
	
	private static JPanel panel_3;

	private static JPopupMenu popup;

	private static JMenuItem menuItem;
	
	
	

	public static String getPath() {
		return path;
	}

	public static void setPath(String path) {
		Interface.path = path;
	}

	/**
	 * @param args
	 */

	public void setVisibility(boolean b) {
		frame.setVisible(b);
	}

	public Controller getController() {
		return cont;
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	private static  void createAndShowGUI() {

		// Create and set up the window.
		frame = new JFrame("Adyen Report Merger");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(1200, 400));
		GridLayout myLayout = new GridLayout(10, 1);
		frame.getContentPane().setLayout(myLayout);

		/** Creating panels **/

		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) panel_1.getLayout();
		flowLayout_4.setVgap(10);
		flowLayout_4.setHgap(10);
		flowLayout_4.setAlignment(FlowLayout.LEFT);
		frame.getContentPane().add(panel_1);

		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panel_2.getLayout();
		flowLayout_3.setVgap(6);
		flowLayout_3.setHgap(10);
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		frame.getContentPane().add(panel_2);

		panel_3 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_3.getLayout();
		flowLayout_2.setVgap(6);
		flowLayout_2.setHgap(10);
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		frame.getContentPane().add(panel_3);

		JPanel panel_4 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_4.getLayout();
		flowLayout_1.setVgap(6);
		flowLayout_1.setHgap(10);
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		frame.getContentPane().add(panel_4);

		JPanel panel_5 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_5.getLayout();
		flowLayout.setVgap(10);
		flowLayout.setHgap(10);
		flowLayout.setAlignment(FlowLayout.LEFT);
		frame.getContentPane().add(panel_5);

		JPanel panel_6 = new JPanel();
		FlowLayout flowLayout_6 = (FlowLayout) panel_6.getLayout();
		flowLayout_6.setVgap(10);
		flowLayout_6.setHgap(10);
		flowLayout_6.setAlignment(FlowLayout.LEFT);
		frame.getContentPane().add(panel_6);

		JPanel panel_7 = new JPanel();
		FlowLayout flowLayout_7 = (FlowLayout) panel_7.getLayout();
		flowLayout_7.setVgap(10);
		flowLayout_7.setHgap(10);
		flowLayout_7.setAlignment(FlowLayout.LEFT);
		frame.getContentPane().add(panel_7);

		JPanel panel_8 = new JPanel();
		FlowLayout flowLayout_8 = (FlowLayout) panel_8.getLayout();
		flowLayout_8.setVgap(10);
		flowLayout_8.setHgap(10);
		flowLayout_8.setAlignment(FlowLayout.LEFT);
		frame.getContentPane().add(panel_8);

		JPanel panel_9 = new JPanel();
		FlowLayout flowLayout_9 = (FlowLayout) panel_7.getLayout();
		flowLayout_9.setVgap(10);
		flowLayout_9.setHgap(10);
		flowLayout_9.setAlignment(FlowLayout.LEFT);
		frame.getContentPane().add(panel_9);
		
		
		
	
		//Creates the right-click window for copy and paste. This is an awsome class from StackOverflow.
	    MouseListener popupListener = new ContextMenuMouseListener();
	  
//	    menuBar.addMouseListener(popupListener);
	    
		pathButton = new JButton("Choose path to save the reports");
		chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File(System
				.getProperty("user.home")));
//		System.out.println("This is the current directory: "
//				+ chooser.getCurrentDirectory());
		path = chooser.getCurrentDirectory().getAbsolutePath();
		pathButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				chooser.setDialogTitle("Choose path to save the file");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				//
				// disable the "All files" option.
				//
				chooser.setAcceptAllFileFilterUsed(false);
								
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					
					path = chooser.getSelectedFile().getAbsolutePath();
					File f = new File(path);
					if (f.exists()) {

//						System.out.println("The selected path is: " + path);
						pathError.setText(path);
						
					}

					else {
						path = System.getProperty("user.home");
						pathError
								.setText("The path does not exist, using default path: "
										+ path);
					}
				}

			}
		});

		JPanel panel_10 = new JPanel();
		FlowLayout flowLayout_10 = (FlowLayout) panel_7.getLayout();
		flowLayout_10.setVgap(10);
		flowLayout_10.setHgap(10);
		flowLayout_10.setAlignment(FlowLayout.LEFT);
		frame.getContentPane().add(panel_10);

		/** Creating panels content **/

		// Panel 1
		introduction = new JLabel(
				"Please introduce the values according with your desires: ");
		panel_1.add(introduction);

		// Panel 2
		levelLabel = new JLabel("Report level: ");
		panel_2.add(levelLabel);

		// levelCombo = new JComboBox<String>();
		levelInfoLabel = new JLabel("Merchant level");
		// levelCombo.addItem("Company level");
		// levelCombo.addItem("Merchant level");
		reportTypeCombo = new JComboBox<String>();

		// if(levelCombo.getSelectedIndex() == 1){
		reportTypeCombo.removeAllItems();
		reportTypeCombo.addItem("Settlement report");
		reportTypeCombo.addItem("Received payments report");
		reportTypeCombo.addItem("Payments accounting report");
		reportTypeCombo.addItem("Dispute report");
		reportTypeCombo.setSelectedIndex(0);
		
		panel_2.add(levelInfoLabel);

		// Panel 3
		reportTypeLabel = new JLabel("Report type: ");
		panel_3.add(reportTypeLabel);
		panel_3.add(reportTypeCombo);
		panel_3.add(reportTypeCombo);

		// Panel 4
		mergeByLabel = new JLabel("Merge by: ");
		panel_4.add(mergeByLabel);

		mergeByCombo = new JComboBox<String>();
		mergeByCombo.addItem("Batch range");
		mergeByCombo.addItem("Date range");
		mergeByCombo.setSelectedIndex(0);
		panel_4.add(mergeByCombo);

		// Panel 5
		companyCodeLabel = new JLabel("Company Code: ");
		panel_5.add(companyCodeLabel);

		companyCode = new JTextField();
		companyCode.addMouseListener(popupListener);
		companyCode.setText("");
		panel_5.add(companyCode);
		companyCode.setColumns(10);
		
		FocusListener companyCodeListener = new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				
//				System.out.println("in companyCodeListener");
				reportUserLabelEnd.setText("@Company." + companyCode.getText());
			}

			@Override
			public void focusGained(FocusEvent e) {
				
			}
		};
		companyCode.addFocusListener(companyCodeListener);

		companyCodeError = new JLabel("");
		panel_5.add(companyCodeError);
		
		// Panel 6
		merchantCodeLabel = new JLabel("Merchant Code: ");
		panel_6.add(merchantCodeLabel);

		merchantCode = new JTextField();
		merchantCode.addMouseListener(popupListener);
		panel_6.add(merchantCode);
		merchantCode.setColumns(10);

		merchantCodeError = new JLabel("");
		panel_6.add(merchantCodeError);

		

		

		// Panel 7
		reportUserLabel = new JLabel("Report user: ");
		panel_7.add(reportUserLabel);

		reportUserName = new JTextField();
		reportUserName.addMouseListener(popupListener);
		panel_7.add(reportUserName);
		reportUserName.setColumns(10);

		reportUserLabelEnd = new JLabel("@Company." + companyCode.getText());
		panel_7.add(reportUserLabelEnd);

		reportUserNameError = new JLabel("");
		panel_7.add(reportUserNameError);

		// Panel 8
		passwordLabel = new JLabel("Report user password: ");
		panel_8.add(passwordLabel);

		passwordField = new JPasswordField();
		passwordField.addMouseListener(popupListener);
		panel_8.add(passwordField);
		passwordField.setColumns(10);

		passwordError = new JLabel("");
		panel_8.add(passwordError);

		// Panel 9

		panel_9.add(pathButton);
		pathError = new JLabel(System.getProperty("user.home"));
		panel_9.add(pathError);

		// Panel 10
		continueButton = new JButton("Continue");
		panel_10.add(continueButton);

		ActionListener continueListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// initialize errors
				merchantCodeError.setText("");
				companyCodeError.setText("");
				reportUserNameError.setText("");
				passwordError.setText("");
				pathError.setText(path);

				boolean isMerchant = false;

				// if (levelCombo.getSelectedIndex() == 0)
				// isMerchant = false;
				// if (levelCombo.getSelectedIndex() == 1)
				isMerchant = true;

				//0: settlement, 1:received_payments, 2:payments_accounting
				//int selectedReportTypePossition = 0;

				
				selectedReportType = ReportTypes.getReportTypePerNumber(reportTypeCombo.getSelectedIndex());
//				System.out.println("LINE 357 INTERFACE: The selected report type is: "+selectedReportType);

				char[] arrayPassword = passwordField.getPassword();
				
				if (!validatingInputs()) {
					String name = companyCode.getText();
					if (isMerchant) {
						name = merchantCode.getText();
					}
					 cont = new	 Controller(isMerchant,name,reportUserName.getText()+"@Company."+companyCode.getText(),arrayPassword,selectedReportType);
										
					cont.connect(path + "/");

					ArrayList<ReportLocation> varReports = cont.getR();
					
					// I check if there is any report of this type for this Merchant/Company account
					if (varReports.isEmpty()){
						
						ReportLocation rl = new ReportLocation(ReportTypes.ERROR_NO_REPORTS_TYPE);
						ArrayList<ReportLocation> rList = new ArrayList<ReportLocation>();
						rList.add(rl);
						cont.setR(rList);
						//System.out.println("LINE 388 INTERFACE: The selected report type is: "+selectedReportType);
						new ErrorScreen(frame,cont,selectedReportType);
						
					}else{
					
						ReportTypes varReportType = varReports.get(0).getReportType();
					
						if (varReportType.isError()) {
						
	//						System.out.println("Name: "
	//								+ cont.getR().get(0).getReportName()
	//								+ " and type: "
	//								+ cont.getR().get(0).getReportType());
							new ErrorScreen(frame, cont);
							
						} else {
	
							if (mergeByCombo.getSelectedIndex() == 0) {
								new SelectReportByBatch(frame, cont);
							} else {
								new SelectReportByDate(frame, cont);
							}
						}
				}

					// continueButton.setEnabled(false);
				} else {
					if (passwordError.getText().compareTo("") != 0) {
						passwordField.setText("");
					}
				}
			}
			
			/**This method returns true when there is any error in the inputs, otherwise false.*/
			private boolean validatingInputs() {
				boolean errors = false;

				// Check if the path is correct
				if (path.equals(null)) {

					pathError.setText("The selected path: " + path
							+ " is incorrect. Please, select a correct path.");
					errors = true;
				}

				// Control sizes
				if ((new String(passwordField.getPassword())).length() < 20) {
					passwordError
							.setText("Password incorrect. A password has to have 20 or more characters.");
					passwordField.moveCaretPosition(0);
					errors = true;
				}

				if (merchantCode.getText().isEmpty()
						|| merchantCode.getText().length() < 4) {
					merchantCodeError
							.setText("Merchant code incorrect. A merchant code has to have 4 or more characters.");
					merchantCode.moveCaretPosition(0);
					errors = true;
				}

				if (companyCode.getText().isEmpty()
						|| companyCode.getText().length() < 4) {
					companyCodeError
							.setText("Company code incorrect. A company code has to have 4 or more characters.");
					companyCode.moveCaretPosition(0);
					errors = true;
				}

				if (reportUserName.getText().isEmpty()
						|| reportUserName.getText().length() < 4) {
					reportUserNameError
							.setText("Report user incorrect. A report user has to have 4 or more characters.");
					reportUserName.moveCaretPosition(0);
					errors = true;
				}

				// Controlling blank spaces
				String passS = new String(passwordField.getPassword());
				if (passS.contains("\t")
						|| passS.contains("\n")
						|| passS.contains("\r")
						|| passS.contains("\f")
						|| passS.contains(" ")) {
//					System.out.println(passS);
					passwordError
							.setText("Password incorrect. A password can not content blank spaces");
					passwordField.moveCaretPosition(0);
					errors = true;
				}

				if (merchantCode.getText().contains("\t")
						|| merchantCode.getText().contains("\n")
						|| merchantCode.getText().contains("\r")
						|| merchantCode.getText().contains("\f")
						|| merchantCode.getText().contains(" ")) {
					merchantCodeError
							.setText("Merchant code incorrect. A merchant code can not content blank spaces.");
					merchantCode.moveCaretPosition(0);
					errors = true;
				}

				if (companyCode.getText().contains(" ")
						|| companyCode.getText().contains("\n")
						|| companyCode.getText().contains("\r")
						|| companyCode.getText().contains("\f")
						|| companyCode.getText().contains(" ")) {
					companyCodeError
							.setText("Company code incorrect. A company code can not content blank spaces.");
					companyCode.moveCaretPosition(0);
					errors = true;
				}

				if (reportUserName.getText().contains(" ")
						|| reportUserName.getText().contains("\n")
						|| reportUserName.getText().contains("\r")
						|| reportUserName.getText().contains("\f")) {
					reportUserNameError
							.setText("Report user incorrect. A report user can not content blank spaces.");
					reportUserName.moveCaretPosition(0);
					errors = true;
				}

//				// Controlling no special characters
//				
//				// First of all allow special characters are not evaluated for every checked input (_,- and .)
//				String aux = merchantCode.getText(); 
//				aux.replaceAll("_", "");
//				aux.replaceAll("-", "");
//				aux.replaceAll(".", "");
//				System.out.println("This is the merchantCode removing _, - and ." + aux);
//				
//				//Then the pattern is applied
//				Pattern pattern = Pattern.compile("[a-zA-Z0-9]");
//				Matcher matcher = pattern.matcher(aux);
//				System.out.println("Contains the pattern?: "+matcher.find());
//				boolean justAllowCharacters = matcher.matches();
//				System.out.println("Doesn it match with the pattern?: "+justAllowCharacters);
//				
//				 if (!justAllowCharacters){
//					 merchantCodeError.setText("Merchant code incorrect. A merchant code can not content special characters different to '_' or '-'.");
//					 merchantCode.moveCaretPosition(0);
//					 errors = true;
//				 }
//				
//				
//				aux = companyCode.getText();
//				aux.replaceAll("_", "");
//				aux.replaceAll("-", "");
//				aux.replaceAll(".", "");
//				
//				matcher = pattern.matcher(aux);
//				justAllowCharacters = matcher.matches();
//				if (!justAllowCharacters){
//					companyCodeError.setText("Company code incorrect. A company code can not content special characters different to '_' or '-'.");
//					companyCode.moveCaretPosition(0);
//					errors = true;
//				}
//				
//				aux = reportUserName.getText();
//				aux.replaceAll("_", "");
//				aux.replaceAll("-", "");
//				aux.replaceAll(".", "");
//				
//				matcher = pattern.matcher(aux);
//				justAllowCharacters = matcher.matches();
//				if (!justAllowCharacters){
//					reportUserNameError.setText("Report user name incorrect. A report user name can not content special characters different to '_' or '-'.");
//					reportUserName.moveCaretPosition(0);
//					errors = true;
//				}

				return errors;
			}
		};
		continueButton.addActionListener(continueListener);
		exitButton = new JButton("Exit");
		panel_10.add(exitButton);
		frame.setVisible(true);

		exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);

			}
		});

	}

	

	public static void main(String[] args) {
		/**
		 * Schedule a job for the event-dispatching thread: creating and showing
		 * this application's GUI.
		 */
		
		String java = System.getProperty("java.specification.version");
		double version = Double.valueOf(java);
		double reqVer = 1.6;
		if (version < reqVer) {
			
			new ErrorScreen("The required version is "+reqVer+" , please update Java");
		}
		else{
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					createAndShowGUI();
				}
			});	
		}
		
	}


	
}

