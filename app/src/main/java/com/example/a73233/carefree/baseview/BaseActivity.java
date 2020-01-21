package com.example.a73233.carefree.baseview;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {
    public final static String TAG = "活动测试";
    public String ACTIVITY_NAME = getClass().getSimpleName();

    public static final int TRANSPARENT_WHITE = 1;
    public static final int TRANSPARENT_BLACK = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,ACTIVITY_NAME+"-->onCreate()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,ACTIVITY_NAME+"-->onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,ACTIVITY_NAME+"-->onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,ACTIVITY_NAME+"-->onDestroy()");
    }

    /**
     * 修改状态栏
     * @param type
     */
    public void ReviseStatusBar(int type){
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.TRANSPARENT);
       if(type == TRANSPARENT_BLACK){
           window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
                   View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
       }else if(type == TRANSPARENT_WHITE){
           window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
                   View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
       }
    }

    /**
     * 界面跳转
     * @param clazz
     */
    public void startActivity(Class<?> clazz){
        Intent intent  = new Intent(this,clazz);
        startActivity(intent);
    }

    /**
     * 携带参数跳转
     * @param clazz
     * @param bundle
     */
    public void startActivity(Class<?> clazz,Bundle bundle){
        Intent intent  = new Intent(this,clazz);
        if(bundle != null){
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


    /**
     * 携带动画跳转
     * @param clazz
     * @param options
     */
    public void startActivityWithOptions(Class<?> clazz,Bundle options){
        Intent intent  = new Intent(this,clazz);
        if(options != null){
            startActivity(intent,options);
        }
    }

    /**
     * 携带参数以及动画跳转
     */
    public void startActivityWithOptions(Class<?> clazz,Bundle bundle,Bundle options){
        Intent intent  = new Intent(this,clazz);
        if(bundle != null){
            intent.putExtras(bundle);
        }
        if(options != null){
            startActivity(intent,options);
        }
    }

    /**
     * 包含回调函数跳转
     * @param clazz
     * @param requestCode
     */
    public void startActivityForResult(Class<?> clazz, int requestCode){
        Intent intent  = new Intent(this,clazz);
        startActivityForResult(intent,requestCode);
    }

    /**
     * 包含回调函数带参数跳转
     * @param clazz
     * @param bundle
     * @param requestCode
     */
    public void startActivityForResult(Class<?> clazz, Bundle bundle,int requestCode){
        Intent intent  = new Intent(this,clazz);
        if(bundle != null){
            intent.putExtras(bundle);
        }
        startActivityForResult(intent,requestCode);
    }

    /**
     * 包含回调函数带动画跳转
     * @param clazz
     * @param options
     */
    public void startActivityForResultWithOptions(Class<?> clazz,int requestCode,Bundle options){
        Intent intent  = new Intent(this,clazz);
        if(options != null){
            startActivityForResult(intent,requestCode,options);
        }
    }

    /**
     * 包含回调函数带参数以及动画跳转
     * @param clazz
     * @param bundle
     * @param requestCode
     * @param options
     */
    public void startActivityForResultWithOptions(Class<?> clazz, Bundle bundle,int requestCode,Bundle options){
        Intent intent  = new Intent(this,clazz);
        if(bundle != null){
            intent.putExtras(bundle);
        }
        if(options != null){
            startActivityForResult(intent,requestCode,options);
        }
    }

    /**
     * 展示长时Toast
     * @param text
     */
    public void showToast(String text){
        Toast.makeText(this,text,Toast.LENGTH_LONG).show();

    }

}
