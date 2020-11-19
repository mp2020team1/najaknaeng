package com.example.najakneang.model;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
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

public class SectionDialog extends Dialog implements View.OnClickListener {

    private final Context context;
    private final String current_fridge;
    private final String current_fridge_category;

    private EditText fridge_name;
    private Spinner spinner;

    SQLiteDatabase db = MainActivity.db;

    public SectionDialog(@NonNull Context context, String current_fridge, String current_fridge_category){
        super(context);
        this.current_fridge = current_fridge;
        this.current_fridge_category = current_fridge_category;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //Dialog 실행시, 주변 화면 흐리게 하기
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        //Dialog 레이아웃 지정
        setContentView(R.layout.dialog_section);

        TextView okBtn = findViewById(R.id.btn_ok_section_dialog);
        TextView cancelBtn = findViewById(R.id.btn_cancel_section_dialog);
        fridge_name = findViewById(R.id.edit_name_section_dialog);
        spinner = findViewById(R.id.spinner_state_section_dialog);

        okBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        final ArrayAdapter<CharSequence> adapter;
        switch (current_fridge_category){
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
                if (name.trim().getBytes().length > 0){
                    String sql = "SELECT " + DBContract.SectionEntry.TABLE_NAME + "." + DBContract.SectionEntry.COLUMN_NAME +
                                    " FROM " + DBContract.SectionEntry.TABLE_NAME +
                                    " INNER JOIN " + DBContract.FridgeEntry.TABLE_NAME +
                                    " ON " + DBContract.FridgeEntry.TABLE_NAME + "." + DBContract.FridgeEntry.COLUMN_NAME + " = " +
                                    DBContract.SectionEntry.TABLE_NAME + "." + DBContract.SectionEntry.COLUMN_FRIDGE +
                                    " WHERE " + DBContract.SectionEntry.TABLE_NAME + "." + DBContract.SectionEntry.COLUMN_NAME + " = "
                                    + "\"" + name + "\"" + " LIMIT 1";

                    Cursor cursor = db.rawQuery(sql, null);

                    if(cursor.getCount() == 0){
                        ContentValues values = new ContentValues();
                        values.put(DBContract.SectionEntry.COLUMN_FRIDGE, current_fridge);
                        values.put(DBContract.SectionEntry.COLUMN_NAME, name);
                        values.put(DBContract.SectionEntry.COLUMN_STORE_STATE, spinner.getSelectedItem().toString());
                        db.insert(DBContract.SectionEntry.TABLE_NAME, null, values);

                        //현재 페이지에 정보갱신
                        ((FridgeActivity)context).loadSection(current_fridge);
                        Toast.makeText(context.getApplicationContext(), "구역이 추가되었습니다",Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                    else{
                        Toast.makeText(context.getApplicationContext(), "이미 있는 구역 이름입니다",Toast.LENGTH_SHORT).show();
                    }
                }
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