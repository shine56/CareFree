package com.example.a73233.carefree.diary.view;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.example.a73233.carefree.R;
import com.example.a73233.carefree.baseView.BaseActivity;
import com.example.a73233.carefree.databinding.ActivityWriteDiaryBinding;
import com.example.a73233.carefree.databinding.DialogChoosePhotoBinding;
import com.example.a73233.carefree.diary.viewModel.WriteVM;
import com.example.a73233.carefree.util.EmotionDataUtil;
import com.example.a73233.carefree.util.PhotoManager;
import com.example.a73233.carefree.util.SpacesItemDecoration;

public class WriteDiaryActivity extends BaseActivity{
    private static final int AI = 1;
    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;
    private ActivityWriteDiaryBinding binding;
    private int diaryId;
    private int addType;
    private WriteVM writeVM;
    private PhotoListAdapter adapter;
    private String imagePath;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_write_diary);
        diaryId = getIntent().getExtras().getInt("diaryId",0);
        addType = getIntent().getIntExtra("addType",1);
        logD("type="+addType);
        adapter = new PhotoListAdapter(this,PhotoListAdapter.BIG);
        writeVM = new WriteVM(adapter,this);
        initView();
    }
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.write_add_photo :
                dialog.show();
                break;
            case R.id.write_delete:
                if(diaryId == -1){
                    finish();
                }else {
                    writeVM.abandonDiary(diaryId);
                }
                break;
            case R.id.write_complete:
                writeVM.saveDiary(addType,diaryId);
                break;
            case R.id.take_photo:
                imagePath = PhotoManager.TakePhoto(WriteDiaryActivity.this);
                dialog.hide();
                break;
            case R.id.choose_photo:
                Intent intentAlbum = new Intent(Intent.ACTION_GET_CONTENT);
                intentAlbum.setType("image/*");
                startActivityForResult(intentAlbum,CHOOSE_PHOTO);
                dialog.hide();
                break;
            case R.id.cancel_add_photo:
                dialog.hide();
                break;
            case R.id.edit_wake_up:
                binding.writeWriteDiary.requestFocus();
                //显示软键盘
                InputMethodManager inputManager =
                        (InputMethodManager) binding.writeWriteDiary.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(binding.writeWriteDiary, 0);
                Editable editable = binding.writeWriteDiary.getText();
                Selection.setSelection(editable,editable.length());
                break;
        }

    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case TAKE_PHOTO:
                if(resultCode == RESULT_OK){
                    writeVM.addPhoto(imagePath);
                }
                break;
            case CHOOSE_PHOTO:
                if(resultCode == RESULT_OK){
                    imagePath = PhotoManager.copyPhoto(this,
                            PhotoManager.GetPathFromUri(this,data.getData()));
                    writeVM.addPhoto(imagePath);
                }
                break;
        }
    }
    private void initView(){
        binding.setBean(writeVM.refreshBean(diaryId));
        binding.setWriteActivity(this);
        //设置对话框
        initDialogView();
        //设置状态栏
        ReviseStatusBar(TRANSPARENT_WHITE);
        //标题栏
        if(diaryId == -1 && addType == AI){
            binding.writeDelete.setImageResource(R.mipmap.back_black);
        }else if(diaryId == -1){
            binding.writeDelete.setImageResource(R.mipmap.back_logo);
        }else {
            binding.writeDelete.setImageResource(R.mipmap.cancel_logo_white);
        }
        //添加图片按钮
        GradientDrawable addPhotoBg = new GradientDrawable();
        addPhotoBg.setColor(Color.WHITE);
        addPhotoBg.setCornerRadius(40);
        binding.writeAddPhoto.setBackground(addPhotoBg);
        //根布局背景
        GradientDrawable parentBackground;
        if(addType == -1){
             parentBackground= new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP
                            , EmotionDataUtil.GetColors(writeVM.getValue()));
        }else {
            parentBackground = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP
                            , EmotionDataUtil.GetColorsByType(addType));
        }

        binding.activityWriteDiary.setBackground(parentBackground);
        //写日记自由选择情绪时
        if(diaryId == -1){
            if(addType == AI){
                //标题栏 日记文本
                binding.writeComplete.setImageResource(R.mipmap.complete_black);
                binding.writeTitle.setTextColor(Color.BLACK);
                binding.writeSignLine.setBackgroundColor(Color.GRAY);
                binding.writeWriteDiary.setTextColor(Color.BLACK);
                binding.writeWriteDiary.setHintTextColor(Color.GRAY);
                ReviseStatusBar(TRANSPARENT_BLACK);
                addPhotoBg.setColor(0XFF6072FF);
                binding.writeAddPhoto.setBackground(addPhotoBg);
                binding.writeAddPhotoLogo.setImageResource(R.mipmap.camera_logo_white);
                binding.activityWriteDiary.setBackgroundResource(R.color.mainBg);
            }
        }
        //初始化图片
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.writeRecycleView.setLayoutManager(layoutManager);
        binding.writeRecycleView.addItemDecoration(new SpacesItemDecoration(1,30));
        binding.writeRecycleView.setAdapter(adapter);
        writeVM.refreshPhoto();
        //对照片进行监听
        adapter.setOnitemClickLintener(new PhotoListAdapter.OnitemClick() {
            @Override
            public void onItemClick(int position) {
                writeVM.reMovePhoto(position);
            }
        });
        //初始化editText
        //监听输入框
        binding.writeWriteDiary.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                writeVM.refreshSingle();
            }
        });
    }
    private void initDialogView(){
        dialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        LayoutInflater inflate = LayoutInflater.from(this);
        DialogChoosePhotoBinding mbinding = DataBindingUtil.inflate(inflate,
                R.layout.dialog_choose_photo,null,false);
        mbinding.setWriteActivity(this);
        //将布局设置给Dialog
        dialog.setContentView(mbinding.getRoot());
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 0;//设置Dialog距离底部的距离
        //宽度填满
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //将属性设置给窗体
        dialogWindow.setAttributes(lp);
    }
}
