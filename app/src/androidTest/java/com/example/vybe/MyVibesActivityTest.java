package com.example.vybe;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;

import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.GeneralSwipeAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Swipe;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.example.vybe.AddEdit.AddEditVibeEventActivity;
import com.example.vybe.Helpers.RecyclerViewItemCountAssertion;
import com.example.vybe.Models.SocSit;
import com.example.vybe.Models.Vibe;
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

import java.text.ParseException;
import java.util.Date;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.vybe.Helpers.SwipeView.LeftSwipe;
import static com.example.vybe.Helpers.SwipeView.RightSwipe;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;


/**
 * This tests for a user's myVibesActivity Page on the MyVibesActivity
 * Tests for requirements of a users personal vibe history, Vibe Events will be here,
 * as well as Other Vibe Details
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class MyVibesActivityTest {

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    private String validLoginEmail = "cappuccino@test.ca";
    private String validUsername = "cappuccino";
    private String validLoginPassword = "vibecheck";
    private SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy hh:mm a");
    private static Date date1;
    private static Date date2;

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void initialize_db() throws InterruptedException {
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

    @Test
    public void Test01_CheckVibeHistory_IsEmpty() throws InterruptedException {

        LogIntoActivity();

        // Check Vibe list is empty
        onView(withId(R.id.my_vibe_list)).check(new RecyclerViewItemCountAssertion(0));

    }

    @Test
    public void Test02_AddVibeEvent_RequiredFields() throws InterruptedException, UiObjectNotFoundException {

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

        // Add a Happy vibe
        onView(withId(R.id.vibe_image)).perform(click());

        Thread.sleep(1000);
        onView(withText("Select a Vibe")).check(matches(isDisplayed()));

        onView(withId(R.id.carousel_picker)).perform(RightSwipe());
        Thread.sleep(1000);

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject obj = device.findObject(new UiSelector().textContains("OK").clickable(true));
        obj.click();
        Thread.sleep(500);

        // Check we are in add/edit activity
        onView(withId(R.id.add_edit_vibes_toolbar))
                .check(matches(isDisplayed()));

        // Get date Vibe was created
        date1 = new Date();

        // Save our Vibe
        onView(withId(R.id.add_btn)).perform(click());

        Thread.sleep(2000);

        // Check we get back to my vibes activity
        onView(withId(R.id.vibe_filter_dropdown))
                .check(matches(isDisplayed()));

        // Click on created vibe list item to check it exists
        onView(withId(R.id.my_vibe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Check we are on the view page of the vibe
        onView(withId(R.id.view_vibes_toolbar))
                .check(matches(isDisplayed()));

        Thread.sleep(1000);

        onView(withId(R.id.view_date_text_view)).check(matches(withText(containsString(formatter.format(date1).split(" ")[0]))));

        pressBack();
    }

    @Test
    public void Test03_EditExistingVibe() throws InterruptedException, UiObjectNotFoundException {
        LogIntoActivity();

        onView(withId(R.id.my_vibe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, LeftSwipe()));
        Thread.sleep(2000);

        // Check we are in add/edit activity
        onView(withId(R.id.add_edit_vibes_toolbar))
                .check(matches(isDisplayed()));


        // Add a surprised vibe
        onView(withId(R.id.vibe_image)).perform(click());

        Thread.sleep(1000);
        onView(withText("Select a Vibe")).check(matches(isDisplayed()));

        onView(withId(R.id.carousel_picker)).perform(RightSwipe());
        Thread.sleep(1000);
        onView(withId(R.id.carousel_picker)).perform(RightSwipe());
        Thread.sleep(1000);
        onView(withId(R.id.carousel_picker)).perform(RightSwipe());
        Thread.sleep(1000);

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject obj = device.findObject(new UiSelector().textContains("OK").clickable(true));
        obj.click();

        Thread.sleep(1000);

        // Edit textual reason
        onView(withId(R.id.reason_edit_text))
                .perform(replaceText("I'm Now Surprised"), closeSoftKeyboard());

        // Click on spinner and select with others
        onView(withId(R.id.soc_sit_field_fragment)).perform(click());
        Thread.sleep(500);
        onData(allOf(is(instanceOf(String.class)), is(SocSit.WITH_SEVERAL_PEOPLE.getDesc()))).perform(click());
        Thread.sleep(500);

        // Save our Vybe
        onView(withId(R.id.add_btn)).perform(click());

        Thread.sleep(2000);

        // Check we get back to my vibes activity
        onView(withId(R.id.vibe_filter_dropdown))
                .check(matches(isDisplayed()));

        // Click on created vibe list item to check it exists
        onView(withId(R.id.my_vibe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Check we are on the view page of the vibe
        onView(withId(R.id.view_vibes_toolbar))
                .check(matches(isDisplayed()));

        Thread.sleep(1000);

        onView(withId(R.id.view_reason_text_view)).check(matches(withText(containsString("I'm Now Surprised"))));
        onView(withId(R.id.view_soc_sit_text_view)).check(matches(withText(containsString(SocSit.WITH_SEVERAL_PEOPLE.getDesc()))));

        pressBack();
    }

    @Test
    public void Test04_AddVibeEvent_AllFields() throws InterruptedException, UiObjectNotFoundException {

        LogIntoActivity();

        onView(withId(R.id.add_vibe_event_btn)).perform(click());

        Thread.sleep(3000);

        // Check we are in add/edit activity
        onView(withId(R.id.add_edit_vibes_toolbar))
                .check(matches(isDisplayed()));

        // Add a Scared vibe
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

        // Check we are in add/edit activity
        onView(withId(R.id.add_edit_vibes_toolbar))
                .check(matches(isDisplayed()));

        // Add textual reason with invalid amount of words
        onView(withId(R.id.reason_edit_text))
                .perform(typeText("I am Scared to fail"), closeSoftKeyboard());

        onView(withId(R.id.add_btn)).perform(click());

        Thread.sleep(2000);

        // Check we are in add/edit activity
        onView(withId(R.id.add_edit_vibes_toolbar))
                .check(matches(isDisplayed()));

        // Add valid textual reason
        onView(withId(R.id.reason_edit_text))
                .perform(replaceText("I am Scared"), closeSoftKeyboard());

        // Click on spinner and select alone
        onView(withId(R.id.soc_sit_field_fragment)).perform(click());
        Thread.sleep(500);
        onData(allOf(is(instanceOf(String.class)), is(SocSit.ALONE.getDesc()))).perform(click());
        Thread.sleep(500);

        // Get date vibe was created
        date2 = new Date();

        // Save our Vybe
        onView(withId(R.id.add_btn)).perform(click());

        Thread.sleep(2000);

        // Check we get back to my vibes activity
        onView(withId(R.id.vibe_filter_dropdown))
                .check(matches(isDisplayed()));

        // Click on created vibe list item to check it exists
        onView(withId(R.id.my_vibe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Check we are on the view page of the vibe
        onView(withId(R.id.view_vibes_toolbar))
                .check(matches(isDisplayed()));

        Thread.sleep(1000);

        // Check details of vibe are correct
        onView(withId(R.id.view_date_text_view)).check(matches(withText(containsString(formatter.format(date2).split(" ")[0]))));
        onView(withId(R.id.view_reason_text_view)).check(matches(withText(containsString("I am Scared"))));
        onView(withId(R.id.view_soc_sit_text_view)).check(matches(withText(containsString(SocSit.ALONE.getDesc()))));

        pressBack();

    }

    @Test
    public void Test05_ConfirmCorrectListOrder() throws InterruptedException, ParseException {
        LogIntoActivity();
        Thread.sleep(5000);

        // Click on index 0 list item
        onView(withId(R.id.my_vibe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        Thread.sleep(2000);

        // Verify it has the more recent date
        onView(withId(R.id.view_date_text_view)).check(matches(withText(containsString(formatter.format(date2).split(" ")[0]))));
        // Go back
        pressBack();
        Thread.sleep(2000);
        // Click on index 1 list item
        onView(withId(R.id.my_vibe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        Thread.sleep(2000);

        // Verify it has the less recent date
        onView(withId(R.id.view_date_text_view)).check(matches(withText(containsString(formatter.format(date1).split(" ")[0]))));

        pressBack();

    }

    @Test
    public void Test06_Filter_VerifyList() throws InterruptedException, ParseException {
        LogIntoActivity();
        // Click on spinner and select angry (no vibes)
        onView(withId(R.id.vibe_filter_dropdown)).perform(click());
        Thread.sleep(500);
        onData(allOf(is(instanceOf(String.class)), is(Vibe.ANGRY.getName()))).perform(click());
        Thread.sleep(500);

        // Check Vibe list is empty
        onView(withId(R.id.my_vibe_list)).check(new RecyclerViewItemCountAssertion(0));


        // Click on spinner and select Surprised (1 vibe)
        onView(withId(R.id.vibe_filter_dropdown)).perform(click());
        Thread.sleep(500);
        onData(allOf(is(instanceOf(String.class)), is(Vibe.SURPRISED.getName()))).perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.my_vibe_list)).check(new RecyclerViewItemCountAssertion(1));
        // Click on spinner and select Scared (1 vibe)
        onView(withId(R.id.vibe_filter_dropdown)).perform(click());
        Thread.sleep(500);
        onData(allOf(is(instanceOf(String.class)), is(Vibe.SCARED.getName()))).perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.my_vibe_list)).check(new RecyclerViewItemCountAssertion(1));

    }


    @Test
    public void Test07_DeleteVibe() throws InterruptedException, UiObjectNotFoundException {
        LogIntoActivity();

        // Check we have two vibes in our list
        onView(withId(R.id.my_vibe_list)).check(new RecyclerViewItemCountAssertion(2));

        // Delete Older Vibe
        onView(withId(R.id.my_vibe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, RightSwipe()));
        Thread.sleep(5000);

        // Check we have one vibe in our list
        onView(withId(R.id.my_vibe_list)).check(new RecyclerViewItemCountAssertion(1));

        // Delete Last Vibe
        onView(withId(R.id.my_vibe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, RightSwipe()));
        Thread.sleep(2000);

        // Check we have No vibes in our list
        onView(withId(R.id.my_vibe_list)).check(new RecyclerViewItemCountAssertion(0));

    }

    @After
    public void SignOut() throws InterruptedException {

        onView(withId(R.id.profile_btn)).perform(click());

        Thread.sleep(1000);

        // Confirm profile information is correct
        onView(withId(R.id.username_profile)).check(matches(withText(containsString(validUsername))));
        onView(withId(R.id.email_profile)).check(matches(withText(containsString(validLoginEmail))));

        // Try signing out
        onView(withId(R.id.logout_btn)).perform(click());
    }

 }



