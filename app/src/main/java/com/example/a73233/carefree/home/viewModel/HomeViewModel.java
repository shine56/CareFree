package com.example.a73233.carefree.home.viewModel;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.icu.util.Calendar;
import android.util.Log;

import com.example.a73233.carefree.bean.Diary_db;
import com.example.a73233.carefree.bean.NoteBean;
import com.example.a73233.carefree.bean.Note_db;
import com.example.a73233.carefree.home.model.HomeModel;
import com.example.a73233.carefree.note.view.NoteListAdapter;
import com.example.a73233.carefree.util.ConstantPool;
import com.example.a73233.carefree.util.EmotionDataUtil;
import com.example.a73233.carefree.util.LogUtil;
import com.example.a73233.carefree.util.NoteUtil;
import com.example.a73233.carefree.util.TimeUtil;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class HomeViewModel{
    public final ObservableInt emotionValue = new ObservableInt();
    public final ObservableInt moodViewPointColor = new ObservableInt();
    public final ObservableField<String []> dayNums = new ObservableField<>();
    public final ObservableField<int []> dayValues = new ObservableField<>();
    public final ObservableField<int []> dayColors = new ObservableField<>();
    public final ObservableField<String> reportSuggest = new ObservableField<>();
    public final ObservableField<int []> energyS = new ObservableField<>();
    public final ObservableInt energySum = new ObservableInt();

    private List<Diary_db> diaryDbList;
    private NoteListAdapter noteListAdapter;
    private HomeModel model;
    private Activity activity;

    public HomeViewModel(NoteListAdapter noteListAdapter, Activity activity) {
        this.noteListAdapter = noteListAdapter;
        this.activity = activity;
        model = new HomeModel();
    }

    public void refreshNote(){
        noteListAdapter.refreshData(model.findTaskNote());
    }

    /**
     * 找出最近7天的心情值，一天有多篇日记则取平均值
     */
    public void initReportViewData(){
        String TAG = "情绪报表数据生成测试";
        //获取今天的日期
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_MONTH);

        //找出距离今天最近七天的日记,同属一天的日记取平均值
        int i = 0;  //记录天数
        int j = 0;  //记录日记条数
        int day = today;  //记录日期
        String dayString;
        int sameDaySum = 0;
        int value = 0;
        int[] DayValue = {0,0,0,0,0,0,0};
        String[] DayNum = {"01","02","03","04","05","06","07"};
        int[] DayColor = {0XFF38D5D6, 0XFF64B0E8, 0XFF9B85FF, 0XFF38D5D6, 0XFF2B5876, 0XFF38D5D6, 0XFF9B85FF};
        diaryDbList  = LitePal.where("isAbandon like ?",""+ ConstantPool.NOT_ABANDON).find(Diary_db.class);

        //初始化日期
        for(i=6; i>-1; i--){
            if(day<10 && day>0){
                dayString = "0"+day;
            } else if(day<0){
                dayString = ""+TimeUtil.GetDayByMonth(day);
            }else {
                dayString = ""+day;
            }
            DayNum[i] = dayString;
            day--;
        }
        day = today; i=0; dayString= "";
        //数据库为空
        if (diaryDbList.size() == 0){
            Log.d(TAG,"数据库没有数据" );
            dayValues.set(DayValue);
            dayNums.set(DayNum);
            dayColors.set(DayColor);
            return;
        }
        Diary_db db = diaryDbList.get((diaryDbList.size() - 1) -j);
        j++; sameDaySum++; value = db.getEmotionValue();
        //数据库只有一条数据
        if(diaryDbList.size() == 1){
            if(value == 0) value = 1;
            DayValue[6] = value/sameDaySum;
        }
        //数据库两条数据以上
        while(i<7 && j<diaryDbList.size()){
            if(day<10)
                dayString = "0"+day;
            else
                dayString = ""+day;
            if(day <=0){
                dayString = ""+(TimeUtil.GetDayByMonth(day));
            }

            //当前日期等于数据库日期
            if(db.getDay().equals(dayString)){
                Log.d(TAG,"当前日期等于数据库日期");
                Diary_db nextDb = diaryDbList.get((diaryDbList.size() - 1) -j);
                //当前日期也等于数据库下一张表的日期
                if(nextDb!=null && nextDb.getDay().equals(dayString)){
                    Log.d(TAG,"当前日期也等于数据库下一张表的日期");
                    db = diaryDbList.get((diaryDbList.size() - 1) -j);
                    j++;
                    value += db.getEmotionValue();
                    sameDaySum++;
                    if(j == diaryDbList.size()){
                        DayNum[6-i] = dayString;
                        if(value == 0) value = 1;
                        DayValue[6-i] = value/sameDaySum;
                        Log.d(TAG,"数据库最后一条数据了");
                    }
                }else {
                    DayNum[6-i] = dayString;
                    if(value == 0) value = 1;
                    DayValue[6-i] = value/sameDaySum;
                    Log.d(TAG,"这天有数据"+DayValue[6-i]);
                    day--;
                    i++;
                    db = diaryDbList.get((diaryDbList.size() - 1) -j);
                    j++;
                    value = db.getEmotionValue();
                    sameDaySum = 1;
                }
            }else {
                DayNum[6-i] = dayString;
                DayValue[6-i] = 0;
                Log.d(TAG,"这天没数据"+DayValue[6-i]);
                day--;
                i++;
            }
        }

        //初始化颜色
        for(i=6; i>-1; i--){
            DayColor[i] = EmotionDataUtil.GetColors(DayValue[i], activity)[1];
        }
        dayValues.set(DayValue);
        dayNums.set(DayNum);
        dayColors.set(DayColor);
        reportSuggest.set(EmotionDataUtil.GetSuggestion(dayValues.get()));
    }

    /**
     * 找出最近7天的能动值的变化。
     */
    public void initEnergyReport(){
        //数据
        int[][] energys = new int[8][8];
        //今天日期
        Date date = new Date();
        String dayString = new SimpleDateFormat("dd").format(date);
        String yearAndMonth = new SimpleDateFormat("yyyy年MM月").format(date);
        String monthAndDay = new SimpleDateFormat("MM月dd日").format(date);
        String day = null;

        //将日期转化成整型
        int dayInt = Integer.parseInt(dayString);

        int i = 0;
        for(i=0; i<7; i++){
            if(dayInt <= 0){
                day = ""+TimeUtil.GetDayByMonth(dayInt);
                yearAndMonth = TimeUtil.getLastMonthByYM(yearAndMonth);
                monthAndDay = TimeUtil.GetLastMonthByMD(monthAndDay)+"月"+day+"日";
                dayInt = Integer.parseInt(day);
            }else if(dayInt <10){
                day = "0"+dayInt;
                monthAndDay = monthAndDay.substring(0,3) + day + "日";
            }else{
                day = String.valueOf(dayInt);
                monthAndDay = monthAndDay.substring(0,3) + day + "日";
            }
            List<Diary_db> diaryDbs = model.findDiaryByDate(yearAndMonth,day);
            for(Diary_db diary_db : diaryDbs){
                energys[i][EmotionDataUtil.GetEnergyType(diary_db.getEmotionValue())] += EmotionDataUtil.GetEnergy(diary_db.getEmotionValue());
            }
            List<Note_db> noteDbs = model.findNoteByDate(monthAndDay);
            for (Note_db noteDb : noteDbs){
                energys[i][NoteUtil.GetEnergyType(noteDb.getRank())] += NoteUtil.GetEnergy(noteDb.getRank(), noteDb.getIsComplete());
            }
            dayInt --;
        }

        int result[]= new int[8];
        int sum = 0;
        int sum_2 = 0;
        for(i=0; i<7; i++){
            for(int j=0; j<7; j++){
                result[i] += energys[j][i];
            }
            sum += result[i];
        }
        for(i=0; i<7; i++){
            sum_2 += energys[0][i];
        }

        energyS.set(result);
        energySum.set(sum);
        //初始化卡片值
        if(isShowEmotionValue()){
            Diary_db diaryDb = model.findLastData();
            if(diaryDb == null){
                emotionValue.set(0);
            }else {
                emotionValue.set(diaryDb.getEmotionValue());
            }
        }else {
            emotionValue.set(sum_2);
        }
        moodViewPointColor.set(EmotionDataUtil.GetColors(emotionValue.get(),activity)[0]);
    }
    public Boolean isShowNote(){
        SharedPreferences pref = activity.getSharedPreferences("setting",MODE_PRIVATE);
        if (pref.getString("homeShowNote",ConstantPool.HOME_SHOW_NOTE).equals(ConstantPool.HOME_SHOW_NOTE)){
            return true;
        }else {
            return false;
        }
    }

    public Boolean isShowEmotionValue(){
        SharedPreferences pref = activity.getSharedPreferences("setting",MODE_PRIVATE);
        if (pref.getString("cardShow",ConstantPool.CARD_SHOW_EMOTION).equals(ConstantPool.CARD_SHOW_EMOTION)){
            return true;
        }else {
            return false;
        }
    }
    public Boolean isOriginColor(Context context){
        SharedPreferences pref = context.getSharedPreferences("setting", MODE_PRIVATE);
        return pref.getBoolean("isOriginColor",true);
    }
}
