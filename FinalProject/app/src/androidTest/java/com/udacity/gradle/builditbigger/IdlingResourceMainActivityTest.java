package com.udacity.gradle.builditbigger;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/** Tests that the Async task which starts when the button is clicked in MainActivity
 *  retrieves a joke.
 */
@RunWith(JUnit4.class)
public class IdlingResourceMainActivityTest {

    private String JOKE_TEXT = "What do you call an alligator detective? An investi-gator.";

    private IdlingResource idlingResource;

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    // test is run before MainActivity is created
    // Registers any resource that needs to be synchronized with Espresso before the test is run.
    @Before
    public void registerIdlingResource() {
        idlingResource = activityTestRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(idlingResource);
    }

    @Test
    public void idlingResourceTest() {
        // click the button to retrieve joke
        onView(withId(R.id.btn_tell_joke)).perform(click());
        // after a delay, JokeActivity should open with the retrieved joke displayed
        onView(withId(R.id.tv_joke)).check(matches(withText(JOKE_TEXT)));
    }

    // Remember to unregister resources when not needed to avoid malfunction.
    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            Espresso.unregisterIdlingResources(idlingResource);
        }
    }
}
