package ir.khabarefori.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import ir.khabarefori.ApplicationContextProvider;
import ir.khabarefori.database.datasource.NewsTable;

/**
 * Created by hani on 2/1/14.
 */
public class SqlLite extends SQLiteOpenHelper {
    private static SqlLite sqlLite;
    public static final String DATABASE = "khabarefori";
    public static final int VERSION = 2;

    public SqlLite(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    public static SQLiteDatabase getInstance() {
        if (sqlLite == null)
            sqlLite = new SqlLite(ApplicationContextProvider.getContext());
        return sqlLite.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.CreateDatabase(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            this.DropDatabase(sqLiteDatabase);
        onCreate(sqLiteDatabase);
    }

    /**
     * Drop database
     */
    public void DropDatabase(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NewsTable.TABLE);
    }

    /**
     * Create Database
     */
    public void CreateDatabase(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(NewsTable.CREATE_TABLE);
    }
}
