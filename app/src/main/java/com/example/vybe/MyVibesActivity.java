package com.example.vybe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class MyVibesActivity extends AppCompatActivity {

    private static final String TAG = "MyVibesActivity";

    ArrayList<VibeEvent> vibeEventList;
    MyVibesAdapter myVibesAdapter;

    private ListView vibesListView;
    private Button addVibeEventBtn;
    private Button myMapBtn;
    private Button socialBtn;

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

        vibesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MyVibesActivity.this, ViewVibeActivity.class);
                VibeEvent vibeEvent = vibeEventList.get(i);
                intent.putExtra("vibeEvent", vibeEvent);
                startActivity(intent);
            }
        });

        addVibeEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyVibesActivity.this, CUDVibeActivity.class);
                startActivity(intent);
            }
        });
    }
}
