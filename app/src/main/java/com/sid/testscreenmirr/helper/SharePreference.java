package com.sid.testscreenmirr.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.RingtoneManager;

public class SharePreference {
    Context ctx;
    public SharedPreferences preferenceRing;
    SharedPreferences.Editor preferenceEditor;
    public SharePreference(Context context) {
        this.ctx = context;
    }
    public void setEnabledBoolean(Boolean aa){
        preferenceRing=ctx.getSharedPreferences("BoL_Value",Context.MODE_PRIVATE);
        preferenceEditor=preferenceRing.edit();
        preferenceEditor.putBoolean("Check_Value",aa);
        preferenceEditor.apply();
    }
    public Boolean getBooleanVAlue(){
        preferenceRing=ctx.getSharedPreferences("BoL_Value",Context.MODE_PRIVATE);
        return preferenceRing.getBoolean("Check_Value",false);
    }

    public void setEnabledAlarm(String key) {
        preferenceRing = ctx.getSharedPreferences("AlarmUri", Context.MODE_PRIVATE);
        preferenceEditor = preferenceRing.edit();
        preferenceEditor.putString("checkAlarmUri", key);
        preferenceEditor.apply();
    }

    public String getEnabledAlarm() {
        preferenceRing = ctx.getSharedPreferences("AlarmUri", Context.MODE_PRIVATE);
        return preferenceRing.getString("checkAlarmUri", String.valueOf(RingtoneManager.getActualDefaultRingtoneUri(ctx
                , RingtoneManager.TYPE_ALARM)));
    }
}
