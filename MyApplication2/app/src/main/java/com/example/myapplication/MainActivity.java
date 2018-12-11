package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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
    private TextView otherVals;
    private CustomDialog customDialog;
    int tabName;
    int whattab;

    int currentYear;
    int currnetMonth;
    int currentDay;

    final static String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/money.txt";
    final static String filePath2 = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/time.txt";



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
                    ShowSavedResult();

                    return true;
                case R.id.navigation_time:
                    mTextMessage.setText(R.string.tab_time);
                    SetSpinner(R.array.time);
                    whattab = 1;
                    adapter.setSpinnerSelectedItem(0, whattab);
                    adapter.notifyDataSetChanged();
                    ShowSavedResult();
                    return true;
                case R.id.navigation_write:
                    customDialog = new CustomDialog(MainActivity.this);
                    customDialog.setwhattab(whattab);
                    customDialog.callFunction(adapter, currentYear, currnetMonth, currentDay);
                    ShowSavedResult();
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
        otherVals = (TextView)findViewById(R.id.othervaluesview);
        dateText = (TextView) findViewById(R.id.dateView);
        dateButton = (ImageButton) findViewById(R.id.datePicker);
        StandardSpinner = (Spinner) findViewById(R.id.standardSpinner);
        StandardSpinner.setSelection(0);
        StandardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //adapter.setSpinnerSelectedItem(tabName);
                //tabview.setText(String.valueOf(position));
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
        //setExamples();

        ReadTimeRecord(filePath2);
        ReadMoneyRecord(filePath);

        ShowSavedResult();
        updateResult();
    }

    private void setExamples()
    {
        CustomDTO dtos = new CustomDTO();
        CustomDTO dtos2 = new CustomDTO();
        dtos.setDate(currentYear,currnetMonth + 1,currentDay);
        dtos2.setDate(currentYear,currnetMonth + 1,currentDay);
        dtos.setResId("예시 항목");
        dtos2.setResId("예시 항목");
        dtos.setTitle("300");
        adapter.setSpinnerSelectedItem(0, 1);
        adapter.addItem(dtos);
        dtos2.setTitle("27000");
        adapter.setSpinnerSelectedItem(0, 0);
        adapter.addItem(dtos2);
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
            File dir = new File (path);
            //디렉토리 폴더가 없으면 생성함
            //if(!dir.exists()){
            //    dir.mkdir();
            //    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            //}
            whattab = 0;
            adapter.setSpinnerSelectedItem(0,whattab);
            InputStream is = new FileInputStream(dir);
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
                adapter.addItem(dto);
                adapter.notifyDataSetChanged();
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
            File dir = new File (path);
            //디렉토리 폴더가 없으면 생성함
            //if(!dir.exists()){
            //    dir.mkdir();
            //    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            //}
            whattab = 1;
            adapter.setSpinnerSelectedItem(0,whattab);
            InputStream is = new FileInputStream(dir);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String itemname;
            String originalVal;
            int itemDate_y;
            int itemDate_m;
            int itemDate_d;
            CustomDTO dto = new CustomDTO();
            itemname = reader.readLine();
            while(itemname!=null){
                dto.setResId(itemname);
                originalVal = reader.readLine();
                dto.setTitle(originalVal);
                itemDate_y = Integer.parseInt(reader.readLine());
                itemDate_m = Integer.parseInt(reader.readLine());
                itemDate_d = Integer.parseInt(reader.readLine());
                dto.setDate(itemDate_y, itemDate_m, itemDate_d);
                adapter.addItem(dto);
                adapter.notifyDataSetChanged();
                itemname = reader.readLine();
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

    public void ShowSavedResult()
    {
        long results;
        if(
                whattab == 0){results = adapter.getSumOfMoney();
                tabview.setText("누적 가상저금액 " + results +"원");
                if(results > 100000){otherVals.setText(" =  책 5권");}
                else if(results > 50000){otherVals.setText(" =  책 2권");}
                else if(results > 5000){otherVals.setText(" =  기분 좋음");}
                else otherVals.setText("");
        }
        else{
            results = adapter.getSumOfTime();
            tabview.setText("누적 가상저금시간 " + results +"분");
            if(results > 1000){otherVals.setText(" =  걸어서 마라톤 완주");}
            else if(results > 500){otherVals.setText(" =  걸어서 하프마라톤 완주");}
            else if(results > 100){otherVals.setText(" =  단편소설 독파");}
            else otherVals.setText("");
        }


    }


    class CustomDialog {

        private Context context;
        final  String foldername = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download";
        final  String filename = "money.txt";
        final  String filename2 = "time.txt";
        int whattab;

        public void setwhattab(int tab){whattab = tab;}


        public CustomDialog(Context context) {
            this.context = context;
        }



        // 호출할 다이얼로그 함수를 정의한다.
        public void callFunction(final CustomAdapter adapter, final int y, final int m, final int d) {


            final Dialog dlg = new Dialog(context);

            dlg.setContentView(R.layout.custon_dialog);

            dlg.show();

            final EditText additm = (EditText) dlg.findViewById(R.id.additem);
            final EditText addval = (EditText) dlg.findViewById(R.id.addvalue);
            final Button okButton = (Button) dlg.findViewById(R.id.okButton);
            final Button cancelButton = (Button) dlg.findViewById(R.id.cancelButton);

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(additm.getText().length() > 0 && addval.getText().length() > 0)
                    {
                        CustomDTO dto = new CustomDTO();
                        dto.setResId(additm.getText().toString());
                        dto.setTitle(addval.getText().toString());
                        dto.setDate(y,m+1,d);

                        if(whattab ==0){WriteTextFile(foldername, filename, additm.getText().toString(),addval.getText().toString(), y, m , d);}
                        else{WriteTextFile(foldername, filename2, additm.getText().toString(),addval.getText().toString(), y, m , d);}

                        adapter.addItem(dto);
                        adapter.notifyDataSetChanged();
                        ShowSavedResult();
                        //WritingRecord();
                        dlg.dismiss();
                    }else
                    {
                        Toast.makeText(context, "값을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "취소 했습니다.", Toast.LENGTH_SHORT).show();
                    dlg.dismiss();
                }
            });
        }

        public void WriteTextFile(String foldername, String filename, String itemName, String Or_val, int y, int m, int d){
            try{
                //File dir = new File (foldername + "/" + filename);
                //디렉토리 폴더가 없으면 생성함
                //if(!dir.exists()){
                //    dir.mkdir();
                //}
                //파일 output stream 생성
                //FileOutputStream output = new FileOutputStream(foldername+"/image.jpg");
                FileOutputStream fos = new FileOutputStream(foldername+"/"+filename, true);
                //파일쓰기
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
                writer.write(itemName + "\n" + Or_val+ "\n" + y+ "\n" + (m+1)+ "\n" + d + "\n");
                writer.flush();

                writer.close();
                fos.close();
            }catch (IOException e){
                e.printStackTrace();
                //Toast.makeText(context, "쓸 수 없습니다.", Toast.LENGTH_SHORT).show();

            }
        }
    }


    class CustomBox {

        private Context context;
        final  String foldername = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download";
        final  String filename = "money.txt";
        final  String filename2 = "time.txt";

        public CustomBox(Context context) {
            this.context = context;
        }

        public void callFunction(final CustomAdapter adapter, final ListView listview, final int positions) {

            final Dialog dlg = new Dialog(context);

            dlg.setContentView(R.layout.deleteview);

            dlg.show();

            final Button okButton = (Button) dlg.findViewById(R.id.deleteokButton);
            final Button cancelButton = (Button) dlg.findViewById(R.id.deletecancelButton);

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapter.removeItem(positions);
                    listview.clearChoices();

                    adapter.notifyDataSetChanged();
                    ShowSavedResult();

                    String item = "";
                    String vals = "";
                    int x = 0, y = 0, z = 0;
                    if(whattab ==0){WriteTextFile(foldername, filename, item, vals, x, y, z );}
                    else{WriteTextFile(foldername, filename2, item, vals, x, y, z );}
                    dlg.dismiss();
                }
            });
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "취소 했습니다.", Toast.LENGTH_SHORT).show();

                    dlg.dismiss();
                }
            });
        }

        public void WriteTextFile(String foldername, String filename, String itemName, String Or_val, int y, int m, int d){
            try{
                FileOutputStream fos = new FileOutputStream(foldername+"/"+filename, false);
                CustomDTO dto;
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
                for(int i = 0; i < adapter.getCount(); i++)
                {
                    dto = adapter.getDTO(i);
                    itemName = dto.getResId();
                    Or_val = dto.getTitle();
                    y = dto.getYears();
                    m = dto.getMonths();
                    d = dto.getDayss();
                    writer.write(itemName + "\n" + Or_val+ "\n" + y+ "\n" + (m+1)+ "\n" + d + "\n");
                }

                writer.flush();

                writer.close();
                fos.close();
            }catch (IOException e){
                e.printStackTrace();
                //Toast.makeText(context, "쓸 수 없습니다.", Toast.LENGTH_SHORT).show();

            }
        }
    }
}





