package com.example.a73233.carefree;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.a73233.carefree.Note.NoteFragment;
import com.example.a73233.carefree.My.MeFragment;
import com.example.a73233.carefree.Diary.DiaryFragment;
import com.example.a73233.carefree.Home.HomeFragment;
import com.example.a73233.carefree.db.Diary_db;

import org.litepal.LitePal;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private HomeFragment homeFragment;
    private DiaryFragment diaryFragment;
    private NoteFragment noteFragment;
    private MeFragment meFragment;
    private LinearLayout HomeButtom;
    private LinearLayout DiaryButtom;
    private LinearLayout NoteButtom;
    private LinearLayout MeButtom;
    private ImageView homeLogo;
    private ImageView diaryLogo;
    private ImageView noteLogo;
    private ImageView meLogo;

    private int FragmentID = 1;
    private static final int HomePage = 1;
    private static final int DiaryPage = 2;
    private static final int NotePage = 3;
    private static final int MePage = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

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

        //初始化碎片
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
                homeLogo.setImageResource(R.drawable.home_logo);
                break;
            case DiaryPage:
                diaryLogo.setImageResource(R.drawable.diary_logo);
                break;
            case NotePage:
                noteLogo.setImageResource(R.drawable.note_logo);
                break;
            case MePage:
                meLogo.setImageResource(R.drawable.me_logo);
                break;
        }
        switch (ChickFragmentID){
            case HomePage:
                homeLogo.setImageResource(R.drawable.home_logo_chick);
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
                diaryLogo.setImageResource(R.drawable.diary_logo_chick);
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
                noteLogo.setImageResource(R.drawable.note_logo_chick);
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
                meLogo.setImageResource(R.drawable.me_logo_chick);
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
    /*
       初始化界面
     */
    private void  initView(){
        //修改状态栏
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        HomeButtom = findViewById(R.id.button_home);
        DiaryButtom = findViewById(R.id.button_diary);
        NoteButtom = findViewById(R.id.button_note);
        MeButtom = findViewById(R.id.button_me);
        homeLogo = findViewById(R.id.home_logo);
        homeLogo.setImageResource(R.drawable.home_logo_chick);
        diaryLogo = findViewById(R.id.diary_logo);
        noteLogo = findViewById(R.id.note_logo);
        meLogo = findViewById(R.id.me_logo);

        HomeButtom.setOnClickListener(this);
        DiaryButtom.setOnClickListener(this);
        NoteButtom.setOnClickListener(this);
        MeButtom.setOnClickListener(this);

        homeFragment = new HomeFragment();
        diaryFragment = new DiaryFragment();
        noteFragment = new NoteFragment();
        meFragment = new MeFragment();
    }
}
