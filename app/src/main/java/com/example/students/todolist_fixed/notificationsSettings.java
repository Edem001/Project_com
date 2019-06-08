package com.example.students.todolist_fixed;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.Calendar;

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

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            tintStatusBar();

        pf = new preferencesWorker(getSharedPreferences("Notifications", MODE_PRIVATE), this);
        context = this;
        calendar = Calendar.getInstance();

        manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        notifyIntent = new Intent(getApplicationContext(), NotificationService.class);
        itsGoingToBeNotification = PendingIntent.getBroadcast(this, 100, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        timeText = findViewById(R.id.selected_time_display);
        cb = findViewById(R.id.checkBox);
        cb.setChecked(TIME.getChecker());
        displayTime(timeText);

        if(cb.isChecked()) {
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
                if(cb.isChecked()) {
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
                    pf.save(2,"true",context);
                    startAlarm(true);
                }
                else {timeText.setOnClickListener(null); pf.save(2,"false",context); stopAlarm();}
            }
        });
    }
    void displayTime(TextView v){
        String text  = getResources().getString(R.string.time_chosen) + " ";
        if(TIME.getHour() < 10) text += "0" + TIME.getHour();
        else text+= TIME.getHour();

        if(TIME.getMinute() <10) text += ":0" + TIME.getMinute();
        else text += ":" + TIME.getMinute();
        Log.d("TEXT", TIME.getHour() + ":" + TIME.getMinute());
        v.setText(text);
    }
    @TargetApi(21)
    void tintStatusBar() {
        getWindow().setStatusBarColor(COLORS.getColorSecondary());
    }
    void startAlarm(boolean restart){
        Log.d("Service_myApp", DashboardActivity.check+"");
        if(!DashboardActivity.check) {
            if (restart) manager.cancel(itsGoingToBeNotification);
            if (calendar.get(Calendar.HOUR_OF_DAY) - TIME.getHour() >= 0 && calendar.get(Calendar.MINUTE) - TIME.getMinute() > 0)
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)+1);
            calendar.set(Calendar.HOUR_OF_DAY, TIME.getHour());
            calendar.set(Calendar.MINUTE, TIME.getMinute());
            calendar.set(Calendar.SECOND, 0);
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, itsGoingToBeNotification);
        }
    }
    void stopAlarm(){
        manager.cancel(itsGoingToBeNotification);
    }
}
