package com.shpp.skobzin.lesson6_welcometosevices;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

    EditText timeText;
    EditText messageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("MainActivity", "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeText = (EditText) findViewById(R.id.timeText);
        messageText = (EditText) findViewById(R.id.messageText);

        if (NotificationService.alreadyRun) {
            SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
            timeText.setText(sharedPreferences.getString("time", getString(R.string.timeTextDefault)));
            messageText.setText(sharedPreferences.getString("message", getString(R.string.messageTextDefault)));
        } else {
            timeText.setText(R.string.timeTextDefault);
            messageText.setText(R.string.messageTextDefault);
        }
    }

    @Override
    protected void onDestroy() {
        Log.i("MainActivity", "onDestroy");

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        sharedPreferences.edit()
                .putString("time", timeText.getText().toString())
                .putString("message", messageText.getText().toString())
                .commit();

        super.onDestroy();
    }

    public void onClick(View view) {
        Intent serviceIntent = new Intent(this, NotificationService.class);
        switch (view.getId()) {
            case R.id.startButton:
                Log.i("MainActivity", "onClick: startButton");

                startService(serviceIntent.putExtra("time", timeText.getText().toString()).putExtra("message", messageText.getText().toString()));
                break;
            case R.id.stopButton:
                Log.i("MainActivity", "onClick: cancelButton");

                stopService(serviceIntent);
        }
    }
}
