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

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * This tests for user Registration and on the sign up activity
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class CreateAccountActivityTest {
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    private String invalidEmail = "email";
    private String invalidLengthPassword = "1234";


    private String existingEmail = "espresso@test.ca";
    private String existingUsername = "espresso";

    private String validNewEmail = "vibe_kill@test.ca";
    private String validNewUsername = "vibekill_test";
    private String validPassword = "StrongPassword";


    @Rule
    public ActivityTestRule<CreateAccountActivity> activityRule =
            new ActivityTestRule<>(CreateAccountActivity.class);

    @Before
    public void initialize_db(){
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

    }

    @Test
    public void Test01_InvalidSignUp_EmptyParameters() throws InterruptedException {
        onView(withId(R.id.confirm_button)).perform(click());

        Thread.sleep(1000);

        // Check we are still in sign up page
        onView(withId(R.id.confirm_button))
                .check(matches(isDisplayed()));
    }

    @Test
    public void Test02_InvalidSignUp_Password() throws InterruptedException {
        onView(withId(R.id.username_create))
                .perform(typeText(validNewUsername), closeSoftKeyboard());

        onView(withId(R.id.email_create))
                .perform(typeText(validNewEmail), closeSoftKeyboard());

        onView(withId(R.id.password_create))
                .perform(typeText(invalidLengthPassword), closeSoftKeyboard());


        onView(withId(R.id.confirm_button)).perform(click());

        Thread.sleep(1000);

        // Check we are still in sign up page
        onView(withId(R.id.confirm_button))
                .check(matches(isDisplayed()));
    }

    @Test
    public void Test03_InvalidSignUp_BadEmail() throws InterruptedException {
        onView(withId(R.id.username_create))
                .perform(typeText(validNewUsername), closeSoftKeyboard());

        onView(withId(R.id.email_create))
                .perform(typeText(invalidEmail), closeSoftKeyboard());

        onView(withId(R.id.password_create))
                .perform(typeText(validPassword), closeSoftKeyboard());


        onView(withId(R.id.confirm_button)).perform(click());

        Thread.sleep(1000);

        // Check we are still in sign up page
        onView(withId(R.id.confirm_button))
                .check(matches(isDisplayed()));
    }

    @Test
    public void Test04_InvalidSignUp_TakenEmail() throws InterruptedException {
        onView(withId(R.id.username_create))
                .perform(typeText(validNewUsername), closeSoftKeyboard());

        onView(withId(R.id.email_create))
                .perform(typeText(existingEmail), closeSoftKeyboard());

        onView(withId(R.id.password_create))
                .perform(typeText(validPassword), closeSoftKeyboard());


        onView(withId(R.id.confirm_button)).perform(click());

        Thread.sleep(1000);

        // Check we are still in sign up page
        onView(withId(R.id.confirm_button))
                .check(matches(isDisplayed()));
    }

    @Test
    public void Test05_InvalidSignUp_TakenUsername() throws InterruptedException {
        onView(withId(R.id.username_create))
                .perform(typeText(existingUsername), closeSoftKeyboard());

        onView(withId(R.id.email_create))
                .perform(typeText(validNewEmail), closeSoftKeyboard());

        onView(withId(R.id.password_create))
                .perform(typeText(validPassword), closeSoftKeyboard());


        onView(withId(R.id.confirm_button)).perform(click());

        Thread.sleep(1000);

        // Check we are still in sign up page
        onView(withId(R.id.confirm_button))
                .check(matches(isDisplayed()));
    }

    @Test
    public void Test06_ValidRegistration() throws InterruptedException {
        onView(withId(R.id.username_create))
                .perform(typeText(validNewUsername), closeSoftKeyboard());

        onView(withId(R.id.email_create))
                .perform(typeText(validNewEmail), closeSoftKeyboard());

        onView(withId(R.id.password_create))
                .perform(typeText(validPassword), closeSoftKeyboard());


        onView(withId(R.id.confirm_button)).perform(click());

        Thread.sleep(10000);

        // Check we logged in and we are on myVibes page
        onView(withId(R.id.filter_spinner))
                .check(matches(isDisplayed()));

        // Check Vibe list is empty
        onView(withId(R.id.image_view))
                .check((doesNotExist()));

        // Delete test entry after
        db.collection("Users").document(mAuth.getCurrentUser().getUid()).delete();
        mAuth.getCurrentUser().delete();
    }

}
