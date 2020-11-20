package com.example.najakneang.model;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.najakneang.R;
import com.example.najakneang.activity.FridgeActivity;
import com.example.najakneang.activity.MainActivity;
import com.example.najakneang.db.DBContract;

// 파일 설명 : 구역 추가 Dialog의 기능을 구성하는 파일
// 파일 주요기능 : 사용자가 Dialog에 입력한 정보를 바탕으로 유효한지 검토하고 DB에 저장 및 수정

// 클래스 설명 : Dialog의 EditText, Spinner에서 입력한 정보를 검토하고 DB에 저장 및 수정

public class SectionDialog extends Dialog implements View.OnClickListener {

    private final Context context;
    private final String current_fridge;
    private final String current_fridge_category;

    private EditText fridge_name;
    private Spinner spinner;

    SQLiteDatabase db = MainActivity.db;

    // 메서드 설명 : GoodsDialog 객체를 생성하고 Dialog가 생선된 Activity의 Context, 구역의 냉장고 이름 및 종류와 연결
    public SectionDialog(@NonNull Context context, String current_fridge, String current_fridge_category){
        super(context);
        this.current_fridge = current_fridge;
        this.current_fridge_category = current_fridge_category;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // Dialog 실행시, 주변 화면 흐리게 하기
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        // Dialog 레이아웃 지정
        setContentView(R.layout.dialog_section);

        // Dialog의 Widget을 id로 받아옴
        TextView okBtn = findViewById(R.id.btn_ok_section_dialog);
        TextView cancelBtn = findViewById(R.id.btn_cancel_section_dialog);
        fridge_name = findViewById(R.id.edit_name_section_dialog);
        spinner = findViewById(R.id.spinner_state_section_dialog);

        okBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        final ArrayAdapter<CharSequence> adapter;

        // 현재 냉장고의 종류에 따라 구역 보과상태에 Spinner 데이터 변경경
        switch(current_fridge_category){
            case "냉장고":
            case "김치 냉장고":
                adapter = ArrayAdapter.createFromResource(context,
                        R.array.sectionArrayFrezze, android.R.layout.simple_spinner_item);
                break;
            case "와인 냉장고":
                adapter = ArrayAdapter.createFromResource(context,
                        R.array.sectionArrayRefrige, android.R.layout.simple_spinner_item);
                break;
            case "팬트리":
                adapter = ArrayAdapter.createFromResource(context,
                        R.array.sectionArrayPantry, android.R.layout.simple_spinner_item);
                break;
            default:
                adapter = ArrayAdapter.createFromResource(context,
                        R.array.sectionArray, android.R.layout.simple_spinner_item);
        }


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.btn_ok_section_dialog:
                String name = fridge_name.getText().toString();

                // 구역 이름이 빈칸이 아닌 경우
                if (name.trim().getBytes().length > 0){

                    // 해당 구역 이름이 냉장고 내에 존재하는지 받아옴
                    String sql = "SELECT " + DBContract.SectionEntry.TABLE_NAME + "." + DBContract.SectionEntry.COLUMN_NAME +
                                    " FROM " + DBContract.SectionEntry.TABLE_NAME +
                                    " INNER JOIN " + DBContract.FridgeEntry.TABLE_NAME +
                                    " ON " + DBContract.FridgeEntry.TABLE_NAME + "." + DBContract.FridgeEntry.COLUMN_NAME + " = " +
                                    DBContract.SectionEntry.TABLE_NAME + "." + DBContract.SectionEntry.COLUMN_FRIDGE +
                                    " WHERE " + DBContract.SectionEntry.TABLE_NAME + "." + DBContract.SectionEntry.COLUMN_NAME + " = "
                                    + "\"" + name + "\"" + " AND " + DBContract.FridgeEntry.TABLE_NAME + "." + DBContract.FridgeEntry.COLUMN_NAME
                                    + " = " + "\"" + current_fridge + "\"" + " LIMIT 1";

                    Cursor cursor = db.rawQuery(sql, null);

                    // 똑같은 구역 이름이 없을 경우 DB에 저장
                    if(cursor.getCount() == 0){
                        ContentValues values = new ContentValues();
                        values.put(DBContract.SectionEntry.COLUMN_FRIDGE, current_fridge);
                        values.put(DBContract.SectionEntry.COLUMN_NAME, name);
                        values.put(DBContract.SectionEntry.COLUMN_STORE_STATE, spinner.getSelectedItem().toString());
                        db.insert(DBContract.SectionEntry.TABLE_NAME, null, values);

                        // 현재 페이지에 정보갱신
                        ((FridgeActivity)context).loadSection(current_fridge);
                        Toast.makeText(context.getApplicationContext(), "구역이 추가되었습니다",Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                    // 냉장고내에 같은 구역 이름이 있는 경우
                    else{
                        Toast.makeText(context.getApplicationContext(), "이미 있는 구역 이름입니다",Toast.LENGTH_SHORT).show();
                    }
                }
                // 입력한 구역 이름이 빈칸인 경우
                else {
                    Toast.makeText(context.getApplicationContext(), "이름을 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_cancel_section_dialog:
                dismiss();
                break;
        }
    }
};