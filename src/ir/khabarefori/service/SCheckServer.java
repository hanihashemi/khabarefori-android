package ir.khabarefori.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hani on 1/19/14.
 */
public class SCheckServer extends Service {
    private static Timer timer = new Timer();
    private Context ctx;

    /**
     * We don't use this method
     * @param arg0
     * @return
     */
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    private void restartService()
    {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 5000,
                restartServicePendingIntent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent){
        restartService();
    }

    /**
     * this method call when service create , not when start
     */
    public void onCreate()
    {
        super.onCreate();
        ctx = this;
        Log.d("service" , "onCreate");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("service" , "onStartCommand");
        timer.scheduleAtFixedRate(new mainTask(), 0, 5000);
        return android.app.Service.START_STICKY;
    }

    /**
     * Do some work in thread , by default service run in main thread :O
     */
    private class mainTask extends TimerTask
    {
        public void run()
        {
            toastHandler.sendEmptyMessage(0);
            Log.d("service" , "timer");
        }
    }

    /**
     * this method call when service kill
     */
    public void onDestroy()
    {
        restartService();

        Log.d("service" , "Destroy");
        super.onDestroy();
        Toast.makeText(this, "Service Stopped ...", Toast.LENGTH_SHORT).show();
    }

    /**
     * Handle toast message
     */
    private final Handler toastHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
        }
    };
}
