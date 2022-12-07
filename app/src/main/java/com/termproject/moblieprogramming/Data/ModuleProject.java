package com.termproject.moblieprogramming.Data;

import com.termproject.moblieprogramming.Data.SubProject;

public class ModuleProject{
    String id;
    String title;
    String respid;
    String startdate;
    String deadline;
    int progress;
    int weight;
    public ModuleProject() {};
    public ModuleProject(String id, String title, String respid, String startdate, String deadline, int progress, int weight) {
        this.id = id;
        this.title = title;
        this.respid = respid;
        this.startdate = startdate;
        this.deadline = deadline;
        this.progress = progress;
        this.weight = weight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRespid() {
        return respid;
    }

    public void setRespid(String respid) {
        this.respid = respid;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
