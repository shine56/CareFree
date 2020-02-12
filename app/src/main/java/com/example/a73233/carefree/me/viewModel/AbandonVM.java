package com.example.a73233.carefree.me.viewModel;

import com.example.a73233.carefree.bean.DiaryBean;
import com.example.a73233.carefree.bean.NoteBean;
import com.example.a73233.carefree.diary.Model.DiaryModel;
import com.example.a73233.carefree.diary.view.DiaryListAdapter_;
import com.example.a73233.carefree.baseview.MyVMImpl;
import com.example.a73233.carefree.me.model.MeModel;
import com.example.a73233.carefree.note.model.NoteModel;
import com.example.a73233.carefree.note.view.NoteListAdapter;
import com.example.a73233.carefree.note.viewModel.NoteVmImpl;

import java.util.List;

public class AbandonVM implements MyVMImpl , NoteVmImpl {
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
        diaryModel.findAllData(this,1);
        noteModel.findAllData(this,1);
    }

    public void recoveryData(int type, int id, int position){
        meModel.recoveryData(type,id);
        if(type == 0){
            diaryAdapter.removeItem(position);
        }else {
            noteAdapter.removeItem(position);
        }
    }
    public void deleteData(int type, int id, int position){
        meModel.deleteData(type, id);
        if(type == 0){
            diaryAdapter.removeItem(position);
        }else {
            noteAdapter.removeItem(position);
        }
    }

    @Override
    public void findListSuccess(List<DiaryBean> diaryBeanList) {
        diaryAdapter.refreshData(diaryBeanList);
    }

    @Override
    public void findSuccess(List<NoteBean> beanList) {
        noteAdapter.refreshData(beanList);
    }
}
