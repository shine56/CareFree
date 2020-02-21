package com.example.a73233.carefree.baseView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class BaseFragment extends Fragment {
    public final static String TAG = "碎片测试";
    public String FRAGMENT_NAME = getClass().getSimpleName();
    public Activity activity = getActivity();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,FRAGMENT_NAME+"-->onCreate()");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,FRAGMENT_NAME+"-->onCreateView()");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG,FRAGMENT_NAME+"-->onActivityCreated()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,FRAGMENT_NAME+"-->onResume()");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,FRAGMENT_NAME+"-->onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,FRAGMENT_NAME+"-->onStop()");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            Log.d(TAG,FRAGMENT_NAME+"-->onHiddenChanged()-->隐藏");
        }else {
            Log.d(TAG,FRAGMENT_NAME+"-->onHiddenChanged()-->显示");
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,FRAGMENT_NAME+"-->onDestroy()");
        getActivity().finish();
    }

    /**
     * 展示短时Toast
     * @param text
     */
    public void showToast(String text){
        Toast.makeText(activity,text,Toast.LENGTH_SHORT).show();
    }


    /**
     * 直接跳转
     * @param clazz
     */
    public void startActivity(Class<?> clazz){
        Intent intent  = new Intent(activity,clazz);
        startActivity(intent);
    }
    /**
     * 携带参数跳转
     * @param clazz
     * @param bundle
     */
    public void startActivity(Class<?> clazz,Bundle bundle){
        Intent intent  = new Intent(activity,clazz);
        if(bundle != null){
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


    /**
     * 打印信息
     * @param text
     */
    public void logD(String text){
        Log.d(TAG,FRAGMENT_NAME+"-->"+text);
    }
}
