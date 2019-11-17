package com.example.vybe.Models;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public enum SocialSituation {
    Alone("Alone"),
    WithAnotherPerson("With one other person"),
    WithSeveralPeople("With two or several people"),
    WithCrowd("With a crowd"),
    Same("Same");

    private String desc;

    SocialSituation(String desc) {
        this.desc = desc;
    }

    @Override @NonNull
    public String toString() {
        return desc;
    }

    public static SocialSituation at(int position) {
        return SocialSituation.values()[position];
    }

    public static ArrayList<String> stringValues() {
        SocialSituation[] socialSituations = SocialSituation.values();

        ArrayList<String> stringValues = new ArrayList<>();

        for (int i = 0; i < socialSituations.length; ++i) {
            stringValues.add(socialSituations[i].toString());
        }

        return stringValues;
    }


}
