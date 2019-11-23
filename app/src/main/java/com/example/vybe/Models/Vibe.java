package com.example.vybe.Models;

import androidx.annotation.NonNull;

import com.example.vybe.R;

import java.util.ArrayList;

public enum Vibe {
    ANGRY       ("Angry", R.color.Red, R.drawable.ic_angry),
    DISGUSTED   ("Disgusted", R.color.Green, R.drawable.ic_disgusted),
    HAPPY       ("Happy", R.color.Yellow, R.drawable.ic_happy),
    SAD         ("Sad", R.color.Gray, R.drawable.ic_sad),
    SCARED      ("Scared", R.color.Blue, R.drawable.ic_scared),
    SURPRISED   ("Surprised", R.color.Orange, R.drawable.ic_surprised);

    private String name;
    private int color;
    private int emoticon;

    Vibe(String name, int color, int emoticon) {
        this.name = name;
        this.color = color;
        this.emoticon = emoticon;
    }

    public static Vibe ofName(String name) {
        return Vibe.valueOf(name.toUpperCase());
    }

    public static Vibe ofEmoticon(int emoticon) {

        for (Vibe vibe: Vibe.values()) {
            if (vibe.emoticon == emoticon) {
                return vibe;
            }
        }

        throw new IllegalArgumentException("Emoticon is not a vibe: " + emoticon);

    }

    public static ArrayList<Integer> getEmoticons() {
        Vibe[] vibes = Vibe.values();
        ArrayList<Integer> emoticons = new ArrayList<>();

        for (Vibe vibe : Vibe.values()) {
            emoticons.add(vibe.getEmoticon());
        }

        return emoticons;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public int getEmoticon() {
        return emoticon;
    }

    @Override
    @NonNull
    public String toString() {
        return name;
    }


}
