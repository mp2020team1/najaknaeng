package com.example.najakneang.model;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.najakneang.R;
import com.example.najakneang.activity.FreshnessActivity;
import com.example.najakneang.activity.MainActivity;
import com.example.najakneang.activity.SectionActivity;
import com.example.najakneang.db.DBContract;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class GoodsDialog extends Dialog implements View.OnClickListener {

    private String fridge;
    private String section;
    private final Context context;

    private EditText name;
    private EditText quantity;

    private EditText expireDate;
    private Spinner typeSpinner;
    private Spinner fridgeSpinner;
    private Spinner sectionSpinner;
    private LinearLayout fridgeLayout;

    SQLiteDatabase db = MainActivity.db;

    public GoodsDialog(@NonNull Context context){
        super(context);
        this.context = context;
    }

    public GoodsDialog(@NonNull Context context, String fridge, String section){
        super(context);
        this.context = context;
        this.fridge = fridge;
        this.section = section;
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
        setContentView(R.layout.dialog_goods);

        TextView okBtn = findViewById(R.id.btn_ok_goods_dialog);
        TextView cancelBtn = findViewById(R.id.btn_cancel_goods_dialog);
        name = findViewById(R.id.edit_name_goods_dialog);
        quantity = findViewById(R.id.edit_quantity_goods_dialog);
        expireDate = findViewById(R.id.edit_expire_date_goods_dialog);
        typeSpinner = findViewById(R.id.spinner_type_goods_dialog);
        fridgeSpinner = findViewById(R.id.spinner_fridge_goods_dialog);
        sectionSpinner = findViewById(R.id.spinner_section_goods_dialog);
        fridgeLayout = findViewById(R.id.fridge_layout);

        okBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        final ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(context,
                R.array.secondTab, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        typeSpinner.setSelection(0);

        if (context.getClass() == FreshnessActivity.class) {
            ArrayList<String> fridgeNameList = new ArrayList<>();
            Cursor cursor = db.query(
                    DBContract.FridgeEntry.TABLE_NAME,
                    new String[]{DBContract.FridgeEntry.COLUMN_NAME},
                    null,
                    null,
                    null,
                    null,
                    null
            );

            fridgeNameList.add("냉장고를 선택해주세요");

            while(cursor.moveToNext()){
                fridgeNameList.add(cursor.getString(
                        cursor.getColumnIndex(DBContract.FridgeEntry.COLUMN_NAME)));
            }
            cursor.close();

            final ArrayAdapter<String> fridgeAdapter = new ArrayAdapter<>(context,
                    android.R.layout.simple_spinner_item, fridgeNameList);
            fridgeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            fridgeSpinner.setAdapter(fridgeAdapter);
            fridgeSpinner.setSelection(0,false);
            fridgeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    ArrayList<String> sectionNameList = new ArrayList<>();
                    Cursor cursor = db.query(
                            DBContract.SectionEntry.TABLE_NAME,
                            new String[]{ DBContract.SectionEntry.COLUMN_NAME },
                            DBContract.SectionEntry.COLUMN_FRIDGE + " = ? ",
                            new String[]{ fridgeSpinner.getSelectedItem().toString() },
                            null,
                            null,
                            null
                    );

                    sectionNameList.add("구역을 선택해주세요");

                    while (cursor.moveToNext()) {
                        sectionNameList.add(cursor.getString(
                                cursor.getColumnIndex(DBContract.FridgeEntry.COLUMN_NAME)));
                    }
                    cursor.close();

                    final ArrayAdapter<String> section_adapter = new ArrayAdapter<>(context,
                            android.R.layout.simple_spinner_item, sectionNameList);
                    section_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sectionSpinner.setAdapter(section_adapter);
                    sectionSpinner.setSelection(0,false);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        else{
            fridgeLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.btn_ok_goods_dialog:
                String nameStr = name.getText().toString();
                String quantityStr = quantity.getText().toString();
                String expireDateStr = expireDate.getText().toString();
                if(nameStr.trim().getBytes().length == 0){
                    Toast.makeText(context.getApplicationContext(),
                            "이름을 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                else if(quantityStr.trim().getBytes().length == 0){
                    Toast.makeText(context.getApplicationContext(),
                            "수량을 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                else if(Integer.parseInt(quantityStr) <= 0){
                    Toast.makeText(context.getApplicationContext(),
                            "수량 형식이 잘못되었습니다",Toast.LENGTH_SHORT).show();
                }
                else if(typeSpinner.getSelectedItemPosition() == 0){
                    Toast.makeText(context.getApplicationContext(),
                            "종류를 선택해주세요",Toast.LENGTH_SHORT).show();
                }
                else if(expireDateStr.trim().getBytes().length == 0){
                    Toast.makeText(context.getApplicationContext(),
                            "날짜 형식이 잘못되었습니다",Toast.LENGTH_SHORT).show();
                }
                else if (fridgeSpinner.getSelectedItemPosition() == 0 && fridgeLayout.getVisibility() == View.GONE){
                    Toast.makeText(context.getApplicationContext(),
                            "냉장고를 선택해주세요",Toast.LENGTH_SHORT).show();
                }
                else if (sectionSpinner.getSelectedItemPosition() == 0 && fridgeLayout.getVisibility() == View.GONE){
                    Toast.makeText(context.getApplicationContext(),
                            "구역을 선택해주세요",Toast.LENGTH_SHORT).show();
                } else {
                    LocalDate expireDate;
                    try {
                        expireDate = LocalDate.parse(expireDateStr, DateTimeFormatter.ofPattern("yyyyMMdd"));

                        ContentValues values = new ContentValues();
                        values.put(DBContract.GoodsEntry.COLUMN_NAME, nameStr);
                        values.put(DBContract.GoodsEntry.COLUMN_QUANTITY, Integer.parseInt(quantityStr));
                        values.put(
                                DBContract.GoodsEntry.COLUMN_REGISTDATE,
                                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                        );
                        values.put(DBContract.GoodsEntry.COLUMN_EXPIREDATE, expireDateStr);
                        values.put(DBContract.GoodsEntry.COLUMN_TYPE, typeSpinner.getSelectedItem().toString());
                        values.put(DBContract.GoodsEntry.COLUMN_FRIDGE,
                                fridgeLayout.getVisibility() == View.GONE? fridgeSpinner.getSelectedItem().toString():fridge);
                        values.put(DBContract.GoodsEntry.COLUMN_SECTION,
                                fridgeLayout.getVisibility() == View.GONE? sectionSpinner.getSelectedItem().toString():section);
                        db.insert(DBContract.GoodsEntry.TABLE_NAME, null, values);


                        if (context.getClass() == FreshnessActivity.class) {
                            ((FreshnessActivity)context).setupFreshnessRecycler(); }
                        else if (context.getClass() == SectionActivity.class) {
                            ((SectionActivity)context).setupFreshnessRecycler(fridge, section);}

                        dismiss();
                    }
                    catch (Exception e) {
                        Toast.makeText(context.getApplicationContext(),
                                "날짜 형식이 잘못되었습니다",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                break;
            case R.id.btn_cancel_goods_dialog:
                dismiss();
                break;
        }
    }
};