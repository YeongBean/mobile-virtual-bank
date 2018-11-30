package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class CustomDialog {

    private Context context;

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
}