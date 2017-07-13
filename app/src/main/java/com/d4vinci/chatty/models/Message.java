package com.d4vinci.chatty.models;

/**
 * Created by D4Vinci on 7/13/2017.
 */

public class Message {
    private String text;
    private String uid;

    public Message(String text, String uid) {
        this.text = text;
        this.uid = uid;
    }

    public Message() {
        // Empty constructor needed for Firebase
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
