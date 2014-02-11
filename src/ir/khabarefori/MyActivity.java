package ir.khabarefori;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import ir.khabarefori.database.datasource.NewsDatasource;
import ir.khabarefori.database.model.NewsModel;
import ir.khabarefori.listview.ListViewAdapter;
import ir.khabarefori.service.SCheckServer;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends Activity implements View.OnClickListener {
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
    }


    private ArrayList<NewsModel> generateData() {
        return (ArrayList) NewsDatasource.getInstance().getAllContents();
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
        }
    }

}
