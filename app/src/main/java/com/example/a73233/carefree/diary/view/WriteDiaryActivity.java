package com.example.a73233.carefree.diary.view;

import android.app.Dialog;
import android.content.Intent;;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a73233.carefree.R;
import com.example.a73233.carefree.util.BigPhotoViewer;
import com.example.a73233.carefree.util.PhotoManager;
import com.example.a73233.carefree.util.SpacesItemDecoration;
import com.example.a73233.carefree.bean.Diary_db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class WriteDiaryActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView toolbarLeft;
    private TextView toolbarMidle;
    private TextView toolbarRight;
    private ConstraintLayout parentLayout;
    private EditText writeDiary;
    private TextView addPhoto;
    private ImageView addPhotoLogo;
    private View writeSignLine;
    private Button takePhoto;
    private Button choosePhoto;
    private Button cancelAddPhoto;
    private Dialog dialog;
    private View inflate;
    private static final int AI = 1;
    private static final int HAPPY = 2;
    private static final int CALM = 3;
    private static final int SAD = 4;
    private static final int REPRESSION = 5;
    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;
    private  String imagePath;
    private List<String> photoList;
    private int emotionValue;
    private RecyclerView recyclerView;
    private PhotoListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_diary);
        initView();

        //对输入框进行监听
        writeDiary.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String diaryText = writeDiary.getText().toString();
                if(diaryText == null || diaryText.equals("")){
                    writeSignLine.setVisibility(View.VISIBLE);
                }else {
                    writeSignLine.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //对照片进行监听
        adapter.setOnitemClickLintener(new PhotoListAdapter.OnitemClick() {
            @Override
            public void onItemClick(int position) {
                photoList.remove(position);
                showPhoto();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, BigPhotoViewer.class);
        switch (v.getId()){
            case R.id.add_photo :
                showDialogView();
                break;
            case R.id.toolbar_left2:
                finish();
                break;
            case R.id.toolbar_right2:
                saveDiary();
                Toast.makeText(this,"日记保存成功",Toast.LENGTH_LONG).show();
                setResult(RESULT_OK);
                finish();
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
        }
    }
    private void saveDiary(){
        Date date = new Date();
        String day = new SimpleDateFormat("dd").format(date);
        String yearAndMonth = new SimpleDateFormat("yyyy年MM月").format(date);
        String week = new SimpleDateFormat("EEEE").format(date);
        String diaryContent = writeDiary.getText().toString();
        Diary_db diaryDb = new Diary_db();
        diaryDb.setDay(day);
        diaryDb.setYearAndMonth(yearAndMonth);
        diaryDb.setWeek(week);
        if(diaryContent == null || diaryContent.equals("")){
            diaryDb.setDiaryContent(diaryContent);
        }else {
            diaryDb.setDiaryContent(diaryContent+"\n\n\n\n\n");
        }

        diaryDb.setPhotoList(photoList);
        //随机数
        Random random = new Random();
        diaryDb.setEmotionValue(random.nextInt(101)-50);
        diaryDb.save();
    }
    private void showDialogView(){
        //设置对话框
        dialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        inflate = LayoutInflater.from(this).inflate(R.layout.dialog_choose_photo, null);
        takePhoto = inflate.findViewById(R.id.take_photo);
        choosePhoto = inflate.findViewById(R.id.choose_photo);
        cancelAddPhoto = inflate.findViewById(R.id.cancel_add_photo);

        takePhoto.setOnClickListener(this);
        choosePhoto.setOnClickListener(this);
        cancelAddPhoto.setOnClickListener(this);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
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
        dialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case TAKE_PHOTO:
                if(resultCode == RESULT_OK){
                    photoList.add(imagePath);
                    showPhoto();
                }
                break;
            case CHOOSE_PHOTO:
                if(resultCode == RESULT_OK){

                    imagePath = PhotoManager.copyPhoto(this,
                            PhotoManager.GetPathFromUri(this,data.getData()));
                    photoList.add(imagePath);
                    showPhoto();
                }
                break;
        }

    }
    private void showPhoto(){
       adapter.setPhotoPathList(photoList);
       adapter.notifyDataSetChanged();
    }
    private void initView(){
        addPhotoLogo = findViewById(R.id.add_photo_logo);
        addPhoto = findViewById(R.id.add_photo);
        toolbarLeft = findViewById(R.id.toolbar_left2);
        toolbarMidle = findViewById(R.id.toolbar_center2);
        toolbarRight = findViewById(R.id.toolbar_right2);
        parentLayout = findViewById(R.id.activity_single_diary);
        writeDiary = findViewById(R.id.write_diary);
        writeSignLine = findViewById(R.id.write_sign_line);
        recyclerView = findViewById(R.id.write_recycle_view);

        adapter = new PhotoListAdapter(this,photoList,2);
        photoList = new ArrayList<>();

        addPhoto.setOnClickListener(this);
        toolbarLeft.setOnClickListener(this);
        toolbarRight.setOnClickListener(this);

        //设置状态栏
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setStatusBarColor(Color.TRANSPARENT);

        //设置标题栏
        toolbarMidle.setText("今日");
        toolbarRight.setText("完成");

        //图片
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SpacesItemDecoration(1,30));
        recyclerView.setAdapter(adapter);

        //设置背景
        int addType = getIntent().getIntExtra("addType",1);
        GradientDrawable parentBackground;
        GradientDrawable addPhotoBg = new GradientDrawable();
        addPhotoBg.setColor(Color.WHITE);
        addPhotoBg.setCornerRadius(40);
        addPhoto.setBackground(addPhotoBg);
        switch (addType){
            case AI :
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

                toolbarLeft.setImageResource(R.mipmap.cancel_logo_black);
                toolbarRight.setTextColor(Color.BLACK);
                toolbarMidle.setTextColor(Color.BLACK);
                writeDiary.setTextColor(0XFF7F7F7F);
                writeSignLine.setBackgroundColor(Color.GRAY);

                addPhotoBg.setColor(0XFF6072FF);
                addPhoto.setBackground(addPhotoBg);
                addPhotoLogo.setImageResource(R.mipmap.camera_logo_white);
                break;
            case HAPPY :
                int[] colors1 = {0XFF3FABD5,0XFF38D5D6};
                parentBackground = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,colors1);
                parentBackground.setGradientType(GradientDrawable.RECTANGLE);
                parentLayout.setBackground(parentBackground);
                Log.d("addtype测试","进入");
                break;
            case CALM :
                int[] colors2 = {0XFF537AE1,0XFF64B0E8};
                parentBackground = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,colors2);
                parentLayout.setBackground(parentBackground);
                break;
            case SAD :
                int[] colors3 = {0XFFAC69DB,0XFF9B85FF};
                parentBackground = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,colors3);
                parentLayout.setBackground(parentBackground);
                break;
            case REPRESSION :
                int[] colors4 = {0XFF09203F,0XFF2B5876};
                parentBackground = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,colors4);
                parentLayout.setBackground(parentBackground);
                break;
        }
    }
}
