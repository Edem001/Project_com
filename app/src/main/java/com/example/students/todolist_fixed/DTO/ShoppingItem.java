package com.example.students.todolist_fixed.DTO;

public class ShoppingItem{
    String itemName;
    float itemPrice;
    int priority;
    boolean isBought;
    byte isBoughtI;
    int amount;
    long id = -1;

    public ShoppingItem(){}

    public ShoppingItem(String name, float price, int amount, boolean isBought){
        itemName = name;
        itemPrice = price;
        this.amount = amount;
        this.isBought = isBought;
        this.isBoughtI = (byte) (isBought ? 1:0);
    }

    public final void setBoughtStatus(boolean status){ isBought = status; isBoughtI = (byte) (status ? 1:0);}
    public final boolean getBoughtStatus(){ return isBought; }
    public final byte getIsBoughtI(){ return  isBoughtI; }

    public final void setItemName(String newName){ this.itemName = newName; }
    public final String getItemName(){ return itemName; }

    public final void setItemPrice(float newPrice){ itemPrice = newPrice; }
    public final float getItemPrice(){ return itemPrice; }

    public final void setPriority(int newPriority){ priority = newPriority; }
    public final int getPriority(){ return priority; }

    public final void setId(long newId){ id = newId; }
    public final long getId(){ return id; }

    public final void setAmount(int newAmount){ amount = newAmount; }
    public final int getAmount(){ return amount;}
    }
