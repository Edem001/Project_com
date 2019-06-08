package com.example.students.todolist_fixed;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView ic_logo;
    MainActivity activity;
    int tempPrimaryColor, tempSecondaryColor, tempAccentColor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesWorker pref = new preferencesWorker(getSharedPreferences("Color", MODE_PRIVATE),this);
        tempPrimaryColor = COLORS.setColorPrimary(pref.loadPreferences(Const.TAKE_PRIMARY));
        tempSecondaryColor = COLORS.setColorSecondary(pref.loadPreferences(Const.TAKE_SECONDARY));
        tempAccentColor = COLORS.setColorAccent(pref.loadPreferences(Const.TAKE_ACCENT));
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            tintStatusBar();

        pref = new preferencesWorker(getSharedPreferences("Notifications", MODE_PRIVATE),this);
        TIME.setHour(pref.loadPreferences(0,1));
        TIME.setMinute(pref.loadPreferences(1,1));
        TIME.setChecker(pref.loadPreferences(2, true).equals("true"));
        TIME.setContext(this);

        setContentView(R.layout.activity_main);
        activity = this;
        ic_logo = findViewById(R.id.ic_logo);

        ic_logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_in));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ic_logo.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.splash_out));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ic_logo.setVisibility(View.GONE);
                        startActivity(new Intent(activity, DashboardActivity.class));
                        finish();
                    }
                }, 500);
            }
        }, 1500);
    }
    @TargetApi(21)
    void tintStatusBar() {
        getWindow().setStatusBarColor(COLORS.getColorSecondary());
    }
    @Override
    protected void onResume() {
        super.onResume();
        preferencesWorker pref = new preferencesWorker(getSharedPreferences("Color", MODE_PRIVATE), this);
        tempSecondaryColor = COLORS.setColorSecondary(pref.loadPreferences(Const.TAKE_SECONDARY));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            tintStatusBar();
    }

}
