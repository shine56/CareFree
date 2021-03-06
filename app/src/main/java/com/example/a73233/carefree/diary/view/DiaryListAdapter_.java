package com.example.a73233.carefree.diary.view;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.a73233.carefree.R;
import com.example.a73233.carefree.baseView.BaseAdapter;
import com.example.a73233.carefree.bean.DiaryBean;
import com.example.a73233.carefree.databinding.DiaryListViewBinding;
import com.example.a73233.carefree.util.EmotionDataUtil;
import com.example.a73233.carefree.util.GlideCircleBorderTransform;

public class DiaryListAdapter_ extends BaseAdapter {
    public DiaryListAdapter_(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        DiaryListViewBinding binding = DataBindingUtil.inflate(inflater, R.layout.diary_list_view,viewGroup,false);
        return new MyHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DiaryListViewBinding binding = DataBindingUtil.getBinding(holder.itemView);
        DiaryBean bean = (DiaryBean)mList.get(mList.size()-1-position);
        binding.setDiaryBean(bean);
        binding.executePendingBindings();
        holder.itemView.scrollTo(0,0);

        binding.emotionValue.setTextColor(EmotionDataUtil.GetColors(bean.diaryEmotionValue.get(), context)[1]);
        binding.diaryListRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.onClick(view,bean.id.get(),position,bean.diaryContent.get());
            }
        });
        binding.diaryListAbandon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem(position);
                itemClick.onClick(view,bean.id.get(),position,null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @BindingAdapter("diary_list_imgUrl")
    public static void loadListImg(ImageView imageView,String imgUrl){
        if(imgUrl != null){
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .bitmapTransform(new GlideCircleBorderTransform(6, R.color.shadowGray,1))
                    .diskCacheStrategy(DiskCacheStrategy.DATA);
            Glide.with(imageView.getContext()).load(imgUrl)
                    //.apply(RequestOptions.bitmapTransform(new RoundedCorners(25)))
                    .apply(options)
                    .skipMemoryCache(true) // 不使用内存缓存
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用磁盘缓存
                    .error(R.mipmap.find_photo_fail)
                    .into(imageView);
        }else {
            imageView.setVisibility(View.GONE);
        }
    }
}
