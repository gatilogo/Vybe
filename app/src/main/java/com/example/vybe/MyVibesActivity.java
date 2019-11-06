package com.example.vybe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.TimeZone;

public class MyVibesActivity extends AppCompatActivity {

    private static final String TAG = "MyVibesActivity";

    ArrayList<VibeEvent> vibeEventList;
    MyVibesAdapter myVibesAdapter;

    private ListView vibesListView;
    private Button addVibeEventBtn;
    private Button myMapBtn;
    private Button socialBtn;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vibes);
        Intent intent = getIntent();
        Log.d(TAG, "onCreate: In my vibes");

        vibesListView = findViewById(R.id.my_vibe_list);
        addVibeEventBtn = findViewById(R.id.add_vibe_event_btn);
        myMapBtn = findViewById(R.id.my_map_btn);
        socialBtn = findViewById(R.id.social_btn);

//        Vibe vibe, Date date, String reason, String socialSituation
        vibeEventList = new ArrayList<VibeEvent>();
        vibeEventList.add(new VibeEvent(new Vibe(), LocalDateTime.now(), "Didn't study for final", "Alone :("));
        vibeEventList.add(new VibeEvent(new Vibe(), LocalDateTime.of(2019, 05, 18, 10, 10), "Worked on CMPUT 301 Project", "With Team Vybe :((("));
        vibeEventList.add(new VibeEvent(new Vibe(), LocalDateTime.now(), "Failed CMPUT 301", "With Everyone :)))"));

        myVibesAdapter = new MyVibesAdapter(this, R.layout.my_vibe_item, vibeEventList);
        vibesListView.setAdapter(myVibesAdapter);

        final CollectionReference collectionReference = db.collection("VibeEvent");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                vibeEventList.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                    LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(doc.getDate("datetime").getTime()),
                            TimeZone.getDefault().toZoneId());
                    Log.d(TAG, ldt.toString());
                    String reason = (String) doc.getData().get("reason");
                    String socSit = (String) doc.getData().get("socSit");
                    vibeEventList.add(new VibeEvent(new Vibe(), ldt, reason, socSit));
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
                        vibeEventList.remove(position);
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
