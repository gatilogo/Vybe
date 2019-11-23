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

        assertEquals(testVibe.getName(), testVibeEvent.getVibe().getName());
        assertEquals(testVibe.getEmoticon(), testVibeEvent.getVibe().getEmoticon());
        assertEquals(testVibe.getColor(), testVibeEvent.getVibe().getColor());

    }


}