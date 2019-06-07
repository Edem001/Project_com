package com.example.students.todolist_fixed;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class preferencesWorker {
    SharedPreferences sPref;
    Context context;
    public preferencesWorker(SharedPreferences pref, Context context){
        sPref = pref;
        this.context = context;
    }
    public void save(int key, String value, Context context) {
        SharedPreferences.Editor edit = sPref.edit();
        edit.putString(key + "", value);
        edit.commit();
        Toast.makeText(context, "Data saved", Toast.LENGTH_SHORT).show();
    }
    public int loadPreferences(int MODE) {
        String resStr = sPref.getString(MODE+"", "");
        if(resStr.equals("")) switch (MODE){
            case 0: return context.getResources().getColor(R.color.colorPrimary);
            case 1: return context.getResources().getColor(R.color.colorPrimaryDark);
            case 2: return context.getResources().getColor(R.color.colorAccent);
        }
        return Integer.parseInt(resStr);
    }
}
