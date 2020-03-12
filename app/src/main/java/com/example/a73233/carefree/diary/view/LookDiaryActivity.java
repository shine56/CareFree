package com.example.a73233.carefree.diary.view;

import android.app.ActivityOptions;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Pair;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.example.a73233.carefree.R;
import com.example.a73233.carefree.baseView.BaseActivity;
import com.example.a73233.carefree.databinding.ActivityLookDiaryBinding;
import com.example.a73233.carefree.diary.viewModel.LookVM;
import com.example.a73233.carefree.util.ConstantPool;
import com.example.a73233.carefree.util.EmotionDataUtil;
import com.example.a73233.carefree.util.PhotoManager;
import com.example.a73233.carefree.util.SpacesItemDecoration;


public class LookDiaryActivity extends BaseActivity implements View.OnClickListener {
    private ImageView toolbarLeft;
    private ImageView toolbarRight;
    private int diaryId;
    private Boolean recycleViewIsVisit = false;

    private ActivityLookDiaryBinding binding;
    private LookVM lookVM;
    private PhotoListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_diary);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_look_diary);
        diaryId = getIntent().getExtras().getInt("diaryId",0);
        adapter = new PhotoListAdapter(this,PhotoListAdapter.SMALL);
        lookVM = new LookVM(adapter);
        initView();
        initPhoto();
    }
    private void initView(){
        binding.setBean(lookVM.refreshBean(diaryId));
        int value = lookVM.getValue();

        toolbarLeft = findViewById(R.id.toolbar_left_3);
        toolbarRight = findViewById(R.id.toolbar_right_3);
        toolbarLeft.setOnClickListener(this);
        toolbarRight.setOnClickListener(this);
        binding.showPhoto.setOnClickListener(this);
        //设置状态栏
        ReviseStatusBar(TRANSPARENT_WHITE);
        //标题栏
        toolbarLeft.setImageResource(R.mipmap.back_logo);
        toolbarRight.setImageResource(R.mipmap.pull_down_white);
        //recyclerView背景
        GradientDrawable recycleBg = new GradientDrawable();
        recycleBg.setColor(Color.WHITE);
        recycleBg.setCornerRadius(50);
        binding.lookRecyclerView.setBackground(recycleBg);
        //标题栏背景
        GradientDrawable bgViewBg = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP
                , EmotionDataUtil.GetColors(value, this));
        bgViewBg.setCornerRadii(getCornerRadii(0f, 0f, 24f, 24f));
        //bgViewBg.setCornerRadius(50);
        binding.bgView.setBackground(bgViewBg);
        //emotionValueBg
        GradientDrawable emotionValueBg = new GradientDrawable();
        emotionValueBg.setCornerRadius(50);
        emotionValueBg.setColor(EmotionDataUtil.GetColors(value, this)[1]);
        binding.lookEmotionValue.setBackground(emotionValueBg);
        //折叠后背景
        GradientDrawable colBg = new GradientDrawable();
        colBg.setColor(EmotionDataUtil.GetColors(value, this)[1]);
        binding.colToolbar.setContentScrim(colBg);
        //字号
        //binding.lookDiaryText.setTextSize(lookVM.getDiaryTextSize(this));
        //长按事件监控
        //binding.lookNestScro.setLongClickable(true);
        binding.showPhoto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                lookVM.shareDiary(LookDiaryActivity.this);
                return false;
            }
        });
    }
    private void initPhoto(){
        binding.showPhoto.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.lookRecyclerView.setLayoutManager(layoutManager);
        binding.lookRecyclerView.addItemDecoration(new SpacesItemDecoration(1,30));
        binding.lookRecyclerView.setAdapter(adapter);
        lookVM.refreshPhoto();
    }

    private float[] getCornerRadii(float leftTop, float rightTop, float leftBottom, float rightBottom){
        return new float[]{db2px(leftTop), db2px(leftTop),
        db2px(rightTop), db2px(rightTop),
        db2px(leftBottom), db2px(leftBottom),
        db2px(rightBottom), db2px(rightBottom)};
    }
    private float db2px(float dpVal){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.showPhoto :
                if(recycleViewIsVisit){
                    binding.lookRecyclerView.setVisibility(View.GONE);
                    recycleViewIsVisit = false;
                }else {
                    binding.lookRecyclerView.setVisibility(View.VISIBLE);
                    recycleViewIsVisit = true;
                }
                break;
            case R.id.toolbar_left_3:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.toolbar_right_3:
                startWriteActivity();
                break;
        }
    }
    private void startWriteActivity(){
        Bundle bundle = new Bundle();
        bundle.putInt("diaryId",diaryId);
        bundle.putInt("addType",-1);
        Pair p1 = new Pair(binding.bgView,"sharedView");
        startActivityForResultWithOptions(WriteDiaryActivity.class,bundle,1
                ,ActivityOptions.makeSceneTransitionAnimation(LookDiaryActivity.this,p1).toBundle());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    initView();
                    lookVM.refreshPhoto();
                }else if(resultCode == RESULT_OK-10){
                    finish();
                }
                break;
        }
    }

}
