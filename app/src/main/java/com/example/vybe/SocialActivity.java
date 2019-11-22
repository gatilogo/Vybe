package com.example.vybe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.vybe.Models.User;
import com.example.vybe.Models.VibeEvent;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * SocialActivity displays the screen for a user to view a list of their followers' vibe events
 */
public class SocialActivity extends AppCompatActivity {

    private static final String TAG = "SocialActivity";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private ArrayList<VibeEvent> vibeEventList;
    private MyVibesAdapter socialVibesAdapter;
//    private String socialVibeEventDBPath;
    private Button myVibesBtn;
    private Button searchBtn;
    private RecyclerView socialVibesRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);
        Log.d(TAG, "onCreate: In social");

        myVibesBtn = findViewById(R.id.my_vibes_btn);
        searchBtn = findViewById(R.id.search_btn);
        socialVibesRecyclerView = findViewById(R.id.social_vibe_list);

        buildRecyclerView();

        myVibesBtn.setOnClickListener((View v) -> {
            finish();
        });

        searchBtn.setOnClickListener((View v) -> {
            startActivity(new Intent(SocialActivity.this, SearchProfilesActivity.class));
        });

        CollectionReference collectionReference = db.collection("Users");

        collectionReference.document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot doc) {
                User myProfile = doc.toObject(User.class);
                ArrayList<String> myFollowing = myProfile.getFollowing();
                if (myFollowing != null){
                    vibeEventList.clear();
                    for (String uid: myFollowing){
                        collectionReference.document(uid)
                                .collection("VibeEvents")
                                .orderBy("datetime", Query.Direction.DESCENDING)
                                .limit(1)
                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDoc) {
                                for (QueryDocumentSnapshot document: queryDoc){
                                    vibeEventList.add(document.toObject(VibeEvent.class));
                                }
                                // TODO: Sort by datetime
                                socialVibesAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            }
        });
    }

    //TODO: Add onStart() method to populate social vibes list

    private void buildRecyclerView() {
        vibeEventList = new ArrayList<>();

        socialVibesAdapter = new MyVibesAdapter(this, R.layout.my_vibe_item, vibeEventList);
        socialVibesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        socialVibesRecyclerView.setAdapter(socialVibesAdapter);

        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        socialVibesRecyclerView.addItemDecoration(itemDecor);
    }

    @Override
    protected void onStart() {
        super.onStart();


    }
}
