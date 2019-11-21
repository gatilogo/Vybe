package com.example.vybe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.vybe.AddEdit.AddEditVibeEventActivity;
import com.example.vybe.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MyRequestsActivity extends AppCompatActivity implements ProfileAdapter.OnItemClickListener {

    private static final String TAG = "MyRequestsActivity";

    ArrayList<User> requestList;
    ProfileAdapter profileAdapter;

    private RecyclerView userRecyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String userRequestDBPath;
    private Button addVibeEventBtn;
    private Button myVibesBtn;

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Can't view profile from here I guess", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests);
        Intent intent = getIntent();

        userRecyclerView = findViewById(R.id.my_request_list);
        addVibeEventBtn = findViewById(R.id.add_vibe_event_btn);
        myVibesBtn = findViewById(R.id.my_vibes_btn);

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

        profileAdapter = new ProfileAdapter(this, R.layout.user_item, requestList);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        userRecyclerView.setAdapter(profileAdapter);

        myVibesBtn.setOnClickListener((View v) -> {
            finish();
        });

        addVibeEventBtn.setOnClickListener((View view) -> {
            Intent addIntent = new Intent(MyRequestsActivity.this, AddEditVibeEventActivity.class);
            startActivity(addIntent);
        });

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
                User user = new User();
                String email = (String) doc.get("email");
                String userID = (String) doc.get("userID");
                String username = (String) doc.get("username");
                user.setEmail(email);
                user.setUserID(userID);
                user.setUsername(username);
                requestList.add(user);
            }
            profileAdapter.notifyDataSetChanged();
        });
    }
}
