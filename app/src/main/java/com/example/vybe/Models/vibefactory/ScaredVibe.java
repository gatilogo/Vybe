package com.example.vybe.Models.vibefactory;

import com.example.vybe.R;

/**
 * The implementation of the vibe interface that represents a scared vibe
 */
public class ScaredVibe implements Vibe {
    private int color;
    private int emoticon;

    /**
     * This Constructor sets the new vibe's color and emoticon
     */
    public ScaredVibe() {
        this.color = R.color.Blue;
        this.emoticon = R.drawable.ic_scared;
    }

    /**
     * This gets the string value of the Vibe class selected by a user
     * @return
     * The string that describes the Vibe selected
     */
    public String getName() {
        return "scared";
    }

    /**
     * @return
     * The emoticon view ID that describes the Vibe selected
     */
    @Override
    public int getEmoticon() {
        return this.emoticon;
    }

    /**
     * @return
     * The color view ID that describes the Vibe selected
     */
    @Override
    public int getColor() {
        return this.color;
    }
}
