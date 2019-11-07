package com.example.vybe;

import com.example.vybe.vibefactory.*;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class VibeFactoryTest {

    private VibeFactory mockVibeFactory() {
        VibeFactory vibeFactory = new VibeFactory();
        return vibeFactory;
    }

    @Test
    public void testGetAngryVibe() {
        VibeFactory vibeFactory = mockVibeFactory();
        Vibe angry = vibeFactory.getVibe("angry");

        assertEquals("angry", angry.getName());
        assertEquals(R.color.Red, angry.getColor());
        assertEquals(R.drawable.ic_angry, angry.getEmoticon());

    }

    @Test
    public void testGetDisgustedVibe() {
        VibeFactory vibeFactory = mockVibeFactory();
        Vibe Disgusted = vibeFactory.getVibe("disgusted");

        assertEquals("disgusted", Disgusted.getName());
        assertEquals(R.color.Green, Disgusted.getColor());
        assertEquals(R.drawable.ic_disgusted, Disgusted.getEmoticon());

    }

    @Test
    public void testGetScaredVibe() {
        VibeFactory vibeFactory = mockVibeFactory();
        Vibe Scared = vibeFactory.getVibe("scared");

        assertEquals("scared", Scared.getName());
        assertEquals(R.color.Blue, Scared.getColor());
        assertEquals(R.drawable.ic_scared, Scared.getEmoticon());

    }

    @Test
    public void testGetHappyVibe() {
        VibeFactory vibeFactory = mockVibeFactory();
        Vibe happy = vibeFactory.getVibe("happy");

        assertEquals("happy", happy.getName());
        assertEquals(R.color.Yellow, happy.getColor());
        assertEquals(R.drawable.ic_happy, happy.getEmoticon());

    }

    @Test
    public void testGetSadVibe() {
        VibeFactory vibeFactory = mockVibeFactory();
        Vibe sad = vibeFactory.getVibe("sad");

        assertEquals("sad", sad.getName());
        assertEquals(R.color.Gray, sad.getColor());
        assertEquals(R.drawable.ic_sad, sad.getEmoticon());

    }

    @Test
    public void testGetSurprisedVibe() {
        VibeFactory vibeFactory = mockVibeFactory();
        Vibe Surprised = vibeFactory.getVibe("surprised");

        assertEquals("surprised", Surprised.getName());
        assertEquals(R.color.Orange, Surprised.getColor());
        assertEquals(R.drawable.ic_surprised, Surprised.getEmoticon());

    }

    @Test
    public void testGetNullVibe_Empty() {
        VibeFactory vibeFactory = mockVibeFactory();
        Vibe vibeless = vibeFactory.getVibe("");

        assertNull(vibeless);
    }

    @Test
    public void testGetNullVibe_NonEmpty() {
        VibeFactory vibeFactory = mockVibeFactory();
        Vibe antivibe = vibeFactory.getVibe("Antivibe");

        assertNull(antivibe);
    }



}
