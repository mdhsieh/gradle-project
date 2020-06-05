package com.michaelhsieh.createajavalibrary;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.michaelhsieh.wizardjokes.JokeWizard;
import com.udacity.gradle.jokes.JokeSmith;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        TextView handcraftedJokeTextView = (TextView) rootView.findViewById(R.id.handcraftedJokeTextView);
        JokeSmith jokeSmith = new JokeSmith();
        handcraftedJokeTextView.setText(jokeSmith.getJoke());

        // Add another text view to MainActivityFragment and populate it with a joke
        // from JokeWizard.
        TextView wizardJokeTextView = (TextView) rootView.findViewById(R.id.wizardJokeTextView);
        JokeWizard jokeWizard = new JokeWizard();
        wizardJokeTextView.setText(jokeWizard.getJoke());

        return rootView;
    }
}
