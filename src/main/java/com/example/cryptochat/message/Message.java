package com.example.cryptochat.message;

import java.util.Date;

public class Message {
    public String text;
    public Date date;
    public String userName;
    public Boolean isOutgoing;

    public Message(String text, String userName, Boolean isOutgoing) {
        this.text = text;
        this.userName = userName;
        this.date = new Date();
        this.isOutgoing = isOutgoing;
    }
}
