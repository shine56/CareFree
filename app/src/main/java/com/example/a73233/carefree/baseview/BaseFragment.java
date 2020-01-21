package com.example.a73233.carefree.baseview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BaseFragment extends Fragment {
    public final static String TAG = "碎片测试";
    public String FRAGMENT_NAME = getClass().getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,FRAGMENT_NAME);
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
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d(TAG,FRAGMENT_NAME+"-->onHiddenChanged()");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,FRAGMENT_NAME+"-->onDestroy()");
        getActivity().finish();
    }
}
