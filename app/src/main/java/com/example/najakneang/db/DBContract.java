package com.example.najakneang.db;

import android.provider.BaseColumns;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class DBContract {

    private DBContract() {}

    public static class GoodsEntry implements BaseColumns{
        public static final String TABLE_NAME = "Goods";
        public static final String COLUMN_NAME = "NAME";
        public static final String COLUMN_IMAGE = "IMAGE";
        public static final String COLUMN_QUANTITY = "QUANTITY";
        public static final String COLUMN_REGISTDATE = "REGISTDATE";
        public static final String COLUMN_EXPIREDATE = "EXPIREDATE";
        public static final String COLUMN_TYPE = "TYPE";
        public static final String COLUMN_FRIDGE = "FRIDGE";
        public static final String COLUMN_SECTION = "SECTION";
        public static long getRemain(String expireDateStr) {
            LocalDate today = LocalDate.now();
            LocalDate expireDate = LocalDate.parse(expireDateStr);
            return ChronoUnit.DAYS.between(today, expireDate);
        }
    }

    public static class SectionEntry implements BaseColumns{
        public static final String TABLE_NAME = "Sections";
        public static final String COLUMN_NAME = "NAME";
        public static final String COLUMN_FRIDGE = "FRIDGE";
        public static final String COLUMN_STORE_STATE = "STORESTATE";
    }

    public static class FridgeEntry implements BaseColumns{
        public static final String TABLE_NAME = "Fridge";
        public static final String COLUMN_NAME = "NAME";
    }
}
