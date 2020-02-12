package com.example.a73233.carefree.me.model;

import com.example.a73233.carefree.bean.Diary_db;
import com.example.a73233.carefree.bean.Note_db;
import com.example.a73233.carefree.bean.UserBean;
import com.example.a73233.carefree.bean.User_db;
import com.example.a73233.carefree.util.LogUtil;

import org.litepal.LitePal;

import java.util.List;

public class MeModel {
    public UserBean getData(){
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
        if(diaryDbList.size() != 0){
            bean.energyValue.set(diaryDbList.get(diaryDbList.size()-1).getEmotionValue());
        }else {
            bean.energyValue.set(28);
        }
        LogUtil.LogD("废纸数目"+bean.abandonNums.get());
        return bean;
    }

    public void saveUserdb(UserBean bean){
        User_db db = LitePal.findAll(User_db.class).get(0);
        db.setUserName(bean.userName.get());
        db.setUserWords(bean.userWords.get());
        db.setUserHeadIma(bean.userHeadIma.get());
        db.save();
    }

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

    public void deleteData(int type, int id){
        if(type == 0){
            LitePal.delete(Diary_db.class, id);
        }else {
            LitePal.delete(Note_db.class, id);
        }

    }
}
