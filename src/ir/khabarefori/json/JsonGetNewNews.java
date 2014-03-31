package ir.khabarefori.json;

import com.google.gson.Gson;
import ir.khabarefori.AppPath;
import ir.khabarefori.MyActivity;
import ir.khabarefori.database.datasource.NewsDatasource;
import ir.khabarefori.database.model.NewsModel;
import ir.khabarefori.json.models.ModelNews;
import ir.khabarefori.notify.Knotify;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

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
                model.setIsBreakingNewsParamBoolean(news.getNews().get(i).isBreakingNews);

                NewsDatasource.getInstance().add(model);
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
}

//    private void startNotify(final String message) {
//        Runnable taskNotify = new Runnable() {
//            @Override
//            public void run() {
//                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//                Intent launchIntenet = new Intent(context,
//                        MyActivity.class);
//                PendingIntent contentIntent = PendingIntent.getActivity(
//                        context, 0, launchIntenet, 0);
//
//                android.app.Notification note = new android.app.Notification(
//                        R.drawable.logo_circle, "خبر فوری",
//                        System.currentTimeMillis());
//                note.setLatestEventInfo(context, message, "خبر فوری",
//                        contentIntent);
//
//                try {
//                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                    Ringtone r = RingtoneManager.getRingtone(context, notification);
//                    r.play();
//                } catch (Exception e) {
//                }
//
//                manager.notify(10110, note);
//            }
//        };
//        Thread threadNotify = new Thread(taskNotify);
//        threadNotify.setName("Thread Notify");
//        threadNotify.start();
//    }