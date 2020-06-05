package com.michaelhsieh.wizardjokes;

import com.udacity.gradle.jokes.JokeSmith;

public class JokeWizard {
    // Implement a getJoke() method on JokeWizard that uses the getJoke() method
    // on JokeSmith
    public String getJoke() {
        JokeSmith jokeSmith = new JokeSmith();
        return jokeSmith.getJoke() + " by the JokeWizard";
    }
}
