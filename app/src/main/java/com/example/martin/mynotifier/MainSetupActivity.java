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
import android.widget.Button;

//TODO lav følgende en app som måler strømforbruget i iven periode og sender det til uret

public class MainSetupActivity extends AppCompatActivity implements View.OnClickListener{

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private Button btnClick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_setup);

        btnClick = (Button) findViewById(R.id.btnStartNotification) ;
        btnClick.setOnClickListener(this);

        Log.i(this.getClass().getSimpleName(),"OnCreate");

        //to make isNotifierAllreadyRunning2 return false you need to uninstal the app!!
        Log.d(this.getClass().getSimpleName(), "Before scheduleAlarm()" + Boolean.toString(isNotifierAllreadyRunning2(getApplicationContext())));
        scheduleAlarm2(getApplicationContext(), false);
        Log.d(this.getClass().getSimpleName(), "After scheduleAlarm()" + Boolean.toString(isNotifierAllreadyRunning2(getApplicationContext())));
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

    @Override
    public void onClick(View v) {
        if( v.getId() == R.id.btnStartNotification)
        {
            Log.d(getApplicationContext().getClass().getSimpleName(), "btnStartNotification press");
        }
    }

    public void scheduleAlarm2(Context context, boolean forceUpdate)
    {
        Log.d(context.getClass().getSimpleName(), "scheduleAlarm2");
        Log.d(context.getClass().getSimpleName(), "scheduleAlarm2, forse update:" + forceUpdate);

        if(isNotifierAllreadyRunning2(getApplicationContext()) || forceUpdate) {
            alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, MyAlarmReceiver.class);
            alarmIntent = PendingIntent.getBroadcast(context, MyAlarmReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                    AlarmManager.INTERVAL_HALF_HOUR, alarmIntent);
        }
    }

    public void cancelAlarm2()
    {
        if(alarmMgr != null)
        {
            alarmMgr.cancel(alarmIntent);
        }
    }

    public boolean isNotifierAllreadyRunning2(Context context)
    {
        boolean alarmUp = (PendingIntent.getBroadcast(  context,MyAlarmReceiver.REQUEST_CODE,
                                                        new Intent(context, MyAlarmReceiver.class), PendingIntent.FLAG_NO_CREATE) != null);
        return alarmUp;
    }


}
