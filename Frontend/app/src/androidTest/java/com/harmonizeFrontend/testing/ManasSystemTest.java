package com.harmonizeFrontend.testing;

import com.example.harmonizefrontend.*;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.Root;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
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

//    @Rule
//    public ActivityScenarioRule<navBar> activityScenarioRuleMain = new ActivityScenarioRule<>(navBar.class);

    @Rule
    public ActivityScenarioRule<LoginScreen> activityScenarioRuleLogin = new ActivityScenarioRule<>(LoginScreen.class);

//    @Rule
//    public ActivityScenarioRule<RegistrationScreen> activityScenarioRuleRegistration = new ActivityScenarioRule<>(RegistrationScreen.class);

//    /*
//     * Test Case 1: User logs in with invalid re-entered password. Test should check for if a toast posts up and tells the user that the second password is incorrect.
//     * User then attempts to re-enter the second password and logs in successfully.
//     */
//    @Test
//    public void inputInvalidRegistrationDetails() {
//        String first = "Doug";
//        String last = "Dimmadome";
//        String username = "DougDimmadome45";
//        String password = "DimmaDoug45!";
//        String wrongPass = "Doug2";
//        activityScenarioRuleRegistration.getScenario().onActivity(activity -> {
////           activity.username = "Null";
////           activity.first = "Doug";
////           activity.last = "Dimmadome";
////           activity.password = "pass";
////           activity.checkPass = "pass";
//        });
//
//        // Writes in first name into the first name box
//        onView(withId(R.id.firstName)).perform(typeText(first), closeSoftKeyboard());
//        // Writes in last name into the last name box
//        onView(withId(R.id.lastName)).perform(typeText(last), closeSoftKeyboard());
//        // Writes in the username into the last name box
//        onView(withId(R.id.UsernameInput)).perform(typeText(username), closeSoftKeyboard());
//        // Writes in the password into the password box
//        onView(withId(R.id.PasswordInput)).perform(typeText(password), closeSoftKeyboard());
//        // Writes in the double-check password into the re-enter password box
//        onView(withId(R.id.reenterPassword)).perform(typeText(wrongPass), closeSoftKeyboard());
//
//        // Clicks the register button
//        onView(withId(R.id.Register)).perform(click());
//
//        // Check to see if the toast message with "Please ensure that the passwords match" is displayed}
//        //TODO FINISH THIS
//
//    }

    /*
    * Logs a user in and verifies that credentials are correct in the AccountPreferences fragment
     */

    @Test
    public void checkUserDetailsAfterLogin() {
        // Uses activityScenarioRuleLogin
        String username = "Manasmathur2023";
        String password = "Backup890!";
        activityScenarioRuleLogin.getScenario().onActivity(activity -> {

        });

        // Writes in the username into the username box
        onView(withId(R.id.UsernameInput)).perform(typeText(username), closeSoftKeyboard());

        // Writes in the password into the password box
        onView(withId(R.id.PasswordInput)).perform(typeText(password), closeSoftKeyboard());

        // Clicks the login button
        onView(withId(R.id.Login)).perform(click());

        // Check to see if its taken you to the navbar screen and the profile Preferences fragment
        ViewInteraction dashboard = onView(withText("This is profile preferences fragment")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Check to see if R.id.usernameHolder matches username variable
        onView(withId(R.id.usernameHolder)).check(ViewAssertions.matches(withText(username)));

        // Check to see if password matches username variable
        onView(withId(R.id.hideunhide)).perform(click());
        onView(withId(R.id.password_text_view)).check(ViewAssertions.matches(withText(password)));
        onView(withId(R.id.hideunhide)).perform(click());
    }


    /*
    * This test checks the ability for the user to change their first and last name
     */
    @Test
    public void changeFirstLastName() {
        String username = "Manasmathur2023";
        String password = "Backup890!";
        activityScenarioRuleLogin.getScenario().onActivity(activity -> {

        });

        // Writes in the username into the username box
        onView(withId(R.id.UsernameInput)).perform(typeText(username), closeSoftKeyboard());

        // Writes in the password into the password box
        onView(withId(R.id.PasswordInput)).perform(typeText(password), closeSoftKeyboard());

        // Clicks the login button
        onView(withId(R.id.Login)).perform(click());

        // Check to see if its taken you to the navbar screen and the profile Preferences fragment
        ViewInteraction dashboard = onView(withText("This is profile preferences fragment")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Turn on edit mode
        onView(withId(R.id.updatePrefs)).perform(click());
        // Edit mode on
        onView(withId(R.id.firstName)).perform(typeText("Sanam"), closeSoftKeyboard());
        onView(withId(R.id.lastName)).perform(typeText("Ruhtam"), closeSoftKeyboard());

        onView(withId(R.id.updatePrefs)).perform(click());
        // Edit mode off

        // Wait 1500ms
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        // Check to see if first name and last name has saved
        onView(withId(R.id.firstname)).check(ViewAssertions.matches(withText("Sanam")));
        onView(withId(R.id.lastname)).check(ViewAssertions.matches(withText("Ruhtam")));

    }

}

