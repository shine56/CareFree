package com.example.a73233.carefree.Diary;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.a73233.carefree.R;
import com.example.a73233.carefree.Util.EmotionUtil;
import com.example.a73233.carefree.Util.SpacesItemDecoration;
import com.example.a73233.carefree.db.Diary_db;

import org.litepal.LitePal;

public class LookDiaryActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView bgView;
    private TextView emotionValue;
    private ImageView toolbarLeft;
    private TextView toolbarRight;
    private int diaryId;
    private TextView diaryContent;
    private TextView day;
    private TextView yearAndMonth;
    private TextView week;
    private RecyclerView recyclerView;
    private ImageView showPhoto;
    private CollapsingToolbarLayout colToolbar;

    private int startActivityFlag = 0;
    private int bgViewHeight = 0;
    private float startY = 0;
    private Boolean recycleViewIsVisit = false;
    private Diary_db diary_db;
    private int value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_diary);

        Intent intent = getIntent();
        diaryId = intent.getIntExtra("diaryId",0);
        diary_db = LitePal.find(Diary_db.class,diaryId);
        if(diary_db != null){
            value = diary_db.getEmotionValue();
            initView();
        }else {
            Toast.makeText(this,"打开日记失败",Toast.LENGTH_LONG).show();
            finish();
        }

    }

    @Override
    protected void onRestart() {
        startActivityFlag = 0;
        super.onRestart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.showPhoto :
                if(recycleViewIsVisit){
                    recyclerView.setVisibility(View.GONE);
                    recycleViewIsVisit = false;
                }else {
                    recyclerView.setVisibility(View.VISIBLE);
                    recycleViewIsVisit = true;
                }
                break;
            case R.id.toolbar_left2:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.toolbar_right2:
                Intent intent = new Intent(LookDiaryActivity.this,
                        ReviseDiaryActivity.class);
                intent.putExtra("diaryId",diary_db.getId());

                startActivityForResult(intent, 1,
                        ActivityOptions.makeSceneTransitionAnimation(LookDiaryActivity.this,bgView
                                , "sharedView").toBundle());
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        bgViewHeight = bgView.getHeight();
    }
    //监控手指滑动实现下拉修改
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float y2 = 0;
        if (ev.getAction() == MotionEvent.ACTION_DOWN){
            startY = ev.getY();
        }

        Log.d("滑动测试","y1 = "+startY+"bg高度="+bgViewHeight);
        if(startY > bgViewHeight-300 && startY<bgViewHeight+50){
            y2 = ev.getY();
            Log.d("滑动测试","y2 = "+y2);
            if( startActivityFlag == 0&& y2-startY >0){
                Intent intent = new Intent(LookDiaryActivity.this,
                        ReviseDiaryActivity.class);
                intent.putExtra("diaryId",diary_db.getId());

                startActivityForResult(intent, 1,
                        ActivityOptions.makeSceneTransitionAnimation(LookDiaryActivity.this,bgView
                                , "sharedView").toBundle());
                startActivityFlag++;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    diary_db = LitePal.find(Diary_db.class,diaryId);
                    value = diary_db.getEmotionValue();
                    initView();
                }
                break;
        }
    }

    private void initView(){
        bgView = findViewById(R.id.bg_view);
        emotionValue = findViewById(R.id.look_emotion_value);
        toolbarLeft = findViewById(R.id.toolbar_left2);
        toolbarRight = findViewById(R.id.toolbar_right2);
        diaryContent = findViewById(R.id.textView9);
        day = findViewById(R.id.look_day);
        yearAndMonth = findViewById(R.id.look_year_month);
        week = findViewById(R.id.look_week);
        recyclerView = findViewById(R.id.look_recyclerView);
        showPhoto = findViewById(R.id.showPhoto);
        //toolbar = findViewById(R.id.look_toolbar);
        colToolbar = findViewById(R.id.col_toolbar);

        toolbarLeft.setOnClickListener(this);
        toolbarRight.setOnClickListener(this);

        //标题栏
        toolbarLeft.setImageResource(R.mipmap.back_logo);
        toolbarRight.setText("下滑修改");

        //设置日记垂直滑动
        diaryContent.setMovementMethod(ScrollingMovementMethod.getInstance());

        //设置状态栏
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setStatusBarColor(Color.TRANSPARENT);

        //设置控件背景
        //recyclerView
        GradientDrawable recycleBg = new GradientDrawable();
        recycleBg.setColor(Color.WHITE);
        recycleBg.setCornerRadius(50);
        recyclerView.setBackground(recycleBg);
        //bgView
        GradientDrawable bgViewBg = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP
                , EmotionUtil.GetColors(value));
        bgViewBg.setCornerRadius(50);
        bgView.setBackground(bgViewBg);
        //emotionValueBg
        GradientDrawable emotionValueBg = new GradientDrawable();
        emotionValueBg.setCornerRadius(50);
        emotionValueBg.setColor(EmotionUtil.GetColors(value)[1]);
        emotionValue.setBackground(emotionValueBg);
        //折叠后背景
        GradientDrawable colBg = new GradientDrawable();
        colBg.setColor(EmotionUtil.GetColors(value)[1]);
        colToolbar.setContentScrim(colBg);

        //初始化情绪值
        emotionValue.setText("情绪值: "+value);

        //初始化日记信息，日期，内容等
        day.setText(diary_db.getDay());
        yearAndMonth.setText(diary_db.getYearAndMonth());
        week.setText(diary_db.getWeek());
        diaryContent.setText(diary_db.getDiaryContent());

        //初始化图片
        if(diary_db.getPhotoList()!=null){
            showPhoto.setVisibility(View.VISIBLE);
            showPhoto.setOnClickListener(this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.addItemDecoration(new SpacesItemDecoration(1,30));
            PhotoListAdapter adapter = new PhotoListAdapter(this,diary_db.getPhotoList(),1);
            recyclerView.setAdapter(adapter);
        }else {
            showPhoto.setVisibility(View.GONE);
        }
    }
}
