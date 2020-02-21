package com.example.a73233.carefree.note.viewModel;

import android.app.Activity;
import android.content.SharedPreferences;

import com.example.a73233.carefree.bean.NoteBean;
import com.example.a73233.carefree.note.model.NoteModel;
import com.example.a73233.carefree.note.view.NoteListAdapter;
import com.example.a73233.carefree.util.ConstantPool;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class NoteVM implements NoteVmImpl{
    private NoteModel model;
    private NoteListAdapter adapter;

    public NoteVM(NoteListAdapter adapter) {
        this.adapter = adapter;
        model = new NoteModel();
    }
    public void refreshAllData(Activity activity){
        if(isRankTop(activity)){
            model.findRankData(this,ConstantPool.NOT_ABANDON);
        }else {
            model.findAllData(this, ConstantPool.NOT_ABANDON);
        }
    }
    public Boolean isRankTop(Activity activity){
        SharedPreferences pref = activity.getSharedPreferences("note_setting",MODE_PRIVATE);
        if (pref.getString("rank3_top","不置顶").equals("置顶")){
            return true;
        }else {
            return false;
        }
    }
    public void deleteData(int id, int type){
        model.deleteData(id,type);
    }

    public void findSuccess(List<NoteBean> beanList){
        adapter.refreshData(beanList);
    }
}
