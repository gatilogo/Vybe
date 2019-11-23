package com.example.vybe;

import com.example.vybe.Models.SocialSituation;
import com.example.vybe.Models.Vibe;
import com.example.vybe.Models.VibeEvent;

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
        return new VibeEvent(mockVibe(), date,
                "just really happy", SocialSituation.ALONE, "", "image", 2.0, 2.0);
    }

    private Vibe mockVibe() {
        return Vibe.HAPPY;
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
        assertEquals("Alone", mockVibeEvent().getSocialSituation().toString());
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