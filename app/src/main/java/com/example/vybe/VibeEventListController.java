package com.example.vybe;

import android.util.Log;

import com.example.vybe.Models.VibeEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * This gets the user's vibe event list and their follower's vibe event list
 * This will be used by MyVibesActivity, MapActivity and SocialActivity
 * There should be getters and setters to allow the activities access to the data
 */
public class VibeEventListController {
    private static final String TAG = "VibeEventListController";
    private ArrayList<OnMyVibeEventsUpdatedListener> myVibeEventListeners = new ArrayList<>();
    private static VibeEventListController instance;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String vibeEventDBPath = "Users/" + mAuth.getCurrentUser().getUid() + "/VibeEvents";
    private ArrayList<VibeEvent> myVibeEvents = new ArrayList<>();

    public interface OnMyVibeEventsUpdatedListener {
        void onMyVibeEventsUpdated();
    }

    private VibeEventListController() {
        db.collection(vibeEventDBPath)
                .orderBy("datetime", Query.Direction.DESCENDING)
                .addSnapshotListener((queryDocumentSnapshots, firebaseFirestoreException) -> {
                    myVibeEvents.clear();

                    if (queryDocumentSnapshots == null) return;

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        VibeEvent vibeEvent = doc.toObject(VibeEvent.class);
                        myVibeEvents.add(vibeEvent);
                    }

                    notifyMyVibeEventsListeners();

                    Log.d(TAG, "MyVibeEvents Updated");
                });
    }

    private void notifyMyVibeEventsListeners() {
        for (OnMyVibeEventsUpdatedListener listener : myVibeEventListeners) {
            listener.onMyVibeEventsUpdated();
        }
    }

    public static VibeEventListController getInstance() {
        if (instance == null)
            instance = new VibeEventListController();

        return instance;
    }

    public void listenForMyVibeEvents(OnMyVibeEventsUpdatedListener listener) {
        myVibeEventListeners.add(listener);
    }

    public ArrayList<VibeEvent> getMyVibeEvents() {
        return myVibeEvents;
    }
}
