package com.example.vybe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vybe.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.Distribution;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This activity still does nothing but will be fixed later
 */
public class ViewProfileActivity extends AppCompatActivity {

    private TextView usernameTextView;
    private TextView emailTextView;
    private Button logoutBtn;
    private Button sendRequestBtn;
    private LinearLayout vibesLayout;
    private LinearLayout followersLayout;
    private LinearLayout followingLayout;
    private LinearLayout statisticsLayout;
    private TextView vibeCount;
    private TextView followerCount;
    private TextView followingCount;

    private User user;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        usernameTextView = findViewById(R.id.username_profile);
        emailTextView = findViewById(R.id.email_profile);
        logoutBtn = findViewById(R.id.logout_btn);
        sendRequestBtn = findViewById(R.id.send_request_btn);
        vibesLayout = findViewById(R.id.profile_vibes_layout);
        followersLayout = findViewById(R.id.profile_followers_layout);
        followingLayout = findViewById(R.id.profile_following_layout);
        vibeCount = findViewById(R.id.vibe_count);
        followerCount = findViewById(R.id.follower_count);
        followingCount = findViewById(R.id.following_count);
        statisticsLayout = findViewById(R.id.statistics_layout);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras.containsKey("user")) {
            user = (User) extras.getSerializable("user");

            if (user.getEmail().equals(mAuth.getCurrentUser().getEmail())) {
                sendRequestBtn.setVisibility(View.GONE);
                statisticsLayout.setVisibility(View.VISIBLE);
            } else {
                logoutBtn.setVisibility(View.GONE);

                if (user.getFollowers() != null){
                    if (user.getFollowers().contains(mAuth.getCurrentUser().getUid())) {
                        sendRequestBtn.setVisibility(View.GONE);
                        statisticsLayout.setVisibility(View.VISIBLE);
                    }
                }
            }

            int followerSize = 0;
            int followingSize = 0;

            if (user.getFollowers() != null) {
                followerSize = user.getFollowers().size();
            }

            if (user.getFollowing() != null) {
                followingSize = user.getFollowing().size();
            }

            db.collection("Users/" + user.getUserID() + "/VibeEvents")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            vibeCount.setText(Integer.toString(queryDocumentSnapshots.size()));
                        }
                    });

            usernameTextView.setText(user.getUsername());
            emailTextView.setText(user.getEmail());
//            vibeCount.setText()
            followerCount.setText(Integer.toString(followerSize));
            followingCount.setText(Integer.toString(followingSize));
        }

        logoutBtn.setOnClickListener(view -> {
            mAuth.signOut();
            Intent restart = new Intent(this, MainActivity.class);
            // Finishes all background activities that may be running
            restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(restart);
        });

        sendRequestBtn.setOnClickListener(view -> {
            String otherUserID = user.getUserID();
            FirebaseUser selfFB = mAuth.getCurrentUser();

            db.collection("Users").document(otherUserID)
                    .update("requests", FieldValue.arrayUnion(selfFB.getUid()));

            // TODO: set the correct username/display name from/for mAuth.getCurrentUser()
            Toast.makeText(this,"Request sent!", Toast.LENGTH_LONG).show();
        });

        followersLayout.setOnClickListener(view -> {
            Intent followersIntent = new Intent(this, ConnectionsActivity.class);
            followersIntent.putExtra("user", user);
            startActivity(followersIntent);
        });

        followingLayout.setOnClickListener(view -> {
            Intent followingIntent = new Intent(this, ConnectionsActivity.class);
            followingIntent.putExtra("user", user);
            startActivity(followingIntent);
        });
    }
}
