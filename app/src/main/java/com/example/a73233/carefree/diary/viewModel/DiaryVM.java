package com.example.a73233.carefree.diary.viewModel;

import android.util.Log;

import com.example.a73233.carefree.bean.DiaryBean;
import com.example.a73233.carefree.diary.Model.DiaryModel;
import com.example.a73233.carefree.diary.view.DiaryListAdapter_;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DiaryVM implements DiaryVMImpl{
    private DiaryModel diaryModel;
    private DiaryListAdapter_ adapter;

    public DiaryVM(DiaryListAdapter_ adapter) {
        this.adapter = adapter;
        diaryModel = new DiaryModel();
    }
    /**
     * 刷新列表所有日记
     */
    public void refreshDiaryList(){

        diaryModel.findAllData(this);
    }

    /**
     * 刷新日历选中日期的日记
     * @param year
     * @param month
     * @param dayOfMonth
     */
    public void refreshDiaryList(int year,int month,int dayOfMonth){
        String yearAndMonth, day;
        if(month+1 < 10){
            yearAndMonth = year+"年0"+(month+1)+"月";
        }else {
            yearAndMonth = year+"年"+(month+1)+"月";
        }
        if(dayOfMonth<10){
            day = "0"+dayOfMonth;
        }else {
            day = dayOfMonth+"";
        }
        diaryModel.findDataByDate(yearAndMonth,day,this);
    }

    /**
     * 刷新搜索到的日记
     * @param text
     */
    public void refreshDiaryList(String text){
        diaryModel.findDataByText(text,this);
    }


    /**
     * 初始化今天日期
     * @return
     */
    public DiaryBean initAddCardData(){
        DiaryBean bean = new DiaryBean();
        Date date = new Date();
        String day = new SimpleDateFormat("dd").format(date);
        String yearAndMonth = new SimpleDateFormat("yyyy年MM月").format(date);
        String week = new SimpleDateFormat("EEEE").format(date);
        bean.day.set(day);
        bean.yearAndMonth.set(yearAndMonth);
        bean.week.set(week);
        return bean;
    }
    @Override
    public void findListSuccess (List<DiaryBean> diaryBeanList) {
        adapter.refreshData(diaryBeanList);
    }
}
