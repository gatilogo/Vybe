package com.example.vybe;


import android.app.Activity;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.time.LocalTime;

import in.goodiebag.carouselpicker.CarouselPicker;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Test class for MainActivity. All the UI tests are written here. Robotium test framework is
 used
 */
@RunWith(AndroidJUnit4.class)
public class AddEditVibeActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<AddEditVibeActivity> rule =
            new ActivityTestRule<>(AddEditVibeActivity.class, true, true);
    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void VibeCarousel_DialogOpen_True(){
        // WIP: Blocking other issues, will continue working on this test later
        // Check That selector starts not selected on a vibe
        ImageView image = (ImageView) solo.getView("vibe_selector");

        solo.clickOnView(image);

        solo.clickOnButton("OK");

        solo.waitForDialogToClose();

        ImageView verify = solo.getImage(0);

        assertEquals(image, verify);
    }


    @Test
    public void SocialSpinner_SelectOptions_Pass(){
        // Check That selector starts not selected on a vibe
        boolean result = solo.isSpinnerTextSelected(1, "Select a Social Situation");
        assertEquals("Select a SC not selected", true, result);

        solo.pressSpinnerItem(1, 2);

        boolean actual = solo.isSpinnerTextSelected(1, "In a group");

        assertEquals("spinner item 'in a group' is not selected",true, actual);

    }

    @Test
    public void StringReason_EnterText_Valid(){
        EditText editText = (EditText) solo.getView("reason_edit_text");
        //Click on Empty Text View
        solo.clickOnView(editText);
        //Enter the text: 'asdfg'
        solo.clearEditText(editText);
        solo.enterText(editText, "asdfg");
        solo.searchEditText("asdfg");
        // Validate the text on the TextView
        assertEquals("Text should be the field value", "asdfg",
                (editText.getText().toString()));

    }

    @Test
    public void StringReason_EnterText_Invalid(){
        // test if more than 3 words is provided
        EditText editText = (EditText) solo.getView("reason_edit_text");
        solo.clickOnView(editText);
        //Enter the text: 'as df ga sd'
        solo.clearEditText(editText);
        solo.enterText(editText, "as df ga sd");
        solo.clickOnButton("Save");

        assertEquals("Expecting an error", "Max 3 words allowed",
                (editText.getError().toString()));
    }

    /*
    Due to a change in requirements, this test will be temporarily disabled until
    it becomes obsolete
     */
    @Ignore
    public void DatePicker_SelectDate_Pass(){
        solo.clickOnEditText(0);
        DatePicker datePicker = solo.getView(DatePicker.class, 0); // the second param is an index
        solo.setDatePicker(datePicker, 2019, 10, 8);
        solo.clickOnButton("OK");

        TimePicker timePicker = solo.getView(TimePicker.class, 0);
        solo.setTimePicker(timePicker, 4, 20);
        solo.clickOnButton("OK");
        solo.waitForDialogToClose();

        String textInDateTimeField = ((EditText) solo.getView("date_time_edit_text")).getText().toString();
        assertEquals("Nov 08 2019, 04:20 AM", textInDateTimeField);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
