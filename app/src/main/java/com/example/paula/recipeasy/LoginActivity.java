package com.example.paula.recipeasy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paula.recipeasy.database.LoginLab;
import com.example.paula.recipeasy.models.Login;

import javax.security.auth.login.LoginException;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsername;
    private EditText mPassword;
    private Button mLogin;
    private TextView mRegisterLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mLogin = findViewById(R.id.login_btn);
        mRegisterLink = findViewById(R.id.register_link);

        mRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();

                Login login = LoginLab.get(getApplicationContext()).getLogin(username, password);

                if (login == null) {
                    try {
                        throw new LoginException("The combination Username and Password don't exist");
                    } catch (LoginException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                    Intent intent = RecipePagerActivity.newIntent(getApplicationContext(), login.getId());
                    startActivity(intent);
                }
            }
        });
    }
}
