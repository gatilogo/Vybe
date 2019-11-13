package com.example.vybe;

import com.example.vybe.vibefactory.Vibe;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class VibeEventTest {

    private LocalDateTime ldt = LocalDateTime.now();

    private VibeEvent mockEmptyVibeEvent() {
        return new VibeEvent();
    }

    private VibeEvent mockVibeEvent() {
        return new VibeEvent(mockVibe().getName(), ldt,
                "just really happy", "Alone", "", "image");
    }

    private Vibe mockVibe() {
        return VibeFactory.getVibe("happy");
    }

    @Test
    public void testEmptyConstructor() {
        assertNull(mockEmptyVibeEvent().getVibe());
        assertNull(mockEmptyVibeEvent().getDateTime());
        assertNull(mockEmptyVibeEvent().getReason());
        assertNull(mockEmptyVibeEvent().getSocialSituation());
        assertNull(mockEmptyVibeEvent().getId());
        assertNull(mockEmptyVibeEvent().getImage());
    }

    @Test
    public void testNonEmptyConstructor() {
        assertEquals(mockVibe().getName(), mockVibeEvent().getVibe().getName());
        assertEquals(ldt, mockVibeEvent().getDateTime());
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

    @Test
    public void testGetDateTimeFormat() {
        Date date = new Date(ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        assertEquals(date, mockVibeEvent().getDateTimeFormat());
    }
}