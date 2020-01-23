package com.example.a73233.carefree.diary.view;

import android.app.ActivityOptions;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import com.example.a73233.carefree.R;
import com.example.a73233.carefree.baseview.BaseActivity;
import com.example.a73233.carefree.databinding.ActivityLookDiaryBinding;
import com.example.a73233.carefree.diary.viewModel.LookVM;
import com.example.a73233.carefree.util.EmotionUtil;
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
        binding.setBean(lookVM.refreshLookView(diaryId));
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
        toolbarRight.setImageResource(R.mipmap.down_white);
        //recyclerView背景
        GradientDrawable recycleBg = new GradientDrawable();
        recycleBg.setColor(Color.WHITE);
        recycleBg.setCornerRadius(50);
        binding.lookRecyclerView.setBackground(recycleBg);
        //标题栏背景
        GradientDrawable bgViewBg = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP
                , EmotionUtil.GetColors(value));
        bgViewBg.setCornerRadius(50);
        binding.bgView.setBackground(bgViewBg);
        //emotionValueBg
        GradientDrawable emotionValueBg = new GradientDrawable();
        emotionValueBg.setCornerRadius(50);
        emotionValueBg.setColor(EmotionUtil.GetColors(value)[1]);
        binding.lookEmotionValue.setBackground(emotionValueBg);
        //折叠后背景
        GradientDrawable colBg = new GradientDrawable();
        colBg.setColor(EmotionUtil.GetColors(value)[1]);
        binding.colToolbar.setContentScrim(colBg);
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
        Pair p2 = new Pair(binding.lookRecyclerView,"sharedView_2");
        Pair p3 = new Pair(binding.lookRecyclerView,"sharedView_3");
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
