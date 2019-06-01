package com.example.proovit.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.proovit.CheckActivity;
import com.example.proovit.R;

public class CheckService extends IntentService {

    private final String ADDRESS = "ADDRESS";
    private String receivedAddress;
    private final String CHANNEL_ID = "PROVEIT";
        // Must create a default constructor
        public CheckService() {
            // Used to name the worker thread, important only for debugging.
            super("check-service");
        }

        @Override
        public void onCreate() {
            super.onCreate(); // if you override onCreate(), make sure to call super().
            // If a Context object is needed, call getApplicationContext() here.
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            // This describes what will happen when service is triggered
            receivedAddress = intent.getStringExtra(ADDRESS);
            System.out.println(receivedAddress);
            String notificationTitle = setNotificationTitle();
            String notificationText = setNotificationText();
            runNotification(notificationText, notificationTitle);

        }

        private void runNotification(String description, String textTitle) {
            Intent intent = new Intent(this, CheckActivity.class);
            intent.putExtra(ADDRESS,receivedAddress);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.validation)
                    .setContentTitle(textTitle)
                    .setContentText(description)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
            notificationManager.notify(1995, builder.build());
        }

        private String setNotificationTitle() {
            return "Nazwa Powiadomienia";
        }

        private String setNotificationText() {
            return "Treść powiadomienia";
        }

}
