package com.example.vybe.Models;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public enum SocialSituation {
    ALONE("Alone"),
    WITH_ANOTHER_PERSON("With one other person"),
    WITH_SEVERAL_PEOPLE("With two or several people"),
    WITH_A_CROWD("With a crowd");

    private static final String TAG = "SocialSituation";

    // Add a desc to every Enum
    private String desc;

    SocialSituation(String desc) {
        this.desc = desc;
    }

    /**
     * Get "With on other person" instead of "WithAnotherPerson"
     *
     * @return The desc of the Social Situation
     */
    @Override
    @NonNull
    public String toString() {
        return desc;
    }

    /**
     * Get a SocialSituation using its index
     * @param position Index of SocialSituation
     * @return SocialSituation at index position
     */
    public static SocialSituation at(int position) {
        return SocialSituation.values()[position];
    }

    /**
     * Get a String ArrayList of all the SocialSituation enums
     * @return ArrayList of Social Situation String
     */
    public static ArrayList<String> stringValues() {
        SocialSituation[] socialSituations = SocialSituation.values();

        ArrayList<String> stringValues = new ArrayList<>();

        for (int i = 0; i < socialSituations.length; ++i) {
            stringValues.add(socialSituations[i].toString());
        }

        return stringValues;
    }

    /**
     * Get the Social Situation of a string. If no matching Social Situation is found, return null.
     * @param name
     * Name of Social Situation eg. ALONE, WITH_ANOTHER_PERSON, etc..
     * NOTE: "Alone", "With another person", etc... are not the names of Social Situation and this function will return NULL if they are entered
     * @return
     * A SocialSocituation using it's name
     */

    public static SocialSituation of(String name) {

        if (name == null)
            return null;

        try {
            return SocialSituation.valueOf(name);

        } catch (IllegalArgumentException e) {
            Log.d(TAG,"[of()] Trying to get value of Invalid Social Situation: " + name);
            return null;
        }
    }

}
