package com.example.proovit;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private final String CHANNEL_ID = "PROVEIT";
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        createNotificationChannel();



        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if(!checkIfLogged()) {
                    intent = new Intent(getApplicationContext(),
                            LoginActivity.class);
                } else {
                    intent = new Intent(getApplicationContext(),
                            MainActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 1000);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    private boolean checkIfLogged() {
        // xD
        preferences = this.getSharedPreferences("com.example.proovit", Context.MODE_PRIVATE);
        String userUUID = preferences.getString("uuid", "");
        if(!userUUID.equals("")) return true;
        else return false;
    }
}
