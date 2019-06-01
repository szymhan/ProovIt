package com.example.proovit.service;

import android.accessibilityservice.AccessibilityService;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;


import com.example.proovit.R;
import com.example.proovit.data.DomainCountInDatabase;
import com.example.proovit.utils.APIInterface;
import com.example.proovit.utils.ApiClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class MyAccessbilityService extends AccessibilityService {

    private static final String TARGET = "TARGET";
    private final String ADDRESS = "ADDRESS";
    private String receivedAddress;
    private final String CHANNEL_ID = "PROVEIT";
    private final String REPORT = "Report";
    private String lastAddress = "";
    APIInterface apiService;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        System.out.println("connected");
        apiService = ApiClient.getClient().create(APIInterface.class);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if(AccessibilityEvent.eventTypeToString(event.getEventType()).contains("WINDOW")){
            AccessibilityNodeInfo nodeInfo = event.getSource();
            dfs(nodeInfo);
        }
    }

    public void dfs(AccessibilityNodeInfo info){
        if(info == null)
            return;
        if(info.getText() != null && info.getText().length() > 0) {
            String adres = info.getText().toString();
            String domain;
            //System.out.println(info.getText() + " class: " + info.getClassName());
            try {
                URI addressUrl = new URI(adres);
                domain = addressUrl.getHost();
                if (!lastAddress.equals(domain)){
                    if (domain != null) {
                        lastAddress = domain;
                        //TODO: TUTAJ OBSŁUGUJEMY POSTA ZE SPRAWDZENIEM
                        sendReport(domain);
                        return;
                    }
                }

            } catch (URISyntaxException e) {
                Log.e("WRONG DOMAIN ADDRESS", adres + " is not an valid address");
            }
        }
        for(int i=0;i<info.getChildCount();i++){
            AccessibilityNodeInfo child = info.getChild(i);
            dfs(child);
            if(child != null){
                child.recycle();
            }
        }
    }

    private void sendReport(final String address) {

        Call<DomainCountInDatabase> call = apiService.checkDomain(address);
        call.enqueue(new Callback<DomainCountInDatabase>() {
            String notificationTitle;
            @Override
            public void onResponse(Call<DomainCountInDatabase> call, Response<DomainCountInDatabase> response) {
                DomainCountInDatabase count = response.body();
                if( count.getCount() != null && count.getCount() == 0) {
                   return;
                }
                else {
                    notificationTitle = setNotificationTitle(address);
                }

                String notificationText = setNotificationText("Uwaga, ta domena została zgłoszona "
                        + String.valueOf(count.getCount()) + " razy.");
                runNotification(notificationText, notificationTitle);
            }

            @Override
            public void onFailure(Call<DomainCountInDatabase> call, Throwable t) {
                Log.e("Accessibility Service", "Podana domena została przetworzona nieprawidłowo.");
//                String notificationTitle = setNotificationTitle("blad");
//                String notificationText = setNotificationText("blad");
//                runNotification(notificationText, notificationTitle, enableAddingArticle);
            }
        });

    }

    private static void debug(Object object) {
        Log.d(TAG, object.toString());
    }

    @Override
    public void onInterrupt() {

    }

    private void runNotification(String description, String textTitle) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.validation)
                .setContentTitle(textTitle)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(1996, builder.build());
    }

    private String setNotificationTitle(String title) {
        return title;
    }

    private String setNotificationText(String text) {
        return text;
    }

}
