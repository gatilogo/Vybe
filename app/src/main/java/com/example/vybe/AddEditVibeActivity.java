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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class AddEditVibeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final String TAG = "AddEditVibeActivity";

    // --- XML Elements ---
    private Spinner vibeDropdown;
    private EditText datetimeField;
    private EditText reasonField;
    private Spinner socialSituationDropdown;
    private Button addBtn;
    private TextView outputBox;
    // -------------------

    private VibeEvent vibeEvent;
    private LocalDate selectedDate;
    private LocalTime selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_vibe);
        Log.d(TAG, "onCreate: In Add/Edit vibes");

        vibeDropdown = findViewById(R.id.vibe_dropdown);
        datetimeField = findViewById(R.id.date_time_edit_text);
        reasonField = findViewById(R.id.reason_edit_text);
        socialSituationDropdown = findViewById(R.id.social_situation_dropdown);
        addBtn = findViewById(R.id.add_btn);
        outputBox = findViewById(R.id.textView);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            //TODO: set vibe and socsit dropdrowns
            vibeEvent = (VibeEvent) extras.getSerializable("vibeEvent");
            reasonField.setText(vibeEvent.getReason());

            setTitle(getString(R.string.edit_vybe_name));
        } else {
            vibeEvent = new VibeEvent();
            vibeEvent.setDateTime(LocalDateTime.now());
        }

        LocalDateTime currDateTime = vibeEvent.getDateTime();
        selectedDate = currDateTime.toLocalDate();
        selectedTime = currDateTime.toLocalTime();
        datetimeField.setText(formatDateTime(currDateTime));



        // --- Vibes Dropdown ---
        String[] vibes = new String[]{"Select a vibe", "Angry", "Disgusted", "Happy", "Sad", "Scared", "Surprised"};
        ArrayAdapter<String> vibesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, vibes);
        vibeDropdown.setAdapter(vibesAdapter);
        vibeDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                vibeEvent.setVibe(vibes[position]);
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
                vibeEvent.setSocialSituation(socialSituations[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // --- Date Picker ---
        datetimeField.setOnClickListener((View view) -> {
            openDatePickerDialog(selectedDate);
        });


        // --- Show Output on button click ---
        addBtn.setOnClickListener(view -> {

            //TODO: integrate firestore stuff here i guess

            vibeEvent.setDateTime(LocalDateTime.of(selectedDate, selectedTime));
            vibeEvent.setReason(reasonField.getText().toString());

            String output = "";
            output += "Vibe: " + vibeEvent.getVibe().getVibe() + "\n";
            output += "DateTime: " + vibeEvent.getDateTime() + "\n";
            output += "Reason: " + vibeEvent.getReason() + "\n";
            output += "Social Situation: " + vibeEvent.getSocialSituation() + "\n";
            outputBox.setText(output);
//            finish();
        });

    }

    private void openDatePickerDialog(LocalDate currDate) {
        int currYear = currDate.getYear();
        int currMonth = currDate.getMonthValue() - 1; // Since indexing starts at 0
        int currDay = currDate.getDayOfMonth();

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddEditVibeActivity.this, AddEditVibeActivity.this, currYear, currMonth, currDay);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        month += 1; // since indexing starts at 0
        selectedDate = LocalDate.of(year, month, day);

        openTimePickerDialog(selectedTime);
    }

    private void openTimePickerDialog(LocalTime currTime) {
        int currHour = currTime.getHour();
        int currMin = currTime.getMinute();

        TimePickerDialog tpd = new TimePickerDialog(AddEditVibeActivity.this, AddEditVibeActivity.this, currHour, currMin, true);
        tpd.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int min) {
        selectedTime = LocalTime.of(hour, min);
        datetimeField.setText(formatDateTime(LocalDateTime.of(selectedDate, selectedTime)));
    }

    public String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("MMM dd yyyy, HH:mm"));
    }

}
