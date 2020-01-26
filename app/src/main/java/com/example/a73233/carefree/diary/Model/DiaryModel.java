package com.example.a73233.carefree.diary.Model;

import android.util.Log;

import com.example.a73233.carefree.bean.DiaryBean;
import com.example.a73233.carefree.bean.Diary_db;
import com.example.a73233.carefree.diary.viewModel.DiaryVMImpl;
import com.example.a73233.carefree.diary.viewModel.WriteVM;
import com.example.a73233.carefree.util.LogUtil;

import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DiaryModel {
    /**
     * 查找日记
     * @param diaryVM
     */
    public void findAllData(DiaryVMImpl diaryVM){
        List<Diary_db> diaryDbList = LitePal.where("isAbandon like ?","0").find(Diary_db.class);
        LogUtil.LogD("找到数据"+diaryDbList.size()+"条");
        diaryVM.findListSuccess(changeDbToBean(diaryDbList));
    }
    public void findDataByDate(String yearAndMonth, String day, DiaryVMImpl diaryVM){
        List<Diary_db> diaryDbList = LitePal.where("yearAndMonth like ? and day like ? and isAbandon like ?"
                , yearAndMonth, day,"0").find(Diary_db.class);
        LogUtil.LogD("日期：");
        diaryVM.findListSuccess(changeDbToBean(diaryDbList));
    }
    public void findDataByText(String text,DiaryVMImpl diaryVM){
        List<Diary_db> diaryDbList = LitePal.where("isAbandon like ? and diaryContent like ?","0","%"+text+"%")
                .find(Diary_db.class);
        diaryVM.findListSuccess(changeDbToBean(diaryDbList));
    }
    public DiaryBean findDataById(int id){
        DiaryBean bean = new DiaryBean();
        Diary_db db = LitePal.find(Diary_db.class,id);
        if(db != null){
            bean.day.set(db.getDay());
            bean.diaryContent.set(db.getDiaryContent());
            bean.diaryEmotionValue.set(db.getEmotionValue());
            bean.photoList.set(db.getPhotoList());
            bean.week.set(db.getWeek());
            bean.yearAndMonth.set(db.getYearAndMonth());
            bean.id.set(db.getId());
            bean.isShowDeleteButton.set(true);
            if(db.getPhotoList() == null){
                bean.isShowImgButton.set(false);
                bean.photoList.set(new ArrayList<>());
            }else {
                bean.isShowImgButton.set(true);
            }
        }
        return bean;
    }

    /**
     * 保存数据
     * @param bean
     */
    public void saveData(DiaryBean bean, int id,WriteVM writeVM){
        Diary_db db;
        if(id == -1){
            db = new Diary_db();
        }else {
            db = LitePal.find(Diary_db.class, id);
        }
        db.setYearAndMonth(bean.yearAndMonth.get());
        db.setPhotoList(bean.photoList.get());
        db.setWeek(bean.week.get());
        db.setEmotionValue(bean.diaryEmotionValue.get());
        db.setDay(bean.day.get());
        db.setDiaryContent(bean.diaryContent.get());
        db.setIsAbandon(0);
        db.save();
        writeVM.saveDataSuccess();
    }

    /**
     * 把数据放进垃圾桶
     * @param id
     * @param writeVM
     */
    public void abandonData(int id,WriteVM writeVM){
        Diary_db db = LitePal.find(Diary_db.class,id);
        db.setIsAbandon(1);
        db.save();
        writeVM.deleteDataSuccess();
    }
    /**
     * 删除数据
     * @param id
     * @param writeVM
     */
    public void deleteData(int id,WriteVM writeVM){
        Diary_db db = LitePal.find(Diary_db.class,id);
        for(String path : db.getPhotoList()){
            File file = new File(path);
            if(file.exists()){
                file.delete();
            }
        }
        LitePal.delete(Diary_db.class, id);
    }

    /**
     * 初始化diaryBeanList
     * @param diaryDbList
     * @return
     */
    private List<DiaryBean> changeDbToBean(List<Diary_db> diaryDbList){
        List<DiaryBean> diaryBeanList = new ArrayList<>();
        for(Diary_db db :diaryDbList){
            DiaryBean bean = new DiaryBean();
            bean.day.set(db.getDay());
            bean.diaryContent.set(db.getDiaryContent());
            bean.diaryEmotionValue.set(db.getEmotionValue());
            bean.photoList.set(db.getPhotoList());
            bean.week.set(db.getWeek());
            bean.yearAndMonth.set(db.getYearAndMonth());
            bean.id.set(db.getId());
            diaryBeanList.add(bean);
        }
        return diaryBeanList;
    }
}
