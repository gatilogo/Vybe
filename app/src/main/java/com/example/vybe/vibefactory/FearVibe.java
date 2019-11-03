package com.example.vybe.vibefactory;

import com.example.vybe.R;

public class FearVibe implements Vibe {
    private int color;
    private int emoticon;

    /**
     * This Constructor sets the new vibe's color and emoticon
     */
    public FearVibe() {
        this.color = R.color.Blue;
        this.emoticon = R.drawable.fear;
    }

    /**
     * This gets the string value of the Vibe class selected by a user
     * @return
     * The string that describes the Vibe selected
     */
    public String getVibe() {
        return "Fear";
    }
}
