package com.example.a73233.carefree.note.model;

import com.example.a73233.carefree.bean.NoteBean;
import com.example.a73233.carefree.bean.Note_db;
import com.example.a73233.carefree.note.viewModel.NoteVM;
import com.example.a73233.carefree.note.viewModel.NoteVmImpl;
import com.example.a73233.carefree.util.ConstantPool;
import com.example.a73233.carefree.util.LogUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class NoteModel {

    /**
     * 从数据去查找所有数据
     * @param noteVM
     */
    public void findAllData(NoteVmImpl noteVM, int type){
        List<Note_db> noteDbList = LitePal.where("isAbandon = ?",""+type).find(Note_db.class);
        noteVM.findSuccess(creatNoteBean(noteDbList));
    }
    public void findRankData(NoteVmImpl noteVM, int type){
        List<Note_db> noteDbList2 = LitePal.where("isAbandon = ? and rank > ?",""+type, "0").find(Note_db.class);
        List<Note_db> noteDbList = LitePal.where("isAbandon = ? and rank = ?",""+type, "0").find(Note_db.class);
        List<NoteBean> beans = creatNoteBean(noteDbList);
        beans.addAll(creatNoteBean(noteDbList2));
        noteVM.findSuccess(beans);

    }

    /**
     * 通过id查找数据
     * @param id
     * @return
     */
    public NoteBean findDataById(int id){
        NoteBean bean = new NoteBean();
        Note_db db = LitePal.find(Note_db.class,id);
        if(db != null){
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
        }
        return bean;
    }

    /**
     * 保存一条数据
     * @param bean
     * @param id
     */
    public void saveData(NoteBean bean,int id){
        Note_db db;
        if(id == -1){
            db = new Note_db();
        }else {
            db = LitePal.find(Note_db.class,id);
        }
        db.setIsAbandon(0);
        db.setWeek(bean.week.get());
        db.setTime(bean.time.get());
        db.setText(bean.text.get());
        db.setRank(bean.rank.get());
        db.setMonthAndDay(bean.monthAndDay.get());
        db.setClockHour(bean.hour.get());
        db.setClockMinutes(bean.minutes.get());
        db.setClockText(bean.clockText.get());
        db.save();
        LogUtil.LogD("保存便贴成功");
    }

    public void abandonData(int id, int type, NoteVmImpl noteVm){
        Note_db db = LitePal.find(Note_db.class,id);
        db.setIsAbandon(ConstantPool.ISABANDON);
        db.setIsComplete(type);
        db.save();
        noteVm.abandonSuccess();
    }

    private List<NoteBean> creatNoteBean(List<Note_db> noteDbList){
        List<NoteBean> beanList = new ArrayList<>();
        for (Note_db db : noteDbList){
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
            beanList.add(bean);
        }
        return beanList;
    }
}
