package com.example.vybe;

import com.example.vybe.Models.User;
import com.example.vybe.Models.VibeEvent;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static String savedUid;

    private OnMyVibeEventsUpdatedListener myVibeEventsListener;
    private ArrayList<VibeEvent> myVibeEvents = new ArrayList<>();
    private OnSocialVibeEventsUpdatedListener socialVibeEventsListener;
    private ArrayList<VibeEvent> socialVibeEvents = new ArrayList<>();

    public interface OnMyVibeEventsUpdatedListener {
        void onMyVibeEventsUpdated();
    }

    public interface OnSocialVibeEventsUpdatedListener {
        void onSocialVibeEventsUpdated();
    }

    private VibeEventListController() {
    }

    // This should only be call once after the user is logged in
    private void refreshMyVibeEventsQuery() {
        String myVibeEventsPath = "Users/" + savedUid + "/VibeEvents";
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

                });
    }

    // This is meant to be called multiple times (ie not just once like refreshMyVibeEventsQuery is)
    public void onUpdateSocialVibeEvents() {
        String profilePath = "Users/" + savedUid;
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
                        // We cannot put all the queries in this for-loop because they are
                        // asynchronous - there is no guarantee that the code after this for-loop
                        // would execute after the queries are all done. So we need to wrap them like this.
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
        if (socialVibeEventsListener != null)
            socialVibeEventsListener.onSocialVibeEventsUpdated();
    }

    private void notifyMyVibeEventsListener() {
        if (myVibeEventsListener != null)
            myVibeEventsListener.onMyVibeEventsUpdated();

    }

    private static void refreshInstance() {
        if (instance == null)
            instance = new VibeEventListController();

        // If a new user logged in, update the saved user id and the myVibes query
        String Uid = mAuth.getCurrentUser().getUid();
        if (!Uid.equals(savedUid)) {
            savedUid = mAuth.getCurrentUser().getUid();
            instance.refreshMyVibeEventsQuery();
        }
    }

    public static ArrayList<VibeEvent> setOnMyVibeEventsUpdatedListener(OnMyVibeEventsUpdatedListener listener) {
        refreshInstance();
        instance.myVibeEventsListener = listener;

        return instance.myVibeEvents;
    }

    public static ArrayList<VibeEvent> setOnSocialVibeEventsUpdatedListener(OnSocialVibeEventsUpdatedListener listener) {
        refreshInstance();
        instance.socialVibeEventsListener = listener;

        return instance.socialVibeEvents;
    }

    /**
     * This method updates the social vibe events that are displayed on the view
     */
    public static void updateSocialVibeEvents() {
        instance.onUpdateSocialVibeEvents();
    }

    public ArrayList<VibeEvent> getMyVibeEvents() {
        return myVibeEvents;
    }

    public ArrayList<VibeEvent> getSocialVibeEvents() {
        return socialVibeEvents;
    }
}
