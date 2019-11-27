package com.example.vybe;

import android.icu.text.SimpleDateFormat;
import android.view.View;
import android.widget.TextView;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.GeneralSwipeAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Swipe;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.example.vybe.Models.SocSit;
import com.example.vybe.Models.Vibe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.util.Date;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
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
@RunWith(AndroidJUnit4.class)
public class MyVibesActivityTest {

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    private String validLoginEmail = "espresso@test.ca";
    private String validUsername = "espresso";
    private String validLoginPassword = "vibecheck";
    private SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy hh:mm a");

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

        // Add a disgusted vibe
        onView(withId(R.id.vibe_image)).perform(click());

        Thread.sleep(1000);
        onView(withText("Select a Vibe")).check(matches(isDisplayed()));

        onView(withId(R.id.carousel_picker)).perform(RightSwipe());
        Thread.sleep(1000);

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject obj = device.findObject(new UiSelector().textContains("OK").clickable(true));
        obj.click();

        // TODO: Add checks for color and emoticon changes
        // Check we are in add/edit activity
        onView(withId(R.id.add_edit_vibes_toolbar))
                .check(matches(isDisplayed()));

        // Get date Vibe was created
        Date testDate = new Date();

        // Save our Vibe
        onView(withId(R.id.add_btn)).perform(click());

        Thread.sleep(2000);

        // Check we get back to my vibes activity
        onView(withId(R.id.filter_spinner))
                .check(matches(isDisplayed()));

