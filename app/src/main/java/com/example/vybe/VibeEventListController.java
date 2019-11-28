package com.example.vybe;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.vybe.Models.User;
import com.example.vybe.Models.VibeEvent;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This gets the user's vibe event list and their follower's vibe event list
 * This will be used by MyVibesActivity, MapActivity and SocialActivity
 * There should be getters and setters to allow the activities access to the data
 */
public class VibeEventListController {
    private static final String TAG = "VibeEventListController";
    private static VibeEventListController instance;
    private OnMyVibeEventsUpdatedListener myVibeEventListener;
    private OnSocialVibeEventsUpdatedListener socialVibeEventListener;

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static String savedUID;
    private String myVibeEventsPath;
    private String profilePath;

    private ArrayList<VibeEvent> myVibeEvents = new ArrayList<>();
    private ArrayList<VibeEvent> socialVibeEvents = new ArrayList<>();

    public interface OnMyVibeEventsUpdatedListener {
        void onMyVibeEventsUpdated();
    }

    public interface OnSocialVibeEventsUpdatedListener {
        void onSocialVibeEventsUpdated();
    }

    private VibeEventListController() {
    }

    private void refresh() {
        savedUID = mAuth.getCurrentUser().getUid();
        myVibeEventsPath = "Users/" + savedUID + "/VibeEvents";
        profilePath = "Users/" + savedUID;

        db.collection(myVibeEventsPath)
                .orderBy("datetime", Query.Direction.DESCENDING)
                .addSnapshotListener((queryDocumentSnapshots, firebaseFirestoreException) -> {
                    myVibeEvents.clear();

                    if (queryDocumentSnapshots == null) return;

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        VibeEvent vibeEvent = doc.toObject(VibeEvent.class);
                        myVibeEvents.add(vibeEvent);
                    }

                    notifyMyVibeEventsListener();

                    Log.d(TAG, "MyVibeEvents Updated");
                });
    }

    public void updateSocialVibeEvents() {
        db.document(profilePath)
                .get()
                .addOnSuccessListener((DocumentSnapshot documentSnapshot) -> {
                    User user = documentSnapshot.toObject(User.class);
                    ArrayList<String> myFollowing = user.getFollowing();

                    socialVibeEvents.clear();

                    if (myFollowing == null) {
                        notifySocialVibeEventsListener();
                        return;
                    }

                    ArrayList<Task<QuerySnapshot>> tasks = new ArrayList<>();
                    for (String userID : myFollowing) {
                        tasks.add(getLatestVibeEvent(userID));
                    }

                    Tasks.whenAllSuccess(tasks).addOnSuccessListener((List<Object> objects) -> {
                        Comparator<VibeEvent> comparator = (s1, s2) -> s2.getDateTime().compareTo(s1.getDateTime());
                        socialVibeEvents.sort(comparator);
                        notifySocialVibeEventsListener();
                    });
                });
    }

    private Task<QuerySnapshot> getLatestVibeEvent(String userID) {
        String userVibeEventsPath = "Users/" + userID + "/VibeEvents";

        return db.collection(userVibeEventsPath)
                .orderBy("datetime", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener((QuerySnapshot documentSnapshots) -> {
                    if (documentSnapshots.isEmpty()) return;
                    QueryDocumentSnapshot snapshot = documentSnapshots.iterator().next();
                    VibeEvent vibeEvent = snapshot.toObject(VibeEvent.class);
                    socialVibeEvents.add(vibeEvent);
                });
    }


    private void notifySocialVibeEventsListener() {
        if (socialVibeEventListener != null)
            socialVibeEventListener.onSocialVibeEventsUpdated();
    }

    private void notifyMyVibeEventsListener() {
        if (myVibeEventListener != null)
            myVibeEventListener.onMyVibeEventsUpdated();

    }

    public static VibeEventListController getInstance(Activity activity) {
        if (instance == null)
            instance = new VibeEventListController();

        if (activity instanceof OnMyVibeEventsUpdatedListener) {
            instance.myVibeEventListener = (OnMyVibeEventsUpdatedListener) activity;

        } else if (activity instanceof OnSocialVibeEventsUpdatedListener) {
            instance.socialVibeEventListener = (OnSocialVibeEventsUpdatedListener) activity;

        } else {
            throw new RuntimeException(activity.toString() + " must Listen to VibeEventListController");
        }

        String currentUID = mAuth.getCurrentUser().getUid();
        if (!currentUID.equals(savedUID))
            instance.refresh();

        return instance;
    }

    public ArrayList<VibeEvent> getMyVibeEvents() {
        return myVibeEvents;
    }

    public ArrayList<VibeEvent> getSocialVibeEvents() {
        return socialVibeEvents;
    }
}
