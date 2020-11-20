package com.example.najakneang.db;

import android.provider.BaseColumns;

import com.example.najakneang.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

// 파일 설명 : 각 테이블의 지니는 속성과 저장된 데이터를 가공하는 메서드를 위한 파일
// 파일 주요기능 : 테이블 속성값과 저장된 값에 따른 아이콘, 색상, 남은 기한 관리

// 클래스 설명 : DB에서 사용하는 속성값 및 메서드 관리
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
        // 품목의 남은 기한을 구하는 메서드
        public static long getRemain(String expireDateStr) {
            LocalDate today = LocalDate.now();
            LocalDate expireDate = LocalDate.parse(expireDateStr, DateTimeFormatter.ofPattern("yyyyMMdd"));
            return ChronoUnit.DAYS.between(today, expireDate);
        }
        // 품목의 분류에 따라 아이콘을 정하는 메서드
        public static final HashMap<String, Integer> typeIconMap = new HashMap<String, Integer>() {
            {
                put("과일", R.drawable.ic_fruit);
                put("채소", R.drawable.ic_vegetable);
                put("수산", R.drawable.ic_fishery);
                put("육류", R.drawable.ic_meat);
                put("유제품", R.drawable.ic_dairy);
                put("반찬", R.drawable.ic_sidedish);
                put("주류", R.drawable.ic_alchohol);
                put("기타", R.drawable.ic_guitar);
            }
        };
        // 품목의 남은 기한에 따라 색상을 정하는 메서드
        public static String getRemainColor(String state, long remain){
            if(state.equals("냉동")) return "#1E90FF";
            if(remain < 0) return "#FF0000";
            if(remain < 7) return "#EEEE00";
            else return "#9C9C9C";
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
        public static final String COLUMN_CATEGORY = "CATEGORY";
        // 냉장고의 분류에 따라 이미지를 정하는 메서드
        public static final HashMap<String, Integer> categoryImageMap = new HashMap<String, Integer>() {
            {
                put("냉장고", R.drawable.img_fridge);
                put("김치 냉장고", R.drawable.img_fridge_kimchi);
                put("와인 냉장고", R.drawable.img_fridge_alcohol);
                put("팬트리", R.drawable.img_pantry);
            }
        };
    }
}
