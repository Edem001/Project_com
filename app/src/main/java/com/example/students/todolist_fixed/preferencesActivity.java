package com.example.students.todolist_fixed;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import yuku.ambilwarna.AmbilWarnaDialog;

public class preferencesActivity extends AppCompatActivity implements View.OnClickListener{

    SharedPreferences sPref;
    RelativeLayout rLayout;
    int defPrimary;
    int defSecondary;
    preferencesWorker preferencesWorker;
    int defAccent;
    Context activityContext = this;
    Button primary;
    Button secondary;
    Button accent;
    Button defButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        rLayout = (RelativeLayout) findViewById(R.id.preferences_layout);
        preferencesWorker = new preferencesWorker(getSharedPreferences("Color", MODE_PRIVATE),this);
        defPrimary = COLORS.getColorPrimary();
        defSecondary = COLORS.getColorSecondary();
        defAccent = COLORS.getColorAccent();
         primary = findViewById(R.id.col_pick_button_primary);
         secondary = findViewById(R.id.col_pick_button_secondary);
         accent = findViewById(R.id.col_pick_button_accent);
         defButton = findViewById(R.id.defColor);

        primary.setOnClickListener(this);
        secondary.setOnClickListener(this);
        accent.setOnClickListener(this);
        defButton.setOnClickListener(this);


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
                preferencesWorker pref = new preferencesWorker(getSharedPreferences("Color", MODE_PRIVATE), this);
                findViewById(R.id.col_pick_button_primary).setBackgroundColor(pref.loadPreferences(0));
                findViewById(R.id.col_pick_button_secondary).setBackgroundColor(pref.loadPreferences(1));
                findViewById(R.id.col_pick_button_accent).setBackgroundColor(pref.loadPreferences(2));
                break;

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
                switch (position){
                    case 0: COLORS.setColorPrimary(color);
                        findViewById(R.id.col_pick_button_primary).setBackgroundColor(color);
                        break;
                    case 1: COLORS.setColorSecondary(color);
                        findViewById(R.id.col_pick_button_secondary).setBackgroundColor(color);
                        break;
                    case 2: COLORS.setColorAccent(color);
                        findViewById(R.id.col_pick_button_accent).setBackgroundColor(color);
                        break;
                }
            }
        });
        ambilWarnaDialog.show();
    }
    @TargetApi(21)
    void tintStatusBar() {
        getWindow().setStatusBarColor(COLORS.getColorSecondary());
    }
    @Override
    protected void onResume(){
        super.onResume();

    }
}