package com.example.a73233.carefree.note.view;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a73233.carefree.R;
import com.example.a73233.carefree.baseview.BaseAdapter;
import com.example.a73233.carefree.bean.NoteBean;
import com.example.a73233.carefree.databinding.NoteListItemBinding;
import com.example.a73233.carefree.util.NoteUtil;

public class NoteListAdapter extends BaseAdapter {

    public NoteListAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        NoteListItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.note_list_item,viewGroup,false);
        return new MyHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NoteListItemBinding binding = DataBindingUtil.getBinding(holder.itemView);
        NoteBean bean = (NoteBean) mList.get(mList.size()-1-position);
        binding.setBean(bean);
        binding.executePendingBindings();
        holder.itemView.scrollTo(0,0);

        binding.noteBody.setBackgroundResource(NoteUtil.GetColorByRank(bean.rank.get()));
        binding.noteBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.onClick(view, bean.id.get(), position,bean.text.get());
            }
        });
        binding.noteRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem(position);
                itemClick.onClick(view, bean.id.get(),position,null);
            }
        });
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }
}
