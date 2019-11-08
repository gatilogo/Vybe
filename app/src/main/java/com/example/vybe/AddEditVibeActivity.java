package com.example.vybe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AddEditVibeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final String TAG = "AddEditVibeActivity";
    private static final int GET_FROM_GALLERY = 1000;

    // --- XML Elements ---
    private Spinner vibeDropdown;
    private EditText datetimeField;
    private EditText reasonField;
    private Spinner socialSituationDropdown;
    private Button addBtn;
    private TextView outputBox;
    private Button pickImageBtn;
    private ImageView imageView;
    // -------------------

    private VibeEvent vibeEvent;
    private LocalDate selectedDate;
    private LocalTime selectedTime;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private boolean editFlag = false;
    private boolean imageIsSelected = false;
    private Bitmap imageBitmap;


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
        pickImageBtn = findViewById(R.id.pickImageBtn);
        imageView = findViewById(R.id.imageView);

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            //TODO: set vibe and socsit dropdrowns
            vibeEvent = (VibeEvent) extras.getSerializable("vibeEvent");
            reasonField.setText(vibeEvent.getReason());
            editFlag = true;

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
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
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
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // --- Date Picker ---
        datetimeField.setOnClickListener((View view) -> {
            openDatePickerDialog(selectedDate);
        });


        // --- Image Picker ---
        pickImageBtn.setOnClickListener((View view) -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, GET_FROM_GALLERY);
        });

        // --- Show Output on button click ---
        addBtn.setOnClickListener(view -> {

            //TODO: integrate firestore stuff here i guess

            vibeEvent.setDateTime(LocalDateTime.of(selectedDate, selectedTime));
            vibeEvent.setReason(reasonField.getText().toString());



            String output = "";
            output += "Vibe: " + vibeEvent.getVibe().getName() + "\n";
            output += "DateTime: " + vibeEvent.getDateTime() + "\n";
            output += "Reason: " + vibeEvent.getReason() + "\n";
            output += "Social Situation: " + vibeEvent.getSocialSituation() + "\n";
            outputBox.setText(output);

            if (editFlag) {
                editVibeEvent(vibeEvent);
            } else {
                addVibeEvent(vibeEvent);
            }
//            finish();
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_FROM_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                imageIsSelected = true;
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            imageView.setImageBitmap(imageBitmap);

        }
    }

    public void uploadImage(Bitmap bitmap, String id) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArray = baos.toByteArray();

//        Log.d(TAG, id + ".jpg");
        String imgPath = "reasons/" + id + ".jpg";
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference mountainsRef = storageRef.child(imgPath);

        UploadTask uploadTask = mountainsRef.putBytes(byteArray);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(), "Succ", Toast.LENGTH_LONG).show();
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });

    }

    // TODO: Move to Controller Class
    public void addVibeEvent(VibeEvent vibeEvent) {
        // TODO: Put as private attribute in Controller class if we use one

        String id = db.collection("VibeEvent").document().getId();
        vibeEvent.setId(id);
        if (imageIsSelected) {
            uploadImage(imageBitmap, id);
            vibeEvent.setImage("reasons/" + id + ".jpg");
        }
        HashMap<String, Object> data = createVibeEventData(vibeEvent);
        db.collection("VibeEvent").document(id).set(data);
    }

    public void editVibeEvent(VibeEvent vibeEvent) {
        if (imageIsSelected) {
            uploadImage(imageBitmap, vibeEvent.getId());
            vibeEvent.setImage("reasons/" + vibeEvent.getId() + ".jpg");
        }
        HashMap<String, Object> data = createVibeEventData(vibeEvent);
        db.collection("VibeEvent").document(vibeEvent.getId()).set(data);
    }

    public HashMap<String, Object> createVibeEventData(VibeEvent vibeEvent) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("ID", vibeEvent.getId());
        data.put("vibe", vibeEvent.getVibe().getName());
        data.put("datetime", vibeEvent.getDateTimeFormat());
        data.put("reason", vibeEvent.getReason());
        data.put("socSit", vibeEvent.getSocialSituation());
        data.put("image", vibeEvent.getImage());
        return data;
    };

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
        return dateTime.format(DateTimeFormatter.ofPattern("MMM dd yyyy, hh:mm a"));
    }

}
