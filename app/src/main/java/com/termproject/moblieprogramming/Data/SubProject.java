package com.termproject.moblieprogramming.Data;

import com.termproject.moblieprogramming.Data.Project;

public class SubProject {
    String id;
    String title;
    String respid;
    String startdate;
    String deadline;
    int progress;
    int weight;
    int totaltask;
    int totalweight;
    SubProject() {};

    public SubProject(String id, String title, String respid, String startdate, String deadline, int progress, int weight, int totaltask, int totalweight) {
        this.id = id;
        this.title = title;
        this.respid = respid;
        this.startdate = startdate;
        this.deadline = deadline;
        this.progress = progress;
        this.weight = weight;
        this.totaltask = totaltask;
        this.totalweight = totalweight;
    }

    public String getId() {return id;}
    public String getTitle() {return title;}
    public String getRespid() {return respid;}
    public String getStartdate() {return startdate;}
    public String getDeadline() {return deadline;}
    public int getProgress() {return progress;}
    public int getWeight() {return weight;}
    public int getTotaltask() {return totaltask;}
    public int getTotalweight() {return totalweight;}

    public void setId(String id) {this.id = id;}
    public void setTitle(String title) {this.title = title;}
    public void setRespid(String respid) {this.respid = respid;}
    public void setStartdate(String startdate) {this.startdate = startdate;}
    public void setDeadline(String deadline) {this.deadline = deadline;}
    public void setProgress(int progress) {this.progress = progress;}
    public void setWeight(int weight) {this.weight = weight;}
    public void setTotaltask(int totaltask) {this.totaltask = totaltask;}
    public void setTotalweight(int totalweight) {this.totalweight = totalweight;}
}
