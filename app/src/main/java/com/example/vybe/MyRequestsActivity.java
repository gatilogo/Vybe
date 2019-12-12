package com.example.vybe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.vybe.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * This activity allows the user to send a follow request to another user.
 */
public class MyRequestsActivity extends AppCompatActivity {

    private static final String TAG = "MyRequestsActivity";

    private ProfileAdapter profileAdapter;

    private RecyclerView userRecyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private RequestController requestController = RequestController.getInstance(MyRequestsActivity.this);
    private ArrayList<User> userRequestList = requestController.getMyUserRequestList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests);

        userRecyclerView = findViewById(R.id.my_request_list);
        profileAdapter = new ProfileAdapter(R.layout.user_item, userRequestList);

        profileAdapter.setRequestClickListener(new ProfileAdapter.OnRequestClickListener() {

            @Override
            public void onAcceptClick(int position) {
                User selectedUser = userRequestList.get(position);
                String selectedID = selectedUser.getUserID();
                requestController.acceptFollowRequest(selectedID);
                profileAdapter.deleteItem(position);

            }

            @Override
            public void onRejectClick(int position) {
                User selectedUser = userRequestList.get(position);
                String selectedID = selectedUser.getUserID();
                requestController.removeFollowRequest(selectedID);
                profileAdapter.deleteItem(position);

            }
        });

        userRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        userRecyclerView.setAdapter(profileAdapter);

        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        userRecyclerView.addItemDecoration(itemDecor);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // get most recent list of requests
        CollectionReference collectionReference = db.collection("Users");

        collectionReference.document(mAuth.getCurrentUser().getUid()).get()
                .addOnSuccessListener((DocumentSnapshot doc) -> {
                    User myProfile = doc.toObject(User.class);
                    ArrayList<String> myRequests = myProfile.getRequests();

                    if (myRequests != null) {
                        displayMyRequests(myRequests);
                    }
                });
    }

    /**
     * This method queries for a user's follow requests
     * @param myRequests list of User IDs who have requested to follow current user
     */
    protected void displayMyRequests(ArrayList<String> myRequests) {
        userRequestList.clear();
        for (String uid: myRequests){
            db.collection("Users").document(uid).get()
                    .addOnSuccessListener((DocumentSnapshot documentSnapshot) -> {
                        User user = documentSnapshot.toObject(User.class);
                        user.setUserID(uid);
                        userRequestList.add(user);
                        profileAdapter.notifyDataSetChanged();
                    });
        }
    }
}
