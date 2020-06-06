package com.michaelhsieh.jokedisplay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class JokeActivity extends AppCompatActivity {

    public static String KEY_JOKE = "key_joke";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);

        TextView jokeDisplay = findViewById(R.id.tv_joke);

        // get the Intent that started this Activity
        Intent intent = getIntent();
        String joke = intent.getStringExtra(KEY_JOKE);
        if (joke != null && joke.length() > 0) {
            jokeDisplay.setText(joke);
        }
    }
}
