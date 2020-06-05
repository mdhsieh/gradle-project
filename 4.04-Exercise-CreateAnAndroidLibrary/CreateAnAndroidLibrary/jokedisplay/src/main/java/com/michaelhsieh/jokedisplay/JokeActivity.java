package com.michaelhsieh.jokedisplay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/* Add a new activity to the library.
 */

public class JokeActivity extends AppCompatActivity {

    public static final String KEY_JOKE = "key_joke";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);

        TextView jokeDisplay = findViewById(R.id.tv_joke);

        /* Have the activity in the Android Library retrieve and display the joke.
         */

        Intent intent = getIntent();
        String joke = intent.getStringExtra(KEY_JOKE);
        if (joke != null && joke.length() > 0) {
            jokeDisplay.setText(joke);
        }
    }
}
