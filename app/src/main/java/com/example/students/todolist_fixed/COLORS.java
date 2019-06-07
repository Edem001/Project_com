package com.example.students.todolist_fixed;

import android.content.SharedPreferences;

public class COLORS {
    private SharedPreferences pref;
    private static int COLOR_PRIMARY ;
    private static int COLOR_SECONDARY;
    private static int COLOR_ACCENT;
    static int getColorPrimary(){ return COLOR_PRIMARY; }
    static int setColorPrimary(int color){
        COLOR_PRIMARY = color;
        return  COLOR_PRIMARY;
    }
    static int getColorSecondary(){
        return COLOR_SECONDARY;
    }
    static int setColorSecondary(int color){
        COLOR_SECONDARY = color; return  COLOR_SECONDARY;
    }
    static int getColorAccent(){
        return COLOR_ACCENT;
    }
    static int setColorAccent(int color){
        COLOR_ACCENT = color; return COLOR_ACCENT;
    }
}
