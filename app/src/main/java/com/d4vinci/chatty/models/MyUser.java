package com.d4vinci.chatty.models;

/**
 * Created by D4Vinci on 7/12/2017.
 */

public class MyUser {
    private String name;
    private String uid;

    public MyUser() {
        // Empty constructor for firebase
    }

    public MyUser(String name, String uid) {
        this.name = name;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
