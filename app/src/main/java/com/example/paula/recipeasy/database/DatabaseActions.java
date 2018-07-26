package com.example.paula.recipeasy.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.paula.recipeasy.database.Helper.DatabaseHelper;

public class DatabaseActions {

    private static DatabaseActions sDatabaseActions;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static DatabaseActions get(Context context){
        if(sDatabaseActions == null){
            sDatabaseActions = new DatabaseActions(context);
        }
        return sDatabaseActions;
    }

    public DatabaseActions(Context context) {
        mContext = context;
        mDatabase = new DatabaseHelper(mContext).getWritableDatabase();
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public SQLiteDatabase getDatabase() {
        return mDatabase;
    }

    public void setDatabase(SQLiteDatabase database) {
        mDatabase = database;
    }
}
