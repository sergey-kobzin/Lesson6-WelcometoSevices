package com.shpp.skobzin.lesson6_welcometosevices;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

    private final String LOG_TAG = "MainActivity";

    EditText timeText;
    EditText messageText;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeText = (EditText) findViewById(R.id.timeText);
        messageText = (EditText) findViewById(R.id.messageText);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                timeText.setText(intent.getStringExtra(Constants.EXTRA_TIME));
                messageText.setText(intent.getStringExtra(Constants.EXTRA_MESSAGE));
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter(Constants.BROADCAST_ACTION));

        if (NotificationService.isAlreadyRun()) {
            Log.i(LOG_TAG, "onCreate: alreadyRun");
            Intent notificationServiceIntent = new Intent(this, NotificationService.class);
            startService(notificationServiceIntent.putExtra(Constants.EXTRA_ACTION, "get"));
        } else {
            Log.i(LOG_TAG, "onCreate: notAlreadyRun");
            timeText.setText(R.string.timeTextDefault);
            messageText.setText(R.string.messageTextDefault);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i(LOG_TAG, "onDestroy");
        unregisterReceiver(broadcastReceiver);
    }

    public void onClick(View view) {
        Intent notificationServiceIntent = new Intent(this, NotificationService.class);
        switch (view.getId()) {
            case R.id.startButton:
                Log.i(LOG_TAG, "onClick: startButton");
                startService(notificationServiceIntent.putExtra(Constants.EXTRA_ACTION, "set")
                        .putExtra(Constants.EXTRA_TIME, timeText.getText().toString())
                        .putExtra(Constants.EXTRA_MESSAGE, messageText.getText().toString()));
                break;
            case R.id.stopButton:
                Log.i(LOG_TAG, "onClick: cancelButton");
                stopService(notificationServiceIntent);
        }
    }
}
