package com.example.a73233.carefree.note.view;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.a73233.carefree.R;
import com.example.a73233.carefree.baseview.BaseActivity;
import com.example.a73233.carefree.util.LogUtil;

public class ClockReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.LogD("接受到闹钟");
        Toast.makeText(context, "您设置的时间到了！",
                Toast.LENGTH_SHORT).show();

        String text = intent.getStringExtra("text");
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "ChannelId"; // 通知渠道
        Notification notification= new Notification.Builder(context)
                .setChannelId(channelId)
                .setContentTitle("careFree提醒您")
                .setSmallIcon(R.mipmap.icon)
                .setContentText(text)
                .build();

        NotificationChannel channel = new NotificationChannel(
                channelId,
                "通知的渠道名称",
                NotificationManager.IMPORTANCE_DEFAULT);
        manager.createNotificationChannel(channel);
// 3. 发送通知(Notification与NotificationManager的channelId必须对应)
        manager.notify(1, notification);
    }
}
