package com.example.vybe.vibefactory;

import com.example.vybe.R;

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
    public String getVibe() {
        return "Scared";
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
