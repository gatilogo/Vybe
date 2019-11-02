package com.example.vybe.vibe;

import com.example.vybe.R;

/**
 * This is a class that encapsulates the Surprise emotion's emoticon and color
 * It extends the AbstractVibe Class and overrides toString's method
 */
public class Surprise extends AbstractVibe {

    /**
     * This Constructor sets the new vibe's color and emoticon
     */
    public Surprise() {
        this.color = R.color.Orange;
        this.emoticon = R.drawable.surprise;
    }

    /**
     * This overrides the toString method to get the string value of the Sad class
     * @return
     * The string that represents the Surprise class
     */
    @Override
    public String toString() {
        return "Surprise";
    }
}
