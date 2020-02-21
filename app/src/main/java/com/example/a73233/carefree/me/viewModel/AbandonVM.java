package com.example.a73233.carefree.me.viewModel;

import com.example.a73233.carefree.bean.DiaryBean;
import com.example.a73233.carefree.bean.NoteBean;
import com.example.a73233.carefree.diary.Model.DiaryModel;
import com.example.a73233.carefree.diary.view.DiaryListAdapter_;
import com.example.a73233.carefree.diary.viewModel.DiaryVMImpl;
import com.example.a73233.carefree.me.model.MeModel;
import com.example.a73233.carefree.note.model.NoteModel;
import com.example.a73233.carefree.note.view.NoteListAdapter;
import com.example.a73233.carefree.note.viewModel.NoteVmImpl;
import com.example.a73233.carefree.util.ConstantPool;

import java.util.List;

public class AbandonVM implements DiaryVMImpl, NoteVmImpl {
    private MeModel meModel;
    private NoteModel noteModel;
    private DiaryModel diaryModel;
    private DiaryListAdapter_ diaryAdapter;
    private NoteListAdapter noteAdapter;

    public AbandonVM(DiaryListAdapter_ diaryAdapter, NoteListAdapter noteAdapter) {
        this.diaryAdapter = diaryAdapter;
        this.noteAdapter = noteAdapter;
        diaryModel = new DiaryModel();
        noteModel = new NoteModel();
        meModel = new MeModel();
    }
    public void refreshView(){
        diaryModel.findAllData(this, ConstantPool.ISABANDON);
        noteModel.findAllData(this,ConstantPool.ISABANDON);
    }

    public void recoveryData(int type, int id, int position){
        meModel.recoveryData(type,id);
        if(type == 0){
            diaryAdapter.removeItem(position);
        }else {
            noteAdapter.removeItem(position);
        }
    }
    public void deleteOneData(int type, int id, int position){
        meModel.deleteOneData(type, id);
        if(type == 0){
            diaryAdapter.removeItem(position);
        }else {
            noteAdapter.removeItem(position);
        }
    }
    public void deleteAllData(){
        meModel.deleteAllData();
        refreshView();
    }

    @Override
    public void findAllDataSuccess(List<DiaryBean> diaryBeanList) {
        diaryAdapter.refreshData(diaryBeanList);
    }

    @Override
    public void findSuccess(List<NoteBean> beanList) {
        noteAdapter.refreshData(beanList);
    }
}
