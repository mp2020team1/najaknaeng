package com.example.najakneang.db;

import android.provider.BaseColumns;

import com.example.najakneang.R;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

public final class DBContract {

    private DBContract() {}

    public static class GoodsEntry implements BaseColumns{
        public static final String TABLE_NAME = "Goods";
        public static final String COLUMN_NAME = "NAME";
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
        public static final HashMap<String, Integer> typeIconMap = new HashMap<String, Integer>() {
            {
                put("과일", R.drawable.ic_type_fruit);
                put("채소", R.drawable.ic_type_vegetable);
                put("수산", R.drawable.ic_type_fish);
                put("육류", R.drawable.ic_type_meat);
                put("유제품", R.drawable.ic_launcher_background);
                put("반찬", R.drawable.ic_launcher_background);
            }
        };
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
        public static final String COLUMN_CATEGORY = "CATEGORY";
        public static final HashMap<String, Integer> categoryImageMap = new HashMap<String, Integer>() {
            {
                put("냉장고", R.drawable.img_fridge);
                put("김치냉장고", R.drawable.img_fridge_kimchi);
                put("팬트리", R.drawable.img_pantry);
            }
        };
    }
}
