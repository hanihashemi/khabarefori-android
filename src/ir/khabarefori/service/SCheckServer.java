package ir.khabarefori.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.os.Process;
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

    /**
     * this method call when service create , not when start
     */
    public void onCreate()
    {
        super.onCreate();
        ctx = this;
        startService();
    }

    /**
     * This method call when service start
     */
    private void startService()
    {
        timer.scheduleAtFixedRate(new mainTask(), 0, 5000);
    }

    /**
     * Do some work in thread , by default service run in main thread :O
     */
    private class mainTask extends TimerTask
    {
        public void run()
        {
            toastHandler.sendEmptyMessage(0);
        }
    }

    /**
     * this method call when service kill
     */
    public void onDestroy()
    {
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
