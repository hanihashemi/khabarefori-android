package ir.khabarefori.database;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.test.suitebuilder.annotation.MediumTest;
import ir.khabarefori.database.datasource.NewsTable;
import ir.khabarefori.database.model.NewsModel;
import ir.khabarefori.json.models.NewsJsonModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Hani on 6/1/14.
 */
public class NewsModelTest extends AndroidTestCase {
    SqlLite database;

    @Override
    public void setUp() {
        RenamingDelegatingContext context
                = new RenamingDelegatingContext(getContext(), "test_");
        database = new SqlLite(context);

        database.DropDatabase(database.getWritableDatabase());
        database.CreateDatabase(database.getWritableDatabase());
    }

    @Override
    public void tearDown() throws Exception {
        database.DropDatabase(database.getWritableDatabase());
        database.close();

        super.tearDown();
    }

    @MediumTest
    public void test_createRow() {
        NewsJsonModel.News news = new NewsJsonModel().new News();

        news.subject = "subject";
        news.context = "context";
        news.link = "http://www.google.com";
        news.datetime = "2014-03-02 12:00:00";
        news.id = 10;
        news.is_breaking_news = 1;

        NewsModel newsModel = NewsModel.createRow(news);

        NewsTable.getInstance(database.getWritableDatabase()).add(newsModel);

        assertEquals(1, NewsTable.getInstance(database.getWritableDatabase()).getAllContents().size());
        NewsModel dbNews = NewsTable.getInstance(database.getWritableDatabase()).getRow(1);
        this.assertEachFields(dbNews);
    }

    @MediumTest
    public void test_checkIsNewNews() {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);

        NewsModel newsModel = new NewsModel();
        newsModel.setDatetime(year + "-" + month + "-" + day + " 12:00:00");

        assertTrue(newsModel.isNew());

        newsModel.setDatetime(year + "-" + month + "-" + (day + 1) + " 12:00:00");

        assertFalse(newsModel.isNew());
    }

    @MediumTest
    public void test_formatToYesterdayOrToday_ForToday() {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);

        NewsModel newsModel = new NewsModel();
        newsModel.setDatetime(year + "-" + month + "-" + day + " 12:00:00");

        assertEquals(" امروز ", newsModel.formatToYesterdayOrToday());
    }

    @MediumTest
    public void test_formatToYesterdayOrToday_ForYesterday() {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);

        NewsModel newsModel = new NewsModel();
        newsModel.setDatetime(year + "-" + month + "-" + (day-1) + " 12:00:00");

        assertEquals(" دیروز ", newsModel.formatToYesterdayOrToday());
    }

    @MediumTest
    public void test_formatToYesterdayOrToday_Else() {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);

        NewsModel newsModel = new NewsModel();
        newsModel.setDatetime((year-1) + "-" + month + "-" + (day) + " 12:00:00");
        newsModel.setPersianDatetime((year-1) + "-" + month + "-" + (day) + " 12:00:00");

        assertEquals((year-1) + "-" + month + "-" + (day) + " 12:00:00", newsModel.formatToYesterdayOrToday());
    }

    @MediumTest
    public void test_createRows()
    {
        NewsJsonModel.News news = new NewsJsonModel().new News();

        news.subject = "subject";
        news.context = "context";
        news.link = "http://www.google.com";
        news.datetime = "2014-03-02 12:00:00";
        news.id = 10;
        news.is_breaking_news = 1;

        NewsJsonModel newsJson = new NewsJsonModel();
        newsJson.addNews(news);
        newsJson.addNews(news);

        NewsTable.getInstance(database.getWritableDatabase());
        NewsModel.createRows(newsJson);

        assertEquals(2, NewsTable.getInstance(database.getWritableDatabase()).getAllContents().size());
        NewsModel dbNews = NewsTable.getInstance(database.getWritableDatabase()).getRow(1);
        this.assertEachFields(dbNews);
    }

    private void assertEachFields(NewsModel news) {
        assertEquals("subject", news.getSubject());
        assertEquals("context", news.getContext());
        assertEquals("http://www.google.com", news.getLink());
        assertEquals("2014-03-02 12:00:00", news.getDatetime());
        assertNull(news.getImage());
        assertEquals("1392-12-11", news.getPersianDateTime());
        assertEquals(10, news.getServerID());
        assertEquals(true, news.getIsBreakingNews());
        assertEquals(1, news.getid());
    }
}
