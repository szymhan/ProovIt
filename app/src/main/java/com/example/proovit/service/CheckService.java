package com.example.proovit.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.proovit.CheckActivity;
import com.example.proovit.MainActivity;
import com.example.proovit.R;
import com.example.proovit.data.ArticleCountInDatabase;
import com.example.proovit.utils.APIInterface;
import com.example.proovit.utils.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CheckService extends IntentService {
    private static final String TAG = "CheckService";

    private static final String TARGET = "TARGET";
    private final String ADDRESS = "ADDRESS";
    private String receivedAddress;
    private final String CHANNEL_ID = "PROVEIT";
    private final String REPORT = "Report";

    APIInterface apiService;
    SharedPreferences preferences;


        // Must create a default constructor
        public CheckService() {
            // Used to name the worker thread, important only for debugging.
            super("check-service");
        }

        @Override
        public void onCreate() {
            apiService = ApiClient.getClient().create(APIInterface.class);
            preferences = this.getSharedPreferences("com.example.proovit", Context.MODE_PRIVATE);
            super.onCreate(); // if you override onCreate(), make sure to call super().
            // If a Context object is needed, call getApplicationContext() here.
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            // This describes what will happen when service is triggered
            receivedAddress = intent.getStringExtra(ADDRESS);
            preferences.edit().putString("lastSite", receivedAddress).apply();
            preferences.edit().putBoolean("used",false).commit();

            System.out.println(receivedAddress);
            Log.e(TAG, "onHandleIntent: address"+receivedAddress );

            Call<ArticleCountInDatabase> call = apiService.checkNumberOfArticleReports(receivedAddress);
            call.enqueue(new Callback<ArticleCountInDatabase>() {
                String notificationTitle;
                boolean enableAddingArticle = false;
                @Override
                public void onResponse(Call<ArticleCountInDatabase> call, Response<ArticleCountInDatabase> response) {
                    ArticleCountInDatabase article = response.body();
                    if(article.getReportCount() == 0) {
                        enableAddingArticle = true;
                        notificationTitle = setNotificationTitle(getResources().getString(R.string.articleNotReported));

                    }
                    else {
                        notificationTitle = setNotificationTitle(getResources().getString(R.string.articleAlreadyReported));
                    }

                        String notificationText = setNotificationText(getResources().getString(R.string.numberOfReports) + " " + String.valueOf(article.getReportCount()));
                    runNotification(notificationText, notificationTitle, enableAddingArticle);
                }

                @Override
                public void onFailure(Call<ArticleCountInDatabase> call, Throwable t) {
                    String notificationTitle = setNotificationTitle("blad");
                    String notificationText = setNotificationText("blad");
                    runNotification(notificationText, notificationTitle, enableAddingArticle);
                }
            });




        }

        private void runNotification(String description, String textTitle, boolean enableAddingArticle) {
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

            if(enableAddingArticle) {

                Intent intentReport = new Intent(getApplicationContext(),
                        MainActivity.class);
                //intentReport.putExtra("last", receivedAddress);
                intentReport.putExtra(TARGET,REPORT);

                PendingIntent pendingIntentReport = PendingIntent.getActivity(this, 0, intentReport, 0);

                killNotifications();
                builder.addAction(R.drawable.ic_stat_new, getResources().getString(R.string.report), pendingIntentReport);


            }



            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
            notificationManager.notify(1995, builder.build());
        }

    private void killNotifications() {
        NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancelAll();
    }

        private String setNotificationTitle(String title) {
            return title;
        }

        private String setNotificationText(String text) {
            return text;
        }

}
