package com.example.vybe.AddEdit;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.vybe.MapFragment;
import com.example.vybe.Models.SocSit;
import com.example.vybe.Models.Vibe;
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
public class AddEditVibeEventActivity extends AppCompatActivity implements SocSitFieldFragment.OnSocSitSelectedListener, ImageFieldFragment.OnImageSelectedListener, VibeCarouselDialogFragment.OnVibeSelectedListener, LocationSelectionDialog.OnLocationSelectedListener, MapFragment.OnMapFragmentReadyListener {

    private static final String TAG = "AddEditVibeEventActivity";
    private AddEditController addEditController = AddEditController.getInstance(AddEditVibeEventActivity.this);

    // --- XML Elements ---
    private ImageView vibeImage;
    private EditText reasonField;
    private Button addBtn;
    private TextView pageTitle;
    private Button pickLocationButton;
    private ImageButton deleteLocationButton;
    private Toolbar toolbar;
    private MapFragment mapFragment;
    private SocSitFieldFragment socSitFragment;
    private ImageFieldFragment imageFieldFragment;
    // -------------------

    private Vibe vibe;
    private String reason;
    private SocSit socSit;
    private Bitmap image;
    private Double latitude;
    private Double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_vibe);

        toolbar = findViewById(R.id.add_edit_vibes_toolbar);
        reasonField = findViewById(R.id.reason_edit_text);
        addBtn = findViewById(R.id.add_btn);
        pageTitle = findViewById(R.id.add_edit_vibe_title);
        pickLocationButton = findViewById(R.id.btn_add_location);
        deleteLocationButton = findViewById(R.id.btn_remove_location);
        vibeImage = findViewById(R.id.vibe_image);
        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.add_edit_map_fragment);
        socSitFragment = (SocSitFieldFragment) getSupportFragmentManager().findFragmentById(R.id.soc_sit_field_fragment);
        imageFieldFragment = (ImageFieldFragment) getSupportFragmentManager().findFragmentById(R.id.image_field_fragment);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            pageTitle.setText(getString(R.string.edit_vibe_name));
            VibeEvent vibeEvent = (VibeEvent) extras.getSerializable("vibeEvent");
            addEditController.editVibeEvent(vibeEvent);

        } else {
            addEditController.addVibeEvent();
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
        
        //---Remove Location---
        deleteLocationButton.setOnClickListener((View v) -> {
            clearLocation();
        });

        // --- Show Output on button click ---
        addBtn.setOnClickListener(view -> {
            if (fieldAreValid()) {
                addEditController.saveVibeEvent(vibe, reason, socSit, image, latitude, longitude);
                finish();
            }
        });

    }

    // ------------- Validation -------------
    public boolean fieldAreValid() {
        return vibeFieldIsValid() && reasonFieldIsValid();
    }

    public boolean vibeFieldIsValid() {
        if (vibe == null) {
            Toast.makeText(getApplicationContext(), "Select a Vibe!", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public boolean reasonFieldIsValid() {
        reason = reasonField.getText().toString().trim();
        if (reason.split("\\s").length <= REASON_FIELD_MAX_WORD_COUNT) {
            reasonField.setError(null);
            return true;

        } else {
            reasonField.setError(String.format(Locale.CANADA, "Max %d words allowed", REASON_FIELD_MAX_WORD_COUNT));
            return false;
        }
    }

    // ------------- Setters --------------

    public void setVibe(Vibe vibe) {
        this.vibe = vibe;
        vibeImage.setImageResource(vibe.getEmoticon());
        toolbar.setBackgroundResource(vibe.getColor());
        addBtn.setBackgroundResource(vibe.getColor());
    }
    public void setReason(String reason) {
        reasonField.setText(reason);
    }

    public void setSocSit(SocSit socSit) {
        this.socSit = socSit;
        socSitFragment.setDefaultSocSit(socSit);
    }


    public void setImage(Bitmap image) {
        this.image = image;
        imageFieldFragment.setImage(image);
    }

    public void setLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        mapFragment.showMap();
        mapFragment.setToLocation(new LatLng(latitude, longitude));
        deleteLocationButton.setVisibility(View.VISIBLE);
    }

    public void clearLocation() {
        latitude = null;
        longitude = null;
        mapFragment.clearMap();
        mapFragment.hideMap();
        deleteLocationButton.setVisibility(View.GONE);
    }

    // ------------- Overrides --------------

    @Override
    public void onSocSitSelected(SocSit socSit) {
        this.socSit = socSit;
    }

    @Override
    public void onImageSelected(Bitmap image) {
        this.image = image;
    }

    @Override
    public void onVibeSelected(Vibe vibe) {
        setVibe(vibe);
    }

    @Override
    public void onMapFragmentReady() {
        addEditController.onMapFragmentReady();
    }

    @Override
    public void onLocationSelected(double latitude, double longitude) {
        setLocation(latitude, longitude);
    }
}
