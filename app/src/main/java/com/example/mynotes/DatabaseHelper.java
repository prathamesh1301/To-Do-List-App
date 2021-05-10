package com.example.mynotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="taskdb";
    private static final String TABLE_NAME="tasktb";
    private static final String COL1="ID";
    private static final String COL2="TASK";
    private static final String COL3="STATUS";


    private SQLiteDatabase db;
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT,TASK TEXT,STATUS INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean addTask(String task){
        db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(COL2,task);
        values.put(COL3,0);
        long result=db.insert(TABLE_NAME,null,values);
        if(result==-1)
            return false;
        else
            return true;
    }
    public Cursor getAllData()
    {
        db=this.getWritableDatabase();
        Cursor res=db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        return res;
    }
    public void deleteItem(int id){
        db=this.getWritableDatabase();
        db.delete(TABLE_NAME,"ID=?",new String[] {String.valueOf(id)});
    }
    public void updateStatus(String task,Integer status,Integer id){
        db=this.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(COL2,task);
        values.put(COL3,status);
        db.update(TABLE_NAME,values,"ID=?",new String[]{String.valueOf(id)});
    }
}
