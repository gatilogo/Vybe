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

    private ArrayList<User> myRequestList = new ArrayList<>();

    private RequestController() { }

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
        return instance;
    }

    public static RequestController getInstance(ViewProfileActivity viewProfileActivity) {
        if (instance == null)
            instance = new RequestController();

        instance.setActivity(viewProfileActivity);
        return instance;
    }

    public ArrayList<User> getMyRequestList() {
        return myRequestList;
    }

    public void sendFollowRequest(User user) {
        String otherUserID = user.getUserID();

        db.collection("Users").document(otherUserID)
                .update("requests", FieldValue.arrayUnion(myUID));

        String requestSentToast = "Request sent to " + user.getUsername() + "!";
        Toast.makeText(this.viewProfileActivity, requestSentToast, Toast.LENGTH_LONG).show();
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
