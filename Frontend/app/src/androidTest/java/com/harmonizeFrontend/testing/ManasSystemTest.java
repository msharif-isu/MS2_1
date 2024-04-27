package com.harmonizeFrontend.testing;

import com.example.harmonizefrontend.*;

//import org.hamcrest.Description;
//import org.hamcrest.TypeSafeMatcher;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import androidx.test.espresso.Root;
//import androidx.test.espresso.ViewInteraction;
//import androidx.test.espresso.assertion.ViewAssertions;
//import androidx.test.espresso.matcher.ViewMatchers;
//import androidx.test.ext.junit.rules.ActivityScenarioRule;
//import androidx.test.filters.LargeTest;
//import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
//
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
//import static androidx.test.espresso.action.ViewActions.typeText;
//import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//import static androidx.test.espresso.matcher.ViewMatchers.withText;
//import static org.hamcrest.core.StringEndsWith.endsWith;
//import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
//import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.intent.Intents.intended;
//import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
//import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
//import androidx.test.espresso.intent.Intents;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.init;
import static androidx.test.espresso.intent.Intents.release;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.allOf;
import static org.junit.Assert.*;







import android.view.View;


/**
 * This testing file uses ActivityScenarioRule instead of ActivityTestRule
 * to demonstrate system testings cases
 */
@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest   // large execution time
public class ManasSystemTest {
    //    @Rule
//    public ActivityScenarioRule<navBar> activityScenarioRuleMain = new ActivityScenarioRule<>(navBar.class);
    private static final int SIMULATED_DELAY_MS = 2000;

    @Rule
    public ActivityScenarioRule<LoginScreen> activityScenarioRuleLogin = new ActivityScenarioRule<>(LoginScreen.class);

    @Test
    public void checkUserDetailsAfterLogin() {
        Intents.init();
        // Uses activityScenarioRuleLogin
        String username = "manasmathur2023";
        String password = "Backup890!";
        activityScenarioRuleLogin.getScenario().onActivity(activity -> {

        });

        // Writes in the username into the username box
        onView(withId(R.id.UsernameInput)).perform(clearText(), typeText(username), closeSoftKeyboard());

        // Writes in the password into the password box
        onView(withId(R.id.PasswordInput)).perform(clearText(), typeText(password), closeSoftKeyboard());


        // Clicks the login button
        onView(withId(R.id.Login)).perform(click());

        // Wait
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        intended(allOf( // Check if landed to the second activity and verify extras
                hasComponent(navBar.class.getName()),
                hasExtra("username", "manasmathur2023"),
                hasExtra("password", "Backup890!"),
                hasExtra("fragment", "profile")
        ));

        // Wait
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        // Check to see if R.id.usernameHolder matches username variable
        onView(withId(R.id.usernameHolder)).check(ViewAssertions.matches(withText(username)));

        // Check to see if password matches username variable
        onView(withId(R.id.hideunhide)).perform(click());
        onView(withId(R.id.password_text_view)).check(ViewAssertions.matches(withText(password)));
        onView(withId(R.id.hideunhide)).perform(click());

        Intents.release();
    }
    /*
    * This test checks the ability for the user to change their first and last name
     */
    @Test
    public void removeFirstLastName() {
        Intents.init();
        // Uses activityScenarioRuleLogin
        String username = "manasmathur2023";
        String password = "Backup890!";
        String newFirst = "Doug";
        String newLast = "Dimmadome";
        activityScenarioRuleLogin.getScenario().onActivity(activity -> {

        });

        // Writes in the username into the username box
        onView(withId(R.id.UsernameInput)).perform(clearText(), typeText(username), closeSoftKeyboard());

        // Writes in the password into the password box
        onView(withId(R.id.PasswordInput)).perform(clearText(), typeText(password), closeSoftKeyboard());


        // Clicks the login button
        onView(withId(R.id.Login)).perform(click());

        // Wait
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        intended(allOf( // Check if landed to the second activity and verify extras
                hasComponent(navBar.class.getName()),
                hasExtra("username", "manasmathur2023"),
                hasExtra("password", "Backup890!"),
                hasExtra("fragment", "profile")
        ));

        // Wait
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        // Click edit button
        onView(withId(R.id.updatePrefs)).perform(click());
        // Give new name
        onView(withId(R.id.firstname)).perform(clearText(), closeSoftKeyboard());
        onView(withId(R.id.lastname)).perform(clearText(), closeSoftKeyboard());

        // Finalize edits
        onView(withId(R.id.updatePrefs)).perform(click());


        Intents.release();
    }

