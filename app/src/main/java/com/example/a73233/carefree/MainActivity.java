/**
 * 名称：CareFree-无忧日记
 * 作者：shine56
 * 日期：2020年2月12日
 */


package com.example.a73233.carefree;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.a73233.carefree.baseView.BaseActivity;
import com.example.a73233.carefree.baseView.FingerprintImpl;
import com.example.a73233.carefree.bean.Diary_db;
import com.example.a73233.carefree.bean.Note_db;
import com.example.a73233.carefree.bean.Users_db;
import com.example.a73233.carefree.note.view.NoteFragment;
import com.example.a73233.carefree.me.view.MeFragment;
import com.example.a73233.carefree.diary.view.DiaryFragment;
import com.example.a73233.carefree.home.view.HomeFragment;
import com.example.a73233.carefree.databinding.ActivityMainBinding;
import com.example.a73233.carefree.util.ConstantPool;
import com.example.a73233.carefree.util.FileUtil;
import com.example.a73233.carefree.util.FingerDiscentListener;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends BaseActivity implements FingerprintImpl {
    private  Dialog dialog;

    private static final int HomePage = 1;
    private static final int DiaryPage = 2;
    private static final int NotePage = 3;
    private static final int MePage = 4;

    private ActivityMainBinding binding;
    private HomeFragment homeFragment;
    private DiaryFragment diaryFragment;
    private NoteFragment noteFragment;
    private MeFragment meFragment;

    private int FragmentID = 1;
    private Boolean isFragmentCreate = false;

    private FingerprintManagerCompat fingerprintManagerCompat;
    //用于取消指纹识别器监听的对象
    private CancellationSignal cancellationSignal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        binding.setMainActivity(this);

        SharedPreferences pref = getSharedPreferences("setting",MODE_PRIVATE);
        //判断是否第一次安装启动app
        if(pref.getBoolean("isFirstStartApp",true)){
            initApp();
            initView();
        }else {
            if(pref.getString("lock","关").equals("开")){
                fingerprintManagerCompat = FingerprintManagerCompat.from(this);
                if(fingerprintManagerCompat.hasEnrolledFingerprints()){
                    cancellationSignal = new CancellationSignal();
                    fingerprintManagerCompat.authenticate(null, 0, cancellationSignal,
                            new FingerDiscentListener(cancellationSignal,this), null);
                    showFigerPrintDiglog();
                }else {
                    showToast("手机未录入指纹，指纹锁暂时失效");
                    initView();
                }
            }else {
                initView();
            }
        }
    }
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_home:
                if(FragmentID != HomePage){
                    ShowFragment(HomePage);
                }
                break;
            case R.id.button_diary:
                if(FragmentID != DiaryPage){
                    ShowFragment(DiaryPage);
                }
                break;
            case R.id.button_note:
                if(FragmentID != NotePage){
                    ShowFragment(NotePage);
                }
                break;
            case R.id.button_me:
                if(FragmentID != MePage){
                    ShowFragment(MePage);
                }
                break;
        }
    }
    private void ShowFragment(int ChickFragmentID){
        switch (FragmentID){
            case HomePage:
                binding.homeLogo.setImageResource(R.mipmap.home_gray);
                break;
            case DiaryPage:
                binding.diaryLogo.setImageResource(R.mipmap.diary_gray);
                break;
            case NotePage:
                binding.noteLogo.setImageResource(R.mipmap.note_gray);
                break;
            case MePage:
                binding.meLogo.setImageResource(R.mipmap.me_gray);
                break;
        }
        switch (ChickFragmentID){
            case HomePage:
                binding.homeLogo.setImageResource(R.mipmap.home_click);
                FragmentID = HomePage;

                FragmentManager fragmentManager1 = getSupportFragmentManager();
                FragmentTransaction transaction1 = fragmentManager1.beginTransaction();
                transaction1.hide(diaryFragment);
                transaction1.hide(noteFragment);
                transaction1.hide(meFragment);
                transaction1.show(homeFragment);
                transaction1.commit();
                break;
            case DiaryPage:
                binding.diaryLogo.setImageResource(R.mipmap.diary_click);
                FragmentID = DiaryPage;

                if(!isFragmentCreate){
                    createFragment();
                    isFragmentCreate = true;
                }
                FragmentManager fragmentManager2 = getSupportFragmentManager();
                FragmentTransaction transaction2 = fragmentManager2.beginTransaction();
                transaction2.hide(homeFragment);
                transaction2.hide(noteFragment);
                transaction2.hide(meFragment);
                transaction2.show(diaryFragment);
                transaction2.commit();
                break;
            case NotePage:
                binding.noteLogo.setImageResource(R.mipmap.note_click);
                FragmentID = NotePage;

                if(!isFragmentCreate){
                    createFragment();
                    isFragmentCreate = true;
                }
                FragmentManager fragmentManager3 = getSupportFragmentManager();
                FragmentTransaction transaction3 = fragmentManager3.beginTransaction();
                transaction3.hide(diaryFragment);
                transaction3.hide(homeFragment);
                transaction3.hide(meFragment);
                transaction3.show(noteFragment);
                transaction3.commit();
                break;
            case MePage:
                binding.meLogo.setImageResource(R.mipmap.me_logo_click);
                FragmentID = MePage;

                if(!isFragmentCreate){
                    createFragment();
                    isFragmentCreate = true;
                }
                FragmentManager fragmentManager4 = getSupportFragmentManager();
                FragmentTransaction transaction4 = fragmentManager4.beginTransaction();
                transaction4.hide(diaryFragment);
                transaction4.hide(noteFragment);
                transaction4.hide(homeFragment);
                transaction4.show(meFragment);
                transaction4.commit();
                break;
        }
    }
    private void createFragment(){
        diaryFragment = new DiaryFragment();
        noteFragment = new NoteFragment();
        meFragment = new MeFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_container,diaryFragment,"diaryFragment");
        transaction.add(R.id.fragment_container,noteFragment,"noteFragment");
        transaction.add(R.id.fragment_container,meFragment,"meFragment");
        transaction.commit();
        logD("创建碎片成功");
    }
    private void initView(){
        ReviseStatusBar(TRANSPARENT_BLACK);
        //初始化底部导航栏
        binding.homeLogo.setImageResource(R.mipmap.home_click);
        //初始化首页碎片
        homeFragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_container, homeFragment,"homeFragment");
        transaction.addToBackStack("add allFragment");
        transaction.commit();
    }
    private void initApp(){
        logD("首次启动，初始化app");
        FileUtil.createAppDirectory(this);
        LitePal.deleteAll(Diary_db.class);
        LitePal.deleteAll(Note_db.class);
        LitePal.deleteAll(Users_db.class);
        initSetting();
        initDiaryDb();
        initNoteDb();
        initMeDb();
        SharedPreferences.Editor editor = getSharedPreferences("setting",MODE_PRIVATE).edit();
        editor.putBoolean("isFirstStartApp",false);
        editor.apply();
    }
    private void initSetting(){
        SharedPreferences.Editor editor = getSharedPreferences("note_setting",MODE_PRIVATE).edit();
        editor.putString("clock_type","非系统闹钟");
        editor.putString("home_show_note","不显示任务");
        editor.putString("rank3_top","不置顶");
        editor.putString("home_show_emotion_value","显示当前情绪值");
        editor.apply();
    }
    private void initMeDb(){
        Users_db db = new Users_db();
        db.setUserName("CareFree");
        db.setUserWords("一切都是最好的安排！");
        db.save();
    }
    private void initDiaryDb(){
        Date date = new Date();
        String day = new SimpleDateFormat("dd").format(date);
        String yearAndMonth = new SimpleDateFormat("yyyy年MM月").format(date);
        String week = new SimpleDateFormat("EEEE").format(date);

        for(int i=0; i<2; i++){
            Diary_db db = new Diary_db();
            db.setIsAbandon(ConstantPool.NOT_ABANDON);
            db.setDay(day);
            db.setWeek(week);
            db.setYearAndMonth(yearAndMonth);

            if(i == 1){
                int id = R.drawable.user_head_img;
                Resources resources = getResources();
                String path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                        + resources.getResourcePackageName(id) + "/"
                        + resources.getResourceTypeName(id) + "/"
                        + resources.getResourceEntryName(id);
                List<String> photos = new ArrayList<>();
                photos.add(path);
                db.setPhotoList(photos);
                db.setDiaryContent("日记共四种心情选择，可记录下您写日记时的心情！添加新的日记会增加或减少能动值哦。保持好的心情，能动值越高！");
                db.setEmotionValue(28);
            }else {
                db.setDiaryContent("左滑删除日记");
                db.setEmotionValue(0);
            }
            db.save();
        }
    }
    private void initNoteDb(){
        Date date = new Date();
        String year = new SimpleDateFormat("yyyy").format(date);
        String monthAndDay = new SimpleDateFormat("MM月dd日").format(date);
        String week = new SimpleDateFormat("EEEE").format(date);
        String time = new SimpleDateFormat("HH:mm").format(date);

        for(int i=0; i<2; i++){
            Note_db db = new Note_db();
            db.setIsAbandon(ConstantPool.NOT_ABANDON);
            db.setMonthAndDay(monthAndDay);
            db.setRank(0);
            db.setTime(time);
            db.setWeek(week);
            db.setYear(year);

            if(i==1){
                db.setText("贴纸分为：临时记录贴和任务贴\n临时记录贴无法设置闹钟。\n\n任务贴必须设置闹钟，有三个级别，设定的级别越高，说明该任务越重要，完成任务时能动值加得越多。\n\n" +
                        "系统闹钟：设置系统闹钟即相当于添加了一个手机自带的闹钟，可前往手机闹钟程序查看。\n\n前往-->我的-->设置-->闹钟，选择是否使用系统闹钟");
            }else {
                db.setText("左滑便贴选择完成/未完成摘下便贴");
            }
            db.save();
        }
    }
    private void showFigerPrintDiglog(){
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_figerprint,null,false);
        dialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //宽度填满
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = (int)(this.getWindowManager().getDefaultDisplay().getHeight() *0.4);
        //将属性设置给窗体
        dialogWindow.setAttributes(lp);
        //不可被取消
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        //点击监控
        TextView textView = view.findViewById(R.id.dialog_fingerprint_cancel);
        textView.setOnClickListener(view1 -> {
            cancellationSignal.cancel();
            dialog.dismiss();
            finish();
        });
        dialog.show();
    }
    public void verifySuccess(){
        initView();
        dialog.dismiss();
    }
    public void verifyFaile(){
        showToast("验证失败");
        dialog.dismiss();
        finish();
    }
}
