package com.example.a73233.carefree.note.viewModel;

import com.example.a73233.carefree.bean.NoteBean;

import java.util.List;

public interface NoteVmImpl {
    void findSuccess(List<NoteBean> beanList);
    void abandonSuccess();
}
