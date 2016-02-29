package com.shpp.skobzin.lesson6_welcometosevices;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class NotificationService extends Service {

    private final String LOG_TAG = "NotificationService";

    private static boolean alreadyRun = false;
    private String mTime;
    private String mMessage;
    private final Handler handler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(LOG_TAG, "onCreate");
        alreadyRun = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(LOG_TAG, "onStartCommand");

        switch (intent.getStringExtra(Constants.EXTRA_ACTION)) {
            case "set":
                Log.i(LOG_TAG, "onStartCommand: set");
                mTime = intent.getStringExtra(Constants.EXTRA_TIME);
                mMessage = intent.getStringExtra(Constants.EXTRA_MESSAGE).replace("[time]", mTime);

                final int time = Integer.parseInt(mTime) * 60 * 1000;

                final Notification notification = new Notification.BigTextStyle(new Notification.Builder(this)
                        .setTicker(mMessage)
                        .setSmallIcon(R.drawable.ic_alarm_white_18dp)
                        .setContentText(mMessage)
                        .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT), PendingIntent.FLAG_UPDATE_CURRENT))
                        .setAutoCancel(true))
                        .bigText(mMessage)
                        .build();

                final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        notificationManager.notify(0, notification);
                        handler.postDelayed(this, time);
                    }
                }, time);
                break;
            case "get":
                Log.i(LOG_TAG, "onStartCommand: get");
                sendBroadcast(new Intent(Constants.BROADCAST_ACTION).putExtra(Constants.EXTRA_TIME, mTime).putExtra(Constants.EXTRA_MESSAGE, mMessage));
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i(LOG_TAG, "onDestroy");
        handler.removeCallbacksAndMessages(null);
        alreadyRun = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static boolean isAlreadyRun() {
        return alreadyRun;
    }
}
