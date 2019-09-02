package com.example.students.todolist_fixed;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class preferencesWorker {
    SharedPreferences sPref;
    Context context;

    public preferencesWorker(SharedPreferences pref, Context context) {
        sPref = pref;
        this.context = context;
    }
    public preferencesWorker(SharedPreferences pref){
        sPref = pref;
        this.context = null;
    }

    public void save(int key, String value, Context context) {
        SharedPreferences.Editor edit = sPref.edit();
        edit.putString(key + "", value);
        edit.commit();
        Toast.makeText(context, context.getResources().getText(R.string.data_saved), Toast.LENGTH_SHORT).show();
    }

    public void save(int key, int value, Context context) {
        SharedPreferences.Editor edit = sPref.edit();
        edit.putString(key + "", Integer.toString(value));
        edit.commit();
        Toast.makeText(context,  context.getResources().getText(R.string.data_saved), Toast.LENGTH_SHORT).show();
    }

    public int loadPreferences(int MODE) {
        /** 0-2 colors, 3 ringtone type **/
        String resStr = sPref.getString(MODE + "", "");
        if (resStr.equals("")) switch (MODE) {
            case 0:
                return context.getResources().getColor(R.color.colorPrimary);
            case 1:
                return context.getResources().getColor(R.color.colorPrimaryDark);
            case 2:
                return context.getResources().getColor(R.color.colorAccent);
        }
        if (resStr!= "")
        return Integer.parseInt(resStr);
        else return  0;
    }
    public int loadPreferences(int MODE, int printSomethingHereForTimeAccess) {
        String resStr = sPref.getString(MODE + "", "");
        if (resStr.equals("")) switch (MODE) {
            case 0:
                return 0;
            case 1:
                return 0;
        }
        return Integer.parseInt(resStr);
    }
    public String loadPreferences(int MODE, boolean toMakeString) {
        String resStr = sPref.getString(MODE + "", "");
        if (resStr.equals("")) resStr = "false";
        return resStr;
    }
    public int loadPreferencesNoContext(int MODE){
        String resStr = sPref.getString(MODE+"", "");
        if (resStr == "") return 0;
        else return Integer.parseInt(resStr);
    }
}
