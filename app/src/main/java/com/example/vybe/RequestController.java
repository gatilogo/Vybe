package com.example.vybe;

import android.widget.Toast;

import com.example.vybe.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RequestController {

    private static RequestController instance;
    private static final String TAG = "RequestController";
    private MyRequestsActivity myRequestsActivity;
    private ViewProfileActivity viewProfileActivity;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String myUID = mAuth.getCurrentUser().getUid();

    private ArrayList<User> myUserRequestList = new ArrayList<>();

    private RequestController() {
    }

    private void setActivity(MyRequestsActivity requestsActivity) {
        this.myRequestsActivity = requestsActivity;
    }

    private void setActivity(ViewProfileActivity viewProfileActivity) {
        this.viewProfileActivity = viewProfileActivity;
    }

    public static RequestController getInstance(MyRequestsActivity requestsActivity) {
        if (instance == null)
            instance = new RequestController();

        instance.setActivity(requestsActivity);
        instance.myUID = instance.mAuth.getCurrentUser().getUid();
        return instance;
    }

    public static RequestController getInstance(ViewProfileActivity viewProfileActivity) {
        if (instance == null)
            instance = new RequestController();

        instance.setActivity(viewProfileActivity);
        instance.myUID = instance.mAuth.getCurrentUser().getUid();
        return instance;
    }

    public ArrayList<User> getMyUserRequestList() {
        return myUserRequestList;
    }

    // send a follow request to another user
    public void sendFollowRequest(User user) {
        String otherUserID = user.getUserID();

        db.collection("Users").document(otherUserID)
                .update("requests", FieldValue.arrayUnion(myUID));

        String requestSentToast = "Request sent to " + user.getUsername() + "!";
        Toast.makeText(this.viewProfileActivity, requestSentToast, Toast.LENGTH_LONG).show();
    }

    // remove userID of selected user from follow request list
    // then add that userID to the appropriate followers/following lists
    public void acceptFollowRequest(String selectedID) {
        removeFollowRequest(selectedID);

        db.collection("Users").document(myUID)
                .update("followers", FieldValue.arrayUnion(selectedID));

        db.collection("Users").document(selectedID)
                .update("following", FieldValue.arrayUnion(myUID));
    }

    // remove userID of selected user from follow request list
    public void removeFollowRequest(String selectedID) {
        db.collection("Users").document(myUID)
                .update("requests", FieldValue.arrayRemove(selectedID));
    }


}
