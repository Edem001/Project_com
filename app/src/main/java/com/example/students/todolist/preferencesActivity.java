package com.example.students.todolist;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.Toolbar;
import kotlin.Suppress;
import yuku.ambilwarna.AmbilWarnaDialog;

import static com.example.students.todolist.Const.TAKE_PRIMARY;

public class preferencesActivity extends AppCompatActivity implements View.OnClickListener{

    SharedPreferences sPref;
    RelativeLayout rLayout;
    int defPrimary;
    int defSecondary;
    preferencesWorker preferencesWorker;
    int defAccent;
    Context activityContext = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        rLayout = (RelativeLayout) findViewById(R.id.preferences_layout);
        preferencesWorker = new preferencesWorker(getSharedPreferences("Color", MODE_PRIVATE),this);
        defPrimary = COLORS.getColorPrimary();
        defSecondary = COLORS.getColorSecondary();
        defAccent = COLORS.getColorAccent();
        Button primary = findViewById(R.id.col_pick_button_primary);
        Button secondary = findViewById(R.id.col_pick_button_secondary);
        Button accent = findViewById(R.id.col_pick_button_accent);
        Button defButton = findViewById(R.id.defColor);
        Button restartButton = findViewById(R.id.restart_button);

        primary.setOnClickListener(this);
        secondary.setOnClickListener(this);
        accent.setOnClickListener(this);
        defButton.setOnClickListener(this);
        restartButton.setOnClickListener(this);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            tintStatusBar();
        primary.setBackgroundColor(COLORS.getColorPrimary());
        secondary.setBackgroundColor(COLORS.getColorSecondary());
        accent.setBackgroundColor(COLORS.getColorAccent());
        findViewById(R.id.preferences_toolbar).setBackgroundColor(COLORS.getColorPrimary());
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.col_pick_button_primary:
                openColorPicker(COLORS.getColorPrimary(), 0);
                findViewById(R.id.col_pick_button_primary).setBackgroundColor(COLORS.getColorPrimary());
                break;
            case R.id.col_pick_button_secondary:
                openColorPicker(COLORS.getColorSecondary(), 1);
                findViewById(R.id.col_pick_button_secondary).setBackgroundColor(COLORS.getColorSecondary());
                break;
            case R.id.col_pick_button_accent:
                openColorPicker(COLORS.getColorAccent(), 2);
                findViewById(R.id.col_pick_button_accent).setBackgroundColor(COLORS.getColorAccent());
                break;
            case R.id.defColor:
                for(int i = 0; i < 3; i++){
                    preferencesWorker.save(i, "", this);
                }
                break;
            case R.id.restart_button:
                Intent mStartActivity = new Intent(this, MainActivity.class);
                int mPendingIntentId = 10;
                PendingIntent mPendingIntent = PendingIntent.getActivity(this, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager mgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis()+100, mPendingIntent);
                System.exit(0);
        }
    }
    void openColorPicker(int color, final int position){
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, color, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                preferencesWorker.save(position, color+"", activityContext);
            }
        });
        ambilWarnaDialog.show();
    }
    @TargetApi(21)
    void tintStatusBar() {
        getWindow().setStatusBarColor(COLORS.getColorSecondary());
    }
}