package ir.khabarefori.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hani on 1/19/14.
 */
public class SCheckServer extends Service {
    private static Timer timer ;
    private Context ctx;
    private int serial_num = 0;

    /**
     * We don't use this method
     *
     * @param arg0
     * @return
     */
    public IBinder onBind(Intent arg0) {
        return null;
    }

    private void restartService() {
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
    public void onTaskRemoved(Intent rootIntent) {
        restartService();
    }

    /**
     * this method call when service create , not when start
     */
    public void onCreate() {
        super.onCreate();
        ctx = this;

        Random randomGenerator = new Random();
        serial_num = randomGenerator.nextInt(1000);

        Log.d("service", "onCreate" + serial_num);

        if (timer == null)
        {
            timer = new Timer();
            timer.scheduleAtFixedRate(new mainTask(), 0, 10000);
            Log.d("service", "Timer start " + serial_num);
        }else
        {
            Log.d("service", "Timer Set null " + serial_num);
            timer.cancel();
            timer = new Timer();
            timer.scheduleAtFixedRate(new mainTask(), 0, 10000);
        }
    }

//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.d("service", "onStartCommand");
//        if (timer.)
//        timer.scheduleAtFixedRate(new mainTask(), 0, 5000);
//        return android.app.Service.START_STICKY;
//    }

    /**
     * Do some work in thread , by default service run in main thread :O
     */
    private class mainTask extends TimerTask {
        public void run() {
            Log.d("service", "timer " + serial_num);
        }
    }

    /**
     * this method call when service kill
     */
    public void onDestroy() {
        restartService();

        Log.d("service", "Destroy" + serial_num);
        super.onDestroy();
        Toast.makeText(this, "Service Stopped ...", Toast.LENGTH_SHORT).show();
    }

    /**
     * Handle toast message
     */
    private final Handler toastHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
        }
    };
}