    /*
    * This test checks the ability to log out
     */
    @Test
    public void checkAbilityToLogOut() {
        Intents.init();
        // Uses activityScenarioRuleLogin
        String username = "manasmathur2023";
        String password = "Backup890!";
        activityScenarioRuleLogin.getScenario().onActivity(activity -> {

        });

        // Writes in the username into the username box
        onView(withId(R.id.UsernameInput)).perform(clearText(), typeText(username), closeSoftKeyboard());

        // Writes in the password into the password box
        onView(withId(R.id.PasswordInput)).perform(clearText(), typeText(password), closeSoftKeyboard());


        // Clicks the login button
        onView(withId(R.id.Login)).perform(click());

        // Wait
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        intended(allOf( // Check if landed to the second activity and verify extras
                hasComponent(navBar.class.getName()),
                hasExtra("username", "manasmathur2023"),
                hasExtra("password", "Backup890!"),
                hasExtra("fragment", "profile")
        ));

        // Wait
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        // Check to see if R.id.usernameHolder matches username variable
        onView(withId(R.id.usernameHolder)).check(ViewAssertions.matches(withText(username)));

        // Check to see if password matches username variable
        onView(withId(R.id.hideunhide)).perform(click());
        onView(withId(R.id.password_text_view)).check(ViewAssertions.matches(withText(password)));
        onView(withId(R.id.hideunhide)).perform(click());

        onView(withId(R.id.logOut)).perform(click());

        // Wait
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        intended(allOf( // Check if landed to the second activity and verify extras
                hasComponent(LoginScreen.class.getName())
        ));

        Intents.release();
    }

    @Test
    public void RegisterAndDelete() {
        Intents.init();
        // Uses activityScenarioRuleLogin
        String username = "SystemTestAcc3";
        String password = "Backup890!";
        String firstName = "Doug";
        String lastName = "Dimmadome";
        activityScenarioRuleLogin.getScenario().onActivity(activity -> {

        });

        // Clicks the Register button
        onView(withId(R.id.Register)).perform(click());

        // Wait
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        intended(allOf( // Check if landed to the second activity and verify extras
                hasComponent(RegistrationScreen.class.getName())
        ));

        // Wait
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        // Enters in first name, last name, user name, password, and re-enter password
        onView(withId(R.id.firstName)).perform(clearText(), typeText(firstName), closeSoftKeyboard());
        onView(withId(R.id.lastName)).perform(clearText(), typeText(lastName), closeSoftKeyboard());
        onView(withId(R.id.UsernameInput)).perform(clearText(), typeText(username), closeSoftKeyboard());
        onView(withId(R.id.PasswordInput)).perform(clearText(), typeText(password), closeSoftKeyboard());
        onView(withId(R.id.reenterPassword)).perform(clearText(), typeText(password), closeSoftKeyboard());

        // Register
        onView(withId(R.id.Register)).perform(click());

        // Wait
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        intended(allOf( // Check if landed to the second activity and verify extras
                hasComponent(RegistrationScreen.class.getName())
        ));

        onView(withId(R.id.usernameHolder)).check(ViewAssertions.matches(withText(username)));

        onView(withId(R.id.delAccount)).perform(click());


        Intents.release();
    }

}

