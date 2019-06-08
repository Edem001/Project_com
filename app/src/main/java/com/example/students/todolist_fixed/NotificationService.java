package com.example.students.todolist_fixed;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;


public class NotificationService extends BroadcastReceiver {
    public NotificationService() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Service_myApp", "I'm in service");

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(context,100,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(context)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(context.getResources().getString(R.string.notification_title))
                .setContentText(context.getResources().getString(R.string.notification_text))
                .setAutoCancel(true);
        if(Build.VERSION.SDK_INT >= 26) setBuilderChannel(builder, manager);
            else builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        manager.notify(100, builder.build());
    }
    @TargetApi(26)
    void setBuilderChannel(Notification.Builder builder, NotificationManager manager){
        NotificationChannel todoChannel = new NotificationChannel("ToDoList", "ToDoList", NotificationManager.IMPORTANCE_DEFAULT);
        todoChannel.setDescription("ToDo app standard notification channel");

        todoChannel.enableLights(true);
        todoChannel.setLightColor(Color.RED);

        todoChannel.enableVibration(true);
        todoChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

        AudioAttributes a = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();

        todoChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),a);

        manager.createNotificationChannel(todoChannel);
        builder.setChannelId("ToDoList");
        }
}