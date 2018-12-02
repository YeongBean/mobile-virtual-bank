package com.example.myapplication;

class CustomDTO {
    private String name;
    private String origin_val;
    private String dates;


    public String getResId() {
        return name;
    }

    public void setResId(String names) {
        this.name = names;
    }

    public String getTitle() {
        return origin_val;
    }

    public void setTitle(String orval) {
        this.origin_val = orval;
    }

    public void setDate(int year, int month, int day)
    {
       dates =  String.format("%d년 %d월 %d일", year , month , day);
    }

    public String getDate()
    {
        return dates;
    }

}