package com.example.vybe;

import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Test class for MainActivity. All the UI tests are written here. The Robotium test framework is
 used
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private String invalidLoginEmailNoExist = "non-existent@user.ca";
    private String invalidLoginPassword = "invalid123";
    private String invalidLengthPassword = "1234";


    private String validLoginEmail = "espresso@test.ca";
    private String validLoginPassword = "vibecheck";

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void InvalidLogin_NonexistantUser(){
        onView(withId(R.id.email_edit_text))
                .perform(typeText(invalidLoginEmailNoExist), closeSoftKeyboard());
        onView(withId(R.id.password_edit_text))
                .perform(typeText(invalidLoginPassword), closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());

        // Check we are still in login page
        onView(withId(R.id.signup_button))
                .check(matches(withText("Sign Up")));

    }


}
