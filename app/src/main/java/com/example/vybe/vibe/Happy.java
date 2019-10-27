package com.example.vybe.vibe;

import com.example.vybe.R;

/**
 * This is a class that encapsulates the Happy emotion's emoticon and color
 * It extends the AbstractVibe Class and overrides toString's method
 */
public class Happy extends AbstractVibe {

    /**
     * This Constructor sets the new vibe's color and emoticon
     */
    public Happy() {
        this.color = R.color.Yellow;
        this.emoticon = R.drawable.happy;
    }

    /**
     * This overrides the toString method to get the string value of the Happy class
     * @return
     * The string that represents the Disgust class
     */
    @Override
    public String toString() {
        return "Happy";
    }
}
