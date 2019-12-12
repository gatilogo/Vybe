package com.example.vybe;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

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
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;

/**
 * This tests for navigating to the profile page activity and verifying
 * a user's details are correct and is able to sign out and return to the login page
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class ViewProfileActivityTest {

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    private String validLoginEmail = "cappuccino@test.ca";
    private String validUsername = "cappuccino";
    private String validLoginPassword = "vibecheck";


    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void initialize_db() throws InterruptedException {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mAuth.getInstance().signOut();
        Thread.sleep(500);

    }


    @Test
    public void Test01_ViewPersonalProfile() throws InterruptedException {
        onView(withId(R.id.email_edit_text))
                .perform(typeText(validLoginEmail), closeSoftKeyboard());
        onView(withId(R.id.password_edit_text))
                .perform(typeText(validLoginPassword), closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());

        Thread.sleep(5000);

        // Check we logged in and we are on myVibes page
        onView(withId(R.id.vibe_filter_dropdown))
                .check(matches(isDisplayed()));

        // Click on profile icon
        onView(withId(R.id.profile_btn)).perform(click());

        Thread.sleep(1000);

        // Confirm profile information is correct
        onView(withId(R.id.username_profile)).check(matches(withText(containsString(validUsername))));
        onView(withId(R.id.email_profile)).check(matches(withText(containsString(validLoginEmail))));

        // Try signing out
        onView(withId(R.id.logout_btn)).perform(click());

        Thread.sleep(2000);

        // Check we are back in login page
        onView(withId(R.id.login_button))
                .check(matches(isDisplayed()));

    }

}
