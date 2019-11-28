package com.example.vybe;

public class ProfileController {
    private static ProfileController instance;

    private ProfileController() { }

    public static ProfileController getInstance() {
        if (instance == null)
            instance = new ProfileController();

        return instance;
    }
}
