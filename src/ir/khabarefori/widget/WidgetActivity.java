package ir.khabarefori.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import ir.khabarefori.ApplicationContextProvider;
import ir.khabarefori.MyActivity;
import ir.khabarefori.R;
import ir.khabarefori.database.datasource.NewsDatasource;
import ir.khabarefori.database.model.NewsModel;

/**
 * Created by hani on 1/11/14.
 */
public class WidgetActivity extends AppWidgetProvider {
    public static String WIDGET_BUTTON = "ir.khabarefori.layout.widget.txtNews";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];

            // create intent for open web site
            PendingIntent contentIntent = PendingIntent.getActivity(ApplicationContextProvider.getContext(), 0, new Intent(ApplicationContextProvider.getContext(), MyActivity.class), 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            views.setOnClickPendingIntent(R.id.imageView, contentIntent);

            // create intent for refresh :D
//            Intent intentRefresh = new Intent(WIDGET_BUTTON);
//            views.setOnClickPendingIntent(R.id.txtNews, contentIntent);
//

            NewsModel lastNews = NewsDatasource.getInstance().getLastNews();
            if (lastNews != null)
                views.setTextViewText(R.id.txtNews, lastNews.getSubject());
            else
                views.setTextViewText(R.id.txtNews, context.getString(R.string.no_internet));

            // just for update
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

//        if (WIDGET_BUTTON.equals(intent.getAction())) {
//            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
//
//            JsonGetNewNews json = new JsonGetNewNews(context, appWidgetManager);
//
//            ComponentName watchWidget = new ComponentName(context, WidgetActivity.class);
//
//            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
//            appWidgetManager.updateAppWidget(watchWidget, views);
//        }
    }
}
