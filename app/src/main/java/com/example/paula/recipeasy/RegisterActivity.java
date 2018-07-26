package com.example.paula.recipeasy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.paula.recipeasy.database.LoginLab;
import com.example.paula.recipeasy.exception.PasswordException;
import com.example.paula.recipeasy.models.Login;

public class RegisterActivity extends AppCompatActivity {

    private EditText mUsername;
    private EditText mPassword;
    private EditText mPasswordConfirm;
    private Button mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUsername = findViewById(R.id.username_reg);
        mPassword = findViewById(R.id.password_reg);
        mPasswordConfirm = findViewById(R.id.password_confirm);
        mRegister = findViewById(R.id.register_btn);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();
                String passwordConfirm = mPasswordConfirm.getText().toString();

                try {
                    if (!password.equals(passwordConfirm)) {
                        throw new PasswordException("Password is different of Password Confirm.");
                    }
                }catch (PasswordException pe){
                    Toast.makeText(getApplicationContext(), pe.getMessage(), Toast.LENGTH_SHORT).show();
                }

                LoginLab.get(getApplicationContext()).addLogin(new Login(username, password));
                Toast.makeText(getApplicationContext(), "Register Success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
