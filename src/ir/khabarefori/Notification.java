package ir.khabarefori;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

/**
 * Created by Hani on 5/20/14.
 */
public class Notification {

    public static int NOTIFICATION_ID = 1261065;

    public void Show(CharSequence text, boolean playAlert) {
        if (playAlert)
            this.AlertNotify();

        this.createNotification(text);
    }

    private void createNotification(CharSequence text) {
        NotificationManager mNotificationManager =
                (NotificationManager) ApplicationContextProvider.getContext().getSystemService
                        (ApplicationContextProvider.getContext().NOTIFICATION_SERVICE);
        android.app.Notification notification = new android.app.Notification(R.drawable.logo_circle_notify, text, System.currentTimeMillis());
        PendingIntent contentIntent = PendingIntent.getActivity(ApplicationContextProvider.getContext(), 0, new Intent(ApplicationContextProvider.getContext(), MyActivity.class), 0);
        notification.setLatestEventInfo(ApplicationContextProvider.getContext(), "خبرفوری", text, contentIntent);

        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

    private void AlertNotify() {
        try {
            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(ApplicationContextProvider.getContext(), sound);
            r.play();
        } catch (Exception e) {
        }
    }

    public static void Close() {
        NotificationManager mNotificationManager = (NotificationManager) ApplicationContextProvider.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(NOTIFICATION_ID);
    }
}
