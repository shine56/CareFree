package com.example.a73233.carefree.baseview;

import com.example.a73233.carefree.bean.DiaryBean;

import java.util.List;

public interface MyVMImpl {

    /**
     * 查找日记成功
     * @return
     */
    public void findListSuccess(List<DiaryBean> diaryBeanList);
}
