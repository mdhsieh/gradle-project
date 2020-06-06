package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.michaelhsieh.jokedisplay.JokeActivity;
import com.michaelhsieh.jokesource.JokeSource;

import static com.michaelhsieh.jokedisplay.JokeActivity.KEY_JOKE;
//import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tellJoke(View view) {
//        JokeSource jokeSource = new JokeSource();
//        Toast.makeText(this, jokeSource.getJoke(), Toast.LENGTH_SHORT).show();

        Intent jokeIntent = new Intent(this, JokeActivity.class);
        JokeSource jokeSource = new JokeSource();
        jokeIntent.putExtra(KEY_JOKE, jokeSource.getJoke());
        startActivity(jokeIntent);
    }


}
