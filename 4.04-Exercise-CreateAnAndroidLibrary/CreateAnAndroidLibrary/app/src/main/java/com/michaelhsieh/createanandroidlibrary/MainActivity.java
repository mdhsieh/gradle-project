package com.michaelhsieh.createanandroidlibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.michaelhsieh.jokedisplay.JokeActivity;
import com.udacity.example.jokes.JokeSmith;

/* Add a button to the main activity that retrieves a joke from the Java
library, packages the joke as an intent extra, and launches the activity from
the Android library.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launchJokeActivity(View view) {
        Intent intent = new Intent(this, JokeActivity.class);
        JokeSmith jokeSmith = new JokeSmith();
        String joke = jokeSmith.tellAHandCraftedJoke();
        intent.putExtra(JokeActivity.KEY_JOKE, joke);
        startActivity(intent);
    }
}
