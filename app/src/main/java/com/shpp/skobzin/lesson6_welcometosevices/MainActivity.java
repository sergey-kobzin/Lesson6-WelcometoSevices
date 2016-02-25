package com.shpp.skobzin.lesson6_welcometosevices;

import android.app.Activity;
import android.content.Intent;
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

        if (savedInstanceState == null) {
            timeText.setText(R.string.timeTextDefault);
            messageText.setText(R.string.messageTextDefault);
        } else {
            timeText.setText(savedInstanceState.getString("time"));
            messageText.setText(savedInstanceState.getString("message"));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.i("MainActivity", "onSaveInstanceState");

        outState.putString("time", timeText.getText().toString());
        outState.putString("message", messageText.getText().toString());
    }

    public void onClick(View view) {
        Intent serviceIntent = new Intent(this, NotificationService.class);
        switch (view.getId()) {
            case R.id.startButton:
                Log.i("MainActivity", "onClick: startButton");

                serviceIntent.putExtra("time", timeText.getText().toString());
                serviceIntent.putExtra("message", messageText.getText().toString());
                startService(serviceIntent);
                break;
            case R.id.stopButton:
                Log.i("MainActivity", "onClick: cancelButton");

                stopService(serviceIntent);
        }
    }
}
