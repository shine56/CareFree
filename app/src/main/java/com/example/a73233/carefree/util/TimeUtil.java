package com.example.a73233.carefree.util;

import android.icu.util.Calendar;

public class TimeUtil {
    /**
     * 传入一个负数返其正确的日期
     * @param day  一个负数或0，表示上个月某日距离今天的天数-1
     * @return
     */
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
            case 2:
                day += 29;
                break;
            default:
                day += 30;
                break;
        }
        return day;
    }

    /**
     * 获取上一个月月份
     * @param yearAndMonth
     * @return
     */
    public static String getLastMonthByYM(String yearAndMonth){
        String lastYearAndMonth;
        int thisYear = Integer.parseInt(yearAndMonth.substring(0,4));
        int thisMonth = Integer.parseInt(yearAndMonth.substring(5,7));
        if(thisMonth == 1){
            lastYearAndMonth = (thisYear-1)+"年12月";
        }else if(thisMonth <11){
            lastYearAndMonth = thisYear+"年0"+(thisMonth-1)+"月";
        }else {
            lastYearAndMonth = thisYear+"年"+(thisMonth-1)+"月";
        }
        return lastYearAndMonth;
    }
    public static String GetLastMonthByMD(String monthAndDay){
        String lastMonth;
        int thisMonth = Integer.parseInt(monthAndDay.substring(0,2));
        if(thisMonth == 1){
            lastMonth = "12";
        }else if(thisMonth <11){
            lastMonth = "0"+(thisMonth-1);
        }else {
            lastMonth = ""+(thisMonth-1);
        }
        return lastMonth;
    }

    /**
     * 返回自动更新频率(String)
     * @param autoF
     * @return
     */
    public static String GetAutoFText(int autoF){
        switch (autoF){
            case 2:
                return "每新增2条日记";
            case 3:
                return "每新增3条日记";
            case 5:
                return "每新增5条日记";
            case 7:
                return "每新增7条日记";
            case 10:
                return "每新增10条日记";
            case -1:
                return "关";
            default:
                return "关";
        }
    }
}
