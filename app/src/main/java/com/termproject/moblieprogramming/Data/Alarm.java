package com.termproject.moblieprogramming.Data;

public class Alarm {
    String projecttitle;
    String subprojecttitle;
    String message;
    String date;
    public Alarm(){};

    public Alarm(String projecttitle, String subprojecttitle, String message, String date) {
        this.projecttitle = projecttitle;
        this.subprojecttitle = subprojecttitle;
        this.message = message;
        this.date = date;
    }

    public String getProjecttitle() {
        return projecttitle;
    }

    public void setProjecttitle(String projecttitle) {
        this.projecttitle = projecttitle;
    }

    public String getSubprojecttitle() {
        return subprojecttitle;
    }

    public void setSubprojecttitle(String subprojecttitle) {
        this.subprojecttitle = subprojecttitle;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
