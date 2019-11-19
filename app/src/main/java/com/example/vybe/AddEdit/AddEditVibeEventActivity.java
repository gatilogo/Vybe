package com.example.vybe.AddEdit;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.vybe.MapFragment;
import com.example.vybe.Models.vibefactory.Vibe;
import com.example.vybe.R;
import com.example.vybe.Models.VibeEvent;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Locale;

import static com.example.vybe.util.Constants.REASON_FIELD_MAX_WORD_COUNT;

/**
 * This Activity displays the screen for a user to add a vibe event, or
 * edit an existing vibe event by adding or modifying the different vibe attributes
 */
public class AddEditVibeEventActivity extends AppCompatActivity implements SocialSituationFieldFragment.OnSocStnSelectedListener, ImageFieldFragment.OnImageSelectedListener, VibeCarouselDialogFragment.OnVibeSelectedListener, LocationSelectionDialog.OnLocationSelectedListener, MapFragment.OnMapFragmentReadyListener {

    private static final String TAG = "AddEditVibeEventActivity";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String vibeEventDBPath;

    // --- XML Elements ---
    private ImageView vibeImage;
    private EditText reasonField;
    private Button addBtn;
    private TextView pageTitle;
    private Button pickLocationButton;
    private Toolbar toolbar;
    private MapFragment mapFragment;
    private ImageView imageView;
    private SocialSituationFieldFragment socStnFragment;
    // -------------------

    private VibeEvent vibeEvent;
    private boolean editMode = false;
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
        vibeImage = findViewById(R.id.vibe_image);
        imageView = findViewById(R.id.imageView);
        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.add_edit_map_fragment);
        socStnFragment = (SocialSituationFieldFragment) getSupportFragmentManager().findFragmentById(R.id.social_situation_field_fragment);

        vibeEventDBPath = "Users/" + mAuth.getCurrentUser().getUid() + "/VibeEvents";

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            editMode = true;
            pageTitle.setText(getString(R.string.edit_vybe_name));
            vibeEvent = (VibeEvent) extras.getSerializable("vibeEvent");

            reasonField.setText(vibeEvent.getReason());

            if (vibeEvent.getImage() != null) {
                loadImageFirebase(imageView, vibeEvent.getImage());
            }

            if (vibeEvent.getSocialSituation() != null) {
                socStnFragment.setDefaultSocStn(vibeEvent.getSocialSituation());
            }


            setTheme(vibeEvent.getVibe());

        } else {
            vibeEvent = new VibeEvent();
        }

        // --- Vibe Carousel Picker ---
        vibeImage.setOnClickListener((View view) -> {
            new VibeCarouselDialogFragment().show(getSupportFragmentManager(), "Select a Vibe");
        });

        // ---Location Picker---
        pickLocationButton.setOnClickListener((View v) -> {
            DialogFragment locationFragment = new LocationSelectionDialog();
            locationFragment.show(getSupportFragmentManager(), "tag");
        });

        // --- Show Output on button click ---
        addBtn.setOnClickListener(view -> {

            if (reasonField.getText().toString().trim().split("\\s").length <= REASON_FIELD_MAX_WORD_COUNT) {
                reasonField.setError(null);
                vibeEvent.setReason(reasonField.getText().toString());
            } else {
                reasonField.setError(String.format(Locale.CANADA, "Max %d words allowed", REASON_FIELD_MAX_WORD_COUNT));
                return;
            }

            if (vibeEvent.getVibe() == null) {
                Toast.makeText(getApplicationContext(), "Select a Vibe!", Toast.LENGTH_LONG).show();
                return;
            }


            uploadVibeEvent();

            finish();
        });

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
    public void onVibeSelected(Vibe vibe) {
        vibeEvent.setVibe(vibe);
        setTheme(vibe);
    }

    private void setTheme(Vibe vibe) {
        vibeImage.setImageResource(vibe.getEmoticon());
        toolbar.setBackgroundResource(vibe.getColor());
        addBtn.setBackgroundResource(vibe.getColor());
    }

    @Override
    public void onMapFragmentReady() {
        if (vibeEvent.getLatitude() != 0 && vibeEvent.getLongitude() != 0) {
            mapFragment.setToLocation(new LatLng(vibeEvent.getLatitude(), vibeEvent.getLongitude()));

        } else {
            mapFragment.setToCurrentLocation();
        }
    }

    @Override
    public void onLocationSelected(double latitude, double longitude) {
        vibeEvent.setLatitude(latitude);
        vibeEvent.setLongitude(longitude);
        mapFragment.setToLocation(new LatLng(latitude, longitude));
    }

    private void uploadImage(Bitmap bitmap, String id) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArray = baos.toByteArray();

        String imgPath = "reasons/" + id + ".jpg";
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference mountainsRef = storageRef.child(imgPath);

        UploadTask uploadTask = mountainsRef.putBytes(byteArray);
        uploadTask.addOnFailureListener((@NonNull Exception exception) -> {
            Toast.makeText(getApplicationContext(), "Image Upload Failed", Toast.LENGTH_LONG).show();

        }).addOnSuccessListener((UploadTask.TaskSnapshot taskSnapshot) -> {
            Toast.makeText(getApplicationContext(), "Image Upload Successful", Toast.LENGTH_LONG).show();
        });

    }

    public void uploadVibeEvent() {
        if (!editMode) {
            String id = db.collection(vibeEventDBPath).document().getId();
            vibeEvent.setId(id);
        }

        if (imageIsSelected) {
            uploadImage(imageBitmap, vibeEvent.getId());
            vibeEvent.setImage("reasons/" + vibeEvent.getId() + ".jpg");
        }

        HashMap<String, Object> data = createVibeEventData(vibeEvent);
        db.collection(vibeEventDBPath).document(vibeEvent.getId()).set(data);
    }

    public HashMap<String, Object> createVibeEventData(VibeEvent vibeEvent) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("ID", vibeEvent.getId());
        data.put("vibe", vibeEvent.getVibe().getName());
        data.put("datetime", vibeEvent.getDateTime());
        data.put("reason", vibeEvent.getReason());
        data.put("socSit", vibeEvent.getSocialSituation());
        data.put("image", vibeEvent.getImage());
        data.put("latitude", vibeEvent.getLatitude());
        data.put("longitude", vibeEvent.getLongitude());
        return data;
    }

    ;

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
