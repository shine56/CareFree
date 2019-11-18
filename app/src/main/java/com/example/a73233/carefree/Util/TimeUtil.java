package com.example.a73233.carefree.Util;

import android.icu.util.Calendar;

public class TimeUtil {
    //处理大小月

    public static int GetDayByMonth(int day){
        //获取上个月月份
        Calendar calendar = Calendar.getInstance();
        int montn = calendar.get(Calendar.MONTH);
        if(montn == 0) montn = 12;
        switch (montn){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day += 31;
                break;
            default:
                day += 30;
                break;
        }
        return day;
    }
}
