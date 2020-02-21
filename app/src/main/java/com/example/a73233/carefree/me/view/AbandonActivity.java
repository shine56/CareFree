package com.example.a73233.carefree.me.view;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a73233.carefree.R;
import com.example.a73233.carefree.baseView.BaseActivity;
import com.example.a73233.carefree.diary.view.DiaryListAdapter_;
import com.example.a73233.carefree.me.viewModel.AbandonVM;
import com.example.a73233.carefree.note.view.NoteListAdapter;
import com.example.a73233.carefree.util.SpacesItemDecoration;

public class AbandonActivity extends BaseActivity {
    private AbandonVM abandonVM;
    private DiaryListAdapter_ diaryAdapter;
    private NoteListAdapter noteAdapter;

    private RecyclerView diaryRecy;
    private RecyclerView noteRecy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abandon);
        diaryAdapter = new DiaryListAdapter_(this);
        noteAdapter = new NoteListAdapter(this);
        abandonVM = new AbandonVM(diaryAdapter,noteAdapter);
        initView();
    }
    private void initView(){
        diaryRecy = findViewById(R.id.ab_diary_recy);
        noteRecy = findViewById(R.id.ab_note_recy);
        ReviseStatusBar(TRANSPARENT_BLACK);
        initRecy();
        abandonVM.refreshView();
        showToast("点击选择恢复.删除");

        TextView textView = findViewById(R.id.abandon_delete_all);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDelete();
            }
        });
    }

    private void initRecy(){
        LinearLayoutManager layoutManager_1 = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager_2 = new LinearLayoutManager(this);
        int space = 50; //间距

        diaryRecy.setLayoutManager(layoutManager_1);
        diaryRecy.addItemDecoration(new SpacesItemDecoration(0,space));
        diaryRecy.setAdapter(diaryAdapter);

        noteRecy.setLayoutManager(layoutManager_2);
        noteRecy.addItemDecoration(new SpacesItemDecoration(0,space));
        noteRecy.setAdapter(noteAdapter);

        diaryAdapter.setItemClick(new DiaryListAdapter_.ItemClickImpl(){

            @Override
            public void onClick(View view, int id, int position, String text) {
                showAlertDialog(0, id,position,text);
            }
        });
        noteAdapter.setItemClick(new NoteListAdapter.ItemClickImpl(){
            @Override
            public void onClick(View view, int id, int position, String text) {
                showAlertDialog(1, id,position,text);
            }
        });
    }
    private void showAlertDialog(int type, int id,int position, String text){
        String[] items = {"恢复","清除"};
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(text)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0 :
                                abandonVM.recoveryData(type,id,position);
                                showToast("恢复成功");
                                break;
                            case 1 :
                                abandonVM.deleteOneData(type,id,position);
                                showToast("删除成功");
                                break;
                        }
                    }
                }).create();
        alertDialog.show();
    }
    private void confirmDelete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确认清空废纸篓吗");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                abandonVM.deleteAllData();
            }
        });
        builder.create().show();

    }
}
