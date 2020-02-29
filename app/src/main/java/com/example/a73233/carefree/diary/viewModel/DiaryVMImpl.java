package com.example.a73233.carefree.diary.viewModel;

import com.example.a73233.carefree.bean.DiaryBean;

import java.util.List;

public interface DiaryVMImpl {

    /**
     * 查找所有数据成功
     * @param beanList
     */
    public void findAllDataSuccess(List<DiaryBean> beanList);
    public void abandonDataSuccess();
}
