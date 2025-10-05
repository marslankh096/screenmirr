package com.sid.testscreenmirr.ads;

import android.content.Context;
import android.content.SharedPreferences;

import com.sid.testscreenmirr.R;

public class MyPrefHelper {
public static MyPrefHelper myPrefHelper;
private static SharedPreferences preferences;

private MyPrefHelper(Context context) {
    if (preferences == null) {
        preferences = context.getSharedPreferences(context.getString(R.string.app_name),
                Context.MODE_PRIVATE);
    }
}

public static MyPrefHelper getPrefIns(Context context) {
    if (myPrefHelper == null || preferences == null) {
        
        myPrefHelper = new MyPrefHelper(context);
    }
    return myPrefHelper;
}



public void setIsFirstTimeInstall(boolean PowerBG) {
    preferences.edit().putBoolean("setIsFirstTimeInstall", PowerBG).apply();
}

public boolean getIsFirstTimeInstall() {
    return preferences.getBoolean("setIsFirstTimeInstall", true);
}


public boolean getAppPurchased() {
    return preferences.getBoolean("getAppPurchased", false);
}

public void setAppPurchased(boolean b) {
    preferences.edit().putBoolean("getAppPurchased", b).apply();
}

public int getInterstitialCount(String key) {
    return preferences.getInt(key, 0);
}

public void setInterstitialCount(String key, int a) {
    preferences.edit().putInt(key, a).apply();
}



}
