package com.example.vybe;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.vybe.Models.User;
import com.example.vybe.Models.VibeEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

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

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
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
        myVibeEventsPath = "Users/" + mAuth.getCurrentUser().getUid() + "/VibeEvents";
        profilePath = "Users/" + mAuth.getCurrentUser().getUid();

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

        db.document(profilePath)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        User user = documentSnapshot.toObject(User.class);
                        ArrayList<String> myFollowing = user.getFollowing();

                        socialVibeEvents.clear();

                        if (myFollowing == null)
                            return;

                        for (String userID : myFollowing) {
                            Log.d(TAG, "onEvent: " + userID);
                            String userVibeEventsPath = "Users/" + userID + "/VibeEvents";
                            db.collection(userVibeEventsPath)
                                    .orderBy("datetime", Query.Direction.DESCENDING)
                                    .limit(1)
                                    .get()
                                    .addOnSuccessListener((QuerySnapshot documentSnapshots) -> {
                                        if (documentSnapshots.isEmpty()) return;

                                        int doc = documentSnapshots.size();
                                        Log.d(TAG, "onEvent: " + doc);
                                    });
                        }

                        notifySocialVibeEventsListener();
                    }
                });


//    addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//        @Override
//        public void onSuccess (DocumentSnapshot doc){
//            User myProfile = doc.toObject(User.class);
//            ArrayList<String> myFollowing = myProfile.getFollowing();
//
//
//            if (myFollowing != null) {
//                vibeEventList.clear();
//                for (String uid : myFollowing) {
//                    collectionReference.document(uid)
//                            .collection("VibeEvents")
//                            .orderBy("datetime", Query.Direction.DESCENDING)
//                            .limit(1)
//                            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                        @Override
//                        public void onSuccess(QuerySnapshot queryDoc) {
//                            for (QueryDocumentSnapshot document : queryDoc) {
//                                vibeEventList.add(document.toObject(VibeEvent.class));
//                            }
//                            // Sort by datetime
//                            Comparator<VibeEvent> comparator = (s1, s2) -> s2.getDateTime().compareTo(s1.getDateTime());
//                            vibeEventList.sort(comparator);
//
//                            socialVibesAdapter.notifyDataSetChanged();
//                        }
//                    });
//                }
//            }
//        }
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
        return instance;
    }

    public ArrayList<VibeEvent> getMyVibeEvents() {
        return myVibeEvents;
    }

    public ArrayList<VibeEvent> getSocialVibeEvents() {
        return socialVibeEvents;
    }
}
