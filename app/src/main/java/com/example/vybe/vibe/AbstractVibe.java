package com.example.vybe.vibe;

import java.io.Serializable;

public abstract class AbstractVibe implements Serializable {
    protected int color;
    protected int emoticon;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getEmoticon() {
        return emoticon;
    }

    public void setEmoticon(int emoticon) {
        this.emoticon = emoticon;
    }
}