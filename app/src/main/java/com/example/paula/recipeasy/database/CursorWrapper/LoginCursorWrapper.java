package com.example.paula.recipeasy.database.CursorWrapper;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.paula.recipeasy.database.RecipeasyDbSchema.LoginTable;
import com.example.paula.recipeasy.models.Login;

import java.util.UUID;

public class LoginCursorWrapper extends CursorWrapper {

    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public LoginCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Login getLogin(){
        String uuidString = getString(getColumnIndex(LoginTable.Cols.UUID));
        String username = getString(getColumnIndex(LoginTable.Cols.USERNAME));
        String password = getString(getColumnIndex(LoginTable.Cols.PASSWORD));

        Login login = new Login(UUID.fromString(uuidString));
        login.setUsername(username);
        login.setPassword(password);

        return login;
    }
}
