package com.example.a73233.carefree.me.viewModel;

import android.app.Activity;
import android.content.SharedPreferences;

import com.example.a73233.carefree.bean.Diary_db;
import com.example.a73233.carefree.bean.Note_db;
import com.example.a73233.carefree.bean.UserBean;
import com.example.a73233.carefree.me.model.MeModel;
import com.example.a73233.carefree.me.view.SettingActivity;
import com.example.a73233.carefree.util.EmotionDataUtil;
import com.example.a73233.carefree.util.LogUtil;
import com.example.a73233.carefree.util.NoteUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MeVM {
    private MeModel model;
    private UserBean bean;
    private Activity activity;

    public MeVM(Activity activity) {
        this.activity = activity;
        model = new MeModel();
    }
    public UserBean refreshData(){
        bean = model.getUserBean();
        //初始化NoteSetting
        SharedPreferences pref = activity.getSharedPreferences("note_setting",MODE_PRIVATE);
        setClockType(pref.getString("clock_type","系统闹钟"));
        setHomeShowNote(pref.getString("home_show_note","显示任务"));
        setRank3Top(pref.getString("rank3_top","不置顶"));
        //获取今天能动值
        Date date = new Date();
        String day = new SimpleDateFormat("dd").format(date);
        String yearAndMonth = new SimpleDateFormat("yyyy年MM月").format(date);
        String monthAndDay = new SimpleDateFormat("MM月dd日").format(date);
        int energy = 0;
        List<Diary_db> diaryDbs = model.findDiaryByDate(yearAndMonth,day);
        for(Diary_db diary_db : diaryDbs){
            energy += EmotionDataUtil.GetEnergy(diary_db.getEmotionValue());
        }
        List<Note_db> noteDbs = model.findNoteByDate(monthAndDay);
        for (Note_db noteDb : noteDbs){
            energy += NoteUtil.GetEnergy(noteDb.getRank(),noteDb.getIsComplete());
        }
        bean.energyValue.set(energy);
        return bean;
    }

    public void saveUser(){
        SharedPreferences.Editor editor = activity.getSharedPreferences("note_setting",MODE_PRIVATE).edit();
        editor.putString("clock_type",bean.clockType.get());
        editor.putString("home_show_note",bean.homeShowNote.get());
        editor.putString("rank3_top",bean.rank3Top.get());
        editor.apply();
        model.saveUserdb(bean);
    }

    public void setImgUrl(String url){
        bean.userHeadIma.set(url);
    }
    public String getName(){
        return bean.userName.get();
    }
    public void setName(String name){
        bean.userName.set(name);
    }
    public String getWords(){
        return bean.userWords.get();
    }
    public void setWords(String words){
        bean.userWords.set(words);
    }

    public void setClockType(String clockType) {
        bean.clockType.set(clockType);
    }
    public void setRank3Top(String rank3Top) {
        bean.rank3Top.set(rank3Top);
    }
    public void setHomeShowNote(String homeShowNote) {
        bean.homeShowNote.set(homeShowNote);
    }

    public Boolean isRank3Top(){
        if(bean.rank3Top.get().equals("置顶")){
            return true;
        }else {
            return false;
        }
    }
    public Boolean isHomeShowNote(){
        if(bean.homeShowNote.get().equals("显示任务")){
            return true;
        }else {
            return false;
        }
    }

    public int getGraphHeight(int max){
        if(bean.energyValue.get() == 0){
            return 1;
        }else {
            return (int)((float)Math.abs(bean.energyValue.get())/50 * max);
        }
    }
    public int getValue(){
        return bean.energyValue.get();
    }
}
