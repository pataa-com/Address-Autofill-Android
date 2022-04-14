package com.pataa.sdk;

import android.util.Log;

public class Logger {
    private static String TAG = "PATAA_SDK_LOGS";

    public static void e(String message) {
        if (PataaAutoFillView.enableLogger) Log.e(TAG, message);
    }

    public static void e(Object object) {
//        if (PataaAutoFillView.enableLogger) Log.e(TAG, new Gson().toJson(object));
    }

    public static void i(String message) {
        if (PataaAutoFillView.enableLogger) Log.i(TAG, message);
    }
}
