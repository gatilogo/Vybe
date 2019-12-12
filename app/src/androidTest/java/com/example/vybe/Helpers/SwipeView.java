package com.example.vybe.Helpers;

import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.GeneralSwipeAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Swipe;

public class SwipeView {

    public static ViewAction RightSwipe() {
        return new GeneralSwipeAction(Swipe.SLOW, GeneralLocation.CENTER_RIGHT,
                GeneralLocation.CENTER_LEFT, Press.FINGER);
    }

    public static ViewAction LeftSwipe() {
        return new GeneralSwipeAction(Swipe.SLOW, GeneralLocation.CENTER_LEFT,
                GeneralLocation.CENTER_RIGHT, Press.FINGER);
    }



}
