package com.example.a73233.carefree.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class Note_db extends LitePalSupport implements Serializable {
    private static final long serialVersionUID = -2083503801423301342L;
    private int id;
    private String text; //墙贴文本
    private String year;
    private String monthAndDay;
    private String week;
    private String time;
    private int rank;//强帖等级 记录贴等级为0，任务贴三个等级：日常(1)、一般(2)、紧急(3)
    private int isAbandon;//是否丢进垃圾桶0和1表示
    private int isComplete; //0和1表示
    private int clockHour;
    private int clockMinutes;
    private String clockText;

    public int getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(int isComplete) {
        this.isComplete = isComplete;
    }

    public String getClockText() {
        return clockText;
    }

    public void setClockText(String clockText) {
        this.clockText = clockText;
    }

    public int getClockHour() {
        return clockHour;
    }

    public void setClockHour(int clockHour) {
        this.clockHour = clockHour;
    }

    public int getClockMinutes() {
        return clockMinutes;
    }

    public void setClockMinutes(int clockMinutes) {
        this.clockMinutes = clockMinutes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getIsAbandon() {
        return isAbandon;
    }

    public void setIsAbandon(int isAbandon) {
        this.isAbandon = isAbandon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getMonthAndDay() {
        return monthAndDay;
    }

    public void setMonthAndDay(String monthAndDay) {
        this.monthAndDay = monthAndDay;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
