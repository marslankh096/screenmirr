package com.sid.testscreenmirr.webcast;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefer {
    private static SharedPreferences mSharedPref;
    public static final String NAME = "NAME";
    public static final String AGE = "AGE";
    public static final String IS_SELECT = "IS_SELECT";

    private SharedPrefer() {

    }

    public static void init(Context context) {
        if (mSharedPref == null)
            mSharedPref = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
    }

    public static String read(String key, String defValue) {
        return mSharedPref.getString(key, defValue);
    }

    public static Boolean read(String key, Boolean defValue) {
        return mSharedPref.getBoolean(key, defValue);
    }

    public static int read(String key, int defValue) {
        return mSharedPref.getInt(key, defValue);
    }

    public static void write(String key, String value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public static void write(String key, Boolean value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.commit();
    }

    public static void write(String key, int value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putInt(key, value);
        prefsEditor.commit();
    }

    public static void setAppPurchased(boolean b) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putBoolean("AppPurchased", b);
        prefsEditor.commit();

    }
    public static Boolean getAppPurchased() {
        return read("AppPurchased",false);

    }

}