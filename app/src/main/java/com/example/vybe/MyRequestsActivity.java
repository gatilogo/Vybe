package com.example.vybe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.vybe.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MyRequestsActivity extends AppCompatActivity {

    private static final String TAG = "MyRequestsActivity";

    private ProfileAdapter profileAdapter;

    private RecyclerView userRecyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private RequestController requestController = RequestController.getInstance(MyRequestsActivity.this);
    private ArrayList<User> requestList = requestController.getMyRequestList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests);

        userRecyclerView = findViewById(R.id.my_request_list);
        profileAdapter = new ProfileAdapter(R.layout.user_item, requestList);

        profileAdapter.setRequestClickListener(new ProfileAdapter.OnRequestClickListener() {
            // TODO: move deleteItem calls from profileAdapter into controller?
            @Override
            public void onAcceptClick(int position) {
                requestController.acceptFollowRequest(position);
                profileAdapter.deleteItem(position);

            }

            @Override
            public void onRejectClick(int position) {
                requestController.removeFollowRequest(position);
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
        // Want to get the most recent list of requests
        // requestController.getRequestUsernames();

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

    protected void displayMyRequests(ArrayList<String> myRequests) {
        requestList.clear();

        // requestController.queryRequests()
        for (String uid: myRequests){
            db.collection("Users").document(uid).get()
                    .addOnSuccessListener((DocumentSnapshot documentSnapshot) -> {
                        User user = documentSnapshot.toObject(User.class);
                        user.setUserID(uid);
                        requestList.add(user);
                        profileAdapter.notifyDataSetChanged();
                    });
        }
    }
}
