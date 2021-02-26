package com.example.homeapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class NewAppReceiver extends BroadcastReceiver {


    private static final String TAG = "NewAppReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Intent: " + intent.getAction());
        if (intent.getAction().contains("PACKAGE_ADDED"))
            Toast.makeText(context.getApplicationContext(), "New App added", Toast.LENGTH_LONG).show();
        if (intent.getAction().contains("PACKAGE_REMOVED"))
            Toast.makeText(context.getApplicationContext(), "App removed", Toast.LENGTH_LONG).show();
    }


}