        // Click on created vibe list item to check it exists
        onView(withId(R.id.my_vibe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Check we are on the view page of the vibe
        onView(withId(R.id.view_vibes_toolbar))
                .check(matches(isDisplayed()));

        Thread.sleep(1000);

        onView(withId(R.id.view_date_text_view)).check(matches(withText(containsString(formatter.format(testDate)))));

    }

    @Test
    public void AddVibeEvent_AllFields() throws InterruptedException, UiObjectNotFoundException {

        LogIntoActivity();

        onView(withId(R.id.add_vibe_event_btn)).perform(click());

        Thread.sleep(3000);

        // Check we are in add/edit activity
        onView(withId(R.id.add_edit_vibes_toolbar))
                .check(matches(isDisplayed()));


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

        // Check we are in add/edit activity
        onView(withId(R.id.add_edit_vibes_toolbar))
                .check(matches(isDisplayed()));

        // Add textual reason with invalid amount of words
        onView(withId(R.id.reason_edit_text))
                .perform(typeText("I am Very Sad"), closeSoftKeyboard());

        onView(withId(R.id.add_btn)).perform(click());

        Thread.sleep(2000);

        // Check we are in add/edit activity
        onView(withId(R.id.add_edit_vibes_toolbar))
                .check(matches(isDisplayed()));

        // Add valid textual reason
        onView(withId(R.id.reason_edit_text))
                .perform(replaceText("I am Sad"), closeSoftKeyboard());

        // Click on spinner and select alone
        onView(withId(R.id.soc_sit_field_fragment)).perform(click());
        Thread.sleep(500);
        onData(allOf(is(instanceOf(String.class)), is(SocSit.ALONE.toString()))).perform(click());
        Thread.sleep(500);

        // Get date vibe was created
        Date testDate = new Date();

        // Save our Vybe
        onView(withId(R.id.add_btn)).perform(click());

        Thread.sleep(2000);

        // Check we get back to my vibes activity
        onView(withId(R.id.filter_spinner))
                .check(matches(isDisplayed()));

        // Click on created vibe list item to check it exists
        onView(withId(R.id.my_vibe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Check we are on the view page of the vibe
        onView(withId(R.id.view_vibes_toolbar))
                .check(matches(isDisplayed()));

        Thread.sleep(1000);

        onView(withId(R.id.view_date_text_view)).check(matches(withText(containsString(formatter.format(testDate)))));

        onView(withId(R.id.view_reason_text_view)).check(matches(withText(containsString("I am Sad"))));
        onView(withId(R.id.view_soc_sit_text_view)).check(matches(withText(containsString(SocSit.ALONE.toString()))));

    }

    // TODO: Get dates of two items and compare their dates to verify ordering
    @Test
    public void ConfirmCorrectListOrder() throws InterruptedException, ParseException {
        LogIntoActivity();
        onView(withId(R.id.my_vibe_list));
    }

    @Test
    public void Filter_VerifyList() throws InterruptedException, ParseException {
        LogIntoActivity();

        // Click on spinner and select angry (no vibes)
        onView(withId(R.id.filter_spinner)).perform(click());
        Thread.sleep(500);
        onData(allOf(is(instanceOf(String.class)), is(Vibe.ANGRY.toString()))).perform(click());
        Thread.sleep(500);

        // Check Vibe list is empty
        onView(withId(R.id.image_view))
                .check((doesNotExist()));

        // Click on spinner and select disgusted (1 vibe)
        onView(withId(R.id.filter_spinner)).perform(click());
        Thread.sleep(500);
        onData(allOf(is(instanceOf(String.class)), is(Vibe.DISGUSTED.toString()))).perform(click());
        Thread.sleep(3000);
        // TODO: Check We have only one list item
        // Click on spinner and select sad (1 vibe)
        onView(withId(R.id.filter_spinner)).perform(click());
        Thread.sleep(500);
        onData(allOf(is(instanceOf(String.class)), is(Vibe.SAD.toString()))).perform(click());
        Thread.sleep(3000);
        // TODO: Check We have only one list item

    }

    @Test
    public void EditExistingVibe() throws InterruptedException, UiObjectNotFoundException {
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
        onData(allOf(is(instanceOf(String.class)), is(SocSit.WITH_SEVERAL_PEOPLE.toString()))).perform(click());
        Thread.sleep(500);

        // Save our Vybe
        onView(withId(R.id.add_btn)).perform(click());

        Thread.sleep(2000);

        // Check we get back to my vibes activity
        onView(withId(R.id.filter_spinner))
                .check(matches(isDisplayed()));

        // Click on created vibe list item to check it exists
        onView(withId(R.id.my_vibe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Check we are on the view page of the vibe
        onView(withId(R.id.view_vibes_toolbar))
                .check(matches(isDisplayed()));

        Thread.sleep(1000);

        onView(withId(R.id.view_reason_text_view)).check(matches(withText(containsString("I'm Now Surprised"))));
        onView(withId(R.id.view_soc_sit_text_view)).check(matches(withText(containsString(SocSit.WITH_SEVERAL_PEOPLE.toString()))));

    }

    @Test
    public void DeleteVibes() throws InterruptedException, UiObjectNotFoundException {
        LogIntoActivity();
        onView(withId(R.id.my_vibe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1, RightSwipe()));
        Thread.sleep(2000);
        onView(withId(R.id.my_vibe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, RightSwipe()));
        Thread.sleep(2000);

        // Check Vibe list is empty
        onView(withId(R.id.image_view))
                .check((doesNotExist()));
    }

    private static ViewAction RightSwipe() {
        return new GeneralSwipeAction(Swipe.SLOW, GeneralLocation.CENTER_RIGHT,
                GeneralLocation.CENTER_LEFT, Press.FINGER);
    }

    private static ViewAction LeftSwipe() {
        return new GeneralSwipeAction(Swipe.SLOW, GeneralLocation.CENTER_LEFT,
                GeneralLocation.CENTER_RIGHT, Press.FINGER);
    }

    String getText(final Matcher<View> matcher) {
        final String[] stringHolder = { null };
        onView(matcher).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "getting text from a TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView tv = (TextView)view; //Save, because of check in getConstraints()
                stringHolder[0] = tv.getText().toString();
            }
        });
        return stringHolder[0];
    }

    @After
    public void Exit(){
        mAuth.getInstance().signOut();
    }
 }
