package com.callrecorderapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
                // Incoming call
                Intent serviceIntent = new Intent(context, CallRecorderService.class);
                serviceIntent.setAction(CallRecorderService.ACTION_START_RECORDING);
                context.startService(serviceIntent);
            } else if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(state)) {
                // Call answered
                Intent serviceIntent = new Intent(context, CallRecorderService.class);
                serviceIntent.setAction(CallRecorderService.ACTION_START_RECORDING);
                context.startService(serviceIntent);
            } else if (TelephonyManager.EXTRA_STATE_IDLE.equals(state)) {
                // Call ended
                Intent serviceIntent = new Intent(context, CallRecorderService.class);
                serviceIntent.setAction(CallRecorderService.ACTION_STOP_RECORDING);
                context.startService(serviceIntent);
            }
        } else if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            // Outgoing call
            Intent serviceIntent = new Intent(context, CallRecorderService.class);
            serviceIntent.setAction(CallRecorderService.ACTION_START_RECORDING);
            context.startService(serviceIntent);
        }
    }
}
