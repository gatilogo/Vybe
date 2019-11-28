package com.example.vybe;

import android.view.KeyEvent;
import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;

/**
 * This tests for a user being able to send a follow request to another user and accepting a
 * request
 * Tests for requirements of a users personal vibe history, Vibe Events will be here,
 * as well as Other Vibe Details
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class SocialActivityTest {
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    private String espressoEmail = "espresso@test.ca";
    private String espressoUser = "espresso";
    private String espressoUID = "5fDHBHFmWqb6hCkb3DkwYUlS2ys1";

    private String decafEmail = "decaf@test.ca";
    private String decafUser = "decaf";
    private String decafUID = "ka1gQkqFcvYjmFl8FAKNV4Ewvx13";

    private String mochaUser = "mocha";

    private String LoginPassword = "vibecheck";

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

    private void LogIntoDecaf() throws InterruptedException {
        // Log in To Activity
        onView(withId(R.id.email_edit_text))
                .perform(typeText(decafEmail), closeSoftKeyboard());
        onView(withId(R.id.password_edit_text))
                .perform(typeText(LoginPassword), closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());

        Thread.sleep(5000);

        // Check we logged in and we are on myVibes page
        onView(withId(R.id.filter_spinner))
                .check(matches(isDisplayed()));

    }

    @Before
    public void initialize_db(){
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    private void LogIntoEspresso() throws InterruptedException {
        // Log in To Activity
        onView(withId(R.id.email_edit_text))
                .perform(typeText(espressoEmail), closeSoftKeyboard());
        onView(withId(R.id.password_edit_text))
                .perform(typeText(LoginPassword), closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());

        Thread.sleep(5000);

        // Check we logged in and we are on myVibes page
        onView(withId(R.id.filter_spinner))
                .check(matches(isDisplayed()));

    }

    @Test
    public void Test01_SendAndAcceptFollowRequest() throws InterruptedException {
        LogIntoDecaf();

        // Go to Social Activity TODO: Use different ID if button changes
        onView(withId(R.id.social_btn)).perform(click());

        Thread.sleep(3000);

        // Check we are in Social activity
        onView(withId(R.id.social_toolbar))
                .check(matches(isDisplayed()));

        // Click on search
        onView(withId(R.id.search_btn)).perform(click());

        Thread.sleep(3000);
        // Check we are in Social activity
        onView(withId(R.id.search_toolbar))
                .check(matches(isDisplayed()));
        // Search for user to follow
        onView(withId(R.id.search_view))
                .perform(typeText("Espresso"), pressKey(KeyEvent.KEYCODE_ENTER));
        Thread.sleep(3000);

        // Click on Users profile
        onView(withId(R.id.search_list_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        Thread.sleep(3000);

        // Confirm users profile information
        onView(withId(R.id.username_profile)).check(matches(withText(containsString(espressoUser))));
        onView(withId(R.id.email_profile)).check(matches(withText(containsString(espressoEmail))));
        // Click on send request button
        onView(withId(R.id.send_request_btn)).perform(click());
        Thread.sleep(3000);

        // Return to MyVibesActivity
        for(int i = 0; i < 4; i++) {
            pressBack();
            Thread.sleep(1000);
        }

        // Go to profile and Logout
        onView(withId(R.id.profile_btn)).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.logout_btn)).perform(click());

        Thread.sleep(2000);

        LogIntoEspresso();

        // Go to Requests Activity
        onView(withId(R.id.request_btn)).perform(click());
        Thread.sleep(3000);

        // Accept users request
        onView(withId(R.id.my_request_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, new ClickOnInternalView(R.id.accept_btn)));

    }

//    @AfterClass
//    public void DeleteFollower(){
////        db.collection("Users").document(espressoUID)
////                .update("followers", FieldValue.arrayRemove(decafUID));
////        db.collection("Users").document(decafUID)
////                .update("following", FieldValue.arrayRemove(espressoUID));
//
//    }

    @After
    public void Exit(){
        mAuth.getInstance().signOut();
    }
}

class ClickOnInternalView implements ViewAction {

    ViewAction click = click();
    int viewID;

    public ClickOnInternalView(int textViewId) {
        this.viewID = textViewId;
    }

    @Override
    public Matcher<View> getConstraints() {
        return click.getConstraints();

    }

    @Override
    public String getDescription() {
        return " click on button with id: " + viewID;
    }

    @Override
    public void perform(UiController uiController, View view) {
        click.perform(uiController, view.findViewById(viewID));
    }
}
