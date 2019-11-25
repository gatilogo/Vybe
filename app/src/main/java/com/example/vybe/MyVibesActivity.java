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
import android.os.PersistableBundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vybe.AddEdit.AddEditVibeEventActivity;
import com.example.vybe.Models.SocSit;
import com.example.vybe.Models.User;
import com.example.vybe.Models.VibeEvent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.example.vybe.util.Constants.ERROR_DIALOG_REQUEST;
import static com.example.vybe.util.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.example.vybe.util.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;


/**
 * This Activity displays the screen for a user to view a list of their
 * vibe event history, sorted by date and time in reverse chronological
 * order
 */

public class MyVibesActivity extends AppCompatActivity {

    private static final String TAG = "MyVibesActivity";
    private boolean mLocationPermissionGranted = false;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private ArrayList<VibeEvent> vibeEventList;
    private MyVibesAdapter myVibesAdapter;
    private String vibeEventDBPath;
    private boolean allFlag;
    private String filterVibe = "Filter Vibe";

    private Spinner filterSpinner;
    private RecyclerView vibesRecyclerView;
    private Button addVibeEventBtn;
    private Button myMapBtn;
    private Button socialBtn;
    private Button requestBtn;
    private ImageButton profileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vibes);
        Log.d(TAG, "onCreate: In my vibes");

        filterSpinner = findViewById(R.id.filter_spinner);
        vibesRecyclerView = findViewById(R.id.my_vibe_list);
        addVibeEventBtn = findViewById(R.id.add_vibe_event_btn);
        myMapBtn = findViewById(R.id.my_map_btn);
        socialBtn = findViewById(R.id.social_btn);
        profileBtn = findViewById(R.id.profile_btn);
        requestBtn = findViewById(R.id.request_btn);

        vibeEventDBPath = "Users/" + mAuth.getCurrentUser().getUid() + "/VibeEvents";
        allFlag = true; // Ask jakey

        buildRecyclerView();

        profileBtn.setOnClickListener((View v) -> {
                Intent intent = new Intent(MyVibesActivity.this, ViewProfileActivity.class);
                // Get the current user's profile information
                db.collection("Users").document(mAuth.getCurrentUser().getUid()).get()
                        .addOnSuccessListener((DocumentSnapshot doc) -> {
                            String username = (String) doc.get("username");
                            String email = (String) doc.get("email");
                            intent.putExtra("user", new User(username, email));
                            startActivity(intent);
                        });
        });

        // --- Vibes Dropdown ---
        // TODO: Refactor with custom spinner and/or different filtering methodology
        String[] vibes = new String[]{"Filter Vibe", "Angry", "Disgusted", "Happy", "Sad", "Scared", "Surprised"};
        ArrayAdapter<String> vibesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, vibes);
        filterSpinner.setAdapter(vibesAdapter);
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                filterVibe = vibes[position];
                allFlag = true;
                if (position != 0){ allFlag = false;}
                getVibeHistory(filterVibe);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        addVibeEventBtn.setOnClickListener((View view) -> {
            Intent addIntent = new Intent(MyVibesActivity.this, AddEditVibeEventActivity.class);
            startActivity(addIntent);
        });

        myMapBtn.setOnClickListener((View view) -> {
            if (mLocationPermissionGranted) {
                Intent MapViewIntent = new Intent(MyVibesActivity.this, MapViewActivity.class);
                MapViewIntent.putExtra("MapViewMode", "Personal");
                startActivity(MapViewIntent);
            } else {
                Toast.makeText(MyVibesActivity.this, "Please enable GPS services", Toast.LENGTH_SHORT);
            }
        });

        socialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyVibesActivity.this, SocialActivity.class));
            }
        });

        requestBtn.setOnClickListener(view -> {
            startActivity(new Intent(MyVibesActivity.this, MyRequestsActivity.class));
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Should get Vibe History with the previous state if we click the back button
        getVibeHistory(filterVibe);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //this is just the easiest way to consistently make sure the user has gps enabled
        if (checkMapServices()) {
            if (!mLocationPermissionGranted){
                getLocationPermission();
            }
        }
    }

    private boolean checkMapServices() {
        if (GoogleServicesWorks()){
            if(isMapsEnabled()){
                return true;
            }
        }
        return false;
    }

    public boolean GoogleServicesWorks(){
        //Makes sure that google api services are installed and enabled
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MyVibesActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MyVibesActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        return false;
    }

    public boolean isMapsEnabled() {
        //Makes sure user has gps services enabled
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            NoGpsMessage();
            return false;
        }
        return true;
    }

    private void NoGpsMessage() {
        //alert for if user does not have gps enabled
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


    protected void OnActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                if (mLocationPermissionGranted){
                    //everything is fine
                }
                else {
                    getLocationPermission();
                }
            }
        }
    }

    private void getLocationPermission() {
        //asks user for permission
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            //do stuff
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        //method ran after user closes the get permission window
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

    private void buildRecyclerView() {
        vibeEventList = new ArrayList<>();

        myVibesAdapter = new MyVibesAdapter(this, R.layout.my_vibe_item, vibeEventList);
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

    public void getVibeHistory(String vibeFilter){
        db.collection(vibeEventDBPath)
                .orderBy("datetime", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener((QuerySnapshot queryDocumentSnapshots) -> {
                    // TODO: Stub out with other query below
                    vibeEventList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots){

                        VibeEvent vibeEvent = new VibeEvent();
                        vibeEvent.setDateTime(doc.getDate("datetime"));
                        vibeEvent.setReason(doc.getString("reason"));
                        vibeEvent.setSocSit(SocSit.of(doc.getString("socSit")));
                        vibeEvent.setId(doc.getId());
                        vibeEvent.setVibe(doc.getString("vibe"));
                        vibeEvent.setOwner(mAuth.getCurrentUser().getDisplayName());

                        if (doc.getData().get("image") != null) {
                            vibeEvent.setImage(doc.getString("image"));
                        }

                        if (doc.getData().get("latitude") != null && doc.getData().get("longitude") != null) {
                            vibeEvent.setLatitude(doc.getDouble("latitude"));
                            vibeEvent.setLongitude(doc.getDouble("longitude"));
                        }

                        if (allFlag) {
                            vibeEventList.add(vibeEvent);
                        } else {
                            if (vibeFilter.equals(vibeEvent.getVibe().getName())){
                                vibeEventList.add(vibeEvent);
                            }
                        }
                    }
                    myVibesAdapter.notifyDataSetChanged();
                });
    }
}
