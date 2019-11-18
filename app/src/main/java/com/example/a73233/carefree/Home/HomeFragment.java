package com.example.a73233.carefree.Home;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.example.a73233.carefree.MainActivity;
import com.example.a73233.carefree.R;
import com.example.a73233.carefree.Util.EmotionUtil;
import com.example.a73233.carefree.Util.TimeUtil;
import com.example.a73233.carefree.db.Diary_db;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class HomeFragment extends Fragment {
    private MoodView moodView;
    private EmotionReportView reportView;
    private TextView leftTitle;
    private Activity  activity;
    private TextView emotionValue;
    private TextView reportSuggest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_home, container, false);
        moodView =view.findViewById(R.id.mood_view);
        reportView = view.findViewById(R.id.emotion_report);
        leftTitle = view.findViewById(R.id.toolbar_left);
        emotionValue = view.findViewById(R.id.mood_value_right);
        reportSuggest = view.findViewById(R.id.report_text);
        activity = getActivity();

        leftTitle.setText("首页");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initMoodView();
        initReportView();
    }

    private void initMoodView(){
        Diary_db diaryDb = LitePal.findLast(Diary_db.class);
        if(diaryDb != null){
            int value = diaryDb.getEmotionValue();
            emotionValue.setText(""+value);
            moodView.setCurrentValue(value);
            moodView.setmPointColor(EmotionUtil.GetColors(value)[0]);
            if(value>15 && value<=50){
                moodView.setBackgroundResource(R.drawable.mood_view_happy_bg);
            }else if(value>-10 && value<=15){
                moodView.setBackgroundResource(R.drawable.mood_view_calm_bg);
            }else if(value>-30 && value<=-10){
                moodView.setBackgroundResource(R.drawable.mood_view_sad_bg);
            }else if(value>=-50 && value<=-30){
                moodView.setBackgroundResource(R.drawable.mood_view_repression_bg);
            }
        }

    }
    private void initReportView(){
        //初始化日期和情绪条
        //获取今天的日期
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_MONTH);
        //找出距离今天最近七天的日记,同属一天的日记取平均值
        int i = 0;  //记录天数
        int j = 0;  //记录日记条数
        int day = today;  //记录日期
        String dayString;
        int sameDay = 0;
        int value = 0;
        int[] DayValue = {0,0,0,0,0,0,0};
        String[] DayNum = {"01","02","03","04","05","06","07"};
        List<Diary_db> diaryDbs = LitePal.findAll(Diary_db.class);

        if (diaryDbs.size() == 0){
            Log.d("初始化情绪报表测试","数据库没有数据" );
            for(i=6; i>-1; i--){
                if(day<10)
                    dayString = "0"+day;
                else
                    dayString = ""+day;
                DayNum[i] = dayString;
                day--;
            }
            reportView.setDayValue(DayValue);
            return;
        }
        Diary_db db = diaryDbs.get((diaryDbs.size() - 1) -j);
        j++; sameDay++; value = db.getEmotionValue();
        while(i<7 && j<diaryDbs.size()){
            if(day<10)
                dayString = "0"+day;
            else
                dayString = ""+day;
            if(day <=0){
                dayString = ""+(TimeUtil.GetDayByMonth(day));
            }

            //当前日期等于数据库日期
            if(db.getDay().equals(dayString)){
                //当前日期也等于数据库下一张表的日期
                Diary_db nextDb = diaryDbs.get((diaryDbs.size() - 1) -j);
                if(nextDb!=null && nextDb.getDay().equals(dayString)){
                    Log.d("初始化情绪报表测试","下一条数据日期相同");
                    db = diaryDbs.get((diaryDbs.size() - 1) -j);
                    j++;
                    value += db.getEmotionValue();
                    sameDay++;
                    if(j == diaryDbs.size()){
                        DayNum[6-i] = dayString;
                        if(value == 0) value = 1;
                        DayValue[6-i] = value/sameDay;
                        Log.d("初始化情绪报表测试","数据库最后一条数据了");
                    }

                }else {
                    DayNum[6-i] = dayString;
                    if(value == 0) value = 1;
                    DayValue[6-i] = value/sameDay;
                    Log.d("初始化情绪报表测试","这天有数据"+DayValue[6-i]);
                    day--;
                    i++;
                    db = diaryDbs.get((diaryDbs.size() - 1) -j);
                    j++;
                    value = db.getEmotionValue();
                    sameDay = 1;
                }
            }else {
                DayNum[6-i] = dayString;
                DayValue[6-i] = 0;
                Log.d("初始化情绪报表测试","这天没数据"+DayValue[6-i]);
                day--;
                i++;
            }
        }
        //传入日期，平均值，对应的颜色
        int[] DayColor = {0XFF38D5D6,0XFF64B0E8,0XFF9B85FF,0XFF38D5D6,0XFF2B5876,0XFF38D5D6,0XFF9B85FF};
        for(i=6; i>-1; i--){
            DayColor[i] = EmotionUtil.GetColors(DayValue[i])[1];
        }
        reportView.setDayNum(DayNum);
        reportView.setDayValue(DayValue);
        reportView.setDayColor(DayColor);
        //初始化建议
        reportSuggest.setText(EmotionUtil.GetSuggestion(DayValue));
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        activity.finish();
    }
}
