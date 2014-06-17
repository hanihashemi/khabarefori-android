package ir.khabarefori;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import ir.khabarefori.database.datasource.NewsTable;
import ir.khabarefori.database.model.NewsModel;
import ir.khabarefori.helper.CheckServerThread;
import ir.khabarefori.helper.Notification;
import ir.khabarefori.helper.update.UpdateManager;
import ir.khabarefori.listview.ListViewAdapter;
import ir.khabarefori.notify.Knotify;
import ir.khabarefori.service.CheckServerService;
import org.acra.ACRA;

import java.util.ArrayList;

public class MyActivity extends ActionBarActivity {
    private static boolean btnReloadIsActive = false;
    private Menu menuBar;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        refreshListView();

        Knotify.updateMainActivity(this);

        new UpdateManager().execute(this);

        // run service
        if (!isSCheckServerRunning())
            startService(new Intent(this, CheckServerService.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        menuBar = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reload:
                CheckServerThread.CheckNews();
                refreshBtnReload();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void refreshListView() {
        ListView listView = (ListView) findViewById(R.id.listView);
        ListViewAdapter adapter = new ListViewAdapter(this, generateData(), listView);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshBtnReload();
        refreshListView();
        Knotify.updateMainActivity(this);
        Notification.Close();
    }

    public static void setBtnReloadIsActive(boolean isActive) {
        btnReloadIsActive = isActive;
    }

    public void refreshBtnReload() {
        try {
            if (android.os.Build.VERSION.SDK_INT < 11 || menuBar == null)
                return;

            MenuItem item = menuBar
                    .findItem(R.id.action_reload);

            if (btnReloadIsActive) {
                item.setActionView(R.layout.reload_progress);
            } else {
                item.setActionView(null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            ACRA.getErrorReporter().handleException(ex);
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
