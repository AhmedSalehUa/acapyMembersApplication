package com.acpay.acapymembers.Background;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public final static String CHANNEL_ID = "AcapyMembers";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            NotificationChannel service = new NotificationChannel(
                    CHANNEL_ID,
                    "Acapy Members Location",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(service);
        }

    }
}
