package ir.khabarefori;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import ir.khabarefori.service.SCheckServer;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        startService(new Intent(this, SCheckServer.class));

    }

    /**
     * check is service SCheckServer running.
     * @return
     */
    public boolean isSCheckServerRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
            if (SCheckServer.class.getName().equals(service.service.getClassName()))
                return true;
        return false;
    }
}
