package com.example.a73233.carefree.Diary;

import android.app.Dialog;
import android.content.Intent;
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
import com.example.a73233.carefree.Util.BigPhotoViewer;
import com.example.a73233.carefree.Util.EmotionUtil;
import com.example.a73233.carefree.Util.PhotoManager;
import com.example.a73233.carefree.Util.SpacesItemDecoration;
import com.example.a73233.carefree.db.Diary_db;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReviseDiaryActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView toolbarLeft;
    private TextView toolbarMidle;
    private TextView toolbarRight;
    private ConstraintLayout parentLayout;
    private EditText writeDiary;
    private TextView addPhoto;
    private Button takePhoto;
    private Button choosePhoto;
    private Button cancelAddPhoto;
    private Dialog dialog;
    private View inflate;
    private View reviseSignLine;
    private RecyclerView recyclerView;
    private PhotoListAdapter adapter;

    private Diary_db diary_db;
    private int emotionValue;
    private List<String> photoList;
    private String imagePath;
    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise_diary);
        Intent intent = getIntent();
        diary_db = LitePal.find(Diary_db.class,intent.getIntExtra("diaryId",0));
        if(diary_db != null){
            emotionValue= diary_db.getEmotionValue();
            initView();
        }else {
            Toast.makeText(this,"修改日记失败",Toast.LENGTH_LONG).show();
            finish();
        }

        //监听输入框
        writeDiary.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String diaryText = writeDiary.getText().toString();
                if(diaryText == null || diaryText.equals("")){
                    reviseSignLine.setVisibility(View.VISIBLE);
                }else {
                    reviseSignLine.setVisibility(View.GONE);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.revise_add_photo :
                showDialogView();
                break;
            case R.id.toolbar_left2:
                deleteDiary();
                break;
            case R.id.toolbar_right2:
                saveDiary();
                Toast.makeText(this,"日记修改成功",Toast.LENGTH_LONG).show();
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.take_photo:
                imagePath = PhotoManager.TakePhoto(ReviseDiaryActivity.this);
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
        String diaryContent = writeDiary.getText().toString();

        diary_db.setDiaryContent(diaryContent);
        diary_db.setPhotoList(photoList);
        //随机数
        Random random = new Random();
        diary_db.setEmotionValue(random.nextInt(101)-50);
        diary_db.save();
    }
    private void deleteDiary(){
        LitePal.delete(Diary_db.class, diary_db.getId());
        finish();
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

                    /*photo1.setVisibility(View.VISIBLE);
                    Glide.with(this).load(data.getData())
                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(40)))
                            .skipMemoryCache(true) // 不使用内存缓存
                            .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用磁盘缓存
                            .into(photo1);*/
                }
                break;
        }

    }
    private void initView(){
        addPhoto = findViewById(R.id.revise_add_photo);
        toolbarLeft = findViewById(R.id.toolbar_left2);
        toolbarMidle = findViewById(R.id.toolbar_center2);
        toolbarRight = findViewById(R.id.toolbar_right2);
        parentLayout = findViewById(R.id.activity_revise_diary);
        writeDiary = findViewById(R.id.revise_write_diary);
        reviseSignLine = findViewById(R.id.revise_sign_line);
        recyclerView = findViewById(R.id.revise_recycle_view);

        photoList = diary_db.getPhotoList();
        if(photoList == null){
            photoList = new ArrayList<>();
        }
        adapter = new PhotoListAdapter(this,photoList,2);

        addPhoto.setOnClickListener(this);
        toolbarLeft.setOnClickListener(this);
        toolbarRight.setOnClickListener(this);

        //设置状态栏
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setStatusBarColor(Color.TRANSPARENT);

        //设置标题栏
        toolbarLeft.setImageResource(R.mipmap.back_logo);
        toolbarMidle.setTextSize(18);
        toolbarMidle.setText(diary_db.getYearAndMonth()+diary_db.getDay()+"日");
        toolbarRight.setText("完成");

        //初始化背景
        GradientDrawable addPhotoBg = new GradientDrawable();
        addPhotoBg.setColor(Color.WHITE);
        addPhotoBg.setCornerRadius(40);
        addPhoto.setBackground(addPhotoBg);

        GradientDrawable parentBackground =
                new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP
                        , EmotionUtil.GetColors(emotionValue));
        parentLayout.setBackground(parentBackground);

        //初始化日记内容
        writeDiary.setText(diary_db.getDiaryContent());
        if(diary_db.getDiaryContent() == null || diary_db.getDiaryContent().equals("")){
            reviseSignLine.setVisibility(View.VISIBLE);
        }else {
            reviseSignLine.setVisibility(View.GONE);
        }
        //初始化图片
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SpacesItemDecoration(1,30));
        recyclerView.setAdapter(adapter);

    }
    private void showPhoto(){
        Log.d("图片测试",""+"显示图片" );
        adapter.setPhotoPathList(photoList);
        adapter.notifyDataSetChanged();
    }
}
