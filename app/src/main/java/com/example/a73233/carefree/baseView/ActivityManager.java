package com.example.a73233.carefree.baseView;

import android.app.Activity;

import com.example.a73233.carefree.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class ActivityManager {

    public static List<Activity> activityList = new ArrayList<>();
    public static void addActivity(Activity activity){
        activityList.add(activity);
    }
    public static void removeActivity(Activity activity){
        activityList.remove(activity);
    }
    public static void finishActivity(Activity activity){
        if(activity != null){
            activity.finish();
        }
        activityList.remove(activity);
    }
    public static void finishAllActivity(){
        for(Activity activity : activityList){
            LogUtil.LogD("活动栈中所有活动 -> "+activity.getClass().getSimpleName());
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
        activityList.clear();
    }
}
