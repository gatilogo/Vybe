package com.example.vybe;

import android.content.Intent;

import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.GeneralSwipeAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Swipe;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * This tests to see if the Google Map is loaded when navigating to the Map section
 * Tests for requirements related to Geolocation and Maps
 * DISCLAIMER: Google Maps Location Authentication must be enabled for these tests to pass
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class MapFragmentTest {
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    private String validLoginEmail = "decaf@test.ca";
    private String validLoginPassword = "vibecheck";

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void initialize_db() {
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
        onView(withId(R.id.vibe_filter_dropdown))
                .check(matches(isDisplayed()));
    }

    // Test Passes if user can get to the mapview switch to social view and click on a followed
    // Users vibe
    @Test
    public void Test01_AddVibeWithLocation() throws InterruptedException, UiObjectNotFoundException {
        LogIntoActivity();

        onView(withId(R.id.add_vibe_event_btn)).perform(click());

        Thread.sleep(3000);


        // Add a sad vibe
        onView(withId(R.id.vibe_image)).perform(click());

        Thread.sleep(1000);
        onView(withText("Select a Vibe")).check(matches(isDisplayed()));

        onView(withId(R.id.carousel_picker)).perform(RightSwipe());
        Thread.sleep(1000);
        onView(withId(R.id.carousel_picker)).perform(RightSwipe());
        Thread.sleep(1000);

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject obj = device.findObject(new UiSelector().textContains("OK").clickable(true));
        obj.click();

        Thread.sleep(2000);

        // Add Current Location
        onView(withId(R.id.btn_add_location)).perform(click());
        Thread.sleep(2000);
        onView(withText("Add a Location")).check(matches(isDisplayed()));

        onView(withId(R.id.btn_current_location)).perform(click());
        Thread.sleep(2000);

        // Save our Vybe
        onView(withId(R.id.add_btn)).perform(click());

        Thread.sleep(2000);

        // Check we get back to my vibes activity
        onView(withId(R.id.vibe_filter_dropdown))
                .check(matches(isDisplayed()));

        // View Activity and check we can see the map
        onView(withId(R.id.my_vibe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.view_vibe_map))
                .check(matches(isDisplayed()));

    }

    @Test
    public void Test02_DisplayMapFragment() throws InterruptedException, UiObjectNotFoundException {
        LogIntoActivity();
        // Click on Map Button
        onView(withId(R.id.my_map_btn)).perform(click());

        Thread.sleep(4000);

        //        Check that the map is displayed
        onView(withId(R.id.my_vibes_map)).check(matches(isDisplayed()));

        // Switch to social view
        onView(withId(R.id.btn_map_toggle)).perform(click());
        Thread.sleep(1000);

        //Check we can click and see the social vibe
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains("mocha"));
        marker.click();
        Thread.sleep(1000);

        // Go back to MyVibesActivity and Delete the vibe created
        pressBack();
        Thread.sleep(1000);

        onView(withId(R.id.my_vibe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, RightSwipe()));

        Thread.sleep(1000);

    }

    private static ViewAction RightSwipe() {
        return new GeneralSwipeAction(Swipe.SLOW, GeneralLocation.CENTER_RIGHT,
                GeneralLocation.CENTER_LEFT, Press.FINGER);
    }

    @After
    public void Exit() {
        mAuth.getInstance().signOut();
    }
}

