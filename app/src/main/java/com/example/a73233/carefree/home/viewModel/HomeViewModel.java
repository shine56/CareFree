package com.example.a73233.carefree.home.viewModel;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.icu.util.Calendar;
import android.util.Log;

import com.example.a73233.carefree.bean.Diary_db;
import com.example.a73233.carefree.util.EmotionUtil;
import com.example.a73233.carefree.util.TimeUtil;

import org.litepal.LitePal;

import java.util.List;

public class HomeViewModel {
    public final ObservableInt emotionValue = new ObservableInt();
    public final ObservableInt moodViewPointColor = new ObservableInt();
    public final ObservableField<String []> dayNums = new ObservableField<>();
    public final ObservableField<int []> dayValues = new ObservableField<>();
    public final ObservableField<int []> dayColors = new ObservableField<>();
    public final ObservableField<String> reportSuggest = new ObservableField<>();

    private List<Diary_db> diaryDbList;

    public void initEmotionValue(){
        diaryDbList = LitePal.findAll(Diary_db.class);
        if(diaryDbList.size() != 0){
            emotionValue.set(diaryDbList.get(diaryDbList.size()-1).getEmotionValue());
        }else {
            emotionValue.set(28);
        }
        moodViewPointColor.set(EmotionUtil.GetColors(emotionValue.get())[0]);
    }
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
        diaryDbList = LitePal.findAll(Diary_db.class);

        //初始化日期
        for(i=6; i>-1; i--){
            if(day<10)
                dayString = "0"+day;
            else
                dayString = ""+day;
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
            DayColor[i] = EmotionUtil.GetColors(DayValue[i])[1];
        }
        dayValues.set(DayValue);
        dayNums.set(DayNum);
        dayColors.set(DayColor);
        reportSuggest.set(EmotionUtil.GetSuggestion(dayValues.get()));
    }
}
