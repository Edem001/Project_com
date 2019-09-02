package com.example.students.todolist_fixed;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import static com.example.students.todolist_fixed.NotificationChannel.ToDoChannel_ID;

public class NotificationReceiver extends BroadcastReceiver {
    String path;
    int meme;
    @Override
    public void onReceive(Context context, Intent intent) {
        meme = intent.getIntExtra("sound", 0);
        switch (meme) {
            case 0:
                path = "android.resource://" + context.getPackageName() + "/raw/just_do_it";
                break;
            case 1:
                path = "android.resource://" + context.getPackageName() + "/raw/omae_wa";
                break;
        }
        Uri sound = Uri.parse(path);
        Intent startIntent = new Intent(context, MainActivity.class);
        startIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                startIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Notification notification = new NotificationCompat.Builder(context, ToDoChannel_ID)
                .setSmallIcon(R.drawable.ic_launcher_full)
                .setAutoCancel(true)
                .setContentTitle(context.getResources().getString(R.string.notification_title))
                .setContentText(context.getResources().getString(R.string.notification_text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setSound(sound)
                .setContentIntent(contentIntent)
                .build();

        notificationManager.notify(1, notification);

    }
}
