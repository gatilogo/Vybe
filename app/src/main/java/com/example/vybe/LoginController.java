package com.example.vybe;

/**
 * The Purpose of this controller is to take all the database shit from:
 * Main Activity and Create Account Activity
 * put it in here
 */
public class LoginController {
    private static LoginController instance;

    private LoginController() { }

    public static LoginController getInstance() {
        if (instance == null)
            instance = new LoginController();

        return instance;
    }
}
