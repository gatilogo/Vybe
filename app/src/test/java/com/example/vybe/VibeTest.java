package com.example.vybe;

import com.example.vybe.vibe.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VibeTest {

    @Test
    void testGetAngry() {
        Angry testAngry = new Angry();
        assertEquals(R.color.Red, testAngry.getColor());
        assertEquals("Angry", testAngry.toString());
    }


    @Test
    void testGetDisgust() {
        Disgust testDisgust = new Disgust();
        assertEquals(R.color.Green, testDisgust.getColor());
        assertEquals("Disgust", testDisgust.toString());
    }

    @Test
    void testGetFear() {
        Fear testFear = new Fear();
        assertEquals(R.color.Blue, testFear.getColor());
        assertEquals("Fear", testFear.toString());
    }


    @Test
    void testGetHappy() {
        Happy testHappy = new Happy();
        assertEquals(R.color.Yellow, testHappy.getColor());
        assertEquals("Happy", testHappy.toString());
    }


    @Test
    void testGetSad() {
        Sad testSad = new Sad();
        assertEquals(R.color.Gray, testSad.getColor());
        assertEquals("Sad", testSad.toString());
    }


    @Test
    void testGetSurprise() {
        Surprise testSurprise = new Surprise();
        assertEquals(R.color.Orange, testSurprise.getColor());
        assertEquals("Surprise", testSurprise.toString());
    }


}
