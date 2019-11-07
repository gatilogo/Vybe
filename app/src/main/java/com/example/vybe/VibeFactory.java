package com.example.vybe;

import com.example.vybe.vibefactory.Vibe;
import com.example.vybe.vibefactory.AngryVibe;
import com.example.vybe.vibefactory.DisgustedVibe;
import com.example.vybe.vibefactory.ScaredVibe;
import com.example.vybe.vibefactory.HappyVibe;
import com.example.vybe.vibefactory.SadVibe;
import com.example.vybe.vibefactory.SurprisedVibe;

import java.io.Serializable;

public class VibeFactory implements Serializable {

    public static Vibe getVibe(String selectedVibe) {
        selectedVibe = selectedVibe.toLowerCase();

        if (selectedVibe.equals("angry")) {
            return new AngryVibe();
        }
        else if (selectedVibe.equals("disgusted")) {
            return new DisgustedVibe();
        }
        else if (selectedVibe.equals("scared")) {
            return new ScaredVibe();
        }
        else if (selectedVibe.equals("happy")) {
            return new HappyVibe();
        }
        else if (selectedVibe.equals("sad")) {
            return new SadVibe();
        }
        else if (selectedVibe.equals("surprised")) {
            return new SurprisedVibe();
        }

        return null;
    }
}
