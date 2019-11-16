package com.example.vybe;

import com.example.vybe.Models.vibefactory.Vibe;
import com.example.vybe.Models.vibefactory.VibeFactory;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class VibeFactoryTest {

    @Test
    public void testGetAngryVibe() {
        Vibe angry = VibeFactory.getVibe("angry");

        assertEquals("angry", angry.getName());
        assertEquals(R.color.Red, angry.getColor());
        assertEquals(R.drawable.ic_angry, angry.getEmoticon());

    }

    @Test
    public void testGetDisgustedVibe() {
        Vibe disgusted = VibeFactory.getVibe("disgusted");

        assertEquals("disgusted", disgusted.getName());
        assertEquals(R.color.Green, disgusted.getColor());
        assertEquals(R.drawable.ic_disgusted, disgusted.getEmoticon());

    }

    @Test
    public void testGetScaredVibe() {
        Vibe scared = VibeFactory.getVibe("scared");

        assertEquals("scared", scared.getName());
        assertEquals(R.color.Blue, scared.getColor());
        assertEquals(R.drawable.ic_scared, scared.getEmoticon());

    }

    @Test
    public void testGetHappyVibe() {
        Vibe happy = VibeFactory.getVibe("happy");

        assertEquals("happy", happy.getName());
        assertEquals(R.color.Yellow, happy.getColor());
        assertEquals(R.drawable.ic_happy, happy.getEmoticon());

    }

    @Test
    public void testGetSadVibe() {
        Vibe sad = VibeFactory.getVibe("sad");

        assertEquals("sad", sad.getName());
        assertEquals(R.color.Gray, sad.getColor());
        assertEquals(R.drawable.ic_sad, sad.getEmoticon());

    }

    @Test
    public void testGetSurprisedVibe() {
        Vibe surprised = VibeFactory.getVibe("surprised");

        assertEquals("surprised", surprised.getName());
        assertEquals(R.color.Orange, surprised.getColor());
        assertEquals(R.drawable.ic_surprised, surprised.getEmoticon());

    }

    @Test
    public void testGetNullVibe_Empty() {
        Vibe vibeless = VibeFactory.getVibe("");

        assertNull(vibeless);
    }

    @Test
    public void testGetNullVibe_NonEmpty() {
        Vibe antivibe = VibeFactory.getVibe("Antivibe");

        assertNull(antivibe);
    }



}
