package com.example.vybe.Models.vibefactory;

import java.io.Serializable;

/**
 * The vibe interface is the interface that is utilized by the VibeFactory
 * to generate and implement the individual user-selected Vibe
 */
public interface Vibe extends Serializable {

    /**
     * This gets the string value of the Vibe class selected by a user
     * @return
     * The string that describes the Vibe selected
     */
    String getName();

    /**
     * This gets the integer valued ID for the Emoticon
     * which represents the Vibe class selected by a user
     * @return
     * The view ID that uniquely identifies the Emoticon representing
     * the vibe selected
     */
    int getEmoticon();

    /**
     * This gets the integer valued ID for the color
     * which represents the Vibe class selected by a user
     * @return
     * The view ID that uniquely identifies the color representing
     * the vibe selected
     */
    int getColor();
}
