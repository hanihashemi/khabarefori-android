package ir.khabarefori.database.datasource;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ir.khabarefori.database.SqlLite;
import ir.khabarefori.database.model.NewsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hani on 2/1/14.
 */
public class NewsTable {
    private static NewsTable instance;
    private SQLiteDatabase sqlLite;
    public static final String TABLE = "news";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SERVER_ID = "server_id";
    public static final String COLUMN_SUBJECT = "subject";
    public static final String COLUMN_CONTEXT = "context";
    public static final String COLUMN_LINK = "link";
    public static final String COLUMN_DATETIME = "datetime";
    public static final String COLUMN_PERSIAN_DATETIME = "persian_datetime";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_IS_BREAKING_NEWS = "is_breaking_news";

    public static final String CREATE_TABLE = "CREATE TABLE \"" + TABLE + "\" (" +
            "    \"" + COLUMN_ID + "\" INTEGER PRIMARY KEY AUTOINCREMENT," +
            "    \"" + COLUMN_SERVER_ID + "\" INTEGER," +
            "    \"" + COLUMN_SUBJECT + "\" TEXT," +
            "    \"" + COLUMN_CONTEXT + "\" TEXT," +
            "    \"" + COLUMN_LINK + "\" TEXT," +
            "    \"" + COLUMN_DATETIME + "\" TEXT," +
            "    \"" + COLUMN_PERSIAN_DATETIME + "\" TEXT," +
            "    \"" + COLUMN_IMAGE + "\" TEXT," +
            "    \"" + COLUMN_IS_BREAKING_NEWS + "\" INTEGER DEFAULT (0)" +
            ");";

    public static NewsTable getInstance(SQLiteDatabase sqlLite) {
        if (instance == null)
            instance = new NewsTable();

        instance.sqlLite = sqlLite;
        return instance;
    }

    public static NewsTable getInstance() {
        if (instance == null)
            instance = new NewsTable();

        return instance;
    }

    private SQLiteDatabase getSqlLite()
    {
        if (this.sqlLite == null)
            this.sqlLite = SqlLite.getInstance();
        return this.sqlLite;
    }

    public long add(NewsModel model) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_SUBJECT, model.getSubject());
        initialValues.put(COLUMN_SERVER_ID, model.getServerID());
        initialValues.put(COLUMN_CONTEXT, model.getContext());
        initialValues.put(COLUMN_IMAGE, model.getImage());
        initialValues.put(COLUMN_LINK, model.getLink());
        initialValues.put(COLUMN_DATETIME, model.getDatetime());
        initialValues.put(COLUMN_PERSIAN_DATETIME, model.getPersianDateTime());
        initialValues.put(COLUMN_IS_BREAKING_NEWS, model.getIsBreakingNews());

        return getSqlLite().insert(TABLE, null, initialValues);
    }

    public void deleteAll() {
        getSqlLite().delete(TABLE, "1=1", null);
    }

    public NewsModel getRow(int id) {
        Cursor cursor = getSqlLite().query(TABLE, new String[]{"*"}, COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return createModel(cursor);
    }

    public NewsModel getLastNews() {
        Cursor cursor = null;
        try {
            cursor = getSqlLite().query(TABLE, new String[]{
                    "*"}, null, null, null, null, null);
        } catch (Exception ex) {
            return null;
        }


        if (cursor.moveToFirst()) {
            return createModel(cursor);
        }

        return null;
    }

    public int getLastId() {
        Cursor cursor = null;
        try {
            cursor = getSqlLite().query(TABLE, new String[]{
                    "max(" + COLUMN_SERVER_ID + ")"}, null, null, null, null, null);
        } catch (Exception ex) {
            return 0;
        }


        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }

        return 0;
    }

    public List<NewsModel> getAllContents() {
        Cursor cursor = getSqlLite().query(TABLE, new String[]{
                "*"}, null, null, null, null, COLUMN_SERVER_ID + " DESC LIMIT 0,10");

        return createModelsOfArray(cursor);
    }

    private List<NewsModel> createModelsOfArray(Cursor cursor) {
        ArrayList<NewsModel> models = new ArrayList<NewsModel>();

        if (cursor.moveToFirst()) {
            do {
                models.add(createModel(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return models;
    }

    private NewsModel createModel(Cursor cursor) {
        NewsModel model = new NewsModel();

        model.setId(cursor.getInt(0));
        model.setServerID(cursor.getInt(1));
        model.setSubject(cursor.getString(2));
        model.setContext(cursor.getString(3));
        model.setLink(cursor.getString(4));
        model.setDatetime(cursor.getString(5));
        model.setPersianDatetime(cursor.getString(6));
        model.setImage(cursor.getString(7));
        model.setIsBreakingNewsParamInt(cursor.getInt(8));

        return model;
    }
}
