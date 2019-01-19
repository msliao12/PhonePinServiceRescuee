package com.example.phonepinservicerescuee;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {
    public static final String HELP_MESSAGE = "Need Imediate Help?!?!";
    @Override
    public void onReceive(Context context, Intent intent){
        String Message = intent.getStringExtra(HELP_MESSAGE);
        Toast.makeText(context, Message, Toast.LENGTH_SHORT).show();
    }
}
