package com.example.vybe;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        CreateAccountActivity.class,
        LoginActivityTest.class,
        ViewProfileActivity.class,
        MyVibesActivity.class,
        SocialActivityTest.class,
        MapFragmentTest.class
})

public class TestIntentSuite {
}
