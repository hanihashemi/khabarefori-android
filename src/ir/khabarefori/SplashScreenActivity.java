package ir.khabarefori;

/**
 * Created by Hani on 3/31/14.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import ir.khabarefori.database.datasource.NewsDatasource;

public class SplashScreenActivity extends Activity {

    private static final int SPLASH_SHOW_TIME = 1800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        new BackgroundSplashTask().execute();
    }

    /**
     * Async Task: can be used to load DB, images during which the splash screen
     * is shown to user
     */
    private class BackgroundSplashTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... arg0) {

            // I have just given a sleep for this thread
            // if you want to load database, make
            // network calls, load images
            // you can do them here and remove the following
            // sleep

            // do not worry about this Thread.sleep
            // this is an async task, it will not disrupt the UI
            try {
                Thread.sleep(SPLASH_SHOW_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            startApp();
            finish();
        }


    }

    protected void startApp() {
        Intent i = new Intent(SplashScreenActivity.this,
                MyActivity.class);
        // any info loaded can during splash_show
        // can be passed to main activity using
        // below
        i.putExtra("loaded_info", " ");
        startActivity(i);


        if (NewsDatasource.getInstance().getLastId() == 0) {
            Intent ii = new Intent(SplashScreenActivity.this,
                    TutorialActivity.class);
            startActivity(ii);
        }
    }
}
