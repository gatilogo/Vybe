package com.example.vybe.vibe;

import com.example.vybe.R;

/**
 * This is a class that encapsulates the Fear emotion's emoticon and color
 * It extends the AbstractVibe Class and overrides toString's method
 */
public class Fear extends AbstractVibe {

    /**
     * This Constructor sets the new vibe's color and emoticon
     */
    public Fear() {
        this.color = R.color.Blue;
        this.emoticon = R.drawable.fear;
    }

    /**
     * This overrides the toString method to get the string value of the Fear class
     * @return
     * The string that represents the Fear class
     */
    @Override
    public String toString() {
        return "Fear";
    }
}
