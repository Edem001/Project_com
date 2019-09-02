package com.example.students.todolist_fixed;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class activityAboutUs extends AppCompatActivity implements View.OnClickListener {
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;
        setContentView(R.layout.activity_about_us);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) tintStatusBar();
        img = findViewById(R.id.imageView);
        img.setOnClickListener(this);

        Button secret = findViewById(R.id.secretBTN);
        secret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferencesWorker pf = new preferencesWorker(getSharedPreferences("Notifications", MODE_PRIVATE), context);
                int curr_type = pf.loadPreferences(3);
                Log.d("curr_type", " " + curr_type);
                switch (curr_type){
                    case 0:
                        pf.save(3, 1, context);
                        break;
                    case 1:
                        pf.save(3, 0, context);
                        break;
                }
            }
        });

    }
    @TargetApi(21)
    void tintStatusBar() {
        getWindow().setStatusBarColor(COLORS.getColorSecondary());
    }

    @Override
    public void onClick(View v) {
        ImageView thisImage = (ImageView) v;
        Log.d("Click", "Clicked " + (((BitmapDrawable)thisImage.getDrawable()).getBitmap() == ((BitmapDrawable)getResources().getDrawable(R.drawable.team)).getBitmap()));
        if (((BitmapDrawable)thisImage.getDrawable()).getBitmap() == ((BitmapDrawable)getResources().getDrawable(R.drawable.easter_egg)).getBitmap()){
            thisImage.setImageResource(R.drawable.team);
        }else thisImage.setImageResource(R.drawable.easter_egg);

    }
}
