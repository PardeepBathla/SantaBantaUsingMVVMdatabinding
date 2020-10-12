package com.example.mvvmsantabanta;

import android.app.Application;

public class AppController extends Application {

    private static AppController mInstance;
    public static boolean SHOW_ADULT_DIALOG = true;

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
}
