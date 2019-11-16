package com.example.vybe.AddEdit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.vybe.LocationSelectionDialog;
import com.example.vybe.MapFragment;
import com.example.vybe.R;
import com.example.vybe.VibeCarouselFragment;
import com.example.vybe.VibeEvent;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * This Activity displays the screen for a user to add a vibe event, or
 * edit an existing vibe event by adding or modifying the different vibe attributes
 */
public class AddEditVibeEventActivity extends AppCompatActivity implements SocialSituationFieldFragment.SocStnSelectedListener, VibeFieldFragment.VibeSelectedListener, ImageFieldFragment.ImageSelectedListener, VibeCarouselFragment.OnFragmentInteractionListener, LocationSelectionDialog.OnFragmentInteractionListener {

    private static final String TAG = "AddEditVibeEventActivity";

    // --- XML Elements ---
    private ImageView vibeSelector;
    private EditText reasonField;
    private Button addBtn;
    private TextView pageTitle;
    private Button pickLocationButton;
    private Toolbar toolbar;
    private VibeFieldFragment vibeFieldFragment;
    private MapFragment addEditMapFragment;
    private ImageView imageView;
    // -------------------

    private VibeEvent vibeEvent;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private boolean editFlag = false;
    private boolean imageIsSelected = false;
    private Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_vibe);

        toolbar = findViewById(R.id.add_edit_vibes_toolbar);
        reasonField = findViewById(R.id.reason_edit_text);
        addBtn = findViewById(R.id.add_btn);
        pageTitle = findViewById(R.id.add_edit_vybe_title);
        pickLocationButton = findViewById(R.id.btn_add_location);
        vibeSelector = findViewById(R.id.vibe_selector);
        imageView = findViewById(R.id.imageView);
        vibeFieldFragment = (VibeFieldFragment) getSupportFragmentManager().findFragmentById(R.id.vibe_field_fragment);
        addEditMapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.add_edit_map_fragment);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            pageTitle.setText(getString(R.string.edit_vybe_name));
            editFlag = true;

            vibeEvent = (VibeEvent) extras.getSerializable("vibeEvent");
            reasonField.setText(vibeEvent.getReason());

            if (vibeEvent.getImage() != null) {
                loadImageFirebase(imageView, vibeEvent.getImage());
            }


            Bundle vibeFieldArgs = new Bundle();
            vibeFieldArgs.putSerializable("vibe", vibeEvent.getVibe().getName());
            vibeFieldFragment.setDefaultVibe(vibeFieldArgs);

            vibeSelector.setImageResource(vibeEvent.getVibe().getEmoticon());

            toolbar.setBackgroundResource(vibeEvent.getVibe().getColor());
            addBtn.setBackgroundResource(vibeEvent.getVibe().getColor());

            if (vibeEvent.getLatitude() != 0 && vibeEvent.getLongitude() != 0) {
                addEditMapFragment.addVibeEventToMap(vibeEvent);
            }

        } else {
            vibeEvent = new VibeEvent();
            vibeEvent.setDateTime(LocalDateTime.now());
            vibeEvent.setLatitude(0);
            vibeEvent.setLongitude(0);
        }

        // --- Vibe Carousel Picker ---
        vibeSelector.setOnClickListener((View view) -> {
            new VibeCarouselFragment().show(getSupportFragmentManager(), "Select a Vibe");
        });

        // ---Location Picker---
        pickLocationButton.setOnClickListener((View v) -> {
            DialogFragment locationFragment = new LocationSelectionDialog();
            locationFragment.show(getSupportFragmentManager(), "tag");
        });

        // --- Show Output on button click ---
        addBtn.setOnClickListener(view -> {

            //TODO: integrate firestore stuff here i guess
            vibeEvent.setReason(reasonField.getText().toString());

            if (editFlag) {
                editVibeEvent(vibeEvent);
            } else {
                addVibeEvent(vibeEvent);
            }
            finish();
        });

    }

    @Override
    public void onVibeSelected(String selectedVibe) {
        vibeEvent.setVibe(selectedVibe);
    }

    @Override
    public void onSocStnSelected(String socStn) {
        vibeEvent.setSocialSituation(socStn);
    }

    @Override
    public void onImageSelected(Bitmap selectedImageBitmap) {
        imageBitmap = selectedImageBitmap;
        imageIsSelected = true;
    }

    @Override
    public void onOkPressed(int selectedEmoticon) {
        vibeEvent.setVibe(selectedEmoticon);
        vibeSelector.setImageResource(selectedEmoticon);
        toolbar.setBackgroundResource(vibeEvent.getVibe().getColor());
        addBtn.setBackgroundResource(vibeEvent.getVibe().getColor());
    }

    @Override
    public void onOkPressed(double latitude, double longitude) {
        vibeEvent.setLatitude(latitude);
        vibeEvent.setLongitude(longitude);
        addEditMapFragment.addSingleMarker(latitude, longitude);
    }

    private void uploadImage(Bitmap bitmap, String id) {
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

    /**
     * This will load an image from Firebase Storage into an ImageView
     *
     * @param imageView The destination ImageView
     * @param imagePath Path to the image in Firebase Storage
     */
    public void loadImageFirebase(ImageView imageView, String imagePath) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        // Get the path to the image
        StorageReference imageRef = storageRef.child(imagePath);

        // Get the download URL for Glide
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext())
                        .load(uri) // Load the image
                        .into(imageView); // Destination to load image into
            }
        });
    }
}
