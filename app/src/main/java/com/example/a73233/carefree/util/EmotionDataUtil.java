package com.example.a73233.carefree.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

public class EmotionDataUtil {

    //传入数值获取对应的颜色
    public static int[] GetColors(int value, Context activity){
        int[] colors = getColorFromPref(activity);
        if(value>15){
            int[] colors1 = {colors[0],colors[1]};
            return colors1;
        }else if(value>-10 && value<=15){
            int[] colors2 = {colors[2],colors[3]};
            return colors2;
        }else if(value>-30 && value<=-10){
            int[] colors3 = {colors[4],colors[5]};
            return colors3;
        }else if(value<=-30){
            int[] colors4 = {colors[6],colors[7]};
            return colors4;
        }
        int[] colors2 = {colors[0],colors[1]};
        return colors2;
    }

    public static int[] GetColorsByType(int type,Context context){
        int[] colors = getColorFromPref(context);
        switch (type){
            case 2:
                int[] colors1 = {colors[0], colors[1]};
                return colors1;
            case 3:
                int[] colors2 = {colors[2], colors[3]};
                return colors2;
            case 4:
                int[] colors3 = {colors[4], colors[5]};
                return colors3;
            case 5:
                int[] colors4 = {colors[6], colors[7]};
                return colors4;
        }
        int[] colors2 = {colors[0], colors[1]};
        return colors2;
    }

    private static int[] getColorFromPref(Context context){
        int[] colors = new int[10];

        SharedPreferences pref = context.getSharedPreferences("setting", MODE_PRIVATE);
        if(!pref.getBoolean("isOriginColor",true)){
            colors[0] = pref.getInt("happy_color_start",0XFF3FABD5);
            colors[1] = pref.getInt("happy_color_end",0XFF38D5D6);
            colors[2] = pref.getInt("calm_color_start",0XFF537AE1);
            colors[3] = pref.getInt("calm_color_end",0XFF64B0E8);
            colors[4] = pref.getInt("sad_color_start",0XFFAC69DB);
            colors[5] = pref.getInt("sad_color_end",0XFF9B85FF);
            colors[6] = pref.getInt("repression_color_start",0XFF09203F);
            colors[7] = pref.getInt("repression_color_end",0XFF2B5876);
        }else {
            colors[0] = 0XFF3FABD5;
            colors[1] = 0XFF38D5D6;
            colors[2] = 0XFF537AE1;
            colors[3] = 0XFF64B0E8;
            colors[4] = 0XFFAC69DB;
            colors[5] = 0XFF9B85FF;
            colors[6] = 0XFF09203F;
            colors[7] = 0XFF2B5876;
        }
        return colors;
    }

    /**
     * 根据七天情绪获取建议
     * @param values
     * @return
     */
    public static String GetSuggestion(int[] values){
        String suggestion = "尽情抒发你的一念一想！";
        int basicFluctuationValue = 175;
        int basicDiaryNum = 2;
        //求写日记的频率 //求平均值
        int diaryNum = 0;  //记录日记数量
        int averageValue = 0;  //平均值
        int sum = 0, i=0;
        for(i=0; i<7; i++){
            if(values[i] !=0){
                diaryNum++;
                sum += values[i];
            }
        }
        if(diaryNum != 0){
            averageValue = sum/diaryNum;
        }else {
            averageValue = 0;
        }

        //求波动性
        int variance = 0; //方差
        sum = 0;
        for(i=0; i<7; i++){
            if (values[i] != 0){
                sum += (averageValue-values[i])*(averageValue-values[i]);
            }
        }
        if(diaryNum != 0){
            variance = sum/diaryNum;
        }else {
            variance = 0;
        }

        Log.d("获取建议测试","平均值="+averageValue+"方差="+variance
                +"日记篇数="+diaryNum+"\n");

        if(diaryNum<=basicDiaryNum){
            suggestion = "最近有点忙哟，可以抽空写写日记抒发一下哟";
        }else if(variance >basicFluctuationValue && averageValue>-10 && averageValue<=10){
            suggestion = "最近情绪波动过大，需要恰当调节情绪。";
        }else if(averageValue>15 && averageValue<=50){
            suggestion = "最近情绪还是积极的哟";
        }else if(averageValue>-10 && averageValue<=15){
            suggestion = "一片飘叶，伴着微风落入水池，泛起点点涟漪。";
        }else if(averageValue>-30 && averageValue<=-10){
            suggestion = "有什么可忧伤的呢，要加油呀！";
        }else if(averageValue>=-50 && averageValue<=-30){
            suggestion = "柳暗花明又一村，要相信自己哟。加油(ง •_•)ง";
        }
        return suggestion;
    }
    public static int GetEnergy(int value){
        if(value>15){
            return 15;
        }else if(value>-10 && value<=15){
            return 10;
        }else if(value>-30 && value<=-10){
            return -10;
        }else if(value>=-50 && value<=-30){
            return -15;
        }
        return 0;
    }
    public static int GetEnergyType(int value){
        if(value>15 && value<=50){
            return 0;
        }else if(value>-10 && value<=15){
            return 1;
        }else if(value>-30 && value<=-10){
            return 2;
        }else if(value>=-50 && value<=-30){
            return 3;
        }
        return 0;
    }
}
