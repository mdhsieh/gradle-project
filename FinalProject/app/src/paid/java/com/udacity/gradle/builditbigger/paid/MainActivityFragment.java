package com.udacity.gradle.builditbigger.paid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.udacity.gradle.builditbigger.R;

/**
 * A placeholder fragment containing a simple view.
 * This fragment is specific to the paid version of the app.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        // don't show ads in the paid version
        Toast.makeText(getContext(), "PAID version", Toast.LENGTH_LONG).show();
        return root;
    }
}
