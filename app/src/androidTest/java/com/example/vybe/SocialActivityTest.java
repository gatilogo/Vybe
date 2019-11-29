package com.example.vybe;

import android.view.KeyEvent;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.example.vybe.Helpers.ClickOnInternalView;
import com.example.vybe.Helpers.RecyclerViewItemCountAssertion;
import com.example.vybe.Helpers.RecyclerViewMatcher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

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
import static com.example.vybe.Helpers.SwipeView.RightSwipe;
import static org.hamcrest.Matchers.containsString;

/**
 * This tests for a user being able to send a follow request to another user and accepting a
 * request
 * Tests for requirements of Mood Following and Sharing
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class SocialActivityTest {
    public static FirebaseFirestore db;
    public static FirebaseAuth mAuth;

    private String cappuccinoEmail = "cappuccino@test.ca";
    private String cappuccinoUser = "cappuccino";
    public static String cappuccinoUID = "ifjnKGTifnaU40iVq5tghK7LRup2";

    private String decafEmail = "decaf@test.ca";
    public static String decafUID = "ka1gQkqFcvYjmFl8FAKNV4Ewvx13";

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
        onView(withId(R.id.vibe_filter_dropdown))
                .check(matches(isDisplayed()));

    }

    @Before
    public void initialize_db() throws InterruptedException {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


    }

    private void LogIntoCappuccino() throws InterruptedException {
        // Log in To Activity
        onView(withId(R.id.email_edit_text))
                .perform(typeText(cappuccinoEmail), closeSoftKeyboard());
        onView(withId(R.id.password_edit_text))
                .perform(typeText(LoginPassword), closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());

        Thread.sleep(5000);

        // Check we logged in and we are on myVibes page
        onView(withId(R.id.vibe_filter_dropdown))
                .check(matches(isDisplayed()));

    }

    @Test
    public void Test01_SendAndAcceptFollowRequest() throws InterruptedException, UiObjectNotFoundException {
        LogIntoDecaf();

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
                .perform(typeText(cappuccinoUser), pressKey(KeyEvent.KEYCODE_ENTER));
        Thread.sleep(3000);

        // Click on Users profile
        onView(withId(R.id.search_list_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        Thread.sleep(3000);

        // Confirm users profile information
        onView(withId(R.id.username_profile)).check(matches(withText(containsString(cappuccinoUser))));
        onView(withId(R.id.email_profile)).check(matches(withText(containsString(cappuccinoEmail))));
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

        LogIntoCappuccino();

        // Post a new vibe
        PostVibe();

        // Go to Requests Activity
        onView(withId(R.id.request_btn)).perform(click());
        Thread.sleep(3000);

        // Accept users request
        onView(withId(R.id.my_request_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, new ClickOnInternalView(R.id.accept_btn)));
        Thread.sleep(3000);
    }

    private void PostVibe() throws UiObjectNotFoundException, InterruptedException {
        onView(withId(R.id.add_vibe_event_btn)).perform(click());

        Thread.sleep(3000);

        // Check we are in add/edit activity
        onView(withId(R.id.add_edit_vibes_toolbar))
                .check(matches(isDisplayed()));

        Thread.sleep(2000);

        // Add a Happy vibe
        onView(withId(R.id.vibe_image)).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.carousel_picker)).perform(RightSwipe());
        Thread.sleep(1000);

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject obj = device.findObject(new UiSelector().textContains("OK").clickable(true));
        obj.click();
        Thread.sleep(500);

        onView(withId(R.id.add_btn)).perform(click());

        Thread.sleep(2000);

    }

    @Test
    public void Test02_ViewSocialActivity() throws InterruptedException {
        LogIntoDecaf();

        // Go to Social Activity TODO: Use different ID if button changes
        onView(withId(R.id.social_btn)).perform(click());

        Thread.sleep(3000);

        // Check we are in Social activity
        onView(withId(R.id.social_toolbar))
                .check(matches(isDisplayed()));
        // check 2 items are on the recycler view
        onView(withId(R.id.social_vibe_list)).check(new RecyclerViewItemCountAssertion(2));

        // make sure @Mocha is the older item/vibe displayed
        onView(new RecyclerViewMatcher(R.id.social_vibe_list)
                .atPositionOnView(1, R.id.vibe_title_text_view))
                .check(matches(withText("@" + mochaUser)));
        // check that @Cappuccino is at the top as it has the most recently posted vibe
        onView(new RecyclerViewMatcher(R.id.social_vibe_list)
                .atPositionOnView(0, R.id.vibe_title_text_view))
                .check(matches(withText("@" + cappuccinoUser)));

        // Delete Entries from follower and following lists
        db.collection("Users").document(cappuccinoUID)
                .update("followers", FieldValue.arrayRemove(decafUID));
        db.collection("Users").document(decafUID)
                .update("following", FieldValue.arrayRemove(cappuccinoUID));

        // Log out of decaf
        pressBack();
        Thread.sleep(1000);

        onView(withId(R.id.profile_btn)).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.logout_btn)).perform(click());

        Thread.sleep(2000);

        LogIntoCappuccino();

        //  Delete the vibe created
        onView(withId(R.id.my_vibe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, RightSwipe()));

        Thread.sleep(1000);

    }

    @After
    public void SignOut() throws InterruptedException {
        mAuth.getInstance().signOut();
        Thread.sleep(500);
    }

}

