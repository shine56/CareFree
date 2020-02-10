package com.example.a73233.carefree.note.viewModel;

import com.example.a73233.carefree.bean.NoteBean;
import com.example.a73233.carefree.note.model.NoteModel;
import com.example.a73233.carefree.note.view.NoteListAdapter;

import java.util.List;

public class NoteVM {
    private NoteModel model;
    private NoteListAdapter adapter;

    public NoteVM(NoteListAdapter adapter) {
        this.adapter = adapter;
        model = new NoteModel();
    }
    public void refreshAllData(){
        model.findAllData(this);
    }
    public void deleteData(int id){
        model.deleteData(id);
    }
    public void findSuccess(List<NoteBean> beanList){
        adapter.refreshData(beanList);
    }
}
