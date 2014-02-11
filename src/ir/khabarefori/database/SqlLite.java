package ir.khabarefori.database;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import ir.khabarefori.ApplicationContextProvider;
import ir.khabarefori.database.datasource.NewsDatasource;

/**
 * Created by hani on 2/1/14.
 */
public class SqlLite extends SQLiteOpenHelper {
    private static SqlLite sqlLite;
    public static final String DATABASE = "khabarefori";
    public static final int VERSION = 1;

    public SqlLite(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    public static SQLiteDatabase getInstance()
    {
        if(sqlLite == null)
            sqlLite = new SqlLite(ApplicationContextProvider.getContext());
        return sqlLite.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(NewsDatasource.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NewsDatasource.TABLE);
        onCreate(sqLiteDatabase);
    }
}
