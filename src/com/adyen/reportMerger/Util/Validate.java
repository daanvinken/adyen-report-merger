package com.adyen.reportMerger.Util;

import com.adyen.reportMerger.entities.ReportLevels;
import com.adyen.reportMerger.gui.StartScreen;

/**
 * Created by andrew on 9/22/16.
 * Class to validate the startscreen inputs
 */
public final class Validate {

    public Validate() {

    }

    public static boolean validateStartscreen () {
        StartScreen startScreen = StartScreen.getInstance();

        boolean errors = false;

        // Check if the path is correct ///what is going on here?
        if (startScreen.getPath() == null) {

            startScreen.getPathError().setText("The selected path: " + startScreen.getPath()
                    + " is incorrect. Please, select a correct path.");
            errors = true;
        }

        if ((new String(startScreen.getPasswordField().getPassword())).length() < 20) {
            startScreen.getPasswordError()
                    .setText("A password has to have 20 or more chars.");
            startScreen.getPasswordField().moveCaretPosition(0);
            errors = true;
        }

        if (startScreen.getMerchantCode().getText().isEmpty()
                || startScreen.getMerchantCode().getText().length() < 4) {
            startScreen.getMerchantCodeError()
                    .setText("A merchant code has to have 4 or more chars.");
            startScreen.getMerchantCode().moveCaretPosition(0);
            errors = true;
        }

        if (startScreen.getCompanyCode().getText().isEmpty()
                || startScreen.getCompanyCode().getText().length() < 4) {
            startScreen.getCompanyCodeError()
                    .setText("A company code has to have 4 or more chars.");
            startScreen.getCompanyCode().moveCaretPosition(0);
            errors = true;
        }

        if (startScreen.getReportUserName().getText().isEmpty()
                || startScreen.getReportUserName().getText().length() < 4) {
            startScreen.getReportUserNameError()
                    .setText("A report user has to have 4 or more chars.");
            startScreen.getReportUserName().moveCaretPosition(0);
            errors = true;
        }

//         Controlling blank spaces
        String passS = new String(startScreen.getPasswordField().getPassword());
        if (passS.contains("\t")
                || passS.contains("\n")
                || passS.contains("\r")
                || passS.contains("\f")
                || passS.contains(" ")) {
            startScreen.getPasswordError()
                    .setText("A password can not content blank spaces");
            startScreen.getPasswordField().moveCaretPosition(0);
            errors = true;
        }

        if (ReportLevels.getLevelFromDescription(startScreen.getLevelCombo().getSelectedItem().toString() ) != ReportLevels.MULTIMERCHANT && (startScreen.getMerchantCode().getText().contains("\t")
                || startScreen.getMerchantCode().getText().contains("\n")
                || startScreen.getMerchantCode().getText().contains("\r")
                || startScreen.getMerchantCode().getText().contains("\f")
                || startScreen.getMerchantCode().getText().contains(" "))) {
            startScreen.getMerchantCodeError()
                    .setText("A merchant code can have blank spaces on "
                            + startScreen.getLevelCombo().getSelectedItem().toString() + ".");
            startScreen.getMerchantCode().moveCaretPosition(0);
            errors = true;
        }

        if (startScreen.getCompanyCode().getText().contains(" ")
                || startScreen.getCompanyCode().getText().contains("\n")
                || startScreen.getCompanyCode().getText().contains("\r")
                || startScreen.getCompanyCode().getText().contains("\f")
                || startScreen.getCompanyCode().getText().contains(" ")) {
            startScreen.getCompanyCodeError()
                    .setText("A company code can not have blank spaces.");
            startScreen.getCompanyCode().moveCaretPosition(0);
            errors = true;
        }

        if (startScreen.getReportUserName().getText().contains(" ")
                || startScreen.getReportUserName().getText().contains("\n")
                || startScreen.getReportUserName().getText().contains("\r")
                || startScreen.getReportUserName().getText().contains("\f")) {

            startScreen.getReportUserNameError()
                    .setText("A report user can not have blank spaces.");
            startScreen.getReportUserName().moveCaretPosition(0);
            errors = true;
        }

        return errors;

    }
}
