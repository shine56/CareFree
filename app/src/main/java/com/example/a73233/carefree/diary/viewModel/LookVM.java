package com.example.a73233.carefree.diary.viewModel;

import com.example.a73233.carefree.bean.DiaryBean;
import com.example.a73233.carefree.diary.Model.DiaryModel;
import com.example.a73233.carefree.diary.view.PhotoListAdapter;

public class LookVM{
    protected DiaryModel model;
    protected DiaryBean bean;
    protected PhotoListAdapter adapter;

    public LookVM(PhotoListAdapter adapter) {
        this.adapter = adapter;
        model = new DiaryModel();
    }

    /**
     * 刷新bean
     * @param id
     * @return
     */
    public DiaryBean refreshBean(int id){
        bean =  model.findDataById(id);
        return bean;
    }

    /**
     * 刷新图片
     */
    public void refreshPhoto(){
        adapter.setPhotoPathList(bean.photoList.get());
    }

    /**
     * 获取当前日记情绪值
     * @return
     */
    public int getValue(){
        return  bean.diaryEmotionValue.get();
    }
}
