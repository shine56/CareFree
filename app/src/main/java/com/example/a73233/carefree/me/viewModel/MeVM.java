package com.example.a73233.carefree.me.viewModel;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.a73233.carefree.bean.Diary_db;
import com.example.a73233.carefree.bean.Note_db;
import com.example.a73233.carefree.bean.UserBean;
import com.example.a73233.carefree.me.model.MeModel;
import com.example.a73233.carefree.me.view.SettingActivity;
import com.example.a73233.carefree.util.ConstantPool;
import com.example.a73233.carefree.util.DataBackup;
import com.example.a73233.carefree.util.EmotionDataUtil;
import com.example.a73233.carefree.util.LogUtil;
import com.example.a73233.carefree.util.NoteUtil;

import java.io.IOException;
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

    /**
     * 刷新UserBean
     * @return
     */
    public UserBean refreshUserBean(){
        bean = model.getUserBean();
        //获取今天能动值
        if(isShowEmotionValue()){
            Diary_db diaryDb = model.findLastData();
            if(diaryDb == null){
                bean.energyValue.set(0);
            }else {
                bean.energyValue.set(diaryDb.getEmotionValue());
            }
        }else {
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
        }
        return bean;
    }

    /**
     * 设置User 头像、昵称、签名
     * @param url
     */
    public void setImgUrl(String url){
        bean.userHeadIma.set(url);
        model.saveUserdb(bean);
    }
    public void setName(String name){
        bean.userName.set(name);
        model.saveUserdb(bean);
    }
    public String getName(){
        return bean.userName.get();
    }
    public String getWords(){
        return bean.userWords.get();
    }
    public void setWords(String words){
        bean.userWords.set(words);
        model.saveUserdb(bean);
    }


    /**
     * 设置MeFragment的卡片
     * @param max
     * @return
     */
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

    /**
     * 获取用户对首页卡片是否显示的是当前情绪值
     * @return
     */
    public Boolean isShowEmotionValue(){
        SharedPreferences pref = activity.getSharedPreferences("setting",MODE_PRIVATE);
        if (pref.getString("cardShow", ConstantPool.CARD_SHOW_EMOTION).equals(ConstantPool.CARD_SHOW_EMOTION)){
            return true;
        }else {
            return false;
        }
    }

    public Boolean isHomeShowNote(){
        SharedPreferences pref = activity.getSharedPreferences("setting",MODE_PRIVATE);
        if (pref.getString("homeShowNote", ConstantPool.NOT_HOME_SHOW_NOTE).equals(ConstantPool.HOME_SHOW_NOTE)){
            return true;
        }else {
            return false;
        }
    }
    public Boolean isTaskTop(){
        SharedPreferences pref = activity.getSharedPreferences("setting",MODE_PRIVATE);
        if (pref.getString("taskIsTop", ConstantPool.TASK_IS_NOT_TOP).equals(ConstantPool.TASK_IS_TOP)){
            return true;
        }else {
            return false;
        }
    }
    public void setTaskIsTop(String str){
        SharedPreferences.Editor editor = activity.getSharedPreferences("setting",MODE_PRIVATE).edit();
        editor.putString("taskIsTop",str);
        editor.apply();
    }
    public void setHomeShowNote(String str){
        SharedPreferences.Editor editor = activity.getSharedPreferences("setting",MODE_PRIVATE).edit();
        editor.putString("homeShowNote",str);
        editor.apply();
    }
}
