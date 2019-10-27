package com.example.vybe.vibe;

import com.example.vybe.R;

public class Angry extends AbstractVibe {

    public Angry() {
        this.color = R.color.Red;
        this.emoticon = R.drawable.angry;
    }

    @Override
    public String toString() {
        return "Angry";
    }
}
