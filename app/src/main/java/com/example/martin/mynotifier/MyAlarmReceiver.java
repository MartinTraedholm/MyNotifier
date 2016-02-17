package com.example.martin.mynotifier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Martin on 13/02/2016.
 */
public class MyAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "com.example.martin.mynotifier.runstatus";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(this.getClass().getSimpleName(),"onReceive");
        Intent i = new Intent(context, MyTestService.class);
        i = new Intent(ACTION, Uri.EMPTY,context, MyTestService.class);
        Log.d(this.getClass().getSimpleName(), i.toString());
        i.putExtra("foo", "bar");
        context.startService(i);
    }
}
