package com.example.vybe;

import com.example.vybe.Models.Vibe;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests the VibeFactory Class and Subclasses
 *
 * These tests ensure that the data of a Vibe is properly stored and returned from a Vibe Object
 */
public class VibeTest {

    @Test
    public void GetAngryVibe() {
        Vibe angry = Vibe.ANGRY;
        assertEquals("Angry", angry.getName());
        assertEquals(R.color.Red, angry.getColor());
        assertEquals(R.drawable.ic_angry, angry.getEmoticon());

        angry = Vibe.ofName("Angry");
        assertEquals("Angry", angry.getName());
        assertEquals(R.color.Red, angry.getColor());
        assertEquals(R.drawable.ic_angry, angry.getEmoticon());

        angry = Vibe.ofEmoticon(R.drawable.ic_angry);
        assertEquals("Angry", angry.getName());
        assertEquals(R.color.Red, angry.getColor());
        assertEquals(R.drawable.ic_angry, angry.getEmoticon());

    }

    @Test
    public void GetDisgustedVibe() {
        Vibe disgusted = Vibe.DISGUSTED;
        assertEquals("Disgusted", disgusted.getName());
        assertEquals(R.color.Green, disgusted.getColor());
        assertEquals(R.drawable.ic_disgusted, disgusted.getEmoticon());

        disgusted = Vibe.ofName("disgusted");
        assertEquals("Disgusted", disgusted.getName());
        assertEquals(R.color.Green, disgusted.getColor());
        assertEquals(R.drawable.ic_disgusted, disgusted.getEmoticon());

        disgusted = Vibe.ofEmoticon(R.drawable.ic_disgusted);
        assertEquals("Disgusted", disgusted.getName());
        assertEquals(R.color.Green, disgusted.getColor());
        assertEquals(R.drawable.ic_disgusted, disgusted.getEmoticon());

    }

    @Test
    public void GetScaredVibe() {
        Vibe scared = Vibe.SCARED;
        assertEquals("Scared", scared.getName());
        assertEquals(R.color.Blue, scared.getColor());
        assertEquals(R.drawable.ic_scared, scared.getEmoticon());

        scared = Vibe.ofName("SCARED");
        assertEquals("Scared", scared.getName());
        assertEquals(R.color.Blue, scared.getColor());
        assertEquals(R.drawable.ic_scared, scared.getEmoticon());

        scared = Vibe.ofEmoticon(R.drawable.ic_scared);
        assertEquals("Scared", scared.getName());
        assertEquals(R.color.Blue, scared.getColor());
        assertEquals(R.drawable.ic_scared, scared.getEmoticon());

    }

    @Test
    public void GetHappyVibe() {
        Vibe happy = Vibe.HAPPY;
        assertEquals("Happy", happy.getName());
        assertEquals(R.color.Yellow, happy.getColor());
        assertEquals(R.drawable.ic_happy, happy.getEmoticon());

        happy = Vibe.ofName("Happy");
        assertEquals("Happy", happy.getName());
        assertEquals(R.color.Yellow, happy.getColor());
        assertEquals(R.drawable.ic_happy, happy.getEmoticon());

        happy = Vibe.ofEmoticon(R.drawable.ic_happy);
        assertEquals("Happy", happy.getName());
        assertEquals(R.color.Yellow, happy.getColor());
        assertEquals(R.drawable.ic_happy, happy.getEmoticon());

    }

    @Test
    public void GetSadVibe() {
        Vibe sad = Vibe.SAD;
        assertEquals("Sad", sad.getName());
        assertEquals(R.color.Gray, sad.getColor());
        assertEquals(R.drawable.ic_sad, sad.getEmoticon());

        sad = Vibe.ofName("SAD");
        assertEquals("Sad", sad.getName());
        assertEquals(R.color.Gray, sad.getColor());
        assertEquals(R.drawable.ic_sad, sad.getEmoticon());

        sad = Vibe.ofEmoticon(R.drawable.ic_sad);
        assertEquals("Sad", sad.getName());
        assertEquals(R.color.Gray, sad.getColor());
        assertEquals(R.drawable.ic_sad, sad.getEmoticon());

    }

    @Test
    public void GetSurprisedVibe() {
        Vibe surprised = Vibe.SURPRISED;
        assertEquals("Surprised", surprised.getName());
        assertEquals(R.color.Orange, surprised.getColor());
        assertEquals(R.drawable.ic_surprised, surprised.getEmoticon());

        surprised = Vibe.ofName("surprised");
        assertEquals("Surprised", surprised.getName());
        assertEquals(R.color.Orange, surprised.getColor());
        assertEquals(R.drawable.ic_surprised, surprised.getEmoticon());

        surprised = Vibe.ofEmoticon(R.drawable.ic_surprised);
        assertEquals("Surprised", surprised.getName());
        assertEquals(R.color.Orange, surprised.getColor());
        assertEquals(R.drawable.ic_surprised, surprised.getEmoticon());

    }

    @Test
    public void GetBlankVibe() {
        Vibe none = Vibe.NONE;
        assertEquals("None", none.getName());
        assertEquals(R.color.Black, none.getColor());
        assertEquals(R.drawable.ic_no_vibe, none.getEmoticon());

        none = Vibe.ofName("none");
        assertEquals("None", none.getName());
        assertEquals(R.color.Black, none.getColor());
        assertEquals(R.drawable.ic_no_vibe, none.getEmoticon());

        none = Vibe.ofEmoticon(R.drawable.ic_no_vibe);
        assertEquals("None", none.getName());
        assertEquals(R.color.Black, none.getColor());
        assertEquals(R.drawable.ic_no_vibe, none.getEmoticon());

    }

    @Test(expected = IllegalArgumentException.class)
    public void InvalidVibeInputs_ThrowException() {
        // Invalid names should result in the NONE Vibe
        assertEquals(Vibe.NONE,  Vibe.ofName("AntiVibe"));

        // Invalid emoticons should throw an error
        Vibe.ofEmoticon(20);

    }
}
