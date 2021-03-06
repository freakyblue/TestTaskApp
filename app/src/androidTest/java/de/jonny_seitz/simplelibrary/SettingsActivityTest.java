package de.jonny_seitz.simplelibrary;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.supportsInputMethods;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

/**
 * Created by Jonny on 23/01/18.
 */

@RunWith(AndroidJUnit4.class)
public class SettingsActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void displayTest() throws Exception {
        Thread.sleep(500);
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText("Settings")).perform(click());
        onView(withText(R.string.settings)).check(matches(isDisplayed()));
        onView(withText(R.string.title_library_name)).check(matches(isDisplayed()));
        onView(withText(R.string.description_library_name)).check(matches(isDisplayed()));
    }

    @Test
    public void nameLibraryTest() throws Exception {
        Thread.sleep(500);
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText("Settings")).perform(click());
        onView(withText("Library name")).perform(click());
        onView(supportsInputMethods())
                .perform(clearText())
                .perform(typeText("Congress Library"));
        onView(withText("OK")).perform(click());
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
        onView(allOf(
                isAssignableFrom(TextView.class),
                withParent(isAssignableFrom(Toolbar.class))
        )).check(matches(withText("Congress Library")));
        Thread.sleep(3000);
    }

}
