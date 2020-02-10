package com.example.a73233.carefree.note.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a73233.carefree.R;
import com.example.a73233.carefree.baseview.BaseFragment;
import com.example.a73233.carefree.databinding.FragmentNoteBinding;
import com.example.a73233.carefree.note.viewModel.NoteVM;
import com.example.a73233.carefree.util.CustomItemTouchCallback;
import com.example.a73233.carefree.util.SpacesItemDecoration;


public class NoteFragment extends BaseFragment {
    private FragmentNoteBinding binding;
    private NoteListAdapter adapter;
    private NoteVM noteVM;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_note,container,false);
        binding.setNoteFragment(this);
        activity = getActivity();
        adapter = new NoteListAdapter(activity);
        noteVM = new NoteVM(adapter);
        logD("这里");
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化recy
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        binding.noteRecy.setLayoutManager(layoutManager);
        int space = 50; //间距
        binding.noteRecy.addItemDecoration(new SpacesItemDecoration(0,space));
        binding.noteRecy.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new CustomItemTouchCallback(adapter));
        itemTouchHelper.attachToRecyclerView(binding.noteRecy);
        noteVM.refreshAllData();

        adapter.setItemClick(new NoteListAdapter.ItemClickImpl(){
            @Override
            public void onClick(View view,int id) {
                switch (view.getId()){
                    case R.id.note_body:
                        Bundle bundle = new Bundle();
                        bundle.putInt("noteId",id);
                        startActivity(NoteWriteActivity.class,bundle);
                        break;
                    case R.id.note_remove:
                        noteVM.deleteData(id);
                        break;
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        noteVM.refreshAllData();
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.add_note:
                Bundle bundle = new Bundle();
                bundle.putInt("noteId",-1);
                startActivity(NoteWriteActivity.class,bundle);
                break;
        }
    }
}
