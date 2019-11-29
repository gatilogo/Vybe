package com.example.vybe.Helpers;

import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import org.hamcrest.Matcher;

import static androidx.test.espresso.action.ViewActions.click;

public class ClickOnInternalView implements ViewAction {

    ViewAction click = click();
    int viewID;

    public ClickOnInternalView(int textViewId) {
        this.viewID = textViewId;
    }

    @Override
    public Matcher<View> getConstraints() {
        return click.getConstraints();

    }

    @Override
    public String getDescription() {
        return " click on button with id: " + viewID;
    }

    @Override
    public void perform(UiController uiController, View view) {
        click.perform(uiController, view.findViewById(viewID));
    }
}
