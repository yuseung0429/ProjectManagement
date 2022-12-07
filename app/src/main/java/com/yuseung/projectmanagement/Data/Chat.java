package com.yuseung.projectmanagement.Data;

public class Chat {
    public Chat (String chatid, String update, String last,String frontid, String frontname, String endid, String endname)
    {
        this.chatid = chatid;
        this.update = update;
        this.last = last;
        this.frontid = frontid;
        this.frontname = frontname;
        this.endid = endid;
        this.endname = endname;
    }
    String chatid;
    String frontid;
    String frontname;
    String endid;
    String endname;
    String update;
    String last;

    public String getChatid() {
        return chatid;
    }

    public String getFrontid() {
        return frontid;
    }
    public String getFrontname() {
        return frontname;
    }

    public String getEndid() {
        return endid;
    }
    public String getEndname() {
        return endname;
    }

    public String getLast() {
        return last;
    }
    public String getUpdate() {
        return update;
    }
}
