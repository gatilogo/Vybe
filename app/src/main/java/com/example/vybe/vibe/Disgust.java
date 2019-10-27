package com.example.vybe.vibe;

import com.example.vybe.R;

public class Disgust extends AbstractVibe {

    Disgust() {
        this.color = R.color.Green;
        this.emoticon = R.drawable.disgust;
    }

    @Override
    public String toString() {
        return "Disgust";
    }
}
