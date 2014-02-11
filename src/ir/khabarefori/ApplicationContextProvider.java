package ir.khabarefori;

import android.app.Application;
import android.content.Context;

/**
 * Created by hani on 2/9/14.
 */

public class ApplicationContextProvider extends Application {

    /**
     * Keeps a reference of the application context
     */
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();

    }

    /**
     * Returns the application context
     *
     * @return application context
     */
    public static Context getContext() {
        return sContext;
    }

}
