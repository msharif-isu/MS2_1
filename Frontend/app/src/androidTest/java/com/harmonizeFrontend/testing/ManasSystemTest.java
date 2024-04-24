package com.harmonizeFrontend.testing;

import com.example.harmonizefrontend.*;
//import com.example.harmonizefrontend.navBar;
//import com.example.harmonizefrontend.LoginScreen;
//import com.example.harmonizefrontend.RegistrationScreen;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import android.view.View;

/**
 * This testing file uses ActivityScenarioRule instead of ActivityTestRule
 * to demonstrate system testings cases
 */
@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest   // large execution time
public class ManasSystemTest {

    private static final int SIMULATED_DELAY_MS = 500;

    @Rule
    public ActivityScenarioRule<navBar> activityScenarioRuleMain = new ActivityScenarioRule<>(navBar.class);

    @Rule
    public ActivityScenarioRule<LoginScreen> activityScenarioRuleLogin = new ActivityScenarioRule<>(LoginScreen.class);

    @Rule
    public ActivityScenarioRule<RegistrationScreen> activityScenarioRuleRegistration = new ActivityScenarioRule<>(RegistrationScreen.class);

    /*
     * Test Case 1: User logs in with invalid re-entered password. Test should check for if a toast posts up and tells the user that the second password is incorrect.
     * User then attempts to re-enter the second password and logs in successfully.
     */
    @Test
    public void inputInvalidRegistrationDetails() {
        String first = "Doug";
        String last = "Dimmadome";
        String username = "DougDimmadome45";
        String password = "DimmaDoug45!";
        String wrongPass = "Doug2";
        activityScenarioRuleRegistration.getScenario().onActivity(activity -> {
//           activity.username = "Null";
//           activity.first = "Doug";
//           activity.last = "Dimmadome";
//           activity.password = "pass";
//           activity.checkPass = "pass";
        });

        // Writes in first name into the first name box
        onView(withId(R.id.firstName)).perform(typeText(first), closeSoftKeyboard());
        // Writes in last name into the last name box
        onView(withId(R.id.lastName)).perform(typeText(last), closeSoftKeyboard());
        // Writes in the username into the last name box
        onView(withId(R.id.UsernameInput)).perform(typeText(username), closeSoftKeyboard());
        // Writes in the password into the password box
        onView(withId(R.id.PasswordInput)).perform(typeText(password), closeSoftKeyboard());
        // Writes in the double-check password into the re-enter password box
        onView(withId(R.id.reenterPassword)).perform(typeText(wrongPass), closeSoftKeyboard());

        // Clicks the register button
        onView(withId(R.id.Register)).perform(click());

        // Check to see if the toast message with "Please ensure that the passwords match" is displayed
        onView(withText("Please ensure that the passwords match"))
                .inRoot(withDecorView(ViewMatchers.is(activityScenarioRuleRegistration.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

}

