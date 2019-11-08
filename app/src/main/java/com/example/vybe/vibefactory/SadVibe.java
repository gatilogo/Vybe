package com.example.vybe.vibefactory;

import com.example.vybe.R;

/**
 * The implementation of the vibe interface that represents a sad vibe
 */
public class SadVibe implements Vibe {
    private int color;
    private int emoticon;

    /**
     * This Constructor sets the new vibe's color and emoticon
     */
    public SadVibe() {
        this.color = R.color.Gray;
        this.emoticon = R.drawable.ic_sad;
    }

    /**
     * This gets the string value of the Vibe class selected by a user
     * @return
     * The string that describes the Vibe selected
     */
    public String getName() {
        return "sad";
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
