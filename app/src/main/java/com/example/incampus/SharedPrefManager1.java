package com.example.incampus;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.util.Set;

public class SharedPrefManager1 {
    private static SharedPrefManager1 instance;
    private static Context ctx;

    private static final String SHARED_PREF_NAME = "mysharedpref";
    private static final String KEY_LOGS = "";
    private static final String KEY_ALL_LOGS = "";


    private SharedPrefManager1(Context context) {
        ctx = context;
    }

    public static synchronized SharedPrefManager1 getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager1(context);
        }
        return instance;
    }


    public boolean allUserLogs(String logs){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_ALL_LOGS, logs);
        editor.apply();

        return true;
    }


    public String getKeyAllLogs(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ALL_LOGS, null);
    }



}