package com.example.students.todolist_fixed;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

public class NotificationChannel extends Application {
    public static final String ToDoChannel_ID = "ToDoChannel";
    @Override
    public void onCreate() {
        super.onCreate();
        tryToCreateChannel();
    }

    void tryToCreateChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            android.app.NotificationChannel channel = new android.app.NotificationChannel(ToDoChannel_ID,
                    "todoChannel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            SharedPreferences pref = getSharedPreferences("Notifications", MODE_PRIVATE);
            preferencesWorker pf = new preferencesWorker(pref);
            int meme = pf.loadPreferencesNoContext(3);
            String path;
            switch (meme){
                case 0:
                    path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://com.example.students.todolist_fixed/raw/just_do_it";
                    break;
                case 1:
                    path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://com.example.students.todolist_fixed/raw/omae_wa";
                    break;
                default:
                    path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://com.example.students.todolist_fixed/raw/just_do_it";
            }
            Uri sound = Uri.parse(path);
            AudioAttributes a = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            channel.setSound(sound,a);
            channel.enableVibration(true);
            channel.setDescription("This is ToDo app channel");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
}
}
