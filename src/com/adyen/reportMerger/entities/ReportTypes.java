package com.adyen.reportMerger.entities;

/**
 * Created by andrew on 9/21/16.
 */
public enum ReportTypes {

    SETTLEMENT_DETAIL (0, "settlement_detail_report","Settlement detail report", false),
    RECEIVED_PAYMENTS (1,"received_payments_report","Received payments report", false) ,
    PAYMENTS_ACCOUNTING (2, "payments_accounting_report","Payments accounting report", false),
    DISPUTE_REPORT (3, "dispute_report","Dispute report", false),
    MANUAL_REVIEW_REPORT(4, "manual_review_report", "Case management report", false),
    MARKETPLACE_REPORT(5, "marketplace_payments_accounting_report", "MarketPlace payment account report", false),
    EXTERNAL_SETTLEMENT_DETAILS_C_LEVEL(6, "external_settlement_detail_report", "External settlement detail (C)", false),
    EXTERNAL_SETTLEMENT_DETAILS_S_LEVEL_WITH_INFO(7, "external_settlement_detail_report_with_info_batch", "External settlement detail with info (S)", false);



    public int guiValue;
    public String reportCode;
    public String reportDescription;
    public boolean isError;

    ReportTypes(int guiValues, String reportCode, String reportDescription, boolean isError) {
        this.guiValue = guiValues;
        this.reportCode = reportCode;
        this.reportDescription = reportDescription;
        this.isError = isError;
    }

    public int getGuiValue() {
        return guiValue;
    }

    public void setGuiValue(int guiValue) {
        this.guiValue = guiValue;
    }

    public String getReportCode() {
        return reportCode;
    }

    public void setReportCode(String reportCode) {
        this.reportCode = reportCode;
    }

    public String getReportDescription() {
        return reportDescription;
    }

    public void setReportDescription(String reportDescription) {
        this.reportDescription = reportDescription;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public static ReportTypes getTypeFromDescription(String text) {
        if (text != null) {
            for (ReportTypes rt : ReportTypes.values()) {
                if (text.equalsIgnoreCase(rt.getReportDescription())) {
                    return rt;
                }
            }
        }
        return null;
    }
}
