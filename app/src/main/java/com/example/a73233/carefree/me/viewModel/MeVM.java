package com.example.a73233.carefree.me.viewModel;

import com.example.a73233.carefree.bean.UserBean;
import com.example.a73233.carefree.me.model.MeModel;
import com.example.a73233.carefree.util.LogUtil;

public class MeVM {
    private MeModel model;
    private UserBean bean;

    public MeVM() {
        model = new MeModel();
    }

    public UserBean refreshData(){
        bean = model.getData();
        return bean;
    }

    public void saveUser(){
        model.saveUserdb(bean);
    }

    public void setImgUrl(String url){
        bean.userHeadIma.set(url);
        LogUtil.LogD("更换图片");
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

    public int getGraphHeight(int max){
        return (int)((float)Math.abs(bean.energyValue.get())/50 * max);
    }
    public int getValue(){
        return bean.energyValue.get();
    }
}
