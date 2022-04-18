package com.ars.kjse;

public class AssignmentModel {
    String filename;
    String fileurl,subject;
    String year,dept,div;
    String due_date;
    String due_time;

    public String getAaay() {
        return aaay;
    }

    public void setAaay(String aaay) {
        this.aaay = aaay;
    }

    String aaay;

    public AssignmentModel(String subject,String filename, String fileurl, String year, String dept, String div, String due_date, String due_time, String aaay) {
        this.filename = filename;
        this.fileurl = fileurl;
        this.year = year;
        this.dept = dept;
        this.div = div;
        this.due_date = due_date;
        this.due_time = due_time;
        this.aaay = aaay;
    }

    public AssignmentModel() {
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getDiv() {
        return div;
    }

    public void setDiv(String div) {
        this.div = div;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getDue_time() {
        return due_time;
    }

    public void setDue_time(String due_time) {
        this.due_time = due_time;
    }
}
