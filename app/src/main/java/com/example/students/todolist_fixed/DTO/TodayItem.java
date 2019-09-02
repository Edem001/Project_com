package com.example.students.todolist_fixed.DTO;

import java.util.Calendar;

public class TodayItem {
    boolean isCompleted = false;
    String itemText;
    String itemCategory;
    int priority;

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
    public boolean getCompleted(){
        return this.isCompleted;
    }

    public void setItemText(String newName){ itemText = newName; }
    public String getItemText(){ return this.itemText; }

    public void setItemCategory(String newCategory){ itemCategory = newCategory; }
    public String getItemCategory(){ return this.itemCategory; }

    public void setPriority(int newPriority){ priority = newPriority; }
    public int getPriority(){ return this.priority; }
}
