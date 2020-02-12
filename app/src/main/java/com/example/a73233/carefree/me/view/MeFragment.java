package com.example.a73233.carefree.me.view;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.a73233.carefree.baseview.BaseFragment;
import com.example.a73233.carefree.R;
import com.example.a73233.carefree.databinding.FragmentMeBinding;
import com.example.a73233.carefree.me.viewModel.MeVM;
import com.example.a73233.carefree.util.EmotionUtil;
import com.example.a73233.carefree.util.GlideCircleBorderTransform;
import com.example.a73233.carefree.util.LogUtil;

public class MeFragment extends BaseFragment {
    private FragmentMeBinding binding;
    private MeVM meVM;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_me,container,false);
        meVM = new MeVM();
        binding.setBean(meVM.refreshData());
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.setBean(meVM.refreshData());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            binding.setBean(meVM.refreshData());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
        initView();
    }

    private void initView(){
        //条形图高度
        ViewGroup.LayoutParams layoutParams = binding.meValueGraph.getLayoutParams();
        layoutParams .height = meVM.getGraphHeight(binding.meValueGraphBg.getLayoutParams().height);
        binding.meValueGraph.setLayoutParams(layoutParams);
        //卡片背景
        GradientDrawable viewBg = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP
                , EmotionUtil.GetColors(meVM.getValue()));
        viewBg.setCornerRadius(50);
        binding.meValueBg.setBackground(viewBg);

        //点击监控
        binding.meAbandonBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(AbandonActivity.class);
            }
        });
        binding.meSettingBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(SettingActivity.class);
            }
        });
    }
    @BindingAdapter("user_head_url")
    public static void LoadHeadIma(ImageView imageView, String imgUrl){
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .bitmapTransform(new GlideCircleBorderTransform(6, R.color.shadowGray))
                .diskCacheStrategy(DiskCacheStrategy.DATA);
        if(imgUrl != null){
            Glide.with(imageView.getContext()).load(imgUrl)
                    //.apply(RequestOptions.bitmapTransform(new RoundedCorners(50)))
                    .apply(options)
                    .skipMemoryCache(true) // 不使用内存缓存
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用磁盘缓存
                    .error(R.mipmap.find_photo_fail)
                    .into(imageView);
        }else {
            Glide.with(imageView.getContext()).load(R.drawable.user_head_ima)
                    .apply(options)
                    .skipMemoryCache(true) // 不使用内存缓存
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用磁盘缓存
                    .error(R.mipmap.find_photo_fail)
                    .into(imageView);
        }
    }

}
