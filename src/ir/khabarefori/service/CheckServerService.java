package ir.khabarefori.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import ir.khabarefori.helper.CheckServerThread;

import java.util.Random;
import java.util.Timer;

/**
 * Created by hani on 1/19/14.
 */
public class CheckServerService extends Service {
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
            timer.scheduleAtFixedRate(new CheckServerThread(), 15000, 300000);
        } else {
            timer.cancel();
            timer = new Timer();
            timer.scheduleAtFixedRate(new CheckServerThread(), 15000, 300000);
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

