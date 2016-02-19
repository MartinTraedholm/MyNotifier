package com.example.martin.mynotifier;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

//TODO lav følgende en app som måler strømforbruget i iven periode og sender det til uret

public class MainSetupActivity extends AppCompatActivity implements View.OnClickListener{

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private Button btnClickStartNotification;
    private Button btnClickStopNotification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_setup);

        btnClickStartNotification = (Button) findViewById(R.id.btnStartNotification) ;
        btnClickStartNotification.setOnClickListener(this);

        btnClickStopNotification = (Button) findViewById(R.id.btnStopNotification) ;
        btnClickStopNotification.setOnClickListener(this);

        Log.i(this.getClass().getSimpleName(),"OnCreate");

        //to make isNotifierAllreadyRunning return false you need to uninstal the app!!
        Log.d(this.getClass().getSimpleName(), "Before scheduleAlarm()" + Boolean.toString(isNotifierAllreadyRunning(getApplicationContext())));
        scheduleAlarm(getApplicationContext(), false);
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

    @Override
    public void onClick(View v) {
        Log.d(getApplicationContext().getClass().getSimpleName(),"onClick callback ");
        if( v.getId() == R.id.btnStartNotification)
        {
            Log.d(getApplicationContext().getClass().getSimpleName(), "btnStartNotification press");
            scheduleAlarm(getApplicationContext(), true);
        }

        if( v.getId() == R.id.btnStopNotification)
        {
            Log.d(getApplicationContext().getClass().getSimpleName(), "btnStopNotification press");
            cancelAlarm();
        }
    }

    public void scheduleAlarm(Context context, boolean forceUpdate)
    {
        Log.d(context.getClass().getSimpleName(), "scheduleAlarm");
        Log.d(context.getClass().getSimpleName(), "scheduleAlarm, forse update:" + forceUpdate);

        if(false == isNotifierAllreadyRunning(getApplicationContext()) || true == forceUpdate) {
            alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, MyAlarmReceiver.class);
            alarmIntent = PendingIntent.getBroadcast(context, MyAlarmReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                    AlarmManager.INTERVAL_HALF_HOUR, alarmIntent);
        }
    }

    public void cancelAlarm()
    {
        //BUG here!! investigate
        Log.d(getApplicationContext().getClass().getSimpleName(), "cancelAlarm");
        if(alarmMgr != null)
        {
            Log.d(getApplicationContext().getClass().getSimpleName(), "cancelAlarm, it will cancel");
            alarmMgr.cancel(alarmIntent);
        }
    }

    public boolean isNotifierAllreadyRunning(Context context)
    {
        boolean alarmUp = (PendingIntent.getBroadcast(  context,MyAlarmReceiver.REQUEST_CODE,
                                                        new Intent(context, MyAlarmReceiver.class), PendingIntent.FLAG_NO_CREATE) != null);
        return alarmUp;
    }


}
