package com.example.vybe.vibefactory;

import com.example.vybe.R;

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
        return "Disgusted";
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
