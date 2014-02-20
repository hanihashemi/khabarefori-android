package ir.khabarefori.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.util.Log;
import ir.khabarefori.ApplicationContextProvider;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by hani on 1/19/14.
 */
public class SCheckServer extends Service {
    private final IBinder myBinder = new MyLocalBinder();

    private static Timer timer;
    private Context context;
    private int serial_num = 0;

    /**
     * We don't use this method
     *
     * @param arg0
     * @return
     */
    public IBinder onBind(Intent arg0) {
// TODO Auto-generated method stub
        return myBinder;
    }

    public String getCurrentTime() {
        SimpleDateFormat dateformat =
                new SimpleDateFormat("HH:mm:ss MM/dd/yyyy", Locale.US);
        return (dateformat.format(new Date()));
    }

    public class MyLocalBinder extends Binder {
        public SCheckServer getService() {
            return SCheckServer.this;
        }
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

        Log.d("Service", "_____________________Service Started_________________");

        context = this;

        Random randomGenerator = new Random();
        serial_num = randomGenerator.nextInt(1000);

        Log.d("service", "onCreate" + serial_num);

        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new mainTask(), 0, 10000);
            Log.d("service", "Timer start " + serial_num);
        } else {
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

        Log.d("Service", "_____________________Service Stop_________________");
        super.onDestroy();
    }


    /**
     * Handle toast message
     */
    private final Handler toastHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
        }
    };
}
