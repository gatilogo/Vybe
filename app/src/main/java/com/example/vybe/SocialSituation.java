package com.example.vybe;

import android.util.Log;

import androidx.annotation.NonNull;

public enum SocialSituation {
    Alone("Alone", 0),
    OneOtherPerson("With one other person", 1),
    SeveralPeople("With two or several people", 2),
    Crowd("With a crowd", 3),
    Same("Same", 4);

    private String desc;
    private int position;

    SocialSituation(String desc, int position) {
        this.desc = desc;
        this.position = position;
    }

    @Override @NonNull
    public String toString() {
        return desc;
    }

    public static String[] getAll() {
        SocialSituation[] socialSituations = SocialSituation.values();
        String[] socialSituationStrs = new String[socialSituations.length];

        for (int i = 0; i < socialSituations.length; ++i) {
            socialSituationStrs[i] = socialSituations[i].toString();
        }

        return socialSituationStrs;
    }

    public static SocialSituation getValueOfString(String inputStr) {
        SocialSituation[] socialSituations = SocialSituation.values();

        for (SocialSituation socialSituation: SocialSituation.values()) {
            if (inputStr.compareTo(socialSituation.toString()) == 0) {
                return socialSituation;
            }
        }

        throw new IllegalArgumentException("No Social Situation Enum of: " + inputStr);
    }

    public static SocialSituation at(int position) {
        return SocialSituation.values()[position];
    }
}
