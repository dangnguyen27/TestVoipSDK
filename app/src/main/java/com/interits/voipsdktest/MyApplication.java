package com.interits.voipsdktest;

import android.app.Application;

import com.google.firebase.analytics.FirebaseAnalytics;

public class MyApplication extends Application {

    public FirebaseAnalytics mFirebaseAnalytics;
    @Override
    public void onCreate() {
        super.onCreate();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }
}
