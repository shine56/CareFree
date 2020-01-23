package com.example.a73233.carefree.baseview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.a73233.carefree.bean.DiaryBean;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter {
    public List<T> mList;
    public Context context;

    public BaseAdapter(Context context) {
        this.context = context;
        mList = new ArrayList<>();
    }

    public void refreshData(List<T> data){
        mList.clear();
        mList.addAll(data);
        notifyDataSetChanged();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        public MyHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
