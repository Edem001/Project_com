package com.example.students.todolist_fixed.DTO;

public class ShoppingItem{
    String itemName;
    float itemPrice;
    int priority;
    boolean isBought;
    long id = -1;

    public final void setBoughtStatus(boolean status){ isBought = true; }
    public final boolean getBoughtStatus(){ return isBought; }

    public final void setItemName(String newName){ this.itemName = newName; }
    public final String getItemName(){ return itemName; }
    public final void setItemPrice(float newPrice){ itemPrice = newPrice; }
    public final float getItemPrice(){ return itemPrice; }

    public final void setPriority(int newPriority){ priority = newPriority; }
    public final int getPriority(){ return priority; }

    public final void setId(long newId){ id = newId; }
    public final long getId(){ return id; }
}
