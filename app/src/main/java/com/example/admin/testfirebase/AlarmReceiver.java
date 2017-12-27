package com.example.admin.testfirebase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Bunny on 11/17/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("I am in reveiver", "Hello");
        // get extra string from the intent
        String extraString = intent.getExtras().getString("extra");
        // create an intent to ringtone service
        Intent serviceIntent = new Intent(context, RingtonePlayService.class);
        // pass extra string from MainActivity to RingronePlayService
        serviceIntent.putExtra("extra", extraString);
        // start ringtone service
        context.startService(serviceIntent);
    }
}
