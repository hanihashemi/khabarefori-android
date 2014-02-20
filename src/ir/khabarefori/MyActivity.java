package ir.khabarefori;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.*;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import ir.khabarefori.database.datasource.NewsDatasource;
import ir.khabarefori.database.model.NewsModel;
import ir.khabarefori.listview.ListViewAdapter;
import ir.khabarefori.service.SCheckServer;

import java.util.ArrayList;

public class MyActivity extends Activity implements View.OnClickListener {
    SCheckServer serviceCheck;
    boolean isBound = false;

    private ServiceConnection myConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            SCheckServer.MyLocalBinder binder = (SCheckServer.MyLocalBinder) service;
            serviceCheck = binder.getService();
            isBound = true;
        }

        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }

    };

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ListView listView = (ListView) findViewById(R.id.listView);
        ListViewAdapter adapter = new ListViewAdapter(this, generateData(), listView);
        listView.setAdapter(adapter);

        ImageButton btnReload = (ImageButton) findViewById(R.id.btnReload);
        btnReload.setOnClickListener(this);

        if (!isSCheckServerRunning())
            startService(new Intent(this, SCheckServer.class));

        doBindService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }

    private void doBindService() {
        bindService(new Intent(this, SCheckServer.class), myConnection, Context.BIND_AUTO_CREATE);
    }

    private void doUnbindService() {
        if (isBound) {
            unbindService(myConnection);
            isBound = false;
        }
    }


    private ArrayList<NewsModel> generateData() {
        ArrayList<NewsModel> models = (ArrayList) NewsDatasource.getInstance().getAllContents();

        return models;
    }

    /**
     * check is service SCheckServer running.
     *
     * @return boolean
     */
    public boolean isSCheckServerRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
            if (SCheckServer.class.getName().equals(service.service.getClassName()))
                return true;
        return false;
    }

    @Override
    public void onClick(View view) {
        if (findViewById(R.id.btnReload).equals(view)) {
            Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.hyperspace_jump);
            view.startAnimation(hyperspaceJumpAnimation);

            Toast.makeText(getApplicationContext(), serviceCheck.getCurrentTime(), Toast.LENGTH_LONG).show();
        }
    }

}
