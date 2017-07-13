package com.d4vinci.chatty.appsettings;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by D4Vinci on 7/13/2017.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
