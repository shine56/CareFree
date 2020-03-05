package com.example.a73233.carefree.note.view;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.a73233.carefree.MainActivity;
import com.example.a73233.carefree.R;
import com.example.a73233.carefree.util.ConstantPool;
import com.example.a73233.carefree.util.LogUtil;

public class ClockReceiver extends BroadcastReceiver {
    private int version = android.os.Build.VERSION.SDK_INT;
    private String channel_id;
    private String channel_name;
    private String channel_desc;
    private int importance;
    private String group_id;
    private Notification notification;
    private Intent mIntent;
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = "任务贴提醒";
        String text = intent.getExtras().getString("text");
        int id = intent.getExtras().getInt("noteDb_id");
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();

        channel_id = "my_cf_c_id";
        channel_name = "my_cf_c_name";
        channel_desc = "my_cf_note_c";
        importance = NotificationManager.IMPORTANCE_DEFAULT;
        group_id = null;
        mIntent = new Intent(context, NoteWriteActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("noteId",id);
        mIntent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,id,mIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //发送通知
        if(version >= 26){
            NotificationChannel channel = new NotificationChannel(channel_id,channel_name,importance);
            //渠道描述
            channel.setDescription(channel_desc);
            //闪灯
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            //震动
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 100, 200});
            //配置渠道组
            if(group_id!=null){
                channel.setGroup(group_id);//设置渠道组
            }
            //创建渠道
            manager.createNotificationChannel(channel);
            //向渠道发布通知
            notification = new NotificationCompat.Builder(context,channel_id)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setSmallIcon(R.mipmap.icon)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .build();
            manager.notify(id,notification);
        }else {
            Notification notification = new NotificationCompat.Builder(context)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setSmallIcon(R.mipmap.icon)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[]{100, 200, 100, 200})
                    .build();
            notification.flags = Notification.FLAG_ONLY_ALERT_ONCE;
            manager.notify(id,notification);
        }
    }
}
