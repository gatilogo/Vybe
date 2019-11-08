package com.example.vybe.vibefactory;

import com.example.vybe.R;

/**
 * The implementation of the vibe interface that represents an angry vibe
 */
public class AngryVibe implements Vibe {
    private int color;
    private int emoticon;

    /**
     * This Constructor sets the new vibe's color and emoticon
     */
    public AngryVibe() {
        this.color = R.color.Red;
        this.emoticon = R.drawable.ic_angry;
    }

    /**
     * @return
     * The string that describes the Vibe selected
     */
    public String getName() {
        return "angry";
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
