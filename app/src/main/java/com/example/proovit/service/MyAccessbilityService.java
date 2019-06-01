package com.example.proovit.service;

import android.accessibilityservice.AccessibilityService;
import android.net.Uri;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.net.URI;
import java.net.URISyntaxException;

import static android.support.constraint.Constraints.TAG;

public class MyAccessbilityService extends AccessibilityService {

    private String lastAddress;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        System.out.println("connected");
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
                    lastAddress = domain;
                    //TODO: TUTAJ OBSŁUGUJEMY POSTA ZE SPRAWDZENIEM
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

    private static void debug(Object object) {
        Log.d(TAG, object.toString());
    }

    @Override
    public void onInterrupt() {

    }
}
