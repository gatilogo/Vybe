package com.example.vybe.vibefactory;

public class VibeFactory {

    public static Vibe getVibe(String selectedVibe) {
        if (selectedVibe.equals("Angry")) {
            return new AngryVibe();
        }
        else if (selectedVibe.equals("Disgust")) {
            return new DisgustVibe();
        }
        else if (selectedVibe.equals("Fear")) {
            return new FearVibe();
        }
        else if (selectedVibe.equals("Happy")) {
            return new HappyVibe();
        }
        else if (selectedVibe.equals("Sad")) {
            return new SadVibe();
        }
        else if (selectedVibe.equals("Surprise")) {
            return new SurpriseVibe();
        }
//        else if (selectedVibe.equals("Clownery")) {
//            return new ClowneryVibe();
//        }
        return null;
    }
}
