package com.example.vybe;


import android.app.Activity;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for MainActivity. All the UI tests are written here. Robotium test framework is
 used
 */
@RunWith(AndroidJUnit4.class)
public class MyVibesActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MyVibesActivity> rule =
            new ActivityTestRule<>(MyVibesActivity.class, true, true);
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
    public void checkList_NoOptional() {
        solo.assertCurrentActivity("Wrong Activity", MyVibesActivity.class);
        solo.clickOnButton("ADD");
        solo.assertCurrentActivity("Wrong Activity", AddEditVibeActivity.class);
        boolean actual = solo.isSpinnerTextSelected(0, "Angry");
        assertEquals("spinner item Angry is not selected", true, actual);
        String textInDateTimeField = ((EditText) solo.getView("date_time_edit_text")).getText().toString();
        assertNotNull(textInDateTimeField);

        //Click on Empty Text View
        solo.clickOnView(solo.getView("reason_edit_text"));
        //Enter the text: 'asdfg'
        solo.clearEditText((android.widget.EditText) solo.getView("reason_edit_text"));
        solo.enterText((android.widget.EditText) solo.getView("reason_edit_text"), "asdfg");
        solo.searchEditText("asdfg");
        // Validate the text on the TextView
        assertEquals("Text should be the field value", "asdfg",
                ((EditText) solo.getView("reason_edit_text")).getText().toString());

        solo.clickOnButton("SAVE");

        assertTrue(solo.waitForText("asdfg", 1, 2000));

    }

    @Test
    public void VibeSpinnerTest(){
        // Check That selector starts not selected on a vibe
        boolean result = solo.isSpinnerTextSelected(0, "Select a vibe");
        assertEquals("Select a vibe not selected", true, result);

        solo.pressSpinnerItem(0, 1);

        boolean actual = solo.isSpinnerTextSelected(0, "Angry");


        assertEquals("spinner item Angry is not selected",true, actual);

    }

    @Test
    public void SocialSpinnerTest(){
        // Check That selector starts not selected on a vibe
        boolean result = solo.isSpinnerTextSelected(1, "Select a Social Situation");
        assertEquals("Select a SC not selected", true, result);

        solo.pressSpinnerItem(1, 2);

        boolean actual = solo.isSpinnerTextSelected(1, "In a group");

        assertEquals("spinner item 'in a group' is not selected",true, actual);

    }

    @Test
    public void stringReasonTest(){
        //Click on Empty Text View
        solo.clickOnView(solo.getView("reason_edit_text"));
        //Enter the text: 'asdfg'
        solo.clearEditText((android.widget.EditText) solo.getView("reason_edit_text"));
        solo.enterText((android.widget.EditText) solo.getView("reason_edit_text"), "asdfg");
        solo.searchEditText("asdfg");
        // Validate the text on the TextView
        assertEquals("Text should be the field value", "asdfg",
                ((EditText) solo.getView("reason_edit_text")).getText().toString());
    }

    @Test
    public void datePickerTest(){
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
