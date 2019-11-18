package com.example.a73233.carefree.Diary;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.a73233.carefree.MainActivity;
import com.example.a73233.carefree.R;
import com.example.a73233.carefree.Util.EmotionUtil;
import com.example.a73233.carefree.db.Diary_db;

import java.util.List;

public class DiaryListAdapter extends RecyclerView.Adapter<DiaryListAdapter.ViewHolder> {
    private List<Diary_db> diary_dbs;
    private MainActivity activity;
    private DiaryListAdapter.OnitemClick onitemClick;   //定义点击事件接口

    //定义设置点击事件监听的方法
    public void setOnitemClickLintener (DiaryListAdapter.OnitemClick onitemClick) {
        this.onitemClick = onitemClick;
    }
    //定义一个点击事件的接口
    public interface OnitemClick {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView day;
        TextView week;
        TextView yearAndMonth;
        TextView emotionValue;
        TextView diaryContent;
        ImageView photo;
        View viewLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewLayout = itemView;
            day = itemView.findViewById(R.id.day);
            week = itemView.findViewById(R.id.week);
            yearAndMonth = itemView.findViewById(R.id.year_month);
            emotionValue = itemView.findViewById(R.id.emotion_value);
            diaryContent = itemView.findViewById(R.id.diary_content);
            photo = itemView.findViewById(R.id.list_photo);
        }
    }

    public DiaryListAdapter(MainActivity activity) {
        this.activity = activity;
    }

    public void setData(List<Diary_db> diary_dbs){
        this.diary_dbs = diary_dbs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.diary_list_view,
                viewGroup,false);      //加载布局
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Diary_db diary_db = diary_dbs.get((diary_dbs.size()-1)-position);
       // Log.d("recy测试","position="+position+"id="+diary_db.getId()+diary_db.getDairyContent());
        holder.day.setText(diary_db.getDay());
        holder.week.setText(diary_db.getWeek());
        holder.yearAndMonth.setText(diary_db.getYearAndMonth());
        holder.diaryContent.setText(diary_db.getDiaryContent());
        holder.emotionValue.setText("情绪值 "+diary_db.getEmotionValue());
        holder.emotionValue.setTextColor(EmotionUtil.GetColors(diary_db.getEmotionValue())[1]);
        if (diary_db.getPhotoList()!= null && 0<diary_db.getPhotoList().size()){
            holder.photo.setVisibility(View.VISIBLE);
            Log.d("图片显示测试",diary_db.getDiaryContent()+diary_db.getPhotoList().size());
            Glide.with(activity).load(diary_db.getPhotoList().get(0))
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(25)))
                    .skipMemoryCache(true) // 不使用内存缓存
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用磁盘缓存
                    .error(R.mipmap.find_photo_fail)
                    .into(holder.photo);
        }else {
            holder.photo.setVisibility(View.GONE);
        }

        holder.viewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onitemClick.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
       // Log.d("recy测试",""+diary_dbs.size());
        return diary_dbs.size();
    }
}
