package com.example.a73233.carefree.Diary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.example.a73233.carefree.Util.SpacesItemDecoration;
import com.example.a73233.carefree.db.Diary_db;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DiaryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DiaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiaryFragment extends Fragment implements View.OnClickListener {
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


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    public DiaryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiaryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiaryFragment newInstance(String param1, String param2) {
        DiaryFragment fragment = new DiaryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

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
                    Log.d("日历测试","dhwguy");
                    calendarView.setVisibility(View.GONE);
                }else {
                    Log.d("日历测试","0000");
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
                Intent intent = new Intent(activity,LookDiaryActivity.class);
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

    // TODO: Rename method, update argument and hook method into UI event
   /* public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        activity.finish();
    }
}
