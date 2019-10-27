package com.example.vybe.vibe;

import com.example.vybe.R;

public class Fear extends AbstractVibe {

    public Fear() {
        this.color = R.color.Blue;
        this.emoticon = R.drawable.fear;
    }

    @Override
    public String toString() {
        return "Fear";
    }
}
