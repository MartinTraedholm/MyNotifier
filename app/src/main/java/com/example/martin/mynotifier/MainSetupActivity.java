package com.example.martin.mynotifier;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

//TODO lav følgende en app som måler strømforbruget i iven periode og sender det til uret

public class MainSetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_setup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.i(this.getClass().getSimpleName(),"OnCreate");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //float thePhoneStatus = getBatteryLevel();
        //fireSimpleDefaultPriorityNotification("Status", "Battery: " +Float.toString(thePhoneStatus)+"% test");
        Log.d(this.getClass().getSimpleName(), "Before scheduleAlarm()" + Boolean.toString(isNotifierAllreadyRunning(getApplicationContext())));
        scheduleAlarm();
        Log.d(this.getClass().getSimpleName(), "After scheduleAlarm()" + Boolean.toString(isNotifierAllreadyRunning(getApplicationContext())));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_setup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void scheduleAlarm() {
        if(false == isNotifierAllreadyRunning(getApplicationContext())) {
            // Construct an intent that will execute the AlarmReceiver
            Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
            // Create a PendingIntent to be triggered when the alarm goes off
            final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            // Setup periodic alarm every 5 seconds
            long firstMillis = System.currentTimeMillis(); // alarm is set right away
            AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
            // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
            alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                    AlarmManager.INTERVAL_HALF_HOUR, pIntent);
        }
    }

    public void cancelAlarm() {
        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);
    }

    public boolean isNotifierAllreadyRunning(Context context)
    {
        boolean alarmUp = (PendingIntent.getBroadcast(context, 0,
                new Intent(context, MyAlarmReceiver.class),
                PendingIntent.FLAG_NO_CREATE) != null);
        return alarmUp;
    }
}
