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
import android.widget.ImageView;
import android.widget.TextView;

//TODO lav følgende en app som måler strømforbruget i iven periode og sender det til uret

public class MainSetupActivity extends AppCompatActivity implements View.OnClickListener{

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private Button btnClickStartNotification;
    private Button btnClickStopNotification;
    private TextView txtStatusText;
    private ImageView imageViewStatusNotification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(this.getClass().getSimpleName(),"OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_setup);

        btnClickStartNotification = (Button) findViewById(R.id.btnStartNotification) ;
        btnClickStartNotification.setOnClickListener(this);

        btnClickStopNotification = (Button) findViewById(R.id.btnStopNotification) ;
        btnClickStopNotification.setOnClickListener(this);

        //--------------------
        // This is not used only to init
        setTextStatusNotification("status for notifier is unknown :( "); //should be overide rigth away!!
        setStatusChange();
        //--------------------

        //scheduleAlarm(getApplicationContext(), false);
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
        Log.d(getApplicationContext().getClass().getSimpleName(), "onClick callback ");
        if( v.getId() == R.id.btnStartNotification)
        {
            Log.d(getApplicationContext().getClass().getSimpleName(), "btnStartNotification press");
            scheduleAlarm(getApplicationContext(), true);
        }

        if( v.getId() == R.id.btnStopNotification)
        {
            Log.d(getApplicationContext().getClass().getSimpleName(), "btnStopNotification press");
            cancelAlarm(getApplicationContext());
        }
    }

    public void scheduleAlarm(Context context, boolean forceUpdate)
    {
        Log.d(context.getClass().getSimpleName(), "scheduleAlarm");
        Log.d(context.getClass().getSimpleName(), "scheduleAlarm, forse update:" + forceUpdate);

        if(false == isNotifierAllreadyRunning(getApplicationContext()) || true == forceUpdate) {
            Log.d(context.getClass().getSimpleName(),"Start alarm");
            alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, MyAlarmReceiver.class);
            alarmIntent = PendingIntent.getBroadcast(context, MyAlarmReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                    AlarmManager.INTERVAL_HALF_HOUR, alarmIntent);
        }
        setStatusChange();
    }

    public void cancelAlarm(Context context)
    {
        Log.d(context.getClass().getSimpleName(), "cancelAlarm");
        if(null == alarmMgr)
        {
            alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }
        Intent intent = new Intent(context, MyAlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, MyAlarmReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        if(alarmIntent != null)
        {
            alarmMgr.cancel(alarmIntent);
            alarmIntent.cancel();
        }
        setStatusChange();
    }

    public boolean isNotifierAllreadyRunning(Context context)
    {
        boolean alarmUp = (PendingIntent.getBroadcast(  context,MyAlarmReceiver.REQUEST_CODE,
                                                        new Intent(context, MyAlarmReceiver.class), PendingIntent.FLAG_NO_CREATE) != null);
        return alarmUp;
    }

    private void setStatusChange()
    {
        if(isNotifierAllreadyRunning(getApplicationContext()))
        {
            Log.d(getApplicationContext().getClass().getSimpleName(), "setStatusChange: Running!");
            setImageNotificationGreenButton();
            setTextStatusNotification("Running");
        }
        else
        {
            Log.d(getApplicationContext().getClass().getSimpleName(), "setStatusChange: Not running!");
            setTextStatusNotification("Not running");
            setImageNotificationRedButton();
        }
    }

    private void setImageNotificationRedButton()
    {
        imageViewStatusNotification = (ImageView) findViewById(R.id.imgStatus);
        imageViewStatusNotification.setImageResource(R.mipmap.red_button2);
    }

    private void setImageNotificationGreenButton()
    {
        imageViewStatusNotification = (ImageView) findViewById(R.id.imgStatus);
        imageViewStatusNotification.setImageResource(R.mipmap.green_button2);
    }
    private void setTextStatusNotification(String text)
    {
        txtStatusText = (TextView) findViewById(R.id.txtStatusNotifier);
        txtStatusText.setText(text);
    }


}
