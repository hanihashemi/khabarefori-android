package ir.khabarefori.json;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import com.google.gson.Gson;
import ir.khabarefori.AppPath;
import ir.khabarefori.MyActivity;
import ir.khabarefori.R;
import ir.khabarefori.json.models.ModelNews;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by hani on 1/11/14.
 */
public class JsonGetNewNews implements Runnable {
    private static ModelNews news;

    RemoteViews remoteViews;
    AppWidgetManager appWidgetManager;
    ComponentName thisWidget;
    Context context;
    private static String lastNews = "";
    private static boolean checkUpdate = true;

    public JsonGetNewNews(Context context, AppWidgetManager appWidgetManager) {
        this.context = context;

        this.appWidgetManager = appWidgetManager;
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.main);
        thisWidget = new ComponentName(context, MyActivity.class);

//        setMessage("", context.getString(R.string.news_loading));
    }

    private void setTextNews(String text) {
        remoteViews.setTextViewText(R.id.txtNews,
                String.valueOf(text));
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
    }

    private void setTextMessage(String text) {
        remoteViews.setTextViewText(R.id.txtStatus,
                String.valueOf(text));
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
    }

    public void run() {
if (true) return;
        try {
            Log.d("Hani", "Start checking ...");


            URL url = new URL(AppPath.Network.getNewNewsPage());

            URLConnection urlc = url.openConnection();

            BufferedReader buffer = new BufferedReader(new InputStreamReader(
                    urlc.getInputStream(), "UTF8"));

            // convert the json string back to object
            Gson gson = new Gson();
            news = gson.fromJson(buffer, ModelNews.class);

            if (getNews().getNews().size() < 1){
//                setMessage(context.getString(R.string.news_nonews), "");
                Log.d("Hani" , "No news .");
            }
            else {
                if (!lastNews.equals(news
                        .getNews().get(0).subject))
                    startNotify(news.getNews().get(0).subject);

                setMessage(news.getNews().get(0).subject, "خبر فوری");
                lastNews = news.getNews().get(0).subject;

                Log.d("Hani" , "Founded new news.");
            }
        } catch (IOException e) {
//            setMessage("", context.getString(R.string.news_nointernet));
            //e.printStackTrace();
        }
    }

    private void startNotify(final String message) {
        Runnable taskNotify = new Runnable() {
            @Override
            public void run() {
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                Intent launchIntenet = new Intent(context,
                        MyActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(
                        context, 0, launchIntenet, 0);

                android.app.Notification note = new android.app.Notification(
                        R.drawable.logo_circle, "خبر فوری",
                        System.currentTimeMillis());
                note.setLatestEventInfo(context, message, "خبر فوری",
                        contentIntent);

                try {
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone(context, notification);
                    r.play();
                } catch (Exception e) {
                }

                manager.notify(10110, note);
            }
        };
        Thread threadNotify = new Thread(taskNotify);
        threadNotify.setName("Thread Notify");
        threadNotify.start();
    }

    public static ModelNews getNews() {
        return news;
    }

    public void setMessage(String news, String status) {
        if (news.equals("")) {
            if (lastNews.equals("")) {
                Log.d("Hani" , "st1 , news= " + news + " , status= " + status);
                setTextNews(status);
                setTextMessage("");
            } else {
                Log.d("Hani" , "st2 , news= " + news + " , status= " + status);
                setTextNews(lastNews);
                setTextMessage(status);
            }
        } else {
            Log.d("Hani" , "st3 , news= " + news + " , status= " + status);
            setTextNews(news);
            setTextMessage(status);
        }
    }
}

