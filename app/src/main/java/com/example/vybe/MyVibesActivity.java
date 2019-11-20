package com.example.vybe;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vybe.AddEdit.AddEditVibeEventActivity;
import com.example.vybe.Models.User;
import com.example.vybe.Models.VibeEvent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

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

    ArrayList<VibeEvent> vibeEventList;
    MyVibesAdapter myVibesAdapter;

    private boolean mLocationPermissionGranted = false;

    private Spinner filterSpinner;
    private RecyclerView vibesRecyclerView;
    private Button addVibeEventBtn;
    private Button myMapBtn;
    private Button socialBtn;
    private ImageButton profileBtn;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String vibeEventDBPath;
    private boolean allFlag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vibes);
        Intent intent = getIntent();
        Log.d(TAG, "onCreate: In my vibes");

        filterSpinner = findViewById(R.id.filter_spinner);
        vibesRecyclerView = findViewById(R.id.my_vibe_list);
        addVibeEventBtn = findViewById(R.id.add_vibe_event_btn);
        myMapBtn = findViewById(R.id.my_map_btn);
        socialBtn = findViewById(R.id.social_btn);
        profileBtn = findViewById(R.id.profile_btn);

        vibeEventDBPath = "Users/" + mAuth.getCurrentUser().getUid() + "/VibeEvents";
        
        allFlag = true; // Ask jakey

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyVibesActivity.this, ViewProfileActivity.class);
                // Get the current user's profile information
                db.collection("Users").document(mAuth.getCurrentUser().getUid()).get()
                        .addOnSuccessListener((DocumentSnapshot doc) -> {
                            String username = (String) doc.get("username");
                            String email = (String) doc.get("email");
                            intent.putExtra("user", new User(username, email));
                            startActivity(intent);
                        });

            }
        });

        // --- Vibes Dropdown ---
        // TODO: Refactor with custom spinner and/or different filtering methodology
        String[] vibes = new String[]{"Filter Vibe", "Angry", "Disgusted", "Happy", "Sad", "Scared", "Surprised"};
        ArrayAdapter<String> vibesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, vibes);
        filterSpinner.setAdapter(vibesAdapter);
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    String filterVibe = vibes[position].toLowerCase();
                    allFlag = true;
                    if (position != 0){ allFlag = false;}
                    db.collection(vibeEventDBPath)
                            .orderBy("datetime", Query.Direction.DESCENDING)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    // TODO: Stub out with other query below
                                    vibeEventList.clear();
                                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots){

                                        VibeEvent vibeEvent = new VibeEvent();
                                        vibeEvent.setDateTime(doc.getDate("datetime"));
                                        vibeEvent.setReason(doc.getString("reason"));
                                        vibeEvent.setSocialSituation(doc.getString("socSit"));
                                        vibeEvent.setId(doc.getId());
                                        vibeEvent.setVibe(doc.getString("vibe"));

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
                                            if (filterVibe.equals(vibeEvent.getVibe().getName())){
                                                vibeEventList.add(vibeEvent);
                                            }
                                        }
                                    }
                                    myVibesAdapter.notifyDataSetChanged();
                                }
                            });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

//        Vibe vibe, Date date, String reason, String socialSituation
        vibeEventList = new ArrayList<>();

        myVibesAdapter = new MyVibesAdapter(this, R.layout.my_vibe_item, vibeEventList);
        vibesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        vibesRecyclerView.setAdapter(myVibesAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {
                    myVibesAdapter.deleteItem(position, vibeEventDBPath);
                } else if (direction == ItemTouchHelper.RIGHT) {
                    Intent intent = new Intent(MyVibesActivity.this, AddEditVibeEventActivity.class);
                    VibeEvent vibeEvent = vibeEventList.get(position);
                    intent.putExtra("vibeEvent", vibeEvent);
                    startActivity(intent);
                }
            }

            @Override
            public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                drawButtons(c, viewHolder, dX);
            }

            private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder, float dX) {
                float corners = 16;

                View itemView = viewHolder.itemView;
                Paint p = new Paint();
                RectF button = new RectF();
                String msg = "";

                if (dX < 0) {   // Swipe left
                    button.set(itemView.getRight() + dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                    int deleteColor = ContextCompat.getColor(MyVibesActivity.this, R.color.Delete);
                    p.setColor(deleteColor);
                    msg = "DELETE";
                } else if (dX > 0) {    // Swipe right
                    button.set(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + dX, itemView.getBottom());
                    int position = viewHolder.getAdapterPosition();
                    VibeEvent vibeEvent = vibeEventList.get(position);
                    int vibeColor = vibeEvent.getVibe().getColor();
                    int editColor = ContextCompat.getColor(MyVibesActivity.this, vibeColor);
                    p.setColor(editColor);
                    msg = "EDIT";
                }

                c.drawRoundRect(button, corners, corners, p);
                drawText(msg, c, button, p);
            }

            private void drawText(String text, Canvas c, RectF button, Paint p) {
                float textSize = 60;
                p.setColor(Color.WHITE);
                p.setAntiAlias(true);
                p.setTextSize(textSize);

                float textWidth = p.measureText(text);
                c.drawText(text, button.centerX()-(textWidth/2), button.centerY()+(textSize/2), p);
            }
        };

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(vibesRecyclerView);

        addVibeEventBtn.setOnClickListener((View view) -> {
            Intent addIntent = new Intent(MyVibesActivity.this, AddEditVibeEventActivity.class);
            startActivity(addIntent);
        });

        myMapBtn.setOnClickListener((View view) -> {
            if (mLocationPermissionGranted) {
                Intent MapViewIntent = new Intent(MyVibesActivity.this, MapViewActivity.class);
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Want to get the most recent list of mood history
        CollectionReference collectionReference = db.collection(vibeEventDBPath);
        Query query = collectionReference.orderBy("datetime", Query.Direction.DESCENDING);

        query.get().addOnSuccessListener((QuerySnapshot queryDocumentSnapshots) -> {
                vibeEventList.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    VibeEvent vibeEvent = new VibeEvent();
                    vibeEvent.setDateTime(doc.getDate("datetime"));
                    vibeEvent.setReason(doc.getString("reason"));
                    vibeEvent.setSocialSituation(doc.getString("socSit"));
                    vibeEvent.setId(doc.getId());
                    vibeEvent.setVibe(doc.getString("vibe"));
                    if (doc.getData().get("image") != null) {
                        vibeEvent.setImage(doc.getString("image"));
                    }
                    if (doc.getData().get("latitude") != null && doc.getData().get("longitude") != null) {
                        vibeEvent.setLatitude(doc.getDouble("latitude"));
                        vibeEvent.setLongitude(doc.getDouble("longitude"));
                    }
                    vibeEventList.add(vibeEvent);
                }
                myVibesAdapter.notifyDataSetChanged();
        });
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
}
