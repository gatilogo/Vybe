package com.example.vybe.vibefactory;

import com.example.vybe.R;

public class SurprisedVibe implements Vibe {
    private int color;
    private int emoticon;

    /**
     * This Constructor sets the new vibe's color and emoticon
     */
    public SurprisedVibe() {
        this.color = R.color.Orange;
        this.emoticon = R.drawable.ic_surprised;
    }

    /**
     * This gets the string value of the Vibe class selected by a user
     * @return
     * The string that describes the Vibe selected
     */
    public String getVibe() {
        return "Surprised";
    }

    @Override
    public int getEmoticon() {
        return this.emoticon;
    }

    @Override
    public int getColor() {
        return this.color;
    }
}
