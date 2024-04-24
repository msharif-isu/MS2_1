package com.harmonizeFrontend.testing;

import com.example.harmonizefrontend.navBar;
import com.example.harmonizefrontend.LoginScreen;
import com.example.harmonizefrontend.RegistrationScreen;
import org.junit.Rule;
import org.testing.annotations.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
//import static org.hamcrest.core.StringEndsWith.endsWith;

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

    @Test
    public void inputInvalidRegistrationDetails() {

    }

}

