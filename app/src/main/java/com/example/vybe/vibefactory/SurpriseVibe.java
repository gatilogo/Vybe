package com.example.vybe.vibefactory;

import com.example.vybe.R;

public class SurpriseVibe implements Vibe {
    private int color;
    private int emoticon;

    /**
     * This Constructor sets the new vibe's color and emoticon
     */
    public SurpriseVibe() {
        this.color = R.color.Orange;
        this.emoticon = R.drawable.surprise;
    }

    /**
     * This gets the string value of the Vibe class selected by a user
     * @return
     * The string that describes the Vibe selected
     */
    public String getVibe() {
        return "Surprise";
    }
}
