package com.example.a73233.carefree.diary.view;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.a73233.carefree.R;
import com.example.a73233.carefree.util.BigPhotoViewer;

import java.util.List;

public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.ViewHolder> {
    private List<String> photoPathList;
    private Activity activity;
    private int SMALL = 1;
    private int BIG = 2;
    private int type;
    private int radian = 40;

    private OnitemClick onitemClick;   //定义点击事件接口

    //定义设置点击事件监听的方法
    public void setOnitemClickLintener (OnitemClick onitemClick) {
        this.onitemClick = onitemClick;
    }
    //定义一个点击事件的接口
    public interface OnitemClick {
        void onItemClick(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView photo;
        ImageView deletePhoto;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.photo_list);
            deletePhoto = itemView.findViewById(R.id.delete_photo);
        }
    }

    public PhotoListAdapter (Activity activity, List<String> photoPathList , int type){
        this.activity = activity;
        this.photoPathList = photoPathList;
        this.type = type;
    }

    public void setPhotoPathList(List<String> photoPathList) {
        this.photoPathList = photoPathList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(type == SMALL){
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.photo_list_view,
                    viewGroup,false);      //加载布局
            ViewHolder holder = new ViewHolder(view);
            radian = 30;
            return holder;
        }else if(type == BIG){
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.write_photo_list_view,
                    viewGroup,false);      //加载布局
            ViewHolder holder = new ViewHolder(view);
            radian = 40;
            return holder;
        }
       return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        if(photoPathList!=null && photoPathList.size()>0){
            String imagePath = photoPathList.get(i);
            Glide.with(activity).load(imagePath)
                    .skipMemoryCache(true) // 不使用内存缓存
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用磁盘缓存
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                    .error(R.mipmap.find_photo_fail)
                    .into(holder.photo);
            holder.photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, BigPhotoViewer.class);
                    intent.putExtra("imagePath",imagePath);
                    activity.startActivity(intent);
                }
            });
            if(type == SMALL){
                holder.deletePhoto.setVisibility(View.GONE);
            }else {
                holder.deletePhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onitemClick.onItemClick(i);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        if(photoPathList != null){
            Log.d("图片测试",""+photoPathList.size());
            return photoPathList.size();
        }else {
            return 0;
        }
    }
}