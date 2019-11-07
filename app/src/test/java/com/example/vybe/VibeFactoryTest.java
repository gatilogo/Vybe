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

        assertEquals("Angry", angry.getVibe());
        assertEquals(R.color.Red, angry.getColor());
        assertEquals(R.drawable.ic_angry, angry.getEmoticon());

    }

    @Test
    public void testGetDisgustVibe() {
        VibeFactory vibeFactory = mockVibeFactory();
        Vibe disgust = vibeFactory.getVibe("disgust");

        assertEquals("Disgust", disgust.getVibe());
        assertEquals(R.color.Green, disgust.getColor());
        assertEquals(R.drawable.ic_disgusted, disgust.getEmoticon());

    }

    @Test
    public void testGetFearVibe() {
        VibeFactory vibeFactory = mockVibeFactory();
        Vibe fear = vibeFactory.getVibe("fear");

        assertEquals("Fear", fear.getVibe());
        assertEquals(R.color.Blue, fear.getColor());
        assertEquals(R.drawable.ic_fear, fear.getEmoticon());

    }

    @Test
    public void testGetHappyVibe() {
        VibeFactory vibeFactory = mockVibeFactory();
        Vibe happy = vibeFactory.getVibe("happy");

        assertEquals("Happy", happy.getVibe());
        assertEquals(R.color.Yellow, happy.getColor());
        assertEquals(R.drawable.ic_happy, happy.getEmoticon());

    }

    @Test
    public void testGetSadVibe() {
        VibeFactory vibeFactory = mockVibeFactory();
        Vibe sad = vibeFactory.getVibe("sad");

        assertEquals("Sad", sad.getVibe());
        assertEquals(R.color.Gray, sad.getColor());
        assertEquals(R.drawable.ic_sad, sad.getEmoticon());

    }

    @Test
    public void testGetSurpriseVibe() {
        VibeFactory vibeFactory = mockVibeFactory();
        Vibe surprise = vibeFactory.getVibe("surprise");

        assertEquals("Surprise", surprise.getVibe());
        assertEquals(R.color.Orange, surprise.getColor());
        assertEquals(R.drawable.ic_surprised, surprise.getEmoticon());

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
