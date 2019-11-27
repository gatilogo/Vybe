package com.example.vybe;

import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.GeneralSwipeAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Swipe;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;


/**
 * This tests for a user's myVibesActivity Page on the MyVibesActivity
 * Tests for requirements of a users personal vibe history, Vibe Events will be here,
 * as well as Other Vibe Details
 *
 */
@RunWith(AndroidJUnit4.class)
public class MyVibesActivityTest {

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    private String validLoginEmail = "espresso@test.ca";
    private String validUsername = "espresso";
    private String validLoginPassword = "vibecheck";

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void initialize_db(){
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    private void LogIntoActivity() throws InterruptedException {
        // Log in To Activity
        onView(withId(R.id.email_edit_text))
                .perform(typeText(validLoginEmail), closeSoftKeyboard());
        onView(withId(R.id.password_edit_text))
                .perform(typeText(validLoginPassword), closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());

        Thread.sleep(5000);

        // Check we logged in and we are on myVibes page
        onView(withId(R.id.filter_spinner))
                .check(matches(isDisplayed()));

    }

    @Test
    public void CheckVibeHistory_IsEmpty() throws InterruptedException {

        LogIntoActivity();

        // Check Vibe list is empty
        onView(withId(R.id.image_view))
                .check((doesNotExist()));
    }

    @Test
    public void AddVibeEvent_RequiredFields() throws InterruptedException, UiObjectNotFoundException {

        LogIntoActivity();

        onView(withId(R.id.add_vibe_event_btn)).perform(click());

        Thread.sleep(3000);

        // Check we are in add/edit activity
        onView(withId(R.id.add_edit_vibes_toolbar))
                .check(matches(isDisplayed()));

        Thread.sleep(2000);
        // Click on save without selecting vibe first
        onView(withId(R.id.add_btn)).perform(click());

        Thread.sleep(1000);
        // Check we are in add/edit activity
        onView(withId(R.id.add_edit_vibes_toolbar))
                .check(matches(isDisplayed()));

        onView(withId(R.id.vibe_image)).perform(click());

        Thread.sleep(1000);
        onView(withText("Select a Vibe")).check(matches(isDisplayed()));

        onView(withId(R.id.carousel_picker)).perform(customSwipe());
        Thread.sleep(1000);
        onView(withId(R.id.carousel_picker)).perform(customSwipe());

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject obj = device.findObject(new UiSelector().textContains("OK").clickable(true));
        obj.click();

        // Check we are in add/edit activity
        onView(withId(R.id.add_edit_vibes_toolbar))
                .check(matches(isDisplayed()));

        // Save our Vybe
        onView(withId(R.id.add_btn)).perform(click());

        Thread.sleep(2000);

        // Check we get back to my vibes activity
        onView(withId(R.id.filter_spinner))
                .check(matches(isDisplayed()));
    }

    private static ViewAction customSwipe() {
        return new GeneralSwipeAction(Swipe.SLOW, GeneralLocation.CENTER_RIGHT,
                GeneralLocation.CENTER_LEFT, Press.FINGER);
    }

    @After
    public void Exit(){
        mAuth.getInstance().signOut();
    }
 }
