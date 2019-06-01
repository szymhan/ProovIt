package com.example.proovit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    EditText loginEditText, passwordEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setElements();
    }

    private void setElements() {
        loginButton = findViewById(R.id.login_button);
        passwordEditText = findViewById(R.id.login_password);
        loginEditText = findViewById(R.id.login_login);
    }
}
