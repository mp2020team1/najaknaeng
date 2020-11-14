package com.example.najakneang.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES1 =
            "CREATE TABLE "+ DBContract.GoodsEntry.TABLE_NAME +" ("+
                    DBContract.GoodsEntry._ID + " INTEGER PRIMARY KEY," +
                    DBContract.GoodsEntry.COLUMN_NAME + " TEXT," +
                    DBContract.GoodsEntry.COLUMN_IMAGE + " INTEGER NOT NULL," +
                    DBContract.GoodsEntry.COLUMN_QUANTITY + " INTEGER NOT NULL," +
                    DBContract.GoodsEntry.COLUMN_REGISTDATE + " TEXT," +
                    DBContract.GoodsEntry.COLUMN_EXPIREDATE + " TEXT," +
                    DBContract.GoodsEntry.COLUMN_TYPE + " TEXT," +
                    DBContract.GoodsEntry.COLUMN_FRIDGE + " TEXT," +
                    DBContract.GoodsEntry.COLUMN_SECTION + " TEXT)";

    private static final String SQL_DELETE_ENTRIES1 =
            "DROP TABLE IF EXISTS " + DBContract.GoodsEntry.TABLE_NAME;

    private static final String SQL_CREATE_ENTRIES2 =
            "CREATE TABLE " + DBContract.SectionEntry.TABLE_NAME + " (" +
                    DBContract.SectionEntry._ID + " INTEGER PRIMARY KEY," +
                    DBContract.SectionEntry.COLUMN_NAME + " TEXT," +
                    DBContract.SectionEntry.COLUMN_FRIDGE + " TEXT, " +
                    DBContract.SectionEntry.COLUMN_STORE_STATE + " TEXT)";

    private static final String SQL_DELETE_ENTRIES2 =
            "DROP TABLE IF EXISTS " + DBContract.SectionEntry.TABLE_NAME;

    private static final String SQL_CREATE_ENTRIES3 =
            "CREATE TABLE " + DBContract.FridgeEntry.TABLE_NAME + " (" +
                    DBContract.FridgeEntry._ID + " INTEGER PRIMARY KEY," +
                    DBContract.FridgeEntry.COLUMN_NAME + " TEXT)";

    private static final String SQL_DELETE_ENTRIES3 =
            "DROP TABLE IF EXISTS " + DBContract.FridgeEntry.TABLE_NAME;

    public static final String DB_NAME = "najakneang.db";
    public static final int DB_VERSION = 1;

    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES1);
        db.execSQL(SQL_CREATE_ENTRIES2);
        db.execSQL(SQL_CREATE_ENTRIES3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES1);
        db.execSQL(SQL_DELETE_ENTRIES2);
        db.execSQL(SQL_DELETE_ENTRIES3);
        onCreate(db);
    }


}
