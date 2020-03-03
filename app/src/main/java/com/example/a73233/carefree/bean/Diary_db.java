package com.example.a73233.carefree.bean;

import android.os.Parcelable;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.List;

public class Diary_db extends LitePalSupport{
    private int id;
    private String diaryContent; //日记内容
    private String day;          //
    private String yearAndMonth;
    private String week;
    private int emotionValue = 0; //日记情绪值
    private List<String> photoList; //保存图片地址的集合
    private int isAbandon;

    public int getIsAbandon() {
        return isAbandon;
    }

    public void setIsAbandon(int isAbandon) {
        this.isAbandon = isAbandon;
    }

    public void setPhotoList(List<String> photoList) {
        this.photoList = photoList;
    }

    public List<String> getPhotoList() {
        return photoList;
    }

    public void setYearAndMonth(String yearAndMonth) {
        this.yearAndMonth = yearAndMonth;
    }

    public String getYearAndMonth() {
        return yearAndMonth;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getWeek() {
        return week;
    }

    public void setEmotionValue(int emotionValue) {
        this.emotionValue = emotionValue;
    }

    public int getEmotionValue() {
        return emotionValue;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDay() {
        return day;
    }

    public void setDiaryContent(String dairyContent) {
        this.diaryContent = dairyContent;
    }

    public String getDiaryContent() {
        return diaryContent;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
