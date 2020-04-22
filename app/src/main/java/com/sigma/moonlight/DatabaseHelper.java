package com.sigma.moonlight;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contacts1.db";
    private static final String TABLEE_NAME = "contacts";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_UNAME = "uname";
    private static final String COLUMN_PASS = "pass";
    /*for signup*/
    private static final String TABLE_CREATE = "create table contacts (id integer primary key ," +
            " name text, email text , uname text , pass text);";
    SQLiteDatabase db;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        this.db = db;
    }


    /*for signup*/

    public void insertcontact(Contact c) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        String query = "select * from contacts";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cv.put(COLUMN_ID, count);
        cv.put(COLUMN_NAME, c.getName());
        cv.put(COLUMN_EMAIL, c.getEmail());
        cv.put(COLUMN_UNAME, c.getUsername());
        cv.put(COLUMN_PASS, c.getPass());

        db.insert(TABLEE_NAME, null, cv);
        db.close();
    }


    public String searchPass(String uname) {
        db = this.getReadableDatabase();
        String query = "select uname, pass from " + TABLEE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String a, b;
        b = "not found";
        if (cursor.moveToFirst()) {
            do {
                a = cursor.getString(0);
                if (a.equals(uname)) {
                    b = cursor.getString(1);
                    break;
                }
            } while (cursor.moveToNext());
        }
        return b;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS" + TABLEE_NAME;
        db.execSQL(query);
        this.onCreate(db);
    }
}
