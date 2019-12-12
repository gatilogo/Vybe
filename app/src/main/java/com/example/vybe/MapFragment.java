package com.example.vybe;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.vybe.Models.VibeEvent;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static com.example.vybe.util.Constants.MAPVIEW_BUNDLE_KEY;
import static com.example.vybe.util.Constants.MAP_ZOOM_LEVEL;

/**
 * This fragment displays the map
 * the activity that calls it can add/remove markers
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "UserListFragment";

    //widgets
    private MapView mMapView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String currUsername = mAuth.getCurrentUser().getDisplayName();
    private GoogleMap mMap;
    private OnMapFragmentReadyListener onMapFragmentReadyListener;

    public interface OnMapFragmentReadyListener {
        void onMapFragmentReady();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnMapFragmentReadyListener) {
            onMapFragmentReadyListener = (OnMapFragmentReadyListener) context;

        } else {
            throw new RuntimeException(context.toString() + " must implement OnMapFragmentReadyListener");

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = view.findViewById(R.id.my_vibes_map);

        initGoogleMap(savedInstanceState);

        return view;
    }

    /**
     * initialized the google map to a default state
     * @param savedInstanceState
     * parameters passed into the map
     */
    private void initGoogleMap(Bundle savedInstanceState) {
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        onMapFragmentReadyListener.onMapFragmentReady();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    /**
     * sets the camera around a location
     * @param latLng
     * the coordinates the map should be centered at
     */
    public void setToLocation(LatLng latLng) {
        Log.d(TAG, "setToLocation: Here");
        clearMap();
        addMarker(latLng, R.drawable.ic_map_marker, currUsername);
        setCamera(latLng);
    }

    /**
     * sets the camera to the user's current location
     */
    public void setToCurrentLocation() {
        Location location = LocationController.getUserLocation(getContext());
        if (location == null) {
            return;
        }
        setCamera(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    /**
     * Adds a marker to the map
     * @param latLng
     * coordinates where the marker should be placed
     * @param drawableRes
     * icon for the marker
     * @param owner
     * user who created the vibe
     */
    public void addMarker(LatLng latLng, @DrawableRes int drawableRes, String owner) {
        BitmapDescriptor marker;

        if (drawableRes != R.drawable.ic_map_marker) {
            marker = emoticonVectorToBitmap(getContext(), drawableRes);
        } else {
            marker = defaultVectorToBitmap(getContext());
        }
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).icon(marker);

        if (owner != currUsername) {
            markerOptions.title(owner);
        }
        mMap.addMarker(markerOptions);
    }

    private void setCamera(LatLng latLng) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, MAP_ZOOM_LEVEL);
        mMap.moveCamera(cameraUpdate);
    }

    public void clearMap() {
        mMap.clear();
    }

    /**
     * Demonstrates converting a {@link Drawable} to a {@link BitmapDescriptor},
     * for use as a marker icon.
     * @param context the context for which the bitmap descriptor is to be drawn on
     * @param markerID the drawable vector asset to convert
     * @return BitmapDescriptor generated from provided vector asset
     */
    private BitmapDescriptor emoticonVectorToBitmap(Context context, @DrawableRes  int markerID) {

        Drawable emoticon = ContextCompat.getDrawable(context, markerID);
        emoticon.setBounds(0, 0, emoticon.getIntrinsicWidth() * 3/5, emoticon.getIntrinsicHeight() *3/5);
        Bitmap bitmap = Bitmap.createBitmap(emoticon.getIntrinsicWidth() * 3/5,
                emoticon.getIntrinsicHeight() * 3/5, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        emoticon.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    /**
     * Demonstrates converting a {@link Drawable} to a {@link BitmapDescriptor},
     * for use as a marker icon.
     * @param context the context for which the bitmap descriptor is to be drawn on
     * @return BitmapDescriptor generated from provided vector asset
     */
    private BitmapDescriptor defaultVectorToBitmap(Context context) {

        Drawable background = ContextCompat.getDrawable(context, R.drawable.ic_map_marker);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(),
                background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }



    public void hideMap() {
        getView().setVisibility(View.GONE);
    }

    public void showMap() {getView().setVisibility(View.VISIBLE);}

}
