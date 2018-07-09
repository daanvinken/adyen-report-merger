package com.adyen.reportMerger.entities;

/**
 * Created by andrew on 9/21/16.
 */
public enum ErrorTypes {
    ERROR_NO_REPORTS_TYPE ("No Reports founds for these accounts"),
    ERROR_INCORRECT_DETAILS ("The provided login credentials are incorrect"),
    ERROR_INCORRECT_ACCOUNT ("The company or merchant account is incorrect"),
    ERROR_CONNECTION ("There was an Error in the Connection"),
    ERROR_GET_REPORTS ("These was an Error fetching the reports"),
    ERROR_DOWNLOAD_FILE ("There was an Error downloading the files");

    String description;

    ErrorTypes(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    /*
    some old error codes that from the origional project

//    ERROR_INVALID_CREDENTIALS (401,"Incorrect Credentials.Please, review the inserted details and try again.","Incorrect Credentials. Please, review the inserted details and try again.", true),
//    ERROR_NO_REPORTS_PERIOD (402, "There are no [Report_Type] available in the selected period of time. Please, select a new period.", "There are no [Report_Type] available in the selected period of time. Please, select a new period.", true),
//    ERROR_ENDDATE_BEFORE_STARTDATE (403, "Date. End date before start date.", "Date. End date before start date.", true),
//    ERROR_PAGE_NOT_FOUND (404, "Page not found.", "Page not found.", true),
//    ERROR_WRITTING_IN_FILE (405, "Problem editing the report. Please, notify adyen support about it and try again removing the created report in the path was choosen.", "Problem editing the report. Please, notify adyen support about it and try again removing the created report in the path was choosen.",true),
//    ERROR_DOWNLOADING_FILE (406, "Problem downloading the reports. Please, notify adyen support about it and try again removing the created reports in the path was choosen.","Problem downloading the reports. Please, notify adyen support about it and try again removing the created reports in the path was choosen.",true),
//    ERROR_NO_REPORTS_TYPE (407, "There are not [Report_Type] available for this account.", "There are not [Report_Type] available for this account.",true),
//    //Not used for now, done manually in ReportMergerGui
//    ERROR_SIZE_PASS (600,"Password incorrect. A password has to have 20 or more characters.","Password incorrect. A password has to have 20 or more characters.", true),
//    ERROR_SIZE_MER (601,"Merchant code incorrect. A merchant code has to have 4 or more characters.","Merchant code incorrect. A merchant code has to have 4 or more characters.", true),
//    ERROR_SIZE_COM (602,"Company code incorrect. A company code has to have 4 or more characters.","Company code incorrect. A company code has to have 4 or more characters.", true),
//    ERROR_SIZE_USER (603,"Report user name incorrect. A report user has to have 4 or more characters.","Report user name incorrect. A report user has to have 4 or more characters.", true),
//
//    ERROR_BLANK_SPACES_PASS (701,"Password incorrect. A password can not content blank spaces.","Password incorrect. A password can not content blank spaces.", true),
//    ERROR_BLANK_SPACES_MER (702,"Merchant code incorrect. A merchant code can not content blank spaces.","Merchant code incorrect. A merchant code can not content blank spaces.",true),
//    ERROR_BLANK_SPACES_COM (703, "Company code incorrect. A company code can not content blank spaces.","Company code incorrect. A company code can not content blank spaces.",true),
//    ERROR_BLANK_SPACES_USER (704, "Report user incorrect. A report user can not content blank spaces.","Report user incorrect. A report user can not content blank spaces.",true);
     */
}
