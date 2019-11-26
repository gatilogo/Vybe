package com.example.vybe;
import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
 * This tests for user Registration and on the sign up activity
 *
 */
@RunWith(AndroidJUnit4.class)
public class CreateAccountActivityTest {
    FirebaseFirestore db;
    FirebaseAuth auth;
    private String invalidEmail = "email";
    private String invalidLengthPassword = "1234";


    private String existingEmail = "espresso@test.ca";
    private String existingUsername = "espresso";

    private String validNewEmail = "expressoCAAT@test.ca";
    private String validNewUsername = "expressoCAAT";


    @Rule
    public ActivityTestRule<CreateAccountActivity> activityRule =
            new ActivityTestRule<>(CreateAccountActivity.class);

    @Before
    public void initialize_db(){
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @Test
    public void InvalidLogin_EmptyParameters() throws InterruptedException {
        onView(withId(R.id.confirm_button)).perform(click());

        Thread.sleep(1000);

        // Check we are still in login page
        onView(withId(R.id.confirm_button))
                .check(matches(isDisplayed()));

    }

}
