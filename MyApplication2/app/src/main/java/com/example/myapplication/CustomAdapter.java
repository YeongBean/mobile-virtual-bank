package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.CharArrayWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter{

    private ArrayList<CustomDTO> listCustom = new ArrayList<>();
    private ArrayList<CustomDTO> listCustom2 = new ArrayList<>();

    private ArrayList<CustomDTO> filteredItemList = new ArrayList<>();

    Filter listfilter;

    private int tabname;
    private int tabnum;
    private String mDate;

    private long SumOfMoney = 0;
    private long SumOfTime = 0;
    private int years;
    private int months;
    private int days;

    public long getSumOfMoney()
    {
        SumOfMoney = 0;
        CustomDTO dto = new CustomDTO();
        for(int i = 0; i < listCustom.size(); i++)
        {
            dto = listCustom.get(i);
            SumOfMoney += Long.parseLong(dto.getTitle());
        }
        return SumOfMoney;
    }

    public long getSumOfTime()
    {
        SumOfTime = 0;
        CustomDTO dto = new CustomDTO();
        for(int i = 0; i < listCustom2.size(); i++)
        {
            dto = listCustom2.get(i);
            SumOfTime += Long.parseLong(dto.getTitle());
        }
        return SumOfTime;
    }


    public void SetDateSelection(int y, int m, int d)
    {
        years = y;
        months = m;
        days = d;
        mDate = String.format("%d년 %d월 %d일", y , m , d);
    }

    public void setSpinnerSelectedItem(int standardnum, int whattab){
        tabname = standardnum;
        tabnum = whattab;
    }

    // ListView에 보여질 Item 수
    @Override
    public int getCount() {
        if(tabnum == 0) {
            return listCustom.size();
        }else {return listCustom2.size();}

    }

    // 하나의 Item(ImageView 1, TextView 2)
    @Override
    public Object getItem(int position) {
        if(tabnum == 0) {
            return listCustom.get(position);
        }else {return listCustom2.get(position);}
    }

    public CustomDTO getDTO(int position) {
        if(tabnum == 0) {
            return listCustom.get(position);
        }else {return listCustom2.get(position);}
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 실제로 Item이 보여지는 부분
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        setSpinnerSelectedItem(tabname, tabnum);
        CustomViewHolder holder = new CustomViewHolder();

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_component_layout, null, false);
        }

            holder.name = (TextView) convertView.findViewById(R.id.original_item);
            holder.origin_val = (TextView) convertView.findViewById(R.id.original_value);
            holder.changed_val = (TextView) convertView.findViewById(R.id.changed_value);
            holder.item_Date = (TextView) convertView.findViewById(R.id.thisDate);
            convertView.setTag(holder);


        CustomDTO dto;
        if(tabnum == 0) { dto = listCustom.get(position);}
        else { dto = listCustom2.get(position);}


            holder.name.setText(dto.getResId());
        if(tabnum == 0) {
            holder.origin_val.setText(dto.getTitle() + "원");
            SumOfMoney += Long.parseLong(dto.getTitle());
        }
        else { holder.origin_val.setText(dto.getTitle() + "분");
            SumOfTime += Long.parseLong(dto.getTitle());}

            holder.item_Date.setText(dto.getDate());
            if (tabnum == 0) {
                if (tabname == 0) {
                    holder.changed_val.setText(String.format("%.2f", Float.parseFloat(dto.getTitle()) / 18000) + "마리");
                } else if (tabname == 1) {
                    holder.changed_val.setText(String.format("%.2f", Float.parseFloat(dto.getTitle()) / 400) + "곡");
                } else if (tabname == 2) {
                    holder.changed_val.setText(String.format("%.2f", Float.parseFloat(dto.getTitle()) / 750) + "병");
                } else if (tabname == 3) {
                    holder.changed_val.setText(String.format("%.2f", Float.parseFloat(dto.getTitle()) / 1000) + "시간");
                } else if (tabname == 4) {
                    holder.changed_val.setText(String.format("%.2f", Float.parseFloat(dto.getTitle()) / 7500) + "끼");
                } else if (tabname == 5) {
                    holder.changed_val.setText(String.format("%.2f", Float.parseFloat(dto.getTitle()) / 1100) + "개");
                } else if (tabname == 6) {
                    holder.changed_val.setText(String.format("%.2f", Float.parseFloat(dto.getTitle()) / 500) + "세트");
                }
            } else {
                if (tabname == 0) {
                    holder.changed_val.setText(String.format("%.2f", Float.parseFloat(dto.getTitle()) / 60) + "번");
                } else if (tabname == 1) {
                    holder.changed_val.setText(String.format("%.2f", Float.parseFloat(dto.getTitle()) / 75) + "연강");
                } else if (tabname == 2) {
                    holder.changed_val.setText(String.format("%.2f", Float.parseFloat(dto.getTitle()) / 30) + "번");
                } else if (tabname == 3) {
                    holder.changed_val.setText(String.format("%.2f", Float.parseFloat(dto.getTitle()) / 4) + "곡");
                } else if (tabname == 4) {
                    holder.changed_val.setText(String.format("%.2f", Float.parseFloat(dto.getTitle()) / 120) + "편");
                } else if (tabname == 5) {
                    holder.changed_val.setText(String.format("%.2f", Float.parseFloat(dto.getTitle()) / 1440) + "일");
                } else if (tabname == 6) {
                    holder.changed_val.setText(String.format("%.2f", Float.parseFloat(dto.getTitle()) / 50) + "번");
                }
            }

        return convertView;
    }



    class CustomViewHolder {
        TextView name;
        TextView origin_val;
        TextView changed_val;
        TextView item_Date;
    }

    public void addItem(CustomDTO dto) {
        if(tabnum == 0){listCustom.add(dto);}
        else {listCustom2.add(dto);}
    }
    public void removeItem(int pos)
    {
        if(tabnum == 0){listCustom.remove(pos);}
        else {listCustom2.remove(pos);}
    }
    public void clearallitem()
    {
        if(tabnum == 0){listCustom.clear();}
        else {listCustom2.clear();}
    }

    public int getYears(){return years;}
    public int getMonths(){return months;}
    public int getDayss(){return days;}


}