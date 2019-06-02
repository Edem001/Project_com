package com.example.students.todolist;

import android.content.SharedPreferences;

public class COLORS {
    private SharedPreferences pref;
    private static int COLOR_PRIMARY ;
    private static int COLOR_SECONDARY;
    private static int COLOR_ACCENT;
    static int getColorPrimary(){
        return COLOR_PRIMARY;
    }
    static void setColorPrimary(int color){
        COLOR_PRIMARY = color;
    }
    static int getColorSecondary(){
        return COLOR_SECONDARY;
    }
    static void setColorSecondary(int color){
        COLOR_SECONDARY = color;
    }
    static int getColorAccent(){
        return COLOR_ACCENT;
    }
    static void setColorAccent(int color){
        COLOR_ACCENT = color;
    }
}
