package com.example.a73233.carefree.diary.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a73233.carefree.MainActivity;
import com.example.a73233.carefree.R;
import com.example.a73233.carefree.baseview.BaseFragment;
import com.example.a73233.carefree.util.SpacesItemDecoration;
import com.example.a73233.carefree.bean.Diary_db;

import org.litepal.LitePal;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class DiaryFragment extends BaseFragment implements View.OnClickListener {
    private MainActivity activity;
    private TextView toolbarLeft;
    private ImageView toolbarRight;
    private RecyclerView recyclerView;
    private DiaryListAdapter adapter;
    private List<Diary_db> diary_dbs;
    private ImageView addAi;
    private ImageView addHappy;
    private ImageView addCalm;
    private ImageView addSad;
    private ImageView addRepression;
    private CalendarView calendarView;
    private ImageView searchDiary;
    private EditText searchEditText;

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(activity, WriteDiaryActivity.class);
        switch (v.getId()){
            case R.id.add_ai :
                intent.putExtra("addType",1);
                startActivityForResult(intent,1);
                break;
            case R.id.add_happy :
                intent.putExtra("addType",2);
                startActivityForResult(intent,1);
                break;
            case R.id.add_calm :
                intent.putExtra("addType",3);
                startActivityForResult(intent,1);
                break;
            case R.id.add_sad :
                intent.putExtra("addType",4);
                startActivityForResult(intent,1);
                break;
            case R.id.add_repression :
                intent.putExtra("addType",5);
                startActivityForResult(intent,1);
                break;
            case R.id.toolbar_right:
                if(calendarView.getVisibility() == View.VISIBLE){
                    calendarView.setVisibility(View.GONE);
                }else {
                    calendarView.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.search_diary :
                String searchText = searchEditText.getText().toString();
                diary_dbs = LitePal.where("diaryContent like ?","%"+searchText+"%")
                        .find(Diary_db.class);
                adapter.setData(diary_dbs);
                adapter.notifyDataSetChanged();
                Toast.makeText(activity,"找到如下结果",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary, container, false);
        addAi = view.findViewById(R.id.add_ai);
        addHappy = view.findViewById(R.id.add_happy);
        addCalm = view.findViewById(R.id.add_calm);
        addSad = view.findViewById(R.id.add_sad);
        addRepression = view.findViewById(R.id.add_repression);
        toolbarRight = view.findViewById(R.id.toolbar_right);
        toolbarLeft = view.findViewById(R.id.toolbar_left);
        recyclerView = view.findViewById(R.id.diary_recycle_view);
        calendarView = view.findViewById(R.id.calendar_view);
        searchDiary = view.findViewById(R.id.search_diary);
        searchEditText = view.findViewById(R.id.editText);

        activity = (MainActivity) getActivity();

        searchDiary.setOnClickListener(this);
        addAi.setOnClickListener(this);
        addHappy.setOnClickListener(this);
        addCalm.setOnClickListener(this);
        addSad.setOnClickListener(this);
        addRepression.setOnClickListener(this);
        toolbarRight.setOnClickListener(this);

        toolbarLeft.setText("日记");
        toolbarRight.setImageResource(R.mipmap.calendar_logo);
        calendarView.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        diary_dbs = LitePal.findAll(Diary_db.class);
        //设置recycleview,指定控件布局方式，将其设置到控件对象中
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DiaryListAdapter(activity);
        adapter.setData(diary_dbs);
        int space = 50; //间距
        recyclerView.addItemDecoration(new SpacesItemDecoration(0,space));
        recyclerView.setAdapter(adapter);

        //监听日记
        adapter.setOnitemClickLintener(new DiaryListAdapter.OnitemClick() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(activity, LookDiaryActivity.class);
                Diary_db diary_db = diary_dbs.get((diary_dbs.size()-1)-position);
                intent.putExtra("diaryId",diary_db.getId());
                startActivityForResult(intent, 2);
            }
        });
        //监听日历
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String yearAndMonth, day;
                if(month+1 < 10){
                    yearAndMonth = year+"年0"+(month+1)+"月";
                }else {
                    yearAndMonth = year+"年"+(month+1)+"月";
                }
                if(dayOfMonth<10){
                    day = "0"+dayOfMonth;
                }else {
                    day = dayOfMonth+"";
                }
                Log.d("日历测试",yearAndMonth);
                diary_dbs = LitePal.where("yearAndMonth like ? and day like ?"
                        , yearAndMonth, day)
                        .find(Diary_db.class);
                adapter.setData(diary_dbs);
                adapter.notifyDataSetChanged();
                Toast.makeText(activity,"找到如下结果",Toast.LENGTH_SHORT).show();
                calendarView.setVisibility(View.GONE);
            }
        });
        //监听搜索框内容变化
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchText = searchEditText.getText().toString();
                diary_dbs = LitePal.where("diaryContent like ?","%"+searchText+"%")
                        .find(Diary_db.class);
                adapter.setData(diary_dbs);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
            case 2:
                if(resultCode == RESULT_OK){
                    Log.d("刷新测试","");
                    diary_dbs = LitePal.findAll(Diary_db.class);
                    adapter.setData(diary_dbs);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }
}
