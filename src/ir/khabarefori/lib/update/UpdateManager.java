package ir.khabarefori.lib.update;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import ir.khabarefori.AppPath;
import ir.khabarefori.ApplicationContextProvider;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Hani on 5/20/14.
 */
public class UpdateManager extends AsyncTask<Context, Void, Context> {

    public UpdateManager() {
    }

    @Override
    protected Context doInBackground(Context... contexts) {
        checkVersion();

        return contexts[0];
    }

    protected void onPostExecute(Context context) {
        if (checkVersion()) {
            try {
                new UpdateDialog().show(context);
            } catch (Exception ex) {
            }
        }
    }

    public boolean checkVersion() {
        int lastVersion = getLastVersionCode();
        int thisVersion = getThisAppCode();

        if (lastVersion != -1 && thisVersion != -1)
            if (lastVersion > thisVersion)
                return true;
        return false;
    }

    private int getThisAppCode() {
        try {
            return ApplicationContextProvider.getContext().getPackageManager().getPackageInfo(
                    ApplicationContextProvider.getContext().getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return -1;
        }
    }

    private int getLastVersionCode() {
        try {
            URL yahoo = new URL(AppPath.Network.getAppVersionPage());
            URLConnection yc = yahoo.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    yc.getInputStream(), "UTF-8"));
            String inputLine;
            StringBuilder a = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                a.append(inputLine);
            in.close();

            return Integer.parseInt(a.toString());
        } catch (Exception ex) {
            return -1;
        }
    }
}
