package com.example.vybe.vibe;

import java.io.Serializable;

/**
 * This is a class that keeps track of a Vibe's color and it's emoticon.
 * It implements Serializable interface
 */
public abstract class AbstractVibe implements Serializable {
    protected int color;
    protected int emoticon;

    /**
     * This gets the colorspace value for the vibe's color
     * @return
     * the vibe's color
     * */
    public int getColor() {
        return color;
    }


    /**
     * This Constructor sets a vibe's color
     * @param  color
     * color of the vibe
     */
    public void setColor(int color) {
        this.color = color;
    }

    /**
     * This gets the drawable value for the vibe's emoticon
     * @return
     * the vibe's emoticon
     * */
    public int getEmoticon() {
        return emoticon;
    }

    /**
     * This Constructor sets a vibe's emoticon
     * @param  emoticon
     * emoticon of the vibe
     */
    public void setEmoticon(int emoticon) {
        this.emoticon = emoticon;
    }
}