package com.example.vybe.vibe;

import com.example.vybe.R;

public class Happy extends AbstractVibe {

    public Happy() {
        this.color = R.color.Yellow;
        this.emoticon = R.drawable.happy;
    }

    @Override
    public String toString() {
        return "Happy";
    }
}
