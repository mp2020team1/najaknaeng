package com.example.najakneang.model;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
    private String fridgeCategory = "";
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

    public GoodsDialog(@NonNull Context context, String fridge, String section, String fridgeCategory) {
        super(context);
        this.context = context;
        this.fridge = fridge;
        this.section = section;
        this.fridgeCategory = fridgeCategory;
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

        setupDialog();
    }

    private void setupDialog() {
        TextView okBtn = findViewById(R.id.btn_ok_goods_dialog);
        TextView cancelBtn = findViewById(R.id.btn_cancel_goods_dialog);
        name = findViewById(R.id.edit_name_goods_dialog);
        quantity = findViewById(R.id.edit_quantity_goods_dialog);
        expireDate = findViewById(R.id.edit_expire_date_goods_dialog);
        typeSpinner = findViewById(R.id.spinner_type_goods_dialog);
        fridgeSpinner = findViewById(R.id.spinner_fridge_goods_dialog);
        sectionSpinner = findViewById(R.id.spinner_section_goods_dialog);
        fridgeLayout = findViewById(R.id.fridge_layout_goods_dialog);

        okBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        if (context.getClass() == FreshnessActivity.class) {
            ArrayList<String> fridgeNameList = new ArrayList<>();
            Cursor cursor = db.query(
                    DBContract.FridgeEntry.TABLE_NAME,
                    new String[]{ DBContract.FridgeEntry.COLUMN_NAME },
                    null,
                    null,
                    null,
                    null,
                    null
            );

            while (cursor.moveToNext()) {
                fridgeNameList.add(cursor.getString(
                        cursor.getColumnIndex(DBContract.FridgeEntry.COLUMN_NAME)));
            }
            cursor.close();

            final ArrayAdapter<String> fridgeAdapter = new ArrayAdapter<>(context,
                    android.R.layout.simple_spinner_item, fridgeNameList);
            fridgeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            fridgeSpinner.setAdapter(fridgeAdapter);
            fridgeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Cursor fridgeCursor = db.query(
                            DBContract.FridgeEntry.TABLE_NAME,
                            new String[]{ DBContract.FridgeEntry.COLUMN_CATEGORY },
                            DBContract.FridgeEntry.COLUMN_NAME + " = ? ",
                            new String[]{ fridgeSpinner.getSelectedItem().toString() },
                            null,
                            null,
                            null
                    );

                    fridgeCursor.moveToNext();
                    String category = fridgeCursor.getString(
                            fridgeCursor.getColumnIndex(DBContract.FridgeEntry.COLUMN_CATEGORY));

                    final ArrayAdapter<CharSequence> newTypeAdapter;
                    if (category.equals("와인 냉장고")) {
                        newTypeAdapter = ArrayAdapter.createFromResource(context,
                                R.array.alchoholArray, android.R.layout.simple_spinner_item);
                    } else {
                        newTypeAdapter = ArrayAdapter.createFromResource(context,
                                R.array.ingredientArray, android.R.layout.simple_spinner_item);
                    }
                    newTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    typeSpinner.setAdapter(newTypeAdapter);
                    typeSpinner.setSelection(0);


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

                    if (cursor.getCount() == 0)
                        Toast.makeText(context, "구역이 없는 냉장고입니다.", Toast.LENGTH_SHORT).show();

                    while (cursor.moveToNext()) {
                        sectionNameList.add(cursor.getString(
                                cursor.getColumnIndex(DBContract.FridgeEntry.COLUMN_NAME)));
                    }
                    cursor.close();

                    final ArrayAdapter<String> sectionAdapter = new ArrayAdapter<>(context,
                            android.R.layout.simple_spinner_item, sectionNameList);
                    sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sectionSpinner.setAdapter(sectionAdapter);
                    sectionSpinner.setSelection(0,false);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });

            fridgeSpinner.setSelection(0,false);
        } else {
            final ArrayAdapter<CharSequence> typeAdapter;
            if (fridgeCategory.equals("와인 냉장고")) {
                typeAdapter = ArrayAdapter.createFromResource(context,
                        R.array.alchoholArray, android.R.layout.simple_spinner_item);
            } else {
                typeAdapter = ArrayAdapter.createFromResource(context,
                        R.array.ingredientArray, android.R.layout.simple_spinner_item);
            }
            typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            typeSpinner.setAdapter(typeAdapter);
            typeSpinner.setSelection(0);

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

                if (fridgeSpinner.getSelectedItemPosition() == -1 && fridgeLayout.getVisibility() == View.VISIBLE) {
                    Toast.makeText(context.getApplicationContext(),
                            "냉장고를 선택해주세요",Toast.LENGTH_SHORT).show();
                } else if (sectionSpinner.getSelectedItemPosition() == -1 && fridgeLayout.getVisibility() == View.VISIBLE) {
                    Toast.makeText(context.getApplicationContext(),
                            "구역을 선택해주세요", Toast.LENGTH_SHORT).show();
                } else if (nameStr.trim().getBytes().length == 0) {
                    Toast.makeText(context.getApplicationContext(),
                            "이름을 입력해주세요",Toast.LENGTH_SHORT).show();
                } else if (quantityStr.trim().getBytes().length == 0) {
                    Toast.makeText(context.getApplicationContext(),
                            "수량을 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if (typeSpinner.getSelectedItemPosition() == -1) {
                    Toast.makeText(context.getApplicationContext(),
                            "종류를 선택해주세요.", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(quantityStr) <= 0) {
                    Toast.makeText(context.getApplicationContext(),
                            "수량 형식이 잘못되었습니다",Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        LocalDate expireDate = LocalDate.parse(expireDateStr, DateTimeFormatter.ofPattern("yyyyMMdd"));

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
                                fridgeLayout.getVisibility() == View.GONE? fridge:fridgeSpinner.getSelectedItem().toString());
                        values.put(DBContract.GoodsEntry.COLUMN_SECTION,
                                fridgeLayout.getVisibility() == View.GONE? section:sectionSpinner.getSelectedItem().toString());
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