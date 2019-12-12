package com.example.vybe;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;


import com.example.vybe.Models.Vibe;
import com.example.vybe.Models.User;
import com.example.vybe.Models.VibeEvent;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;

import static com.example.vybe.R.id.map_view_fragment;

/**
 * Placeholder activity for where the map fragment is displayed.
 * In the future, this activity will allow for switching between
 * a user's personal vibe event history or their social vibe history
 */
public class MapViewActivity extends AppCompatActivity implements MapFragment.OnMapFragmentReadyListener {

    private static final String TAG = "MapViewActivity";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String currUsername = mAuth.getCurrentUser().getDisplayName();
    private String vibeEventDBPath;
    private MapFragment mapFragment;
    private FloatingActionButton mapToggleButton;
    //if this is true, the map mode is viewing personal vibes
    private boolean viewMyVibes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        vibeEventDBPath = "Users/" + mAuth.getCurrentUser().getUid() + "/VibeEvents";

        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(map_view_fragment);
        mapToggleButton = findViewById(R.id.btn_map_toggle);

        //see where user came from
        Bundle extras = getIntent().getExtras();
        if (extras.getSerializable("MapViewMode").equals("Social")) {
            viewMyVibes = false;
        } else {
            viewMyVibes = true;
        }

        //changes view mode
        mapToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapFragment.clearMap();
                if (viewMyVibes) {
                    addFollowedVibeLocations();
                    mapToggleButton.setImageResource(R.drawable.button_toggle_personal);
                } else {
                    addMyVibeLocations();
                    mapToggleButton.setImageResource(R.drawable.button_group);
                }

            }
        });
    }

    /**
     * Fills map markers once map is ready - based on map mode
     */
    @Override
    public void onMapFragmentReady() {
        mapFragment.setToCurrentLocation();
        if (viewMyVibes) {
            addMyVibeLocations();
        } else {
            addFollowedVibeLocations();
        }
    }

    /**
     * Adds personal vibe markers to the map
     */
    public void addMyVibeLocations() {
        viewMyVibes = true;
        // TODO: add condition for user ID
        mapFragment.clearMap();
        db.collection(vibeEventDBPath).get().addOnSuccessListener((QuerySnapshot queryDocumentSnapshots) -> {
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                Double latitude = doc.getDouble("latitude");
                Double longitude = doc.getDouble("longitude");
                if ((latitude != null) && (longitude != null)) {
                    String vibeName = (String) doc.getData().get("vibe");
                    Vibe vibe = Vibe.ofName(vibeName);

                    mapFragment.addMarker(new LatLng(latitude, longitude), vibe.getEmoticon(), currUsername);

                }
            }

        });

    }

    /**
     * Adds most recent vibe marker from each person you follow to the map
     */
    public void addFollowedVibeLocations() {
        viewMyVibes = false;
        mapFragment.clearMap();

        CollectionReference collectionReference = db.collection("Users");

        collectionReference.document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot doc) {
                User myProfile = doc.toObject(User.class);
                ArrayList<String> myFollowing = myProfile.getFollowing();
                if (myFollowing != null){
                    for (String uid: myFollowing){
                        collectionReference.document(uid)
                                .collection("VibeEvents")
                                .orderBy("datetime", Query.Direction.DESCENDING)
                                .limit(1)
                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDoc) {
                                for (QueryDocumentSnapshot document: queryDoc){
                                    Double latitude = document.getDouble("latitude");
                                    Double longitude = document.getDouble("longitude");
                                    if ((latitude != null) && (longitude != null)) {
                                        String vibeName = (String) document.getData().get("vibe");
                                        Vibe vibe = Vibe.ofName(vibeName);
                                        String vibeOwner = (String) document.getData().get("owner");

                                        mapFragment.addMarker(new LatLng(latitude, longitude), vibe.getEmoticon(), vibeOwner);

                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}
