package com.termproject.moblieprogramming.Data;

public class Message {
    public Message (){}
    public Message (String name, String message, String update)
    {
        this.name = name;
        this.message = message;
        this.update = update;
    }
    String name;
    String message;
    String update;



    public String getMessage() {
        return message;
    }

    public String getUpdate() {
        return update;
    }

    public String getName() {
        return name;
    }

    public void setMessage(String message) {this.message = message;}

    public void setUpdate() {this.update = update;}

    public void setName() {
        this.name = name;
    }
}
