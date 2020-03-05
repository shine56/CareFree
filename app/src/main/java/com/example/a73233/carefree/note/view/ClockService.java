package com.example.a73233.carefree.note.view;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.TimePicker;

import com.example.a73233.carefree.util.LogUtil;

import static android.app.PendingIntent.FLAG_CANCEL_CURRENT;

public class ClockService extends Service {
    public ClockService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.LogD("服务被创建");
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.LogD("服务启动");
        //设置闹钟
        int hour = intent.getIntExtra("hour",-1);
        int minutes = intent.getIntExtra("minutes",-1);
        String text = intent.getStringExtra("text");
        int id = intent.getIntExtra("noteDb_id",-1);
        createAlarm(text,hour,minutes,id);
        //保活操作
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
//        long eightHour = 2 * 60 * 60 * 1000;
//        Calendar c = Calendar.getInstance();
//        c.setTimeInMillis(System.currentTimeMillis());
//        Intent i = new Intent(this,ClockService.class);
//        i.putExtra("hour",hour);
//        i.putExtra("minutes",minutes);
//        i.putExtra("text",text);
//        PendingIntent pi = PendingIntent.getService(this,1,i,0);
//        alarmManager.cancel(pi);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis()+eightHour,pi);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        LogUtil.LogD("服务结束");
        super.onDestroy();
    }

    private void createAlarm(String message, int hour, int minutes, int noteDb_id) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
        Intent intent = new Intent(this, ClockReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putString("text",message);
        bundle.putInt("noteDb_id",noteDb_id);
        intent.putExtras(bundle);
        PendingIntent pi = PendingIntent.getBroadcast(this,noteDb_id, intent, FLAG_CANCEL_CURRENT);

        //设置当前时间
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        // 根据用户选择的时间来设置Calendar对象
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minutes);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND, 0);
        // ②设置AlarmManager在Calendar对应的时间启动Activity
        alarmManager.cancel(pi);
        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
        LogUtil.LogD("设置闹钟完成");
    }
}
