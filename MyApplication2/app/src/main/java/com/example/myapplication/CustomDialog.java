package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class CustomDialog {

    private Context context;
    final static String foldername = Environment.getExternalStorageDirectory().getAbsolutePath()+"/TestLog";
    final static String filename = "money.txt";
    final static String filename2 = "time.txt";
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
                    dto.setDate(y,m,d);

                    adapter.addItem(dto);
                    adapter.notifyDataSetChanged();



                    try{
                        File dir = new File (foldername);
                        //디렉토리 폴더가 없으면 생성함
                        if(!dir.exists()){
                            dir.mkdir();
                        }
                        //파일 output stream 생성
                        FileOutputStream fos;
                        if(whattab == 0){ fos = new FileOutputStream(foldername+"/"+filename, true);}
                        else { fos = new FileOutputStream(foldername+"/"+filename2, true);}
                        //파일쓰기
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
                        writer.write(additm.getText().toString() + "\n"+ addval.getText().toString() + "\n"+ String.valueOf(y)+ "\n"+ String.valueOf(m)+ "\n"+ String.valueOf(d) +"\n");
                        writer.flush();

                        writer.close();
                        fos.close();
                    }catch (IOException e){
                        e.printStackTrace();
                        Toast.makeText(context, "쓸 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }



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

    public void WriteTextFile(String foldername, String filename, String contents){
        try{
            File dir = new File (foldername);
            //디렉토리 폴더가 없으면 생성함
            if(!dir.exists()){
                dir.mkdir();
            }
            //파일 output stream 생성
            FileOutputStream fos = new FileOutputStream(foldername+"/"+filename, true);
            //파일쓰기
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
            writer.write(contents);
            writer.flush();

            writer.close();
            fos.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}