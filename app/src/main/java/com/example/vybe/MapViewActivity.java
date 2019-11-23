package com.example.vybe;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.vybe.Models.Vibe;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    private String vibeEventDBPath;
    private MapFragment mapFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        vibeEventDBPath = "Users/" + mAuth.getCurrentUser().getUid() + "/VibeEvents";
        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(map_view_fragment);
    }

    @Override
    public void onMapFragmentReady() {
        addVibeLocations();
    }

    public void addVibeLocations() {
        // TODO: add condition for user ID
        db.collection(vibeEventDBPath).get().addOnSuccessListener((QuerySnapshot queryDocumentSnapshots) -> {
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                if ((doc.getData().get("latitude") != null) && (doc.getData().get("latitude") != null)) {
                    double latitude = doc.getDouble("latitude");
                    double longitude = doc.getDouble("longitude");
                    String vibeName = (String) doc.getData().get("vibe");
                    Vibe vibe = Vibe.ofName(vibeName);

                    mapFragment.addMarker(new LatLng(latitude, longitude), vibe.getEmoticon());

                }
            }

        });

    }
}
