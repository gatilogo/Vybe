package com.example.vybe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.time.LocalDateTime;

public class AddVibeActivity extends AppCompatActivity {

    private static final String TAG = "AddVibeActivity";

    EditText vibeField;
    EditText datetimeField;
    EditText reasonField;
    EditText socialSituationField;
    Button addBtn;

    Spinner vibeDropdown;

    TextView outputBox;
    VibeEvent newVibeEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addvibe);

        Log.d(TAG, "onCreate: In Add vibes");

        vibeField = findViewById(R.id.vibe_edit_text);
        datetimeField = findViewById(R.id.date_time_edit_text);
        reasonField = findViewById(R.id.reason_edit_text);
        socialSituationField = findViewById(R.id.social_situation_edit_text);
        addBtn = findViewById(R.id.add_btn);

        vibeDropdown = findViewById(R.id.vibe_dropdown);

        outputBox = findViewById(R.id.textView);

        newVibeEvent = new VibeEvent();

//        final String selectedVibe = new String("");
        String[] vibes = new String[]{"Happy", "Sad", "Spicy"};
        ArrayAdapter<String> vibesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, vibes);
        vibeDropdown.setAdapter(vibesAdapter);
        vibeDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                newVibeEvent.setVibe(new Vibe(vibes[i]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });


        addBtn.setOnClickListener(view -> {
            newVibeEvent.setDateTime(LocalDateTime.now());
            newVibeEvent.setReason(reasonField.getText().toString());
            newVibeEvent.setSocialSituation(socialSituationField.getText().toString());

            String output = "";
            output += "Vibe: " + newVibeEvent.getVibe().getName() + "\n";
            output += "DateTime: " + newVibeEvent.getDateTime() + "\n";
            output += "Reason: " + newVibeEvent.getReason() + "\n";
            output += "Social Situation: " + newVibeEvent.getSocialSituation() + "\n";
            outputBox.setText(output);
        });


    }
}
