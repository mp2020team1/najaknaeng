package com.example.najakneang.model;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.najakneang.R;
import com.example.najakneang.activity.FreshnessActivity;
import com.example.najakneang.activity.MainActivity;
import com.example.najakneang.db.DBContract;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class GoodsDialog extends Dialog implements View.OnClickListener {
    public interface DialogEventListener {
        public void DialogEvent(boolean value);
    }

    private Context context;
    private DialogEventListener onDialogEventListener;

    private TextView btn_ok;
    private TextView btn_cancel;
    private EditText name;
    private EditText quantity;
    private EditText expire_date;
    private Spinner type_spinner;
    private Spinner fridge_spinner;
    private Spinner section_spinner;

    SQLiteDatabase db = MainActivity.db;

    public GoodsDialog(@NonNull Context context){
        super(context);
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
        setContentView(R.layout.dialog_goods);

        btn_ok = findViewById(R.id.btn_ok);
        btn_cancel = findViewById(R.id.btn_cancel);
        name = findViewById(R.id.ingredient_Name);
        quantity = findViewById(R.id.ingredient_Quantity);
        expire_date = findViewById(R.id.ingredient_expireDate);
        type_spinner = findViewById(R.id.ingredient_type_spinner);
        fridge_spinner = findViewById(R.id.ingredient_fridge_spinner);
        section_spinner = findViewById(R.id.ingredient_section_spinner);

        btn_ok.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        final ArrayAdapter<CharSequence> type_adapter = ArrayAdapter.createFromResource(context,
                R.array.secondTab, android.R.layout.simple_spinner_item);
        type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type_spinner.setAdapter(type_adapter);
        type_spinner.setSelection(0);

        if(context.getClass() == FreshnessActivity.class){
            fridge_spinner.setVisibility(View.VISIBLE);
            section_spinner.setVisibility(View.VISIBLE);

            ArrayList<String> fridge_name = new ArrayList<>();
            Cursor cursor = db.query(
                    DBContract.FridgeEntry.TABLE_NAME,
                    new String[]{DBContract.FridgeEntry.COLUMN_NAME},
                    null,
                    null,
                    null,
                    null,
                    null
            );

            fridge_name.add("냉장고를 선택해주세요");

            while(cursor.moveToNext()){
                fridge_name.add(cursor.getString(
                        cursor.getColumnIndex(DBContract.FridgeEntry.COLUMN_NAME)));
            }
            cursor.close();

            final ArrayAdapter<String> fridge_adapter = new ArrayAdapter<>(context,
                    android.R.layout.simple_spinner_item, fridge_name);
            fridge_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            fridge_spinner.setAdapter(fridge_adapter);
            fridge_spinner.setSelection(0,false);
            fridge_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    ArrayList<String> section_name = new ArrayList<>();
                    String test = fridge_spinner.getSelectedItem().toString();
                    Log.i("Activated", test);
                    Cursor cursor = db.query(
                            DBContract.SectionEntry.TABLE_NAME,
                            new String[]{DBContract.SectionEntry.COLUMN_NAME},
                            DBContract.SectionEntry.COLUMN_FRIDGE + " = ? ",
                            new String[]{ fridge_spinner.getSelectedItem().toString() },
                            null,
                            null,
                            null
                    );

                    section_name.add("구역을 선택해주세요");

                    while(cursor.moveToNext()){
                        section_name.add(cursor.getString(
                                cursor.getColumnIndex(DBContract.FridgeEntry.COLUMN_NAME)));
                    }
                    cursor.close();

                    final ArrayAdapter<String> section_adapter = new ArrayAdapter<>(context,
                            android.R.layout.simple_spinner_item, section_name);
                    section_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    section_spinner.setAdapter(section_adapter);
                    section_spinner.setSelection(0,false);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        else{
            fridge_spinner.setVisibility(View.INVISIBLE);
            section_spinner.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btn_ok:
                String nameStr = name.getText().toString();
                String quantityStr = quantity.getText().toString();
                String expireDateStr = expire_date.getText().toString();
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
                else if(type_spinner.getSelectedItemPosition() == 0){
                    Toast.makeText(context.getApplicationContext(),
                            "종류를 선택해주세요",Toast.LENGTH_SHORT).show();
                }
                else if(expireDateStr.trim().getBytes().length == 0){
                    Toast.makeText(context.getApplicationContext(),
                            "날짜 형식이 잘못되었습니다",Toast.LENGTH_SHORT).show();
                }
                else if(fridge_spinner.getSelectedItemPosition() == 0){
                    Toast.makeText(context.getApplicationContext(),
                            "냉장고를 선택해주세요",Toast.LENGTH_SHORT).show();
                }
                else if(section_spinner.getSelectedItemPosition() == 0){
                    Toast.makeText(context.getApplicationContext(),
                            "구역을 선택해주세요",Toast.LENGTH_SHORT).show();
                }
                else{
                    LocalDate expireDate;
                    try{
                        expireDate = LocalDate.parse(expireDateStr);

                        ContentValues values = new ContentValues();
                        values.put(DBContract.GoodsEntry.COLUMN_NAME, nameStr);
                        values.put(DBContract.GoodsEntry.COLUMN_QUANTITY, Integer.parseInt(quantityStr));
                        values.put(
                                DBContract.GoodsEntry.COLUMN_REGISTDATE,
                                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        );
                        values.put(DBContract.GoodsEntry.COLUMN_EXPIREDATE, expireDateStr);
                        values.put(DBContract.GoodsEntry.COLUMN_TYPE, type_spinner.getSelectedItem().toString());
                        values.put(DBContract.GoodsEntry.COLUMN_FRIDGE, fridge_spinner.getSelectedItem().toString());
                        values.put(DBContract.GoodsEntry.COLUMN_SECTION, section_spinner.getSelectedItem().toString());
                        db.insert(DBContract.GoodsEntry.TABLE_NAME, null, values);

                        if(context.getClass() == FreshnessActivity.class){((FreshnessActivity)context).setupFreshnessRecycler(); }
                    }
                    catch(Exception e){
                        Toast.makeText(context.getApplicationContext(),
                                "날짜 형식이 잘못되었습니다",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }
};