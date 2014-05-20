package ir.khabarefori.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.util.Log;
import ir.khabarefori.json.JsonGetNewNews;

import java.util.*;

/**
 * Created by hani on 1/19/14.
 */
public class ServiceCheckServer extends Service {
    private final String LOGTAG = "ServiceCheckServer";
    private static Timer timer;
    private Context context;

    private int serial_num = 0;

    public IBinder onBind(Intent arg0) {
        return null;
    }


    /**
     * this method call when service create , not when start
     */
    public void onCreate() {
        super.onCreate();

        context = this;

        Random randomGenerator = new Random();
        serial_num = randomGenerator.nextInt(1000);

        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new mainTask(), 5000, 300000);
        } else {
            timer.cancel();
            timer = new Timer();
            timer.scheduleAtFixedRate(new mainTask(), 5000, 300000);
        }
    }

    /**
     * Do some work in thread , by default service run in main thread :O
     */
    private class mainTask extends TimerTask {
        public void run() {
            JsonGetNewNews.CheckNews();
        }
    }

    /**
     * this method call when service kill
     */
    @Override
    public void onDestroy() {
        restartService();
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        restartService();
    }

    private void restartService() {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 10000,
                restartServicePendingIntent);
    }
}

