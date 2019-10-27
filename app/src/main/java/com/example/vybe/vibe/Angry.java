package com.example.vybe.vibe;

import com.example.vybe.R;

public class Angry extends AbstractVibe {

    Angry() {
        this.color = R.color.Red;
        this.emoticon = R.drawable.angry;
    }

    @Override
    public String toString() {
        return "Angry";
    }
}
