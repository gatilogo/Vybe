package com.example.vybe.vibe;

import com.example.vybe.R;

/**
 * This is a class that encapsulates the Disgust emotion's emoticon and color
 * It extends the AbstractVibe Class and overrides toString's method
 */
public class Disgust extends AbstractVibe {

    /**
     * This Constructor sets the new vibe's color and emoticon
     */
    public Disgust() {
        this.color = R.color.Green;
        this.emoticon = R.drawable.disgust;
    }

    /**
     * This overrides the toString method to get the string value of the Disgust class
     * @return
     * The string that represents the Disgust class
     */
    @Override
    public String toString() {
        return "Disgust";
    }
}
