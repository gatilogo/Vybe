package com.example.vybe;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vybe.AddEdit.AddEditVibeEventActivity;
import com.example.vybe.Models.User;
import com.example.vybe.Models.Vibe;
import com.example.vybe.Models.VibeEvent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import static com.example.vybe.util.Constants.ERROR_DIALOG_REQUEST;
import static com.example.vybe.util.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.example.vybe.util.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;


/**
 * This Activity displays the screen for a user to view a list of their
 * vibe event history, sorted by date and time in reverse chronological
 * order
 */

public class MyVibesActivity extends AppCompatActivity implements VibeFilterFragment.OnVibeSelectedListener, VibeEventListController.OnMyVibeEventsUpdatedListener {

    private static final String TAG = "MyVibesActivity";
    private boolean mLocationPermissionGranted = false;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String vibeEventDBPath = "Users/" + mAuth.getCurrentUser().getUid() + "/VibeEvents";
    private ArrayList<VibeEvent> myVibeEvents = VibeEventListController.setOnMyVibeEventsUpdatedListener(this);

    private ArrayList<VibeEvent> shownVibeEvents = new ArrayList<>();
    private Vibe filterVibe = Vibe.NONE;
    private MyVibesAdapter myVibesAdapter;

    // ------------ XML ------------
    private ImageButton profileBtn;
    private FloatingActionButton addVibeEventBtn;
    private ImageButton socialBtn;
    private ImageButton myMapBtn;
    private ImageButton requestBtn;
    private RecyclerView vibesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vibes);

        profileBtn = findViewById(R.id.profile_btn);
        addVibeEventBtn = findViewById(R.id.add_vibe_event_btn);
        socialBtn = findViewById(R.id.social_btn);
        myMapBtn = findViewById(R.id.my_map_btn);
        requestBtn = findViewById(R.id.request_btn);
        vibesRecyclerView = findViewById(R.id.my_vibe_list);

        buildRecyclerView();

        profileBtn.setOnClickListener((View v) -> {
                Intent intent = new Intent(MyVibesActivity.this, ViewProfileActivity.class);
                // Get the current user's profile information
                db.collection("Users").document(mAuth.getCurrentUser().getUid()).get()
                        .addOnSuccessListener((DocumentSnapshot doc) -> {
                            User user = doc.toObject(User.class);
                            user.setUserID(doc.getId());
                            intent.putExtra("user", user);
                            startActivity(intent);
                        });
        });

        addVibeEventBtn.setOnClickListener(view -> {
            Intent addIntent = new Intent(MyVibesActivity.this, AddEditVibeEventActivity.class);
            startActivity(addIntent);
        });

        myMapBtn.setOnClickListener(view -> {
            if (mLocationPermissionGranted) {
                Intent MapViewIntent = new Intent(MyVibesActivity.this, MapViewActivity.class);
                MapViewIntent.putExtra("MapViewMode", "Personal");
                startActivity(MapViewIntent);
            } else {
                Toast.makeText(MyVibesActivity.this, "Please enable GPS services", Toast.LENGTH_SHORT);
            }
        });

        socialBtn.setOnClickListener(view -> {
            startActivity(new Intent(MyVibesActivity.this, SocialActivity.class));

        });

        requestBtn.setOnClickListener(view -> {
            startActivity(new Intent(MyVibesActivity.this, MyRequestsActivity.class));
        });
    }

    @Override
    public void onVibeSelected(Vibe vibe) {
        filterVibe = vibe;
        updateShownVibes();
    }

    @Override
    public void onMyVibeEventsUpdated() {
        updateShownVibes();
    }

    private void updateShownVibes() {
        shownVibeEvents.clear();
        for (VibeEvent event : myVibeEvents) {
            if (filterVibe == Vibe.NONE || event.getVibe() == filterVibe) {
                shownVibeEvents.add(event);
            }
        }

        myVibesAdapter.notifyDataSetChanged();
    }


    private void buildRecyclerView() {
        myVibesAdapter = new MyVibesAdapter(this, R.layout.vibe_event_item, shownVibeEvents);
        vibesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        vibesRecyclerView.setAdapter(myVibesAdapter);

        SwipeItemTouchHelper itemTouchHelperCallback = new SwipeItemTouchHelper(
                this,
                0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,
                myVibesAdapter,
                vibeEventDBPath);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(vibesRecyclerView);
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        vibesRecyclerView.addItemDecoration(itemDecor);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Reset swipe animation after pressing back button
        updateShownVibes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // this is just the easiest way to consistently make sure the user has gps enabled
        if (GoogleServicesWorks() && GpsIsEnabled() && !mLocationPermissionGranted) {
            getLocationPermission();
        }
    }

    public boolean GoogleServicesWorks() {
        // Makes sure that google api services are installed and enabled
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MyVibesActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MyVibesActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        return false;
    }

    public boolean GpsIsEnabled() {
        // Makes sure user has gps services enabled
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            NoGpsMessage();
            return false;
        }
        return true;
    }

    private void NoGpsMessage() {
        // alert for if user does not have gps enabled
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("We need you to vybe with us using your GPS services, please enable it for us")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent enableGpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    protected void OnActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                if (mLocationPermissionGranted) {
                    // Everything is fine
                } else {
                    getLocationPermission();
                }
            }
        }
    }

    private void getLocationPermission() {
        // Asks user for permission
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        // Method ran after user closes the get permission window
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

}
