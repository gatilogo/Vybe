package com.example.vybe.Models;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * Enum representing a social situation and various functionality relating
 * to a user's selection for said social situation
 */
public enum SocSit {
    ALONE("Alone"),
    WITH_ANOTHER_PERSON("With one other person"),
    WITH_SEVERAL_PEOPLE("With two or several people"),
    WITH_A_CROWD("With a crowd");

    private static final String TAG = "SocSit";

    // Add a desc to every Enum
    private String desc;

    SocSit(String desc) {
        this.desc = desc;
    }

    /**
     * Get "With on other person" instead of "WithAnotherPerson"
     *
     * @return The desc of the Social Situation
     */
    @NonNull
    public String getDesc() {
        return desc;
    }

    /**
     * Get a SocSit using its index
     * @param position Index of SocSit
     * @return SocSit at index position
     */
    public static SocSit at(int position) {
        return SocSit.values()[position];
    }

    /**
     * Get a String ArrayList of all the SocSit enums
     * @return ArrayList of Social Situation String
     */
    public static ArrayList<String> stringValues() {
        SocSit[] socSits = SocSit.values();

        ArrayList<String> stringValues = new ArrayList<>();

        for (int i = 0; i < socSits.length; ++i) {
            stringValues.add(socSits[i].desc);
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

    public static SocSit of(String name) {

        if (name == null)
            return null;

        try {
            return SocSit.valueOf(name);

        } catch (IllegalArgumentException e) {
            Log.d(TAG,"[of()] Trying to get value of Invalid Social Situation: " + name);
            return null;
        }
    }

}
