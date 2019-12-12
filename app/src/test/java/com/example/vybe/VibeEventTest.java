package com.example.vybe;

import com.example.vybe.Models.SocSit;
import com.example.vybe.Models.Vibe;

import android.icu.text.SimpleDateFormat;

import com.example.vybe.Models.VibeEvent;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests the VibeEvent Class
 * <p>
 * These tests ensure that the data of a Vibe Event is properly stored and returned from a VibeEvent Object
 */
public class VibeEventTest {

    private VibeEvent mockEmptyVibeEvent() {
        return new VibeEvent();
    }

    private VibeEvent mockVibeEvent() {
        Date date = new Date();

        return new VibeEvent(mockVibe(), date,
                "just really happy", SocSit.ALONE, "", "image", 2.0, 2.0);
    }

    private Vibe mockVibe() {
        return Vibe.HAPPY;
    }

    @Test
    public void VibeEventConstructor_EmptyInit() {
        VibeEvent testVibeEvent = mockEmptyVibeEvent();

        Date testDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy hh:mm a");

        assertNull(testVibeEvent.getVibe());
        assertNull(testVibeEvent.getReason());
        assertNull(testVibeEvent.getSocSit());
        assertEquals(testDate.getClass(), testVibeEvent.getDateTime().getClass());
        assertEquals(formatter.format(testDate), testVibeEvent.getDateTimeString());
        assertNull(testVibeEvent.getID());
        assertNull(testVibeEvent.getImage());
        assertNull(testVibeEvent.getLatitude());
        assertNull(testVibeEvent.getLongitude());

    }

    @Test
    public void VibeEventConstructor_FullInit() {
        VibeEvent testVibeEvent = mockVibeEvent();
        Vibe testVibe = mockVibe();

        Date testDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy hh:mm a");

        assertEquals(testVibe.getName(), testVibeEvent.getVibe().getName());
        assertEquals("just really happy", testVibeEvent.getReason());
        assertEquals(testDate.getClass(), testVibeEvent.getDateTime().getClass());
        assertEquals(formatter.format(testDate), testVibeEvent.getDateTimeString());
        assertEquals("ALONE", testVibeEvent.getSocSit().toString());
        assertEquals("", testVibeEvent.getID());
        assertEquals("image", testVibeEvent.getImage());
        assertEquals(2.0, testVibeEvent.getLongitude(), 0);
        assertEquals(2.0, testVibeEvent.getLongitude(), 0);

    }

    @Test
    public void GetVibe() {
        VibeEvent testVibeEvent = mockVibeEvent();
        Vibe testVibe = mockVibe();

        assertEquals(testVibe.getName(), testVibeEvent.getVibe().getName());
        assertEquals(testVibe.getEmoticon(), testVibeEvent.getVibe().getEmoticon());
        assertEquals(testVibe.getColor(), testVibeEvent.getVibe().getColor());
    }

    @Test
    public void SetVibe() {
        VibeEvent testVibeEvent = mockEmptyVibeEvent();

        assertNull(testVibeEvent.getVibe());

        Vibe testVibe = mockVibe();

        testVibeEvent.setVibe(testVibe.toString());

        assertEquals(testVibe.getName(), testVibeEvent.getVibe().getName());
        assertEquals(testVibe.getEmoticon(), testVibeEvent.getVibe().getEmoticon());
        assertEquals(testVibe.getColor(), testVibeEvent.getVibe().getColor());

    }

    @Test
    public void GetDateTimeString() {
        VibeEvent testVibeEvent = mockVibeEvent();
        Date testDate = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy hh:mm a");

        assertEquals(formatter.format(testDate), testVibeEvent.getDateTimeString());

    }

    @Test
    public void SetDateTime() {
        VibeEvent testVibeEvent = mockEmptyVibeEvent();

        Date testDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy hh:mm a");

        testVibeEvent.setDateTime(testDate);

        assertEquals(formatter.format(testDate), testVibeEvent.getDateTimeString());


    }


    @Test
    public void GetReason() {
        VibeEvent testVibeEvent = mockVibeEvent();

        assertEquals("just really happy", testVibeEvent.getReason());

    }

    @Test
    public void SetReason() {
        VibeEvent testVibeEvent = mockEmptyVibeEvent();

        testVibeEvent.setReason("Not so happy");

        assertEquals("Not so happy", testVibeEvent.getReason());

    }

    @Test
    public void GetSocialSituation() {
        VibeEvent testVibeEvent = mockVibeEvent();

        assertEquals("ALONE", testVibeEvent.getSocSit().toString());

    }

    @Test
    public void SetSocialSituation() {
        VibeEvent testVibeEvent = mockEmptyVibeEvent();

        testVibeEvent.setSocSit(SocSit.WITH_A_CROWD.toString());

        assertEquals("WITH_A_CROWD", testVibeEvent.getSocSit().toString());

    }

    @Test
    public void GetId() {
        VibeEvent testVibeEvent = mockVibeEvent();

        assertEquals("", testVibeEvent.getID());

    }

    @Test
    public void SetId() {
        VibeEvent testVibeEvent = mockEmptyVibeEvent();

        testVibeEvent.setID("123");

        assertEquals("123", testVibeEvent.getID());

    }

    @Test
    public void GetImage() {
        VibeEvent testVibeEvent = mockVibeEvent();

        assertEquals("image", testVibeEvent.getImage());

    }

    @Test
    public void SetImage() {
        VibeEvent testVibeEvent = mockEmptyVibeEvent();

        testVibeEvent.setImage("test.png");

        assertEquals("test.png", testVibeEvent.getImage());

    }

    @Test
    public void GetLatitude() {
        VibeEvent testVibeEvent = mockVibeEvent();

        assertEquals(2.0, testVibeEvent.getLatitude(), 0);

    }

    @Test
    public void testNonEmptyConstructor() {
        assertEquals(mockVibe().getName(), mockVibeEvent().getVibe().getName());
        assertEquals("just really happy", mockVibeEvent().getReason());
        assertEquals("ALONE", mockVibeEvent().getSocSit().toString());
        assertEquals("", mockVibeEvent().getID());
        assertEquals("image", mockVibeEvent().getImage());
    }

    @Test
    public void SetLatitude() {
        VibeEvent testVibeEvent = mockEmptyVibeEvent();

        testVibeEvent.setLatitude(5.0);

        assertEquals(5.0, testVibeEvent.getLatitude(), 0);

    }

    @Test
    public void GetLongitude() {
        VibeEvent testVibeEvent = mockVibeEvent();

        assertEquals(2.0, testVibeEvent.getLongitude(), 0);

    }

    @Test
    public void SetLongitude() {
        VibeEvent testVibeEvent = mockEmptyVibeEvent();

        testVibeEvent.setLongitude(5.0);

        assertEquals(5.0, testVibeEvent.getLongitude(), 0);
    }


}