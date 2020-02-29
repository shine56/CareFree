package com.example.a73233.carefree.note.viewModel;

import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.a73233.carefree.bean.NoteBean;
import com.example.a73233.carefree.note.model.NoteModel;
import com.example.a73233.carefree.note.view.NoteListAdapter;
import com.example.a73233.carefree.util.ConstantPool;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class NoteVM implements NoteVmImpl{
    private NoteModel model;
    private NoteListAdapter adapter;
    private Activity activity;

    public NoteVM(NoteListAdapter adapter, Activity activity) {
        this.activity = activity;
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
    public void abandonData(int id, int type){
        model.abandonData(id,type, this);
    }


    public void findSuccess(List<NoteBean> beanList){
        adapter.refreshData(beanList);
    }

    @Override
    public void abandonSuccess() {
        Toast.makeText(activity, "便贴已放进废纸篓",Toast.LENGTH_SHORT).show();
    }
}
