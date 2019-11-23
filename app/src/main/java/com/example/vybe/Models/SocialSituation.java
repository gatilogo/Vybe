package com.example.vybe.Models;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public enum SocialSituation {
    Alone("Alone"),
    WithAnotherPerson("With one other person"),
    WithSeveralPeople("With two or several people"),
    WithCrowd("With a crowd"),
    Same("Same");

    // Add a desc to every Enum
    private String desc;
    SocialSituation(String desc) {
        this.desc = desc;
    }

    /**
     * Get "With on other person" instead of "WithAnotherPerson"
     * @return The desc of the Social Situation
     */
    @Override @NonNull
    public String toString() {
        return desc;
    }

    /**
     * Get a SocialSituation using its index
     * @param position
     * Index of SocialSituation
     * @return SocialSituation at index position
     */
    //
    public static SocialSituation at(int position) {
        return SocialSituation.values()[position];
    }

//    /**
//     * Get a SocialSituation using its desc
//     * @param desc
//     * Description of the SocialSituation Enum
//     * @return SocialSituation that has the description desc
//     */
//    public static SocialSituation of(SocialSituation desc) {
//        for (SocialSituation ss: SocialSituation.values()) {
//            if (ss.toString().equals(desc)) {
//                return ss;
//            }
//        }
//
//        return null;
//    }

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

}
