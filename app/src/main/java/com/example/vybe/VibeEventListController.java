package com.example.vybe;

/**
 * This gets the user's vibe event list and their follower's vibe event list
 * This will be used by MyVibesActivity, MapActivity and SocialActivity
 * There should be getters and setters to allow the activities access to the data
 */
public class VibeEventListController {
    private static VibeEventListController instance;

    private VibeEventListController() { }

    public static VibeEventListController getInstance() {
        if (instance == null)
            instance = new VibeEventListController();

        return instance;
    }
}
