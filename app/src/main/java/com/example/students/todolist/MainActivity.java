package com.example.students.todolist;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.example.students.todolist.COLORS;

public class MainActivity extends AppCompatActivity {

    ImageView ic_logo;
    MainActivity activity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesWorker pref = new preferencesWorker(getSharedPreferences("Color", MODE_PRIVATE),this);
        COLORS.setColorPrimary(pref.loadPreferences(Const.TAKE_PRIMARY));
        COLORS.setColorSecondary(pref.loadPreferences(Const.TAKE_SECONDARY));
        COLORS.setColorAccent(pref.loadPreferences(Const.TAKE_ACCENT));
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            tintStatusBar();


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
}
