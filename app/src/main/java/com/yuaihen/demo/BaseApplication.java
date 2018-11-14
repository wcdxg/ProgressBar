package com.yuaihen.demo;

import android.app.Application;
import android.content.Context;

/**
 * Created by Yuaihen.
 * on 2018/11/14
 */
public class BaseApplication extends Application {

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getmContext() {
        return mContext;
    }

}
