package com.example.vybe;

import android.icu.text.SimpleDateFormat;

import com.example.vybe.Models.VibeEvent;
import com.example.vybe.Models.vibefactory.Vibe;
import com.example.vybe.Models.vibefactory.VibeFactory;

import org.junit.Ignore;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests the VibeEvent Class
 *
 * These tests ensure that the data of a Vibe Event is properly stored and returned from a VibeEvent Object
 */
public class VibeEventTest {

    private VibeEvent mockEmptyVibeEvent() {
        return new VibeEvent();
    }

    private VibeEvent mockVibeEvent() {
        Date date = new Date();

        return new VibeEvent(mockVibe().getName(), date,
                "just really happy", "Alone", "", "image", 2.0, 2.0);
    }

    private Vibe mockVibe() {
        return VibeFactory.getVibe("happy");
    }

    @Test
    public void VibeEventConstructor_EmptyInit() {
        VibeEvent testVibeEvent = mockEmptyVibeEvent();

        assertNull(testVibeEvent.getVibe());
        assertNull(testVibeEvent.getReason());
        assertNull(testVibeEvent.getSocialSituation());
        assertNull(testVibeEvent.getId());
        assertNull(testVibeEvent.getImage());
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
        assertEquals("Alone", testVibeEvent.getSocialSituation());
        assertEquals("", testVibeEvent.getId());
        assertEquals("image", testVibeEvent.getImage());
    }

    /*
    * TODO: Discuss if we want test methods for each getter since we're mainly just
    *  doing the same thing we do for testing the constructor tests
     * */

    @Test
    public void GetVibe() {
        VibeEvent testVibeEvent = mockVibeEvent();
        Vibe testVibe = mockVibe();

        assertEquals(testVibe.getName(), testVibeEvent.getVibe().getName());
        assertEquals(testVibe.getEmoticon(), testVibeEvent.getVibe().getEmoticon());
        assertEquals(testVibe.getColor(), testVibeEvent.getVibe().getColor());
    }

    @Test
    public void SetVibe(){
        VibeEvent testVibeEvent = mockEmptyVibeEvent();

        assertNull(testVibeEvent.getVibe());

        Vibe testVibe = mockVibe();

        testVibeEvent.setVibe(testVibe);

        assertEquals(testVibe.getName(), testVibeEvent.getVibe().getName());
        assertEquals(testVibe.getEmoticon(), testVibeEvent.getVibe().getEmoticon());
        assertEquals(testVibe.getColor(), testVibeEvent.getVibe().getColor());

    }

    // TODO: Still not sure how we want to test DateTime Methods
    @Test
    public void GetDateTime(){
        VibeEvent testVibeEvent = mockEmptyVibeEvent();

    }

    @Test
    public void SetDateTime(){
        VibeEvent testVibeEvent = mockEmptyVibeEvent();

        Date testDate = new Date();

        testVibeEvent.setDateTime(testDate);

    }

    @Test
    public void GetDateTimeString(){

    }

    @Test
    public void GetReason(){
        VibeEvent testVibeEvent = mockVibeEvent();

        assertEquals("just really happy", testVibeEvent.getReason());

    }

    @Test
    public void SetReason(){
        VibeEvent testVibeEvent = mockEmptyVibeEvent();

        testVibeEvent.setReason("Not so happy");

        assertEquals("Not so happy", testVibeEvent.getReason());

    }

    // TODO: These will likely have to change in the next few merges
    @Test
    public void GetSocialSituation(){
        VibeEvent testVibeEvent = mockVibeEvent();

        assertEquals("Alone", testVibeEvent.getSocialSituation());

    }

    @Test
    public void SetSocialSituation(){
        VibeEvent testVibeEvent = mockEmptyVibeEvent();

        testVibeEvent.setSocialSituation("In A Crowd");

        assertEquals("In A Crowd", testVibeEvent.getSocialSituation());

    }

    @Test
    public void GetId(){
        VibeEvent testVibeEvent = mockVibeEvent();

        assertEquals("", testVibeEvent.getId());

    }

    @Test
    public void SetId(){
        VibeEvent testVibeEvent = mockEmptyVibeEvent();

        testVibeEvent.setId("123");

        assertEquals("123", testVibeEvent.getId());

    }
    @Test
    public void GetImage(){
        VibeEvent testVibeEvent = mockVibeEvent();

        assertEquals("image", testVibeEvent.getImage());

    }

    @Test
    public void SetImage(){
        VibeEvent testVibeEvent = mockEmptyVibeEvent();

        testVibeEvent.setImage("test.png");

        assertEquals("test.png", testVibeEvent.getImage());

    }
    @Test
    public void GetLatitude(){
        VibeEvent testVibeEvent = mockVibeEvent();

        assertEquals(2.0, testVibeEvent.getLatitude(),0 );

    }

    @Test
    public void SetLatitude(){
        VibeEvent testVibeEvent = mockEmptyVibeEvent();

        testVibeEvent.setLatitude(5);

        assertEquals(5, testVibeEvent.getLatitude(),0 );

    }

    @Test
    public void GetLongitude(){
        VibeEvent testVibeEvent = mockVibeEvent();

        assertEquals(2.0, testVibeEvent.getLongitude(), 0);

    }

    @Test
    public void SetLongitude(){
        VibeEvent testVibeEvent = mockEmptyVibeEvent();

        testVibeEvent.setLongitude(5);

        assertEquals(5, testVibeEvent.getLongitude(),0 );

    }


}