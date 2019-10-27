package com.example.vybe.vibe;

import com.example.vybe.R;

/**
 * This is a class that encapsulates the angry emotion's emoticon and color
 * It extends the AbstractVibe Class and overrides toString's method
 */
public class Angry extends AbstractVibe {
    /**
     * This Constructor sets the new vibe's color and emoticon
     */
    public Angry() {
        this.color = R.color.Red;
        this.emoticon = R.drawable.angry;
    }

    /**
     * This overrides the toString method to get the string value of the Angry class
     * @return
     * The string that represents the Angry class
     */
    @Override
    public String toString() {
        return "Angry";
    }
}
