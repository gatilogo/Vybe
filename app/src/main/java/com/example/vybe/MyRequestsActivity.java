package com.example.vybe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.vybe.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MyRequestsActivity extends AppCompatActivity {

    private static final String TAG = "MyRequestsActivity";

    private ArrayList<User> requestList;
    private ProfileAdapter profileAdapter;

    private RecyclerView userRecyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String userRequestDBPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests);

        userRecyclerView = findViewById(R.id.my_request_list);

        userRequestDBPath = "Users/" + mAuth.getCurrentUser().getUid() + "/Requests";

        requestList = new ArrayList<>();

        // Get users from the database
        final CollectionReference collectionReference = db.collection("Users");
        Query query = collectionReference.orderBy("username", Query.Direction.DESCENDING);
        query.addSnapshotListener((@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) -> {
            requestList.clear();
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                String email = (String) doc.get("email");
                String userID = (String) doc.get("userID");
                String username = (String) doc.get("username");

                User user = new User(username, email);
                user.setUserID(userID);
                user.setUsername(username);
                requestList.add(user);
            }
        });

        profileAdapter = new ProfileAdapter(R.layout.user_item, requestList);
        profileAdapter.setRequestClickListener(new ProfileAdapter.OnRequestClickListener() {
            @Override
            public void onAcceptClick(int position) {
                acceptFollowRequest(position);
            }

            @Override
            public void onRejectClick(int position) {
                removeFollowRequest(position);
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
        // Want to get the most recent list of requests
        CollectionReference collectionReference = db.collection(userRequestDBPath);
        Query query = collectionReference.orderBy("username", Query.Direction.DESCENDING);

        query.get().addOnSuccessListener((QuerySnapshot queryDocumentSnapshots) -> {
            requestList.clear();
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                User user = doc.toObject(User.class);
                requestList.add(user);
            }
            profileAdapter.notifyDataSetChanged();
        });
    }

    //TODO: stub out request functionality into its own, singleton class maybe?
    public void acceptFollowRequest(int position) {
        User user = requestList.get(position);
        removeFollowRequest(position);
        String myID = mAuth.getCurrentUser().getUid();
        String theirID = user.getUserID();
        db.collection("Users").document(myID)
                .update("followers", FieldValue.arrayUnion(theirID));

        db.collection("Users").document(theirID)
                .update("following", FieldValue.arrayUnion(myID));
    }

    public void removeFollowRequest(int position) {
        User user = requestList.get(position);
        String requestDBPath = "Users/" + mAuth.getCurrentUser().getUid() + "/Requests";
        db.collection(requestDBPath).document(user.getUserID()).delete();
        profileAdapter.deleteItem(position);
    }
}
