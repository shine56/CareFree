package com.example.a73233.carefree.diary.viewModel;

import com.example.a73233.carefree.bean.DiaryBean;
import com.example.a73233.carefree.diary.Model.DiaryModel;
import com.example.a73233.carefree.diary.view.DiaryListAdapter_;
import com.example.a73233.carefree.util.ConstantPool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DiaryVM implements DiaryVMImpl {
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
        diaryModel.findAllData(this, ConstantPool.NOT_ABANDON);
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
    public DiaryBean refreshBeanDate(){
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

    public void abandonDiary(int diaryId){
        diaryModel.abandonData(diaryId,this);
    }
    @Override
    public void findAllDataSuccess (List<DiaryBean> diaryBeanList) {
        adapter.refreshData(diaryBeanList);
    }

    @Override
    public void abandonDataSuccess() {

    }
}
