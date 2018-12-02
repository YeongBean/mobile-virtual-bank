package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    public TextView dateText;
    public ImageButton dateButton;
    public Spinner StandardSpinner;
    public ListView mListView;
    private String mStrDate = "????";
    private CustomAdapter adapter;
    private TextView tabview;

    int tabName;
    int whattab;

    int currentYear;
    int currnetMonth;
    int currentDay;

    final static String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/TestLog/money.txt";
    final static String filePath2 = Environment.getExternalStorageDirectory().getAbsolutePath()+"/TestLog/time.txt";


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_money:
                    mTextMessage.setText(R.string.tab_money);
                    SetSpinner(R.array.money);
                    whattab = 0;
                    adapter.setSpinnerSelectedItem(0, whattab);
                    adapter.notifyDataSetChanged();
                    return true;
                case R.id.navigation_time:
                    mTextMessage.setText(R.string.tab_time);
                    SetSpinner(R.array.time);
                    whattab = 1;
                    adapter.setSpinnerSelectedItem(0, whattab);
                    adapter.notifyDataSetChanged();
                    return true;
                case R.id.navigation_write:
                    CustomDialog customDialog = new CustomDialog(MainActivity.this);
                    customDialog.setwhattab(whattab);
                    customDialog.callFunction(adapter, currentYear, currnetMonth, currentDay);
                    return true;
            }

            return false;
        }
    };


    private void SetSpinner(int itemNum) {
        ArrayAdapter fAdapter;
        fAdapter = ArrayAdapter.createFromResource(this, itemNum, android.R.layout.simple_spinner_item);
        fAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        StandardSpinner.setAdapter(fAdapter);
        StandardSpinner.setSelection(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ReadMoneyRecord(filePath);
        ReadTimeRecord(filePath2);


        whattab = 0;
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        tabview = (TextView)findViewById(R.id.tabnameview);
        dateText = (TextView) findViewById(R.id.dateView);
        dateButton = (ImageButton) findViewById(R.id.datePicker);
        StandardSpinner = (Spinner) findViewById(R.id.standardSpinner);
        StandardSpinner.setSelection(0);
        StandardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //adapter.setSpinnerSelectedItem(tabName);
                tabview.setText(String.valueOf(position));
                tabName = position;
                adapter.setSpinnerSelectedItem(tabName, whattab);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        registerForContextMenu(StandardSpinner);
        Calendar cal = Calendar.getInstance();
        currentYear = cal.get(cal.YEAR);
        currnetMonth = cal.get(cal.MONTH);
        currentDay = cal.get(cal.DAY_OF_MONTH);
        mStrDate = String.format("%d년 %d월 %d일", currentYear , currnetMonth + 1, currentDay);

        mListView = (ListView) findViewById(R.id.ListComponents);

        adapter = new CustomAdapter();

        mListView.setAdapter(adapter);

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                CustomBox customDialog2 = new CustomBox(MainActivity.this);
                customDialog2.callFunction(adapter, mListView, position);
                return false;
            }
        });
        adapter.SetDateSelection(currentYear,currnetMonth + 1,currentDay);
        updateResult();
    }

    private void updateResult()
    {
        dateText.setText(mStrDate);
    }

    public void mOnClick(View v)
    {
        Calendar c = Calendar.getInstance();

        int year =c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this, mDateSetListener, year, month, day).show();
        //adapter.SetDateSelection(year, month, day);
        //adapter.notifyDataSetChanged();

    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mStrDate = String.format("%d년 %d월 %d일", year , month+1, dayOfMonth);
            adapter.SetDateSelection(year, month+1, dayOfMonth);
            adapter.notifyDataSetChanged();
            updateResult();
        }
    };

    public String ReadMoneyRecord(String path){
        StringBuffer strBuffer = new StringBuffer();
        try{
            InputStream is = new FileInputStream(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String itemname;
            String originalVal;
            int itemDate_y;
            int itemDate_m;
            int itemDate_d;
            CustomDTO dto = new CustomDTO();
            while((itemname=reader.readLine())!=null){
                dto.setResId(itemname);
                originalVal = reader.readLine();
                dto.setTitle(originalVal);
                itemDate_y = Integer.parseInt(reader.readLine());
                itemDate_m = Integer.parseInt(reader.readLine());
                itemDate_d = Integer.parseInt(reader.readLine());
                dto.setDate(itemDate_y, itemDate_m, itemDate_d);
                whattab = 0;
                adapter.setSpinnerSelectedItem(0,whattab);
                adapter.addItem(dto);
            }

            reader.close();
            is.close();
        }catch (IOException e){
            e.printStackTrace();
            Toast.makeText(this, "저장된 값이 없습니다.", Toast.LENGTH_SHORT).show();
            return null;
        }
        return strBuffer.toString();
    }

    public String ReadTimeRecord(String path){
        StringBuffer strBuffer = new StringBuffer();
        try{
            InputStream is = new FileInputStream(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String itemname;
            String originalVal;
            int itemDate_y;
            int itemDate_m;
            int itemDate_d;
            CustomDTO dto = new CustomDTO();
            while((itemname=reader.readLine())!=null){
                dto.setResId(itemname);
                originalVal = reader.readLine();
                dto.setTitle(originalVal);
                itemDate_y = Integer.parseInt(reader.readLine());
                itemDate_m = Integer.parseInt(reader.readLine());
                itemDate_d = Integer.parseInt(reader.readLine());
                dto.setDate(itemDate_y, itemDate_m, itemDate_d);
                whattab = 1;
                adapter.setSpinnerSelectedItem(0,whattab);
                adapter.addItem(dto);
            }

            reader.close();
            is.close();
        }catch (IOException e){
            e.printStackTrace();
            Toast.makeText(this, "저장된 기록이 없습니다.", Toast.LENGTH_SHORT).show();
            return null;
        }
        return strBuffer.toString();
    }
}



