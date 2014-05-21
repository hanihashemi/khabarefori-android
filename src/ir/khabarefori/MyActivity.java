package ir.khabarefori;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ListView;
import ir.khabarefori.database.datasource.NewsDatasource;
import ir.khabarefori.database.model.NewsModel;
import ir.khabarefori.json.JsonGetNewNews;
import ir.khabarefori.lib.datetime.Notification;
import ir.khabarefori.lib.datetime.update.UpdateManager;
import ir.khabarefori.listview.ListViewAdapter;
import ir.khabarefori.notify.Knotify;
import ir.khabarefori.service.ServiceCheckServer;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MyActivity extends Activity implements View.OnClickListener {
    private static boolean btnReloadIsActive = false;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        refreshListView();

        ImageButton btnReload = (ImageButton) findViewById(R.id.btnReload);
        btnReload.setOnClickListener(this);

        Knotify.updateMainActivity(this);

        new UpdateManager().execute(this);

        Notification.Close();

        // run service
        if (!isSCheckServerRunning())
            startService(new Intent(this, ServiceCheckServer.class));
    }

    public void refreshListView() {
        ListView listView = (ListView) findViewById(R.id.listView);
        ListViewAdapter adapter = new ListViewAdapter(this, generateData(), listView);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshbtnReload();
        refreshListView();
        Knotify.updateMainActivity(this);
    }

    @Override
    public void onClick(View view) {
        if (findViewById(R.id.btnReload).equals(view)) {
            JsonGetNewNews.CheckNews();
            refreshbtnReload();

            Timer timer  = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    new Notification().Show("salam" , true);
                }
            }, 5000, 300000);
        }
    }

    public static void setBtnReloadIsActive(boolean isActive) {
        btnReloadIsActive = isActive;
    }

    public void refreshbtnReload() {
        if (btnReloadIsActive) {
            ImageButton btnReload = (ImageButton) findViewById(R.id.btnReload);

            Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.hyperspace_jump);
            if (hyperspaceJumpAnimation != null)
                btnReload.startAnimation(hyperspaceJumpAnimation);
        } else {
            ImageButton btnReload = (ImageButton) findViewById(R.id.btnReload);

            btnReload.clearAnimation();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        Knotify.isOpen = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Knotify.isOpen = false;
    }

    private ArrayList<NewsModel> generateData() {
        ArrayList<NewsModel> models = (ArrayList<NewsModel>) NewsDatasource.getInstance().getAllContents();
        return models;
    }

    /**
     * check is service SCheckServer running.
     *
     * @return boolean
     */
    public boolean isSCheckServerRunning() {
        try {
            ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
                if (ServiceCheckServer.class.getName().equals(service.service.getClassName()))
                    return true;
        } catch (NullPointerException e) {
        }
        return false;
    }

    public int toDIPMetric(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }
}
