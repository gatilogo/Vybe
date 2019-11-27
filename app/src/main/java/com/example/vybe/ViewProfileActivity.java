package com.example.vybe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vybe.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

/**
 * This activity still does nothing but will be fixed later
 */
public class ViewProfileActivity extends AppCompatActivity {

    private TextView usernameTextView;
    private TextView emailTextView;
    private Button logoutBtn;
    private Button sendRequestBtn;

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

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras.containsKey("user")) {
            user = (User) extras.getSerializable("user");

            if (user.getEmail().equals(mAuth.getCurrentUser().getEmail())) {
                sendRequestBtn.setVisibility(View.GONE);

            } else {
                logoutBtn.setVisibility(View.GONE);
                // TODO: hide both buttons if other user is already followed pls and tnx
                // Refactor user class arraylist pls and thx ask jakey
                if (user.getFollowers() != null){
                    if (user.getFollowers().contains(mAuth.getCurrentUser().getUid())) {
                        sendRequestBtn.setVisibility(View.GONE);
                    }
                }
            }

            usernameTextView.setText(user.getUsername());
            emailTextView.setText(user.getEmail());
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

    }
}
