package com.example.vybe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;

public class AddVibeActivity extends AppCompatActivity {

    private static final String TAG = "AddVibeActivity";

    // --- XML Elements ---
    Spinner vibeDropdown;
    EditText datetimeField;
    EditText reasonField;
    Spinner socialSituationDropdown;
    Button addBtn;
    TextView outputBox;
    // -------------------

    VibeEvent newVibeEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addvibe);
        Log.d(TAG, "onCreate: In Add vibes");

        vibeDropdown = findViewById(R.id.vibe_dropdown);
        datetimeField = findViewById(R.id.date_time_edit_text);
        reasonField = findViewById(R.id.reason_edit_text);
        socialSituationDropdown = findViewById(R.id.social_situation_dropdown);
        addBtn = findViewById(R.id.add_btn);
        outputBox = findViewById(R.id.textView);

        newVibeEvent = new VibeEvent();

        // --- Vibes Dropdown ---
        String[] vibes = new String[]{"Select a vibe", "Happy", "Sad", "Spicy"};
        ArrayAdapter<String> vibesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, vibes);
        vibeDropdown.setAdapter(vibesAdapter);
        vibeDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                newVibeEvent.setVibe(new Vibe(vibes[position]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // --- Social Situation Dropdown ---
        String[] socialSituations = new String[]{"Select a Social Situation", "Alone", "In a group", "Alone in a group"};
        ArrayAdapter<String> socialSituationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, socialSituations);
        socialSituationDropdown.setAdapter(socialSituationAdapter);
        socialSituationDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                newVibeEvent.setSocialSituation(socialSituations[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // --- Date Picker ---
        newVibeEvent.setDateTime(LocalDateTime.now());
        datetimeField.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int currYear = c.get(Calendar.YEAR);
                int currMonth = c.get(Calendar.MONTH);
                int currDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(AddVibeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month += 1; // since indexing starts at 0
                        String dateString = year + "-" + month + "-" + day;
                        int[] selectedTime = new int[2];
                        timeSelector(selectedTime);
                        String timeString = selectedTime[0] + ":" + selectedTime[1];

                        datetimeField.setText(dateString + " " + timeString);
                        //datetimeField.clearFocus();

                        newVibeEvent.setDateTime(LocalDateTime.of(year, month, day, selectedTime[0], selectedTime[1]));
                    }
                }, currYear, currMonth, currDay);
                dpd.show();
                outputBox.setText("HERE");
            }
        });


        // --- Show Output on button click ---
        addBtn.setOnClickListener(view -> {
            newVibeEvent.setReason(reasonField.getText().toString());

            String output = "";
            output += "Vibe: " + newVibeEvent.getVibe().getName() + "\n";
            output += "DateTime: " + newVibeEvent.getDateTime() + "\n";
            output += "Reason: " + newVibeEvent.getReason() + "\n";
            output += "Social Situation: " + newVibeEvent.getSocialSituation() + "\n";
            outputBox.setText(output);
        });


    }

    public void timeSelector(int[] selectedTime) {

        // initialize and display time picker dialog
        TimePickerDialog tpd = new TimePickerDialog(AddVibeActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
                public void onTimeSet(TimePicker timePicker, int hour, int min) {
                    String timeString = hour + ":" + min;
                    //text.setText(timeString);
                    datetimeField.clearFocus();
                    outputBox.setText(timeString);
                    selectedTime[0] = hour;
                    selectedTime[1] = min;
                }
                }, 0, 0, true);
        tpd.show();
    }

}
