package com.example.vybe.vibe;

import com.example.vybe.R;

/**
 * This is a class that encapsulates the Sad emotion's emoticon and color
 * It extends the AbstractVibe Class and overrides toString's method
 */
public class Sad extends AbstractVibe {

    /**
     * This Constructor sets the new vibe's color and emoticon
     */
    public Sad() {
        this.color = R.color.Gray;
        this.emoticon = R.drawable.sad;
    }

    /**
     * This overrides the toString method to get the string value of the Sad class
     * @return
     * The string that represents the Disgust class
     */
    @Override
    public String toString() {
        return "Sad";
    }
}
