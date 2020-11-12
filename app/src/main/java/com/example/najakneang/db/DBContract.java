package com.example.najakneang.db;

import android.provider.BaseColumns;

public final class DBContract {

    private DBContract(){}

    public static class GoodsEntry implements BaseColumns{
        public static final String TABLE_NAME = "Goods";
        public static final String COLUMNS_NAME = "NAME";
        public static final String COLUMNS_QUANTITY = "QUANTITY";
        public static final String COLUMNS_REGISTDATE = "REGISTDATE";
        public static final String COLUMNS_EXPIREDATE = "EXPIREDATE";
        public static final String COLUMNS_TYPE = "TYPE";
        public static final String COLUMNS_FRIDGE = "FRIDGE";
        public static final String COLUMNS_SECTION = "SECTION";
    }

    public static class SectionEntry implements BaseColumns{
        public static final String TABLE_NAME = "Sections";
        public static final String COLUMNS_NAME = "NAME";
        public static final String COLUMNS_FRIDGE = "FRIDGE";
        public static final String COLUMNS_STORE_STATE = "STORESTATE";
    }

    public static class FridgeEntry implements BaseColumns{
        public static final String TABLE_NAME = "Fridge";
        public static final String COLUMNS_NAME = "NAME";
    }
}
