package com.example.najakneang;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "test.db";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Items (" +
                "NAME "         + "TEXT," +
                "QUANTITY "     + "INTEGER NOT NULL," +
                "EXPIREDATE "   + "TEXT," +
                "CLASSID "        + "TEXT," +
                "FRIDGEID "     + "TEXT," +
                "STORAGEID "    + "TEXT" + ");" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Items");
        onCreate(db);
    }


}
