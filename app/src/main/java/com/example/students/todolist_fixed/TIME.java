package com.example.students.todolist_fixed;

import android.content.Context;

public class TIME {
    static int hour_this, minute_this;
    static boolean checker;
    static Context context;

    static public Context getContext(){ return context;}
    static public int getHour(){
        return hour_this;
    }
    static public int getMinute(){
        return minute_this;
    }
    static public boolean getChecker(){return checker;}

    static public void setHour(int hour) {
        hour_this = hour;
    }
    static public void setMinute(int minute) {
        minute_this = minute;
    }
    static public void setChecker(boolean check){checker = check;}

    public static void setContext(Context context) {
        TIME.context = context;
    }
}

