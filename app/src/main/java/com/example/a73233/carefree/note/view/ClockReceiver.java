package com.example.a73233.carefree.note.view;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.a73233.carefree.R;
import com.example.a73233.carefree.util.LogUtil;

public class ClockReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.LogD("发送通知开始");
        String text = intent.getExtras().getString("text");
        Toast.makeText(context, text,
                Toast.LENGTH_SHORT).show();

        //发送通知
        String channelId = "cf"+System.currentTimeMillis();
        NotificationChannel channel = new NotificationChannel(channelId,
                "carefree_channel",
                NotificationManager.IMPORTANCE_DEFAULT);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        Notification notification= new Notification.Builder(context,channelId)
                .setChannelId(channelId)
                .setContentTitle("careFree提醒您")
                .setSmallIcon(R.mipmap.icon)
                .setContentText(text)
                .setAutoCancel(true)
                .build();
        manager.notify(1, notification);
    }
}
