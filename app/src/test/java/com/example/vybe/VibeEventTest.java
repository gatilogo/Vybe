package com.example.vybe;

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

    private Date date = new Date();

    private VibeEvent mockEmptyVibeEvent() {
        return new VibeEvent();
    }

    private VibeEvent mockVibeEvent() {
        return new VibeEvent(mockVibe().getName(), date,
                "just really happy", "Alone", "", "image", 2.0, 2.0);
    }

    private Vibe mockVibe() {
        return VibeFactory.getVibe("happy");
    }

    @Test
    public void testEmptyConstructor() {
        // TODO: talk about how to test
        assertNull(mockEmptyVibeEvent().getVibe());
        assertNull(mockEmptyVibeEvent().getReason());
        assertNull(mockEmptyVibeEvent().getSocialSituation());
        assertNull(mockEmptyVibeEvent().getId());
        assertNull(mockEmptyVibeEvent().getImage());
    }

    @Test
    public void testNonEmptyConstructor() {
        assertEquals(mockVibe().getName(), mockVibeEvent().getVibe().getName());
//        assertEquals(ldt, mockVibeEvent().getDateTime());
        assertEquals("just really happy", mockVibeEvent().getReason());
        assertEquals("Alone", mockVibeEvent().getSocialSituation());
        assertEquals("", mockVibeEvent().getId());
        assertEquals("image", mockVibeEvent().getImage());
    }

    @Test
    public void testGetVibe() {
        assertEquals(mockVibe().getName(), mockVibeEvent().getVibe().getName());
        assertEquals(mockVibe().getEmoticon(), mockVibeEvent().getVibe().getEmoticon());
        assertEquals(mockVibe().getColor(), mockVibeEvent().getVibe().getColor());
    }

    @Ignore
    public void GetDateTimeString() {

    }

}