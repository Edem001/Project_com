package com.example.students.todolist_fixed;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.students.todolist_fixed.DTO.ShoppingItem;
import com.example.students.todolist_fixed.DTO.ToDo;
import com.example.students.todolist_fixed.DTO.ToDoItem;
import com.example.students.todolist_fixed.DTO.TodayItem;

import java.util.ArrayList;

import static com.example.students.todolist_fixed.Const.*;

public class DBHandler extends SQLiteOpenHelper {

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createToDoTable = "CREATE TABLE " + TABLE_TODO + " (" +
                COL_ID + " integer PRIMARY KEY AUTOINCREMENT," +
                COL_CREATED_AT + " datetime DEFAULT CURRENT_TIMESTAMP," +
                COL_NAME + " varchar)";
        String createToDoItemTable =
                "CREATE TABLE " + TABLE_TODO_ITEM + " (" +
                        COL_ID + " integer PRIMARY KEY AUTOINCREMENT," +
                        COL_CREATED_AT + " datetime DEFAULT CURRENT_TIMESTAMP," +
                        COL_TODO_ID + " integer," +
                        COL_ITEM_NAME + " varchar," +
                        COL_IS_COMPLETED + " integer)";
        String createShoppingTable = "create table " + TABLE_SHOPPING +" (" +
                COL_ID + " integer primary key autoincrement," +
                COL_CREATED_AT + " datetime default CURRENT_TIMESTAMP," +
                COL_ITEM_DESCRIPTION + " text not null," +
                COL_PRICE + " float not null, " +
                COL_AMOUNT + " integer not null, " +
                COL_IS_BOUGHT + " integer)";

        String createTodayTable = "CREATE TABLE " + TABLE_TODAY + " (" +
                COL_ID + " integer PRIMARY KEY AUTOINCREMENT," +
                COL_CREATED_AT + " datetime DEFAULT CURRENT_TIMESTAMP," +
                COL_TODO_ID + " integer," +
                COL_ITEM_NAME + " text not null," +
                COL_IS_COMPLETED + " integer)";


        db.execSQL(createToDoTable);
        db.execSQL(createToDoItemTable);
        db.execSQL(createShoppingTable);
        db.execSQL(createTodayTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    boolean addToDo(ToDo todo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, todo.getName());
        long result = db.insert(TABLE_TODO, null, cv);
        return result != -1;
    }

