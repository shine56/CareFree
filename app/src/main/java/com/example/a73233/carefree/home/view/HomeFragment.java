package com.example.a73233.carefree.home.view;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a73233.carefree.R;
import com.example.a73233.carefree.baseview.BaseFragment;
import com.example.a73233.carefree.databinding.FragmentHomeBinding;
import com.example.a73233.carefree.home.viewModel.HomeViewModel;

public class HomeFragment extends BaseFragment {
    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);
        viewModel = new HomeViewModel();
        binding.setHomeViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.homeToolbar.toolbarLeft.setText("首页");
        initMoodView();
        initReportView();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            initMoodView();
            initReportView();
        }
    }

    //初始化能动值卡片
    private void initMoodView(){
        viewModel.initEmotionValue();

        int value = viewModel.emotionValue.get();
        if(value>15 && value<=50){
            binding.homeMoodView.moodView.setBackgroundResource(R.drawable.mood_view_happy_bg);
        }else if(value>-10 && value<=15){
            binding.homeMoodView.moodView.setBackgroundResource(R.drawable.mood_view_calm_bg);
        }else if(value>-30 && value<=-10){
            binding.homeMoodView.moodView.setBackgroundResource(R.drawable.mood_view_sad_bg);
        }else if(value>=-50 && value<=-30){
            binding.homeMoodView.moodView.setBackgroundResource(R.drawable.mood_view_repression_bg);
        }
    }
    //初始化能动值报表
    private void initReportView(){
        viewModel.initReportViewData();
    }

}
