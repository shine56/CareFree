package com.example.a73233.carefree.note.viewModel;

import android.app.Activity;
import android.content.SharedPreferences;

import com.example.a73233.carefree.bean.NoteBean;
import com.example.a73233.carefree.note.model.NoteModel;
import com.example.a73233.carefree.util.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class NoteWriteVM {
    NoteModel model;
    NoteBean bean;

    public NoteWriteVM() {
        model = new NoteModel();
    }

    /**
     * 刷新界面
     * @param id
     * @return
     */
    public NoteBean refreshData(int id){
        bean = model.findDataById(id);
        Date date = new Date();
        String year = new SimpleDateFormat("yyyy").format(date);
        String monthAndDay = new SimpleDateFormat("MM月dd日").format(date);
        String week = new SimpleDateFormat("EEEE").format(date);
        bean.year.set(year);
        bean.monthAndDay.set(monthAndDay);
        bean.week.set(week);
        return bean;
    }

    /***
     * 保存便贴
     * @param id
     */
    public void saveNote(int id){
        if(id == -1){
            Date date = new Date();
            String time = new SimpleDateFormat("HH:mm").format(date);
            LogUtil.LogD("time="+time);
            bean.time.set(time);
        }
        model.saveData(bean,id);
    }

    public void setClock(int h, int m){
        bean.hour.set(h);
        bean.minutes.set(m);
    }
    public void setClockTitle(String text){
        bean.clockText.set(text);
    }
    public int getHour(){
        return bean.hour.get();
    }
    public int getMinutes(){
        return bean.minutes.get();
    }

    /**
     * 获取文本首句
     * @return
     */
    public String getNoteFirstText(){
        String text = bean.text.get();
        String firstText = "";
        int min = 0;
        if(text != null){
            min = text.length();
            if(text.indexOf("\n")!=-1 && text.indexOf("\n") <min) min = text.indexOf("\n");
            if(text.indexOf(" ")!=-1 && text.indexOf(" ") <min) min = text.indexOf(" ");
            if(text.indexOf(".")!=-1 && text.indexOf(".") <min) min = text.indexOf(".");
            if(text.indexOf("。")!=-1 && text.indexOf("。") <min) min = text.indexOf("。");
            if(text.indexOf("！")!=-1 && text.indexOf("！") <min) min = text.indexOf("！");
            if(text.indexOf("!")!=-1 && text.indexOf("!") <min) min = text.indexOf("！");
            if(text.indexOf("？")!=-1 && text.indexOf("？") <min) min = text.indexOf("？");
            if(text.indexOf("?")!=-1 && text.indexOf("?") <min) min = text.indexOf("！");
            firstText = text.substring(0,min);
        }
        return firstText;
    }
    public void refreshRank(int rank){
        bean.rank.set(rank);
    }
    public int getRank(){
        return bean.rank.get();
    }
    public Boolean isSystemClock(Activity activity){
        SharedPreferences pref = activity.getSharedPreferences("note_setting",MODE_PRIVATE);
        if (pref.getString("clock_type","非系统闹钟").equals("系统闹钟")){
            return true;
        }else {
            return false;
        }
    }
    public void findSuccess(NoteBean bean){}
}