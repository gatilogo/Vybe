package com.example.vybe.vibe;

import com.example.vybe.R;

public class Surprise extends AbstractVibe {

    public Surprise() {
        this.color = R.color.Orange;
        this.emoticon = R.drawable.surprise;
    }

    @Override
    public String toString() {
        return "Surprise";
    }
}
