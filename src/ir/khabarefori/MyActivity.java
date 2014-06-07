package ir.khabarefori;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import ir.khabarefori.database.datasource.NewsTable;
import ir.khabarefori.database.model.NewsModel;
import ir.khabarefori.helper.CheckServerThread;
import ir.khabarefori.helper.Notification;
import ir.khabarefori.helper.update.UpdateManager;
import ir.khabarefori.listview.ListViewAdapter;
import ir.khabarefori.notify.Knotify;
import ir.khabarefori.service.CheckServerService;

import java.util.ArrayList;

public class MyActivity extends ActionBarActivity implements View.OnClickListener {
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
            startService(new Intent(this, CheckServerService.class));
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu items for use in the action bar
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_activity_actions, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle presses on the action bar items
//        switch (item.getItemId()) {
//            case R.id.action_search:
//                Toast.makeText(this , "search" , Toast.LENGTH_SHORT).show();
////                openSearch();
//                return true;
//            case R.id.action_settings:
//                Toast.makeText(this , "search" , Toast.LENGTH_SHORT).show();
////                openSettings();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

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
            CheckServerThread.CheckNews();
            refreshbtnReload();
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
        ArrayList<NewsModel> models = (ArrayList<NewsModel>) NewsTable.getInstance().getAllContents();
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
                if (CheckServerService.class.getName().equals(service.service.getClassName()))
                    return true;
        } catch (NullPointerException e) {
        }
        return false;
    }

    public int toDIPMetric(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }
}
