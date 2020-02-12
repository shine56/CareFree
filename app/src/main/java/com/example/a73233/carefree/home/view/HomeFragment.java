package com.example.a73233.carefree.home.view;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a73233.carefree.R;
import com.example.a73233.carefree.baseview.BaseFragment;
import com.example.a73233.carefree.databinding.FragmentHomeBinding;
import com.example.a73233.carefree.home.viewModel.HomeViewModel;
import com.example.a73233.carefree.note.view.NoteListAdapter;
import com.example.a73233.carefree.note.view.NoteWriteActivity;
import com.example.a73233.carefree.note.viewModel.NoteVM;
import com.example.a73233.carefree.util.SpacesItemDecoration;

public class HomeFragment extends BaseFragment {
    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private NoteListAdapter noteAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);
        activity = getActivity();
        noteAdapter = new NoteListAdapter(activity);
        viewModel = new HomeViewModel(noteAdapter);
        binding.setHomeViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.homeToolbar.toolbarLeft.setText("首页");
        initView();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            initMoodView();
            initReportView();
            viewModel.refreshNote();
        }
    }

    private void initView(){
        initMoodView();
        initReportView();
        initRecy();
    }
    private void initRecy(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        binding.homeNoteRecy.setLayoutManager(layoutManager);
        binding.homeNoteRecy.addItemDecoration(new SpacesItemDecoration(0,50));
        binding.homeNoteRecy.setAdapter(noteAdapter);
        viewModel.refreshNote();

        noteAdapter.setItemClick(new NoteListAdapter.ItemClickImpl(){
            @Override
            public void onClick(View view, int id, int position, String text) {
                switch (view.getId()){
                    case R.id.note_body:
                        Bundle bundle = new Bundle();
                        bundle.putInt("noteId",id);
                        startActivity(NoteWriteActivity.class,bundle);
                        break;
                }
            }
        });
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
