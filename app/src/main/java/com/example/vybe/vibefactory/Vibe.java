package com.example.vybe.vibefactory;

import java.io.Serializable;

public interface Vibe extends Serializable {

    /**
     * This gets the string value of the Vibe class selected by a user
     * @return
     * The string that describes the Vibe selected
     */
    String getName();

    int getEmoticon();
    int getColor();
}
