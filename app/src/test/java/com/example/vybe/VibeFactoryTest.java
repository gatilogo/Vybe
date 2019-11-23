package com.example.vybe;

import com.example.vybe.Models.vibefactory.Vibe;
import com.example.vybe.Models.vibefactory.VibeFactory;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests the VibeFactory Class and Subclasses
 *
 * These tests ensure that the data of a Vibe is properly stored and returned from a Vibe Object
 */
public class VibeFactoryTest {

    @Test
    public void GetAngryVibe() {
        Vibe angry = VibeFactory.getVibe("angry");

        assertEquals("angry", angry.getName());
        assertEquals(R.color.Red, angry.getColor());
        assertEquals(R.drawable.ic_angry, angry.getEmoticon());

    }

    @Test
    public void GetDisgustedVibe() {
        Vibe disgusted = VibeFactory.getVibe("disgusted");

        assertEquals("disgusted", disgusted.getName());
        assertEquals(R.color.Green, disgusted.getColor());
        assertEquals(R.drawable.ic_disgusted, disgusted.getEmoticon());

    }

    @Test
    public void GetScaredVibe() {
        Vibe scared = VibeFactory.getVibe("scared");

        assertEquals("scared", scared.getName());
        assertEquals(R.color.Blue, scared.getColor());
        assertEquals(R.drawable.ic_scared, scared.getEmoticon());

    }

    @Test
    public void GetHappyVibe() {
        Vibe happy = VibeFactory.getVibe("happy");

        assertEquals("happy", happy.getName());
        assertEquals(R.color.Yellow, happy.getColor());
        assertEquals(R.drawable.ic_happy, happy.getEmoticon());

    }

    @Test
    public void GetSadVibe() {
        Vibe sad = VibeFactory.getVibe("sad");

        assertEquals("sad", sad.getName());
        assertEquals(R.color.Gray, sad.getColor());
        assertEquals(R.drawable.ic_sad, sad.getEmoticon());

    }

    @Test
    public void GetSurprisedVibe() {
        Vibe surprised = VibeFactory.getVibe("surprised");

        assertEquals("surprised", surprised.getName());
        assertEquals(R.color.Orange, surprised.getColor());
        assertEquals(R.drawable.ic_surprised, surprised.getEmoticon());

    }

    @Test
    public void GetNullVibe_Empty() {
        Vibe vibeless = VibeFactory.getVibe("");

        assertNull(vibeless);
    }

    @Test
    public void GetNullVibe_NonEmpty() {
        Vibe antivibe = VibeFactory.getVibe("Antivibe");

        assertNull(antivibe);
    }



}
