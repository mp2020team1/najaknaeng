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
import com.example.najakneang.activity.MainActivity;
import com.example.najakneang.db.DBContract;

// 파일 설명 : 냉장고 추가 Dialog의 기능을 구성하는 파일
// 파일 주요기능 : 사용자가 Dialog에 입력한 정보를 바탕으로 유효한지 검토하고 DB에 저장

// 클래스 설명 : Dialog의 EditText, Spinner에서 입력한 정보를 검토하고 DB에 저장

public class FridgeDialog extends Dialog implements View.OnClickListener {

    private final Context context;

    private EditText fridgeName;
    private Spinner categorySpinner;

    SQLiteDatabase db = MainActivity.db;

    // 메서드 설명 : FridgeDialog 객체를 생성하고 Dialog가 생선된 Activity의 Context와 연결
    public FridgeDialog(@NonNull Context context){
        super(context);
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
        setContentView(R.layout.dialog_fridge);

        // Dialog의 Widget을 id로 받아옴
        TextView okBtn = findViewById(R.id.btn_ok_fridge_dialog);
        TextView cancelBtn = findViewById(R.id.btn_cancel_fridge_dialog);
        fridgeName = findViewById(R.id.edit_name_fridge_dialog);
        categorySpinner = findViewById(R.id.spinner_category_fridge_dialog);

        // 확인, 취소 버튼을 클릭 리스너와 연결
        okBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        // 냉장고 종류 Spinner에 들어갈 데이터 지정
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.fridgeArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            // 확인 버튼이 눌렀을 경우
            case R.id.btn_ok_fridge_dialog:
                String name = fridgeName.getText().toString();

                // 이름이 공백인지 확인
                if (name.trim().getBytes().length > 0) {

                    // 입력한 이름의 냉장고가 있는지 DB에서 받아옴
                    Cursor cursor = db.query(
                            DBContract.FridgeEntry.TABLE_NAME,
                            null,
                            DBContract.FridgeEntry.COLUMN_NAME + " = ? ",
                            new String[]{ name },
                            null,
                            null,
                            null,
                            "1"
                    );

                    // 같은 이름의 냉장고가 있으면 생성 불가
                    if (cursor.getCount() > 0) {
                        cursor.close();
                        Toast.makeText(context.getApplicationContext(), "이미 있는 냉장고입니다",Toast.LENGTH_SHORT).show();
                    } else {
                        cursor.close();

                        // 입력한 데이터를 바탕으로 db에 저장
                        ContentValues values = new ContentValues();
                        values.put(DBContract.FridgeEntry.COLUMN_NAME, name);
                        values.put(DBContract.FridgeEntry.COLUMN_CATEGORY, categorySpinner.getSelectedItem().toString());
                        db.insert(DBContract.FridgeEntry.TABLE_NAME, null, values);

                        //현재 페이지에 정보갱신
                        ((MainActivity)context).setupFridgeViewPager();
                        Toast.makeText(context.getApplicationContext(), "냉장고가 추가되었습니다",Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                }
                else {
                    Toast.makeText(context.getApplicationContext(), "이름을 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                break;
            // 취소버튼 클릭시
            case R.id.btn_cancel_fridge_dialog:
                dismiss();
                break;
        }
    }
};