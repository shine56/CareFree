package com.example.a73233.carefree.diary.view;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a73233.carefree.R;
import com.example.a73233.carefree.baseView.BaseFragment;
import com.example.a73233.carefree.databinding.FragmentDiaryBinding;
import com.example.a73233.carefree.diary.viewModel.DiaryVM;
import com.example.a73233.carefree.util.CustomItemTouchCallback;
import com.example.a73233.carefree.util.SpacesItemDecoration;

public class DiaryFragment extends BaseFragment{
    private DiaryListAdapter_ adapter;
    private FragmentDiaryBinding binding;
    private DiaryVM diaryVM;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_diary,container,false);
        binding.setDiaryFragment(this);
        activity = getActivity();
        adapter = new DiaryListAdapter_(activity);
        diaryVM = new DiaryVM(adapter, activity);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.diaryToolbar.toolbarLeft.setText("日记");
        initAddCard();
        initRecyclerView();

        //监听搜索框内容变化
        binding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchText = binding.searchEditText.getText().toString();
                diaryVM.refreshDiaryList(searchText);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initAddCard();
        binding.diaryRecycleView.setAdapter(adapter);
        diaryVM.refreshDiaryList();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            initAddCard();
            binding.diaryRecycleView.setAdapter(adapter);
            diaryVM.refreshDiaryList();
        }
    }

    private void initAddCard(){
        binding.setBean(diaryVM.refreshBeanDate());
    }
    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        binding.diaryRecycleView.setLayoutManager(layoutManager);
        int space = 50; //间距
        binding.diaryRecycleView.addItemDecoration(new SpacesItemDecoration(0,space));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new CustomItemTouchCallback(adapter));
        itemTouchHelper.attachToRecyclerView(binding.diaryRecycleView);

        adapter.setItemClick(new DiaryListAdapter_.ItemClickImpl(){
            @Override
            public void onClick(View view, int id, int position, String text) {
                switch (view.getId()){
                    case R.id.diary_list_root:
                        Bundle bundle = new Bundle();
                        bundle.putInt("diaryId",id);
                        startActivity(LookDiaryActivity.class,bundle);
                        break;
                    case R.id.diary_list_abandon:
                        diaryVM.abandonDiary(id);
                        break;
                }
            }
        });
    }
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putInt("diaryId",-1);
        switch (v.getId()){
            case R.id.add_ai :
                bundle.putInt("addType",1);
                startActivity(WriteDiaryActivity.class,bundle);
                break;
            case R.id.add_happy :
                bundle.putInt("addType",2);
                startActivity(WriteDiaryActivity.class,bundle);
                break;
            case R.id.add_calm :
                bundle.putInt("addType",3);
                startActivity(WriteDiaryActivity.class,bundle);
                break;
            case R.id.add_sad :
                bundle.putInt("addType",4);
                startActivity(WriteDiaryActivity.class,bundle);
                break;
            case R.id.add_repression :
                bundle.putInt("addType",5);
                startActivity(WriteDiaryActivity.class,bundle);
                break;
            case R.id.search_diary :
                String searchText = binding.searchEditText.getText().toString();
                diaryVM.refreshDiaryList(searchText);
                showToast("找到以上结果");
                break;
        }
    }

}
