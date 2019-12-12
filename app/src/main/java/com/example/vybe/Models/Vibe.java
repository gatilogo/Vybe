package com.example.vybe.Models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.vybe.R;

import java.util.ArrayList;

/**
 * Enum representing a user's emotional state or "vibe" and all
 * functionality relating to using said vibe in reference to what a user has selected
 */
public enum Vibe {
    NONE        ("None", R.color.Black, R.drawable.ic_no_vibe),
    ANGRY       ("Angry", R.color.Red, R.drawable.ic_angry),
    DISGUSTED   ("Disgusted", R.color.Green, R.drawable.ic_disgusted),
    HAPPY       ("Happy", R.color.Yellow, R.drawable.ic_happy),
    SAD         ("Sad", R.color.Gray, R.drawable.ic_sad),
    SCARED      ("Scared", R.color.Blue, R.drawable.ic_scared),
    SURPRISED   ("Surprised", R.color.Orange, R.drawable.ic_surprised);

    private static final String TAG = "Vibe (Enum)";

    private String name;
    private int color;
    private int emoticon;

    Vibe(String name, int color, int emoticon) {
        this.name = name;
        this.color = color;
        this.emoticon = emoticon;
    }

    /**
     * Get a Vibe Enum using the name of the vibe. If a Vibe is not found, an exception will be thrown.
     * NOTE: It's case insensitive. "happy", "HAPPY", "Happy" are all valid
     * @param name
     * The name of the vibe
     * @return A Vibe Enum
     */
    public static Vibe ofName(String name) {
        try {
            return Vibe.valueOf(name.toUpperCase());

        } catch (IllegalArgumentException e) {
            Log.d(TAG, "[ofName(String name)] Vibe Name: " + name + " is invalid. Defaulting to NONE Vibe.");
            return Vibe.NONE;

        }
    }

    /**
     * Get a Vibe Enum using it's emoticon id. If a vibe is not found, an exception will be thrown.
     * @param emoticon
     * The int value of the emoticon of the vibe
     * @return A Vibe Enum
     */
    public static Vibe ofEmoticon(int emoticon) {
        for (Vibe vibe: Vibe.values()) {
            if (vibe.emoticon == emoticon) {
                return vibe;
            }
        }

        throw new IllegalArgumentException("Emoticon is not a vibe: " + emoticon);
    }

    /**
     * Get an ArrayList of all the emoticons of all the vibes
     * @return
     * An ArrayList of all the emoticons of all the vibes
     */
    public static ArrayList<Integer> getEmoticons() {
        ArrayList<Integer> emoticons = new ArrayList<>();

        for (Vibe vibe : Vibe.values()) {
            emoticons.add(vibe.getEmoticon());
        }

        return emoticons;
    }

    public static ArrayList<String> getNames() {
        ArrayList<String> names = new ArrayList<>();

        for (Vibe vibe : Vibe.values()) {
            names.add(vibe.getName());
        }

        return names;
    }

    @NonNull
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
