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

public class CustomAdapter extends BaseAdapter implements Filterable {

    private ArrayList<CustomDTO> listCustom = new ArrayList<>();
    private ArrayList<CustomDTO> listCustom2 = new ArrayList<>();

    private ArrayList<CustomDTO> filteredItemList = new ArrayList<>();

    Filter listfilter;

    private int tabname;
    private int tabnum;
    private int years;
    private int months;
    private int days;
    private String mDate;

    public void SetDateSelection(int y, int m, int d)
    {
        years = y;
        months = m;
        days = d;
        mDate = String.valueOf(y) +"년 "+ String.valueOf(m) +"월 "+ String.valueOf(d) + "일";
    }

    public void setSpinnerSelectedItem(int standardnum, int whattab){
        tabname = standardnum;
        tabnum = whattab;
    }

    // ListView에 보여질 Item 수
    @Override
    public int getCount() {
        return filteredItemList.size();
    }

    // 하나의 Item(ImageView 1, TextView 2)
    @Override
    public Object getItem(int position) {
        return filteredItemList.get(position);
    }

    // Item의 id : Item을 구별하기 위한 것으로 position 사용
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 실제로 Item이 보여지는 부분
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_component_layout, null, false);

            holder = new CustomViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.original_item);
            holder.origin_val = (TextView) convertView.findViewById(R.id.original_value);
            holder.changed_val = (TextView) convertView.findViewById(R.id.changed_value);

            convertView.setTag(holder);
        } else {
            holder = (CustomViewHolder) convertView.getTag();
        }


        CustomDTO dto = filteredItemList.get(position);
        //if(tabnum == 0) { dto = listCustom.get(position);}
        //else { dto = listCustom2.get(position);}

            holder.name.setText(dto.getResId());
            holder.origin_val.setText(dto.getTitle());
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

    @Override
    public Filter getFilter() {
        if(listfilter == null){
            listfilter = new listFilter();
        }
        return listfilter;
    }

    class CustomViewHolder {
        TextView name;
        TextView origin_val;
        TextView changed_val;
    }

    // MainActivity에서 Adapter에있는 ArrayList에 data를 추가시켜주는 함수
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


    private class listFilter extends Filter {


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                if (tabnum == 0) {
                    results.values = listCustom;
                    results.count = listCustom.size();
                } else {
                    results.values = listCustom2;
                    results.values = listCustom2.size();
                }
            } else {
                ArrayList<CustomDTO> itemlist = new ArrayList<CustomDTO>();
                if (tabnum == 0) {
                    for (CustomDTO item : listCustom) {
                        if (item.getDate().contains(mDate)) {
                            itemlist.add(item);
                        }
                    }
                } else {
                    for (CustomDTO item : listCustom2) {
                        if (item.getDate().contains(mDate)) {
                            itemlist.add(item);
                        }
                    }

                }
                results.values = itemlist;
                results.count = itemlist.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredItemList = (ArrayList<CustomDTO>)results.values;
            if(results.count > 0){
                notifyDataSetChanged();
            }else {
                notifyDataSetInvalidated();
            }

        }
    }


}