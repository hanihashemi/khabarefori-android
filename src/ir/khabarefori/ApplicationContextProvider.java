package ir.khabarefori;

import android.app.Application;
import android.content.Context;
import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

@ReportsCrashes(formKey = "",
        formUri = "http://91.109.23.175/ACRA/index.php",
        formUriBasicAuthLogin = "yourlogin",
        formUriBasicAuthPassword = "y0uRpa$$w0rd",
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text)

public class ApplicationContextProvider extends Application {

    /**
     * Keeps a reference of the application context
     */
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        ACRA.init(this);

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
