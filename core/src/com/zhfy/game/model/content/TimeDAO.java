package com.zhfy.game.model.content;

public class TimeDAO {
    private int year;
    private int month;
    private int yearMin;
    private int pageMax;//为his的b
    private int page;//从0开始

    public TimeDAO(int year,int yearMin,int pageMax){
        this.year=year;
        this.month=1;
        this.yearMin=yearMin;
        this.pageMax=(pageMax-1)*12;
        this.page=0;
    }
    public TimeDAO(int year,int month,int yearMin,int pageMax){
        this.year=year;
        this.month=month;
        this.yearMin=yearMin;
        this.pageMax=(pageMax-1)*12;
        this.page=0;
    }
    public void lastTime(){
        if(year>=yearMin&&month>1){
            month=month-1;
            if(month<1){
                month=12;
                year=year-1;
            }
            page=page-1;
        }
    }

    public void nextTime(){
        if(page<pageMax-1) {
            month = month + 1;
            if (month > 12) {
                month = 1;
                year = year + 1;
            }
            page=page+1;
        }
    }

    public void setTime(int year,int month){
        this.page=page+(year-yearMin);
        this.year=year;
        this.month=month;
    }


    //1840/01
    public String getTime(){
        String month;
        if(this.month>9){
            month=""+this.month;
        }else{
            month="0"+this.month;
        }
        return year+"/"+month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.page=page+(year-yearMin);
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
