package com.example.a73233.carefree.home.model;

import com.example.a73233.carefree.bean.Diary_db;
import com.example.a73233.carefree.bean.NoteBean;
import com.example.a73233.carefree.bean.Note_db;
import com.example.a73233.carefree.util.ConstantPool;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class HomeModel {
    public List<NoteBean> findTaskNote(){
        List<Note_db> dbs = LitePal.where("rank>? and isAbandon=?","0","0").find(Note_db.class);
        List<NoteBean> beans = new ArrayList<>();
        for (Note_db db : dbs){
            NoteBean bean = new NoteBean();
            bean.idAbandon.set(db.getIsAbandon());
            bean.monthAndDay.set(db.getMonthAndDay());
            bean.rank.set(db.getRank());
            bean.text.set(db.getText());
            bean.time.set(db.getTime());
            bean.week.set(db.getWeek());
            bean.id.set(db.getId());
            bean.hour.set(db.getClockHour());
            bean.minutes.set(db.getClockMinutes());
            bean.clockText.set(db.getClockText());
            beans.add(bean);
        }
        return beans;
    }

    /**
     * 通过日期找数据
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
                , monthAndDay,""+ConstantPool.ISABANDON).find(Note_db.class);
    }
}
