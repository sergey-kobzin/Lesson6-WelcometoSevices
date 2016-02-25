package com.shpp.skobzin.lesson6_welcometosevices;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class NotificationService extends Service {

    final Handler handler = new Handler();

    @Override
    public void onCreate() {
        Log.i("Notification Service", "onCreate");

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Notification Service", "onStartCommand");

        String timeText = intent.getStringExtra("time");
        String messageText = intent.getStringExtra("message").replace("[time]", timeText);

        final int time = Integer.parseInt(timeText) * 60 * 1000;

        final Notification notification = new Notification.BigTextStyle(new Notification.Builder(this)
                .setTicker(messageText)
                .setSmallIcon(R.drawable.ic_alarm_white_18dp)
                .setContentText(messageText)
                .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT), 0))
                .setAutoCancel(true))
                .bigText(messageText)
                .build();

        final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        handler.removeCallbacks(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notificationManager.notify(0, notification);
                handler.postDelayed(this, time);
            }
        }, time);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i("Notification Service", "onDestroy");

        handler.removeCallbacksAndMessages(null);

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // It's just a cap
        return null;
    }
}
