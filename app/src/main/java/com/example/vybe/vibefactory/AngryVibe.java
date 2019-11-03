package com.example.vybe.vibefactory;

import com.example.vybe.R;

public class AngryVibe implements Vibe {
    private int color;
    private int emoticon;

    /**
     * This Constructor sets the new vibe's color and emoticon
     */
    public AngryVibe() {
        this.color = R.color.Red;
        this.emoticon = R.drawable.angry;
    }

    /**
     * This gets the string value of the Vibe class selected by a user
     * @return
     * The string that describes the Vibe selected
     */
    public String getVibe() {
        return "Angry";
    }
}
