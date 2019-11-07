package com.example.vybe;

import java.io.Serializable;

public class Vibe implements Serializable {

    String name;
    String emoticon;

    public Vibe() {
        this.name = "Happy";
    }

    public Vibe(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmoticon() {
        return emoticon;
    }

    public void setEmoticon(String emoticon) {
        this.emoticon = emoticon;
    }
}
