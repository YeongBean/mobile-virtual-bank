package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;

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
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    String fname;
    int tabName;
    int whattab;

    int currentYear;
    int currnetMonth;
    int currentDay;

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
        currnetMonth = cal.get(cal.MONTH) +1;
        currentDay = cal.get(cal.DAY_OF_MONTH);
        mStrDate = String.format("%d년 %d월 %d일", currentYear , currnetMonth, currentDay);

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
        adapter.SetDateSelection(currentYear,currnetMonth,currentDay);
        updateResult();

        dateText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String filtertext = mStrDate.toString();
                mListView.setFilterText(filtertext);
            }
        });
    }

    private void updateResult()
    {
        dateText.setText(mStrDate);
    }

    public void mOnClick(View v)
    {
        Calendar c = Calendar.getInstance();

        int year =c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this, mDateSetListener, year, month, day).show();
        adapter.SetDateSelection(year, month, day);
        adapter.notifyDataSetChanged();

    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mStrDate = String.format("%d년 %d월 %d일", year , month, dayOfMonth);
            adapter.SetDateSelection(year, month, dayOfMonth);
            adapter.notifyDataSetChanged();
            updateResult();
        }
    };



}
