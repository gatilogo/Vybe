package com.example.vybe.Models.vibefactory;

import com.example.vybe.R;

/**
 * The implementation of the vibe interface that represents a disgusted vibe
 */
public class DisgustedVibe implements Vibe {
    private int color;
    private int emoticon;

    /**
     * This Constructor sets the new vibe's color and emoticon
     */
    public DisgustedVibe() {
        this.color = R.color.Green;
        this.emoticon = R.drawable.ic_disgusted;
    }

    /**
     * This gets the string value of the Vibe class selected by a user
     * @return
     * The string that describes the Vibe selected
     */
    public String getName() {
        return "disgusted";
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
