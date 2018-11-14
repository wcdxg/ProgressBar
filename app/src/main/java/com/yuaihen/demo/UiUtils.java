package com.yuaihen.demo;

import android.util.TypedValue;

/**
 * Created by Yuaihen.
 * on 2018/11/14
 */
public class UiUtils {

    public static int dp2px(int dpVal) {

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal,
                BaseApplication.getmContext().getResources().getDisplayMetrics());
    }

    public static int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal,
                BaseApplication.getmContext().getResources().getDisplayMetrics());
    }
}
