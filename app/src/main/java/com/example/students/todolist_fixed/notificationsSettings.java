package com.example.students.todolist_fixed;

import android.annotation.TargetApi;
import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.AlarmClock;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.students.todolist_fixed.NotificationChannel.ToDoChannel_ID;

public class notificationsSettings extends AppCompatActivity {
    TextView timeText;
    preferencesWorker pf;
    Context context;
    TimePickerDialog dialog;
    Calendar calendar;
    CheckBox cb;
    AlarmManager manager;
    Intent notifyIntent;
    PendingIntent itsGoingToBeNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_settings);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            tintStatusBar();

        /*if (Build.VERSION.SDK_INT >= 26) {
            Intent accessIntent = new Intent();
            accessIntent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            accessIntent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            startActivity(accessIntent);
        }*/

        pf = new preferencesWorker(getSharedPreferences("Notifications", MODE_PRIVATE), this);
        context = this;
        calendar = Calendar.getInstance();

        notifyIntent = new Intent(getApplicationContext(), NotificationReceiver.class);
        int type = pf.loadPreferences(3);
        notifyIntent.putExtra("sound", type);
        itsGoingToBeNotification = PendingIntent.getBroadcast(getApplicationContext(), 54255, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        timeText = findViewById(R.id.selected_time_display);
        cb = findViewById(R.id.checkBox);
        cb.setButtonTintList(ColorStateList.valueOf(COLORS.getColorAccent()));
        cb.setChecked(TIME.getChecker());
        displayTime(timeText);

        if (cb.isChecked()) {
            timeText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            pf.save(0, hourOfDay, context);
                            pf.save(1, minute, context);
                            TIME.setHour(hourOfDay);
                            TIME.setMinute(minute);
                            displayTime(timeText);
                            startAlarm(true);
                        }
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                    dialog.show();
                }
            });

        }

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb.isChecked()) {
                    timeText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    pf.save(0, hourOfDay, context);
                                    pf.save(1, minute, context);
                                    TIME.setHour(hourOfDay);
                                    TIME.setMinute(minute);
                                    displayTime(timeText);
                                    startAlarm(false);
                                }
                            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                            dialog.show();
                        }
                    });
                    pf.save(2, "true", context);
                    startAlarm(false);
                } else {
                    timeText.setOnClickListener(null);
                    pf.save(2, "false", context);
                    stopAlarm();
                }
            }
        });
    }

    void displayTime(TextView v) {
        String text = getResources().getString(R.string.time_chosen) + " ";
        if (TIME.getHour() < 10) text += "0" + TIME.getHour();
        else text += TIME.getHour();

        if (TIME.getMinute() < 10) text += ":0" + TIME.getMinute();
        else text += ":" + TIME.getMinute();
        Log.d("TEXT", TIME.getHour() + ":" + TIME.getMinute());
        v.setText(text);
    }

    @TargetApi(21)
    void tintStatusBar() {
        getWindow().setStatusBarColor(COLORS.getColorSecondary());
    }

    void startAlarm(boolean restart) {
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.HOUR_OF_DAY, TIME.getHour());
        calendar2.set(Calendar.MINUTE, TIME.getMinute());
        calendar2.set(Calendar.SECOND, 0);
        Log.d("Service_myApp", DashboardActivity.check + " ");
        if (!DashboardActivity.check) {
            if (restart) manager.cancel(itsGoingToBeNotification);
            if (calendar.getTimeInMillis() - calendar2.getTimeInMillis() > 0)
                calendar2.set(Calendar.DAY_OF_MONTH, calendar2.get(Calendar.DAY_OF_MONTH) + 1);
            Log.d("Calendar", calendar2.getTime().toString());
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), AlarmManager.INTERVAL_DAY, itsGoingToBeNotification);
            PendingIntent testIntent = PendingIntent.getBroadcast(context, 54255, notifyIntent, PendingIntent.FLAG_NO_CREATE);
            if(testIntent == null) Log.d("Alarm", "Alarm successfully created");
        }
    }

    void stopAlarm() {
        manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(itsGoingToBeNotification);
    }
    public void makeTestNotification(View v){
        String path;
        int meme = pf.loadPreferences(3);
        switch (meme) {
            case 0:
                path = "android.resource://" + context.getPackageName() + "/raw/just_do_it";
                break;
            case 1:
                path = "android.resource://" + context.getPackageName() + "/raw/omae_wa";
                break;
                default: path = "android.resource://" + context.getPackageName() + "/raw/just_do_it";
        }
        Uri sound = Uri.parse(path);
        Intent startIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, startIntent, PendingIntent.FLAG_UPDATE_CURRENT);

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
        if(Build.VERSION.SDK_INT >= 26) EnableEffects();
        notificationManager.notify(1, notification);

    }
    void unlockTestButton(){
        Button testBtn = findViewById(R.id.test_butt);

    }
    @TargetApi(26)
    void EnableEffects(){
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Toast.makeText(this,manager.isNotificationPolicyAccessGranted()+ "", Toast.LENGTH_SHORT).show();
    }
}
