package com.example.vybe.Models.vibefactory;

import com.example.vybe.R;

/**
 * A factory class that generates instances of different implementations of
 * the Vibe interface depending on the user selected implementation. The
 * generated implementation is chosen based on the vibe selected by the user
 * and returns the appropriate Vibe object
 */
public class VibeFactory {

    /**
     * Returns the vibe object the user has selected
     * @param selectedVibe
     *      This is the string representation of the user selected vibe
     * @return
     *      Get an appropriate implementation and instance of a Vibe object
     */
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

    public static Vibe getVibe(int selectedVibe) {

        if (selectedVibe == R.drawable.ic_angry) {
            return new AngryVibe();
        }
        else if (selectedVibe == R.drawable.ic_disgusted) {
            return new DisgustedVibe();
        }
        else if (selectedVibe == R.drawable.ic_scared) {
            return new ScaredVibe();
        }
        else if (selectedVibe == R.drawable.ic_happy) {
            return new HappyVibe();
        }
        else if (selectedVibe == R.drawable.ic_sad) {
            return new SadVibe();
        }
        else if (selectedVibe == R.drawable.ic_surprised) {
            return new SurprisedVibe();
        }

        return null;
    }
}
