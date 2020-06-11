package com.sapling.customview;

import android.app.Application;

/**
 * create by cral
 * create at 2020/4/9
 **/
public class MyApplication extends Application {

    private static MyApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
