package com.example.vybe;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.vybe.Models.vibefactory.Vibe;
import com.example.vybe.Models.vibefactory.VibeFactory;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.tasks.OnSuccessListener;
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
    private MapFragment mapFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(map_view_fragment);
        Log.d(TAG, "onCreate: showing map");
    }

    @Override
    public void onMapFragmentReady() {
        addVibeLocations();
    }

    public void addVibeLocations() {
        // TODO: add condition for user ID
        db.collection("VibeEvent").get().addOnSuccessListener((QuerySnapshot queryDocumentSnapshots) -> {
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                if ((doc.getData().get("latitude") != null) && (doc.getData().get("latitude") != null)) {
                    double latitude = doc.getDouble("latitude");
                    double longitude = doc.getDouble("longitude");
                    String vibeName = (String) doc.getData().get("vibe");
                    Vibe vibe = VibeFactory.getVibe(vibeName);

                    BitmapDescriptor vibeBitmapDesc = vectorToBitmap(vibe.getEmoticon());
                    mapFragment.addMarker(new LatLng(latitude, longitude), vibeBitmapDesc);

                }
            }

        });

    }

    /**
     * Demonstrates converting a {@link Drawable} to a {@link BitmapDescriptor},
     * for use as a marker icon.
     */
    private BitmapDescriptor vectorToBitmap(@DrawableRes int id) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(getResources(), id, null);
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
