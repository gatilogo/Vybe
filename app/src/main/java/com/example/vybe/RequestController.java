package com.example.vybe;

import com.example.vybe.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RequestController {

    private static RequestController instance;
    private static final String TAG = "RequestController";
    private MyRequestsActivity activity;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String myUID = mAuth.getCurrentUser().getUid();

    private ArrayList<User> myRequestList = new ArrayList<>();

    private RequestController() { }

    private void setActivity(MyRequestsActivity requestsActivity) {
        this.activity = requestsActivity;
    }

    public static RequestController getInstance(MyRequestsActivity requestsActivity) {
        if (instance == null)
            instance = new RequestController();

        instance.setActivity(requestsActivity);
        return instance;
    }

    public ArrayList<User> getMyRequestList() {
        return myRequestList;
    }

    public void acceptFollowRequest(int position) {
        User user = myRequestList.get(position);
        removeFollowRequest(position);
        String theirID = user.getUserID();
        db.collection("Users").document(myUID)
                .update("followers", FieldValue.arrayUnion(theirID));

        db.collection("Users").document(theirID)
                .update("following", FieldValue.arrayUnion(myUID));
    }

    public void removeFollowRequest(int position) {
        User user = myRequestList.get(position);
        String theirID = user.getUserID();
        db.collection("Users").document(myUID)
                .update("requests", FieldValue.arrayRemove(theirID));
    }
}
