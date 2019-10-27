package com.example.vybe.vibe;

import com.example.vybe.R;

public class Sad extends AbstractVibe {

    public Sad() {
        this.color = R.color.Gray;
        this.emoticon = R.drawable.sad;
    }

    @Override
    public String toString() {
        return "Sad";
    }
}
