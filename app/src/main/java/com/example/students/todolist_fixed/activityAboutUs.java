package com.example.students.todolist_fixed;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class activityAboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) tintStatusBar();
    }
    @TargetApi(21)
    void tintStatusBar() {
        getWindow().setStatusBarColor(COLORS.getColorSecondary());
    }
}
