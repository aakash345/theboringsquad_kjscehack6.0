package com.ars.kjse;

public class AssSubmissionModel {
    String fileurl, submitted;

    public AssSubmissionModel() {
    }

    public AssSubmissionModel(String fileurl, String submitted) {
        this.fileurl = fileurl;
        this.submitted = submitted;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

    public String getSubmitted() {
        return submitted;
    }

    public void setSubmitted(String submitted) {
        this.submitted = submitted;
    }
}
