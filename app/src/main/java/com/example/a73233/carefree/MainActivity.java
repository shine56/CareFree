package com.example.a73233.carefree;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.a73233.carefree.baseview.BaseActivity;
import com.example.a73233.carefree.bean.Diary_db;
import com.example.a73233.carefree.bean.Note_db;
import com.example.a73233.carefree.bean.User_db;
import com.example.a73233.carefree.note.view.NoteFragment;
import com.example.a73233.carefree.me.view.MeFragment;
import com.example.a73233.carefree.diary.view.DiaryFragment;
import com.example.a73233.carefree.home.view.HomeFragment;
import com.example.a73233.carefree.databinding.ActivityMainBinding;


public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;

    private HomeFragment homeFragment;
    private DiaryFragment diaryFragment;
    private NoteFragment noteFragment;
    private MeFragment meFragment;

    private int FragmentID = 1;
    private static final int HomePage = 1;
    private static final int DiaryPage = 2;
    private static final int NotePage = 3;
    private static final int MePage = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        binding.setMainActivity(this);
        initApp();
       // LitePal.deleteAll(Diary_db.class);
       // LitePal.deleteAll(Note_db.class);
        //initDb();
        //initNoteDb();
        //initMeDb();
    }
    private void initMeDb(){
        User_db db = new User_db();
        db.setUserName("Shine56");
        db.setUserWords("世界和平、无灾、无难。");
        db.save();
    }
    private void initDiaryDb(){
        for(int i=15; i<21; i++){
            Diary_db db = new Diary_db();
            db.setDay(""+i);
            db.setDiaryContent("第"+i);
            db.setEmotionValue(i);
            db.setWeek("周三");
            db.setYearAndMonth("2020年01月");
            db.save();
        }
    }
    private void initNoteDb(){
        for(int i=0; i<4; i++){
            Note_db db = new Note_db();
            db.setIsAbandon(0);
            db.setMonthAndDay("1月27");
            db.setRank(i);
            db.setText("今天真开心");
            db.setTime("18:11");
            db.setWeek("星期三");
            db.save();
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(MainActivity.this,"获取权限被拒绝",Toast.LENGTH_LONG).show();
            Log.d("获取权限测试","获取权限被拒绝");
        }
        /*switch (requestCode){
            case 1 :
                if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(MainActivity.this,"获取权限被拒绝",Toast.LENGTH_LONG).show();
                    Log.d("获取权限测试","获取权限被拒绝");
                }
                break;
            case 2:
                if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(MainActivity.this,"获取权限被拒绝",Toast.LENGTH_LONG).show();
                    Log.d("获取权限测试","获取权限被拒绝");
                }
                break;
        }*/

    }
    private void  initApp(){
        ReviseStatusBar(TRANSPARENT_BLACK);
        //初始化底部导航栏
        binding.homeLogo.setImageResource(R.mipmap.home_click);
        //申请权限
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA},2);
        }
        //实例化碎片
        homeFragment = new HomeFragment();
        diaryFragment = new DiaryFragment();
        noteFragment = new NoteFragment();
        meFragment = new MeFragment();

        //初始化碎片管理器
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_container,diaryFragment,"diaryFragment");
        transaction.add(R.id.fragment_container, homeFragment,"homeFragment");
        transaction.add(R.id.fragment_container,noteFragment,"noteFragment");
        transaction.add(R.id.fragment_container,meFragment,"meFragment");
        transaction.addToBackStack("add allFragment");
        transaction.hide(diaryFragment);
        transaction.hide(noteFragment);
        transaction.hide(meFragment);
        transaction.commit();
    }
}
