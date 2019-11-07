package com.example.vybe;

import com.example.vybe.vibefactory.Vibe;
import com.example.vybe.vibefactory.AngryVibe;
import com.example.vybe.vibefactory.DisgustVibe;
import com.example.vybe.vibefactory.FearVibe;
import com.example.vybe.vibefactory.HappyVibe;
import com.example.vybe.vibefactory.SadVibe;
import com.example.vybe.vibefactory.SurpriseVibe;

import java.io.Serializable;

public class VibeFactory implements Serializable {

    public static Vibe getVibe(String selectedVibe) {
        selectedVibe = selectedVibe.toLowerCase();

        if (selectedVibe.equals("angry")) {
            return new AngryVibe();
        }
        else if (selectedVibe.equals("disgust")) {
            return new DisgustVibe();
        }
        else if (selectedVibe.equals("fear")) {
            return new FearVibe();
        }
        else if (selectedVibe.equals("happy")) {
            return new HappyVibe();
        }
        else if (selectedVibe.equals("sad")) {
            return new SadVibe();
        }
        else if (selectedVibe.equals("surprise")) {
            return new SurpriseVibe();
        }

        return null;
    }
}
