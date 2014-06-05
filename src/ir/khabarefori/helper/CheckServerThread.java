package ir.khabarefori.helper;

import ir.khabarefori.MyActivity;
import ir.khabarefori.Server;
import ir.khabarefori.database.datasource.NewsTable;
import ir.khabarefori.database.model.NewsModel;
import ir.khabarefori.json.models.NewsJsonModel;
import ir.khabarefori.notify.Knotify;

import java.util.TimerTask;

/**
 * Created by Hani on 6/2/14.
 */
public class CheckServerThread extends TimerTask {
    private static boolean isRun = false;

    public static void CheckNews() {
        Thread thread = new Thread(new CheckServerThread());
        thread.start();
    }

    public void run() {
        if (isRun)
            return;
        isRun = true;

        try {
            MyActivity.setBtnReloadIsActive(true);
            knotify(Knotify.MessageType.MSG_TRY_CONNECT_TO_SERVER);

            NewsJsonModel news = Server.getLastNews();
            NewsModel.createRows(news);
            checkIsThereNewNews(news);
        } catch (
                Exception ex
                ) {
            knotify(Knotify.MessageType.MSG_NO_INTERNET);
        } finally {
            MyActivity.setBtnReloadIsActive(false);
            isRun = false;
        }
    }

    /**
     *
     */
    private void checkIsThereNewNews(NewsJsonModel news) {
        if (news.getNews().size() == 0) {
            knotify(Knotify.MessageType.MSG_NO_NEWS);
        } else {
            knotify(Knotify.MessageType.MSG_NEW_NEWS_UPDATED);

            NewsModel lastNews = NewsTable.getInstance().getLastNews();
            if (lastNews.isNew())
                new Notification().Show(lastNews.getSubject(), lastNews.getIsBreakingNews());
        }
    }

    /**
     * @param type
     */
    private void knotify(int type) {
        try {
            Knotify.getInstance().show(type);
        } catch (Exception ex) {
        }
    }
}
