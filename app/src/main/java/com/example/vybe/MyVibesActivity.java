package com.example.vybe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.TimeZone;

/**
 * This Activity displays the screen for a user to view a list of their
 * vibe event history, sorted by date and time in reverse chronological
 * order
 */
public class MyVibesActivity extends AppCompatActivity {

    private static final String TAG = "MyVibesActivity";

    ArrayList<VibeEvent> vibeEventList;
    MyVibesAdapter myVibesAdapter;

    private Spinner filterSpinner;
    private ListView vibesListView;
    private Button addVibeEventBtn;
    private Button myMapBtn;
    private Button socialBtn;
    private ImageButton profileBtn;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean allFlag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vibes);
        Intent intent = getIntent();
        Log.d(TAG, "onCreate: In my vibes");

        filterSpinner = findViewById(R.id.filter_spinner);
        vibesListView = findViewById(R.id.my_vibe_list);
        addVibeEventBtn = findViewById(R.id.add_vibe_event_btn);
        myMapBtn = findViewById(R.id.my_map_btn);
        socialBtn = findViewById(R.id.social_btn);
        profileBtn = findViewById(R.id.profile_btn);

        allFlag = true; // Ask jakey

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyVibesActivity.this, ViewProfileActivity.class);
                startActivity(intent);
            }
        });

        // --- Vibes Dropdown ---
        String[] vibes = new String[]{"Filter Vibe", "Angry", "Disgusted", "Happy", "Sad", "Scared", "Surprised"};
        ArrayAdapter<String> vibesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, vibes);
        filterSpinner.setAdapter(vibesAdapter);
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    String filterVibe = vibes[position].toLowerCase();
                    allFlag = true;
                    if (position != 0){ allFlag = false;}
                    db.collection("VibeEvent")
                            .orderBy("datetime", Query.Direction.DESCENDING)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    // TODO: Stub out with other query below
                                    vibeEventList.clear();
                                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots){

                                            LocalDateTime ldt = doc.getDate("datetime").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                                            Log.d(TAG, ldt.toString());
                                            String reason = (String) doc.getData().get("reason");
                                            String socSit = (String) doc.getData().get("socSit");
                                            String id = (String) doc.getData().get("ID");
                                            String vibe = (String) doc.getData().get("vibe");
                                            String image = (String) doc.getData().get("image");
                                        if (allFlag) {
                                            vibeEventList.add(new VibeEvent(vibe, ldt, reason, socSit, id, image));
                                        } else {
                                            if (filterVibe.equals(vibe)){
                                                vibeEventList.add(new VibeEvent(vibe, ldt, reason, socSit, id, image));
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
        vibesListView.setAdapter(myVibesAdapter);

        final CollectionReference collectionReference = db.collection("VibeEvent");
        Query query = collectionReference.orderBy("datetime", Query.Direction.DESCENDING);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                // TODO: Stub out with other query above
                vibeEventList.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
//                    LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(doc.getDate("datetime").getTime()),
//                            TimeZone.getDefault().toZoneId());
                    LocalDateTime ldt = doc.getDate("datetime").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    Log.d(TAG, ldt.toString());
                    String reason = (String) doc.getData().get("reason");
                    String socSit = (String) doc.getData().get("socSit");
                    String id = (String) doc.getData().get("ID");
                    String vibe = (String) doc.getData().get("vibe");
                    String image = (String) doc.getData().get("image");
                    vibeEventList.add(new VibeEvent(vibe, ldt, reason, socSit, id, image));
                }
                myVibesAdapter.notifyDataSetChanged();
            }
        });

        vibesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MyVibesActivity.this, ViewVibeActivity.class);
                VibeEvent vibeEvent = vibeEventList.get(i);
                intent.putExtra("vibeEvent", vibeEvent);
                startActivity(intent);
            }
        });

        vibesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyVibesActivity.this);
                builder.setMessage("yay");
                builder.setCancelable(true);

                // Delete ride if user clicks on "Yes" button
                builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(MyVibesActivity.this, AddEditVibeActivity.class);
                        VibeEvent vibeEvent = vibeEventList.get(position);
                        intent.putExtra("vibeEvent", vibeEvent);
                        startActivity(intent);
                    }
                });

                // Close dialog if user clicks on "No" button
                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        VibeEvent vibeEvent = vibeEventList.get(position);
                        db.collection("VibeEvent").document(vibeEvent.getId()).delete();
                        myVibesAdapter.notifyDataSetChanged();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            }
        });

        addVibeEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyVibesActivity.this, AddEditVibeActivity.class);
                startActivity(intent);
            }
        });
    }
}