    void updateToDo(ToDo todo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, todo.getName());
        db.update(TABLE_TODO, cv, COL_ID + "=?", new String[]{String.valueOf(todo.getId())});
    }

    void updateShopping(ShoppingItem shopItem){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_ITEM_DESCRIPTION, shopItem.getItemName());
        cv.put(COL_PRICE, shopItem.getItemPrice());
        cv.put(COL_AMOUNT, shopItem.getAmount());
        cv.put(COL_IS_BOUGHT, shopItem.getIsBoughtI());
        Log.d("SHOP_LIST_UPDATE",db.update(TABLE_SHOPPING, cv, COL_ID + "=?", new String[]{String.valueOf(shopItem.getId())})+"");

    }

    void updateToday(TodayItem todayItem){

    }

    void deleteToDo(Long todoId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_TODO_ITEM, COL_TODO_ID + "=?", new String[]{String.valueOf(todoId)});
        db.delete(TABLE_TODO, COL_ID + "=?", new String[]{String.valueOf(todoId)});
    }

    void updateToDoItemCompletedStatus(Long todoId, Boolean isCompleted) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor queryResult = db.rawQuery("SELECT * FROM " + TABLE_TODO_ITEM + " WHERE " + COL_TODO_ID + "=" + todoId, null);
        if (queryResult.moveToFirst()) {
            do {
                ToDoItem item = new ToDoItem();
                item.setId(queryResult.getLong(queryResult.getColumnIndex(COL_ID)));
                item.setToDoId(queryResult.getLong(queryResult.getColumnIndex(COL_TODO_ID)));
                item.setItemName(queryResult.getString(queryResult.getColumnIndex(COL_ITEM_NAME)));
                item.setCompleted(isCompleted);
                updateToDoItem(item);
            } while (queryResult.moveToNext());
        }
        queryResult.close();
    }

    public void updateToDoItem(ToDoItem item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_ITEM_NAME, item.getItemName());
        cv.put(COL_TODO_ID, item.getToDoId());
        cv.put(COL_IS_COMPLETED, item.isCompleted());
        db.update(TABLE_TODO_ITEM, cv, COL_ID + "=?", new String[]{String.valueOf(item.getId())});
    }

    ArrayList<ToDo> getToDos() {
        ArrayList<ToDo> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor queryResult = db.rawQuery("SELECT * from " + TABLE_TODO, null);
        if (queryResult.moveToFirst()) {
            do {
                ToDo todo = new ToDo();
                todo.setId(queryResult.getLong(queryResult.getColumnIndex(COL_ID)));
                todo.setName(queryResult.getString(queryResult.getColumnIndex(COL_NAME)));
                result.add(todo);
            } while (queryResult.moveToNext());
        }
        queryResult.close();
        return result;
    }

    public boolean addToDoItem(ToDoItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_ITEM_NAME, item.getItemName());
        cv.put(COL_TODO_ID, item.getToDoId());
        cv.put(COL_IS_COMPLETED, item.isCompleted());

        long result = db.insert(TABLE_TODO_ITEM, null, cv);
        return result != -1;
    }

    public void deleteToDoItem(long itemId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_TODO_ITEM, COL_ID + "=?", new String[]{String.valueOf(itemId)});

    }

    public ArrayList<ToDoItem> getToDoItems(long todoId) {
        ArrayList<ToDoItem> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor queryResult = db.rawQuery("SELECT * FROM " + TABLE_TODO_ITEM + " WHERE " + COL_TODO_ID + "=" + todoId, null);
        if (queryResult.moveToFirst()) {
            do {
                ToDoItem item = new ToDoItem();
                item.setId(queryResult.getLong(queryResult.getColumnIndex(COL_ID)));
                item.setToDoId(queryResult.getLong(queryResult.getColumnIndex(COL_TODO_ID)));
                item.setItemName(queryResult.getString(queryResult.getColumnIndex(COL_ITEM_NAME)));
                item.setCompleted(queryResult.getInt(queryResult.getColumnIndex(COL_IS_COMPLETED)) == 1);
                result.add(item);
            } while (queryResult.moveToNext());
        }

        queryResult.close();
        return result;
    }
    /*Now something custom begins*/
    boolean addShoppingItem(ShoppingItem item){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_ITEM_DESCRIPTION, item.getItemName());
        cv.put(COL_PRICE, item.getItemPrice());
        cv.put(COL_AMOUNT, item.getAmount());

        long result = db.insert(TABLE_SHOPPING, null, cv);
        return result != -1;
    }
    boolean deleteShoppingItem(long id){
        SQLiteDatabase db = getWritableDatabase();
        int result = db.delete(TABLE_SHOPPING, COL_ID + "=?", new String[]{String.valueOf(id)});
        if(result < 1) return true;
        else return false;
    }
    ArrayList<ShoppingItem> getShoppingItems(){
        ArrayList<ShoppingItem> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor queryResult = db.rawQuery("SELECT * from " + TABLE_SHOPPING, null);
        if (queryResult.moveToFirst()) {
            do {
                ShoppingItem item = new ShoppingItem();
                item.setId(queryResult.getLong(queryResult.getColumnIndex(COL_ID)));
                item.setItemName(queryResult.getString(queryResult.getColumnIndex(COL_ITEM_DESCRIPTION)));
                item.setItemPrice(queryResult.getLong(queryResult.getColumnIndex(COL_PRICE)));
                item.setAmount(queryResult.getInt(queryResult.getColumnIndex(COL_AMOUNT)));
                boolean bool;
                int i = queryResult.getInt(queryResult.getColumnIndex(COL_IS_BOUGHT));
                if(i == 1) bool = true;
                else bool = false;
                item.setBoughtStatus(bool);
                result.add(item);
            } while (queryResult.moveToNext());
        }
        queryResult.close();
        return result;
    }
    void addTodayItem(){}
    void deleteTodayItem(){}
    ArrayList<TodayItem> getTodayItems(){
        return null;
    }
}
