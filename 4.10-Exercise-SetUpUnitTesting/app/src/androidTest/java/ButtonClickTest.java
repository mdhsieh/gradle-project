import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.clickcounter.ClickActivity;
import com.example.android.clickcounter.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static android.support.test.espresso.Espresso.onView;

@RunWith(AndroidJUnit4.class)
public class ButtonClickTest {

    @Rule
    public ActivityTestRule<ClickActivity> activityRule
            = new ActivityTestRule<>(ClickActivity.class);

    @Test
    public void testClick() {
        onView(withId(R.id.click_count_text_view))
                .check(matches(withText("0")));
        onView(withId(R.id.click_button)).perform(click());
        onView(withId(R.id.click_count_text_view))
                .check(matches(withText("1")));
    }

}
