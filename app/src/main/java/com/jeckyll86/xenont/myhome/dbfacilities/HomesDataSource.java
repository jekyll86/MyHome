package com.jeckyll86.xenont.myhome.dbfacilities;

/**
 * Created by XenonT on 24/04/2015.
 */
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.jeckyll86.xenont.myhome.datamodel.Home;

public class HomesDataSource {

    // Database fields
    private SQLiteDatabase database;
    private DbHelper dbHelper;
    private String[] allColumns = { DbHelper.COLUMN_ID, DbHelper.COLUMN_NAME,
            DbHelper.COLUMN_ADDRESS, DbHelper.COLUMN_PORT};

    public HomesDataSource(Context context) {
        dbHelper = new DbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long createHome(Home myHome) {


        ContentValues values = new ContentValues();
        values.put(DbHelper.COLUMN_NAME, myHome.getName());
        values.put(DbHelper.COLUMN_ADDRESS, myHome.getAddress());

        if (myHome.getPort() != null) {
            values.put(DbHelper.COLUMN_PORT, myHome.getPort());
        }

        open();
        long insertId = database.insert(DbHelper.TABLE_HOMES, null,
                values);

        close();
        /*Cursor cursor = database.query(DbHelper.TABLE_HOMES,
                allColumns, DbHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Home newComment = cursorToHome(cursor);
        cursor.close();
        return newComment;*/


        return insertId;
    }

    public int updateHome(Home myHome){
        long id = myHome.getId();

        ContentValues values = new ContentValues();
        values.put(DbHelper.COLUMN_NAME, myHome.getName());
        values.put(DbHelper.COLUMN_ADDRESS, myHome.getAddress());
        if (myHome.getPort() != null) {
            values.put(DbHelper.COLUMN_PORT, myHome.getPort());
        }



        String whereClause= DbHelper.COLUMN_ID + " = ?";
        String[] whereArgs = new String[] { String.valueOf(id) };
        // updating row
        open();
        int result = database.update(DbHelper.TABLE_HOMES, values, whereClause, whereArgs);
        close();
        return result;
    }

    public void deleteHome(Home homeToDelete) {
        long id = homeToDelete.getId();
        System.out.println("Comment deleted with id: " + id);
        open();
        database.delete(DbHelper.TABLE_HOMES, DbHelper.COLUMN_ID
                + " = " + id, null);

        close();
    }

    public List<Home> getAllHomes() {
        List<Home> allHomes = new ArrayList<Home>();

        open();
        Cursor cursor = database.query(DbHelper.TABLE_HOMES,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Home comment = cursorToHome(cursor);
            allHomes.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();

        close();
        return allHomes;
    }

    public Home getHomeById(String id) {
        Home home = null;

        String whereClause = DbHelper.COLUMN_ID + " = ?";
        String[] whereArgs = new String[]{id};

        open();
        Cursor cursor = database.query(DbHelper.TABLE_HOMES,
                allColumns, whereClause, whereArgs, null, null, null);

        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            home = cursorToHome(cursor);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        close();
        return home;
    }


    public Home getHomeById(long id) {
        return getHomeById(String.valueOf(id));
    }

    private Home cursorToHome(Cursor cursor) {
        Home myHome = new Home();
        myHome.setId(cursor.getLong(0));
        myHome.setName(cursor.getString(1));
        myHome.setAddress(cursor.getString(2));
        myHome.setPort(cursor.getInt(3));
        return myHome;
    }
}

