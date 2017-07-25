package com.phattarapong.liveat500px;

import android.app.Application;

import com.phattarapong.liveat500px.manager.Contextor;


/**
 * Created by Phattarapong on 11-Jul-17.
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Contextor.getInstance().init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
