package com.example.proovit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proovit.data.User;
import com.example.proovit.data.UserUUID;
import com.example.proovit.utils.APIInterface;
import com.example.proovit.utils.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    Button loginButton;
    EditText loginEditText, passwordEditText;
    SharedPreferences preferences;
    APIInterface apiService;
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
        apiService = ApiClient.getClient().create(APIInterface.class);

        preferences = this.getSharedPreferences("com.example.proovit", Context.MODE_PRIVATE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String login = loginEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                User loginUser = new User(login, password);
                Call<UserUUID> loggedUser = apiService.loginUser(loginUser);
                loggedUser.enqueue(new Callback<UserUUID>() {
                    @Override
                    public void onResponse(Call<UserUUID> call, Response<UserUUID> response) {
                        Log.d(TAG, "onResponse: call::: " + call.toString());
                        Log.d(TAG, "onResponse: response::: " + response.body().getUUID());
                        UserUUID logged = response.body();
                        if(logged != null) {
                            Log.d(TAG, "onResponse: logged body::: " + logged.getUUID());
                            preferences.edit().putString("uuid", logged.getUUID()).apply();
                            Intent intent = new Intent(getApplicationContext(),
                                    MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserUUID> call, Throwable t) {
                        Log.e(TAG, "onFailure: login problem!");
                    }
                });
                }
        });
    }

    private boolean checkIfLogged() {
        // xD

        String userUUID = preferences.getString("uuid", "");
        if(!userUUID.equals("")) return true;
        else return false;
    }
}
