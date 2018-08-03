package com.example.paula.recipeasy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.paula.recipeasy.database.CursorWrapper.LoginCursorWrapper;
import com.example.paula.recipeasy.database.RecipeasyDbSchema.LoginTable;
import com.example.paula.recipeasy.models.Login;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LoginLab extends DatabaseActions {

    private static LoginLab sDatabaseActions;

    public static LoginLab get(Context context){
        if(sDatabaseActions == null){
            sDatabaseActions = new LoginLab(context);
        }
        return sDatabaseActions;
    }

    public LoginLab(Context context) {
        super(context);
    }

    private static ContentValues getLoginValues(Login login){
        ContentValues values = new ContentValues();
        values.put(LoginTable.Cols.UUID, login.getId().toString());
        values.put(LoginTable.Cols.USERNAME, login.getUsername().toString());
        values.put(LoginTable.Cols.PASSWORD, login.getPassword().toString());

        return values;
    }

    private LoginCursorWrapper query(String whereClause, String[] whereArgs){
        Cursor cursor = getDatabase().query(
                LoginTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new LoginCursorWrapper(cursor);
    }

    public void addLogin(Login login){
        ContentValues values = getLoginValues(login);

        getDatabase().insert(LoginTable.NAME, null, values);
    }

    public void deleteLogin(UUID id){
        String uuidString = id.toString();

        getDatabase().delete(LoginTable.NAME, LoginTable.Cols.UUID + " = ?",
                new String[] {uuidString});
    }

    public void updateLogin(Login login){
        String uuidString = login.getId().toString();
        ContentValues values = getLoginValues(login);

        getDatabase().update(LoginTable.NAME, values, LoginTable.Cols.UUID + " = ?",
                new String[] {uuidString});
    }

    public Login getLogin(String username, String password){
        LoginCursorWrapper cursor = query(
                LoginTable.Cols.USERNAME + " = ? and "
                        + LoginTable.Cols.PASSWORD + " = ?",
                new String[] {username, password}
        );

        try{
            if(cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getLogin();
        } finally {
            cursor.close();
        }
    }

    public Login getLogin(UUID id){
        LoginCursorWrapper cursor = query(
                LoginTable.Cols.UUID + " = ?",
                new String[] {id.toString()}
        );

        try{
            if(cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getLogin();
        } finally {
            cursor.close();
        }
    }

    public List<Login> getLogins(){
        List<Login> logins = new ArrayList<>();

        LoginCursorWrapper cursor = query(null, null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                logins.add(cursor.getLogin());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return logins;
    }
}
