package com.example.martin.mynotifier;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.text.format.Time;
import android.util.Log;
import android.view.View;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Martin on 13/02/2016.
 */
public class MyTestService extends IntentService {
    public MyTestService() {
        super("MyTestService");
    }

    private NotificationManager mNotificationManager;
    private static final int simpleDefaultPriorityNotificationID = 12;
    @Override
    public void onCreate() {
        super.onCreate(); // if you override onCreate(), make sure to call super().
        Log.d(this.getClass().getSimpleName(), "onReceive");
        // If a Context object is needed, call getApplicationContext() here.
        mNotificationManager =
                (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Do the task here
        Log.i(this.getClass().getSimpleName(), "Service running test!!!!!");
        float batteryNow = getBatteryLevel();
        float batteryLastStatus;
        GregorianCalendar gc = new GregorianCalendar();

        GregorianCalendar gcAfter = (GregorianCalendar) gc.clone();
        gcAfter.set(Calendar.HOUR_OF_DAY,22);
        gcAfter.set(Calendar.MINUTE, 0);

        GregorianCalendar gcBefore = (GregorianCalendar) gc.clone();
        gcBefore.set(Calendar.HOUR_OF_DAY,7);
        gcBefore.set(Calendar.MINUTE,0);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("lastBatteryStatus",batteryNow);
        editor.commit();


        Date timeNow = gc.getTime();
        Date timeAfter = gcAfter.getTime();
        Date timeBefore = gcBefore.getTime();
        Log.i(this.getClass().getSimpleName(),timeNow.toString() + " After: " + timeAfter.toString() + "Before: " + timeBefore.toString());
        if(timeNow.before(timeAfter) && timeNow.after(timeBefore))
            fireSimpleDefaultPriorityNotification("Status", "Battery: " + Float.toString(getBatteryLevel()) + "% test");
    }

    private void fireSimpleDefaultPriorityNotification(String notificationTitle, String contentText)
    {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle(notificationTitle)
                        .setContentText(contentText)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainSetupActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainSetupActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        // simpleDefaultPriorityNotificationID allows you to update the notification later on.
        mNotificationManager.notify(simpleDefaultPriorityNotificationID, mBuilder.build());
    }

    public float getBatteryLevel() {
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        // Error checking that probably isn't needed but I added just in case.
        if(level == -1 || scale == -1) {
            return 50.0f;
        }

        return ((float)level / (float)scale) * 100.0f;
    }
}
