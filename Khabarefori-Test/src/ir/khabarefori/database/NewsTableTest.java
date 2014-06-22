package ir.khabarefori.database;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.MediumTest;
import ir.khabarefori.database.datasource.NewsTable;
import ir.khabarefori.database.model.NewsModel;

/**
 * Created by Hani on 5/30/14.
 */
public class NewsTableTest extends AndroidTestCase {

    SqlLite database;

    @Override
    public void setUp(){
        RenamingDelegatingContext context
                = new RenamingDelegatingContext(getContext(), "test_");
        database = new SqlLite(context);

        database.DropDatabase(database.getWritableDatabase());
        database.CreateDatabase(database.getWritableDatabase());
    }

    @Override
    public void tearDown() throws Exception{
        database.DropDatabase(database.getWritableDatabase());
        database.close();

        super.tearDown();
    }

    @MediumTest
    public void test_add() {
        NewsTable.getInstance(database.getWritableDatabase()).deleteAll();
        addRow();
        assertEquals(1, NewsTable.getInstance(database.getWritableDatabase()).getAllContents().size());

        NewsModel news = NewsTable.getInstance().getLastNews();
        this.assertEachFields(news);
    }

    @MediumTest
    public void test_getRow()
    {
        NewsTable.getInstance(database.getWritableDatabase()).deleteAll();
        addRow();
        assertEquals(1, NewsTable.getInstance(database.getWritableDatabase()).getAllContents().size());

        NewsModel news =  NewsTable.getInstance(database.getWritableDatabase()).getRow(1);
        this.assertEachFields(news);

    }

    @LargeTest
    public void test_getAllContents()
    {
        NewsTable.getInstance(database.getWritableDatabase()).deleteAll();
        for (int i=0 ; i<10 ; i++)
            this.addRow(i+10);

        assertEquals(10, NewsTable.getInstance(database.getWritableDatabase()).getAllContents().size());

        NewsModel news = NewsTable.getInstance(database.getWritableDatabase()).getAllContents().get(9);
        assertEachFields(news);


    }

    @LargeTest
    public void test_getLastId()
    {
        NewsTable.getInstance(database.getWritableDatabase()).deleteAll();
        for (int i=0 ; i<10 ; i++)
            this.addRow(i+10);

        assertEquals(10, NewsTable.getInstance(database.getWritableDatabase()).getAllContents().size());

        assertEquals(19 , NewsTable.getInstance(database.getWritableDatabase()).getLastId());
    }

    private void assertEachFields(NewsModel news)
    {
        assertEquals("subject" , news.getSubject());
        assertEquals("context" , news.getContext());
        assertEquals("http://www.google.com" , news.getLink());
        assertEquals("2014-03-02 12:00:00" , news.getDatetime());
        assertEquals("image url" , news.getImage());
        assertEquals("1392-12-11" , news.getPersianDateTime());
        assertEquals(10, news.getServerID());
        assertEquals(true , news.getIsBreakingNews());
        assertEquals(1 , news.getid());
    }

    private void addRow(int ServiceId)
    {
        NewsModel model = new NewsModel();

        model.setServerID(ServiceId);
        model.setSubject("subject");
        model.setContext("context");
        model.setLink("http://www.google.com");
        model.setDatetime("2014-03-02 12:00:00");
        model.setImage("image url");
        model.setIsBreakingNewsParamBoolean(true);
        model.setPersianDatetimeAndConvert("2014-03-02 12:00:00");

        NewsTable.getInstance(database.getWritableDatabase()).add(model);
    }

    private void addRow()
    {
        this.addRow(10);
    }

    @LargeTest
    public void test_getLastNews()
    {
        NewsTable.getInstance(database.getWritableDatabase()).deleteAll();
        for (int i=0 ; i<10 ; i++)
            this.addRow(i+10);

        assertEquals(10, NewsTable.getInstance(database.getWritableDatabase()).getAllContents().size());

        assertEquals(10 , NewsTable.getInstance(database.getWritableDatabase()).getLastNews().getid());
    }

}
