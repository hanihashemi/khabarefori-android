package ir.khabarefori.json;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import com.google.gson.Gson;
import ir.khabarefori.AppPath;
import ir.khabarefori.ApplicationContextProvider;
import ir.khabarefori.MyActivity;
import ir.khabarefori.R;
import ir.khabarefori.database.datasource.NewsDatasource;
import ir.khabarefori.database.model.NewsModel;
import ir.khabarefori.json.models.ModelNews;
import ir.khabarefori.notify.Knotify;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Calendar;

/**
 * Created by hani on 1/11/14.
 */
public class JsonGetNewNews implements Runnable {

    private static final String LOGTAG = JsonGetNewNews.class.getName();
    private static boolean isRun = false;

    public static void CheckNews() {
        Thread thread = new Thread(new JsonGetNewNews());
        thread.start();
    }

    public void run() {
        if (isRun)
            return;
        isRun = true;

        try {
            MyActivity.setBtnReloadIsActive(true);
            Knotify.getInstance().show(Knotify.MessageType.MSG_TRY_CONNECT_TO_SERVER);

            URL url = new URL(AppPath.Network.getNewNewsPage(NewsDatasource.getInstance().getLastId()));
            InputStream inputStream = url.openConnection().getInputStream();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(
                    inputStream, "UTF8"));

            // convert the json string back to object
            Gson gson = new Gson();
            ModelNews news = gson.fromJson(buffer, ModelNews.class);

            for (int i = 0; i < news.getNews().size(); i++) {
                NewsModel model = new NewsModel();
                model.setServerID(news.getNews().get(i).id);
                model.setSubject(news.getNews().get(i).subject);
                model.setContext(news.getNews().get(i).context);
                model.setPersianDatetime(news.getNews().get(i).datetime);
                model.setIsBreakingNewsParamBoolean(news.getNews().get(i).getIsBreakingNews());

                NewsDatasource.getInstance().add(model);

                if (model.getIsBreakingNews() && checkIsNewNews(news.getNews().get(i).datetime))
                    GoNotify(model.getSubject());
            }

            MyActivity.setBtnReloadIsActive(false);
            if (news.getNews().size() == 0) {
                Knotify.getInstance().show(Knotify.MessageType.MSG_NO_NEWS);
            } else {
                Knotify.getInstance().show(Knotify.MessageType.MSG_NEW_NEWS_UPDATED);
            }

        } catch (
                Exception ex
                ) {
            ex.printStackTrace();
            MyActivity.setBtnReloadIsActive(false);
            Knotify.getInstance().show(Knotify.MessageType.MSG_NO_INTERNET);
        } finally {
            isRun = false;
        }
    }

    private boolean checkIsNewNews(String date) {
        try {
            String[] strDate = date.split(" ")[0].split("-");
            int[] intDate = new int[3];
            for (int i = 0; i < strDate.length; i++)
                intDate[i] = Integer.parseInt(strDate[i]);

            Calendar c = Calendar.getInstance();
            int day = c.get(Calendar.DAY_OF_MONTH);
            int month = c.get(Calendar.MONTH) + 1;
            int year = c.get(Calendar.YEAR);

            if (intDate[0] == year && intDate[1] == month && intDate[2] == day)
                return true;
        } catch (Exception ex) {

        }

        return false;
    }

    private void GoNotify(String subject) {
        NotificationManager mNotificationManager =
                (NotificationManager) ApplicationContextProvider.getContext().getSystemService
                        (ApplicationContextProvider.getContext().NOTIFICATION_SERVICE);
        CharSequence text = subject;
        android.app.Notification notification = new android.app.Notification(R.drawable.logo_circle_notify, text, System.currentTimeMillis());
        PendingIntent contentIntent = PendingIntent.getActivity(ApplicationContextProvider.getContext(), 0, new Intent(ApplicationContextProvider.getContext(), MyActivity.class), 0);
        notification.setLatestEventInfo(ApplicationContextProvider.getContext(), "خبرفوری", text, contentIntent);

        try {
            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(ApplicationContextProvider.getContext(), sound);
            r.play();
        } catch (Exception e) {
        }

        mNotificationManager.notify(subject.length(), notification);
    }

}