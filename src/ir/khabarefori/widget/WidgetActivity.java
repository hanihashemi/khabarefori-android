package ir.khabarefori.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import ir.khabarefori.R;
import ir.khabarefori.json.JsonGetNewNews;

/**
 * Created by hani on 1/11/14.
 */
public class WidgetActivity extends AppWidgetProvider {
    public static String WIDGET_BUTTON = "ir.khabarefori.layout.widget.txtNews";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        JsonGetNewNews json = new JsonGetNewNews(context, appWidgetManager);
        json.run();

        final int N = appWidgetIds.length;

        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];

            // create intent for open web site
            String url = "http://www.khabarefori.ir";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main);
            views.setOnClickPendingIntent(R.id.imageView, pendingIntent);

            // create intent for refresh :D
            Intent intentRefresh = new Intent(WIDGET_BUTTON);
            PendingIntent pendingIntentRefresh = PendingIntent.getBroadcast(context, 0, intentRefresh, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.txtNews, pendingIntentRefresh);

            // just for update
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }


    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (WIDGET_BUTTON.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            JsonGetNewNews json = new JsonGetNewNews(context, appWidgetManager);
            json.run();

            ComponentName watchWidget = new ComponentName(context, WidgetActivity.class);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main);
            appWidgetManager.updateAppWidget(watchWidget, views);
        }
    }
}
