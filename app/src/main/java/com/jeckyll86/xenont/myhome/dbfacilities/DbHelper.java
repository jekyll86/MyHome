package com.jeckyll86.xenont.myhome.dbfacilities;

/**
 * Created by XenonT on 24/04/2015.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jeckyll86.xenont.myhome.utils.AppConstants;

public class DbHelper extends SQLiteOpenHelper {
    private String TAG = DbHelper.class.getSimpleName();


    public static final String TABLE_HOMES = "homes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_PORT = "port";

    private static final String DATABASE_NAME = "myHomes.db";
    private static final int DATABASE_VERSION = 2;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table " + TABLE_HOMES +
            "(" + COLUMN_ID + " integer primary key autoincrement, "
                + COLUMN_NAME + " text not null unique, "
                + COLUMN_ADDRESS + " text not null unique, "
                + COLUMN_PORT + " integer default " + AppConstants.DEFAULT_PORT +");";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG,
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOMES);
        onCreate(db);
    }

}