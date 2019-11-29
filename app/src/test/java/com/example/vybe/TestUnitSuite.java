package com.example.vybe;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        VibeTest.class,
        UserTest.class,
        VibeEventTest.class
})

public class TestUnitSuite {
}
