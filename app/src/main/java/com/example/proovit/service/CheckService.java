package com.example.proovit.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.proovit.CheckActivity;
import com.example.proovit.R;
import com.example.proovit.data.ArticleCountInDatabase;
import com.example.proovit.logic.ReportFakeActivity;
import com.example.proovit.utils.APIInterface;
import com.example.proovit.utils.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckService extends IntentService {

    private final String ADDRESS = "ADDRESS";
    private String receivedAddress;
    private final String CHANNEL_ID = "PROVEIT";

    APIInterface apiService;


        // Must create a default constructor
        public CheckService() {
            // Used to name the worker thread, important only for debugging.
            super("check-service");
        }

        @Override
        public void onCreate() {
            apiService = ApiClient.getClient().create(APIInterface.class);
            super.onCreate(); // if you override onCreate(), make sure to call super().
            // If a Context object is needed, call getApplicationContext() here.
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            // This describes what will happen when service is triggered
            receivedAddress = intent.getStringExtra(ADDRESS);
            System.out.println(receivedAddress);

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
                        ReportFakeActivity.class);
                PendingIntent pendingIntentReport = PendingIntent.getActivity(this, 0, intentReport, 0);

                builder.addAction(R.drawable.ic_stat_new, getResources().getString(R.string.report), pendingIntentReport);
            }



            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
            notificationManager.notify(1995, builder.build());
        }

        private String setNotificationTitle(String title) {
            return title;
        }

        private String setNotificationText(String text) {
            return text;
        }

}
