package com.example.vybe;

import android.icu.text.SimpleDateFormat;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.Date;

/**
 * This tests for a user being able to send a follow request to another user and accepting a
 * request
 * Tests for requirements of a users personal vibe history, Vibe Events will be here,
 * as well as Other Vibe Details
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class SocialActivityTest {

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    private String espressoEmail = "espresso@test.ca";
    private String espressoUser = "espresso";
    private String espressoUID = "5fDHBHFmWqb6hCkb3DkwYUlS2ys1";

    private String decafEmail = "decaf@test.ca";
    private String decafUser = "decaf";
    private String decafUID = "ka1gQkqFcvYjmFl8FAKNV4Ewvx13";

    private String mochaUser = "mocha";

    private String LoginPassword = "vibecheck";

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void initialize_db(){
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

    }

    @AfterClass
    public void DeleteFollower(){
        db.collection("Users").document(espressoUID)
                .update("followers", FieldValue.arrayRemove(decafUID));
        db.collection("Users").document(decafUID)
                .update("following", FieldValue.arrayRemove(espressoUID));

    }
}
