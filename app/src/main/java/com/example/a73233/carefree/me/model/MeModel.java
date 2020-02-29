package com.example.a73233.carefree.me.model;

import android.content.SharedPreferences;

import com.example.a73233.carefree.bean.Diary_db;
import com.example.a73233.carefree.bean.Note_db;
import com.example.a73233.carefree.bean.UserBean;
import com.example.a73233.carefree.bean.User_db;
import com.example.a73233.carefree.util.ConstantPool;
import com.example.a73233.carefree.util.LogUtil;

import org.litepal.LitePal;

import java.util.List;

public class MeModel {

    public UserBean getUserBean(){
        UserBean bean = new UserBean();
        User_db db = LitePal.findAll(User_db.class).get(0);
        List<Diary_db> diaryDbList = LitePal.where("isAbandon like ?","0").find(Diary_db.class);
        List<Note_db> noteDbList = LitePal.where("isAbandon = ?","0").find(Note_db.class);
        bean.userName.set(db.getUserName());
        bean.userHeadIma.set(db.getUserHeadIma());
        bean.userWords.set(db.getUserWords());
        bean.abandonNums.set(
                LitePal.findAll(Diary_db.class).size()
                +LitePal.findAll(Note_db.class).size()
                -diaryDbList.size()
                -noteDbList.size());
        bean.diaryNums.set(diaryDbList.size());
        bean.noteNums.set(noteDbList.size());
        return bean;
    }

    /**
     * 通过日期找diary and note
     * @param yearAndMonth
     * @param day
     * @return
     */
    public List<Diary_db> findDiaryByDate(String yearAndMonth, String day){
        return LitePal.where("yearAndMonth like ? and day like ? and isAbandon like ?"
                , yearAndMonth, day,"0").find(Diary_db.class);
    }
    public List<Note_db> findNoteByDate(String monthAndDay){
        return LitePal.where("monthAndDay like ? and isAbandon like ?"
                , monthAndDay,"1").find(Note_db.class);
    }
    /**
     * 保存User数据
     * @param bean
     */
    public void saveUserdb(UserBean bean){
        User_db db = LitePal.findAll(User_db.class).get(0);
        db.setUserName(bean.userName.get());
        db.setUserWords(bean.userWords.get());
        db.setUserHeadIma(bean.userHeadIma.get());
        db.save();
    }

    /**
     * 恢复数据
     * @param type
     * @param id
     */
    public void recoveryData(int type, int id){
        if(type == 0){
            Diary_db diary_db = LitePal.find(Diary_db.class,id);
            diary_db.setIsAbandon(0);
            diary_db.save();
        }else {
            Note_db note_db = LitePal.find(Note_db.class,id);
            note_db.setIsAbandon(0);
            note_db.save();
        }
    }

    /**
     * 删除数据
     * @param type
     * @param id
     */
    public void deleteOneData(int type, int id){
        if(type == 0){
            LitePal.delete(Diary_db.class, id);
        }else {
            LitePal.delete(Note_db.class, id);
        }
    }
    public void deleteAllData(){
        LitePal.deleteAll(Diary_db.class,"isAbandon like ?",""+ConstantPool.ISABANDON);
        LitePal.deleteAll(Note_db.class,"isAbandon like ?",""+ConstantPool.ISABANDON);
    }
    public Diary_db findLastData(){
        List<Diary_db> diaryDbs = LitePal.where("isAbandon like ?"
                ,ConstantPool.NOT_ABANDON+"").find(Diary_db.class);
        return diaryDbs.get(diaryDbs.size()-1);
    }
}
