package com.example.vybe;

import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * This tests for user login and authentication on the main login activity
 * This also tests for getting to the registration screen
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    private String invalidLoginEmailNoExist = "non-existent@user.ca";
    private String invalidLoginPassword = "invaliduser";
    private String invalidLengthPassword = "1234";


    private String validLoginEmail = "cappuccino@test.ca";
    private String validLoginPassword = "vibecheck";

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void initialize_db(){
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    // Log In Tests
    @Test
    public void Test01_InvalidLogin_EmptyParameters() throws InterruptedException {
        onView(withId(R.id.login_button)).perform(click());

        Thread.sleep(1000);

        // Check we are still in login page
        onView(withId(R.id.signup_button))
                .check(matches(withText("No account yet? Create one")));

    }

    @Test
    public void Test02_InvalidLogin_NonExistantUser() throws InterruptedException {
        onView(withId(R.id.email_edit_text))
                .perform(typeText(invalidLoginEmailNoExist), closeSoftKeyboard());
        onView(withId(R.id.password_edit_text))
                .perform(typeText(invalidLoginPassword), closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());

        Thread.sleep(1000);

        // Check we are still in login page
        onView(withId(R.id.login_button))
                .check(matches(isDisplayed()));
    }

    @Test
    public void Test03_InvalidLogin_TooShortPassword() throws InterruptedException {
        onView(withId(R.id.email_edit_text))
                .perform(typeText(invalidLoginEmailNoExist), closeSoftKeyboard());
        onView(withId(R.id.password_edit_text))
                .perform(typeText(invalidLengthPassword), closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());

        Thread.sleep(1000);

        // Check we are still in login page
        onView(withId(R.id.login_button))
                .check(matches(isDisplayed()));
    }

    @Test
    public void Test04_Login_AuthorizationPasses() throws InterruptedException {
        onView(withId(R.id.email_edit_text))
                .perform(typeText(validLoginEmail), closeSoftKeyboard());
        onView(withId(R.id.password_edit_text))
                .perform(typeText(validLoginPassword), closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());

        Thread.sleep(2000);

        // Check we logged in and we are on myVibes page
        onView(withId(R.id.vibe_filter_dropdown))
                .check(matches(isDisplayed()));

        mAuth.getInstance().signOut();
    }


    @Test
    public void Test05_Signup_GetToActivity() throws InterruptedException {
        // Click on Signup button
        onView(withId(R.id.signup_button)).perform(click());

        Thread.sleep(2000);

        // Check we are on the sign up page
        onView(withId(R.id.username_create))
                .check(matches(isDisplayed()));
    }

}
