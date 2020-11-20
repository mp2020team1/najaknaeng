package com.example.najakneang.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// 파일 설명 : sqlite를 사용한 내부 DB 구현을 위한 파일
// 파일 주요 기능 : DBHelper class를 통하여 database를 관리한다. 냉장고, 저장소 구역, 저장된 품목 등을
//                  각각 테이블로 저장하여 관리하며 각 테이블에는 필요한 특성을 저장하게 하였다.

public class DBHelper extends SQLiteOpenHelper {

    // 각 테이블의 생성 및 삭제의 편의를 위해 sql문을 String 객체로 저장한다.
    private static final String SQL_CREATE_ENTRIES1 =
            "CREATE TABLE "+ DBContract.GoodsEntry.TABLE_NAME +" ("+
                    DBContract.GoodsEntry._ID + " INTEGER PRIMARY KEY," +
                    DBContract.GoodsEntry.COLUMN_NAME + " TEXT," +
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
                    DBContract.FridgeEntry.COLUMN_NAME + " TEXT," +
                    DBContract.FridgeEntry.COLUMN_CATEGORY + " TEXT)";

    private static final String SQL_DELETE_ENTRIES3 =
            "DROP TABLE IF EXISTS " + DBContract.FridgeEntry.TABLE_NAME;

    public static final String DB_NAME = "najakneang.db";
    public static final int DB_VERSION = 1;

    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    // DB 생성을 위한 메서드
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES1);
        db.execSQL(SQL_CREATE_ENTRIES2);
        db.execSQL(SQL_CREATE_ENTRIES3);
    }

    // DB 갱신을 위한 메서드
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES1);
        db.execSQL(SQL_DELETE_ENTRIES2);
        db.execSQL(SQL_DELETE_ENTRIES3);
        onCreate(db);
    }


}
