package ir.khabarefori.database.datasource;

import android.content.ContentValues;
import android.database.Cursor;
import ir.khabarefori.database.SqlLite;
import ir.khabarefori.database.model.NewsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hani on 2/1/14.
 */
public class NewsDatasource {
    private String LOGTAG = getClass().getName();
    private static NewsDatasource instance;
    public static final String TABLE = "news";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SERVER_ID = "server_id";
    public static final String COLUMN_SUBJECT = "subject";
    public static final String COLUMN_CONTEXT = "context";
    public static final String COLUMN_LINK = "link";
    public static final String COLUMN_DATETIME = "datetime";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_IS_BREAKING_NEWS = "is_breaking_news";

    public static final String CREATE_TABLE = "CREATE TABLE \"" + TABLE + "\" (" +
            "    \"" + COLUMN_ID + "\" INTEGER PRIMARY KEY AUTOINCREMENT," +
            "    \"" + COLUMN_SERVER_ID + "\" INTEGER," +
            "    \"" + COLUMN_SUBJECT + "\" TEXT," +
            "    \"" + COLUMN_CONTEXT + "\" TEXT," +
            "    \"" + COLUMN_LINK + "\" TEXT," +
            "    \"" + COLUMN_DATETIME + "\" TEXT," +
            "    \"" + COLUMN_IMAGE + "\" TEXT," +
            "    \"" + COLUMN_IS_BREAKING_NEWS + "\" INTEGER DEFAULT (0)" +
            ");";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE;

    public static NewsDatasource getInstance() {
        if (instance == null)
            instance = new NewsDatasource();
        return instance;
    }

    public long add(NewsModel model) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_SUBJECT, model.getSubject());
        initialValues.put(COLUMN_SERVER_ID, model.getServerID());
        initialValues.put(COLUMN_CONTEXT, model.getContext());
        initialValues.put(COLUMN_IMAGE, model.getImage());
        initialValues.put(COLUMN_LINK, model.getLink());
        initialValues.put(COLUMN_DATETIME, model.getDatetime());
        initialValues.put(COLUMN_IS_BREAKING_NEWS, model.getIsBreakingNews());

        return SqlLite.getInstance().insert(TABLE, null, initialValues);
    }

    public boolean deleteContact(int id) {
        return SqlLite.getInstance().delete(TABLE, COLUMN_ID + "=" + id, null) > 0;
    }

    public void deleteAll() {
        SqlLite.getInstance().delete(TABLE, "1=1", null);
    }

    public NewsModel getRow(int id) {
        //@TODO fix this
//        Cursor cursor = SqlLite.instance().query(TABLE, new String[]{KEY_ROWID, KEY_NAME,
//                KEY_DESC, KEY_ABS, KEY_PAGECOUNT, KEY_PRICE, KEY_PATH, KEY_RATEVALUE, KEY_PUBDATETIME, KEY_PUBLISHER
//                , KEY_WRITER, KEY_TRANSLATOR, KEY_CATEGORY, KEY_TYPE}, KEY_ROWID + "=?", new String[]{String.valueOf(id)}, null, null, null);
//
//        if (cursor != null) {
//            cursor.moveToFirst();
//        }
//
//        return createModel(cursor);
        return null;
    }

    public NewsModel getLastNews() {
        Cursor cursor = null;
        try {
            cursor = SqlLite.getInstance().query(TABLE, new String[]{
                    "max(" + COLUMN_SERVER_ID + ") , " + COLUMN_SERVER_ID + " , " + COLUMN_SUBJECT + " ,*"}, null, null, null, null, null);
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
            cursor = SqlLite.getInstance().query(TABLE, new String[]{
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
        Cursor cursor = SqlLite.getInstance().query(TABLE, new String[]{
                COLUMN_ID,
                COLUMN_SERVER_ID,
                COLUMN_SUBJECT,
                COLUMN_CONTEXT,
                COLUMN_IMAGE,
                COLUMN_LINK,
                COLUMN_DATETIME,
                COLUMN_IS_BREAKING_NEWS}, null, null, null, null, "'" + COLUMN_ID + "' DESC LIMIT 0,10");

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
        model.setDatetime(cursor.getString(6));
        model.setImage(cursor.getString(5));
        model.setIsBreakingNewsParamInt(cursor.getInt(7));

        return model;
    }

    /**
     * Remove table
     */
    public void dropTable() {
        SqlLite.getInstance().execSQL("DROP TABLE IF EXISTS " + TABLE);
    }
}
