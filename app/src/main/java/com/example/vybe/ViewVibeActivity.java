package com.example.vybe;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.vybe.Models.SocSit;
import com.example.vybe.Models.VibeEvent;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This Activity displays the screen for a vibe event and all its available details
 */
public class ViewVibeActivity extends AppCompatActivity implements MapFragment.OnMapFragmentReadyListener{

    private static final String TAG = "ViewVibeActivity";

    private VibeEvent vibeEvent;

    private ImageView vibeImage;
    private TextView dateField;
    private TextView reasonField;
    private TextView reasonLabel;
    private TextView socialSituationField;
    private TextView socialSituationLabel;
    private ImageView reasonImage;
    private Toolbar toolbar;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_vibe);
        Log.d(TAG, "onCreate: In View vibes");

        vibeImage = findViewById(R.id.view_vibe_image_view);
        dateField = findViewById(R.id.view_date_text_view);
        reasonField = findViewById(R.id.view_reason_text_view);
        reasonLabel = findViewById(R.id.view_reason_label);
        socialSituationField = findViewById(R.id.view_social_situation_text_view);
        socialSituationLabel = findViewById(R.id.view_social_situation_label);
        reasonImage = findViewById(R.id.reason_image);

        reasonImage.setDrawingCacheEnabled(true);
        reasonImage.buildDrawingCache();
        toolbar = findViewById(R.id.view_vibes_toolbar);

        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.view_vibe_map);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras.containsKey("vibeEvent")) {
            vibeEvent = (VibeEvent) extras.getSerializable("vibeEvent");
            populateVibeEventDetails(vibeEvent);
        }

    }


    /**
     * This will populate the appropriate fields for viewing Vibe Event details
     * @param vibeEvent
     *      The Vibe Event object containing details to be displayed
     */
    public void populateVibeEventDetails(VibeEvent vibeEvent) {
        //
        String datetimeText = vibeEvent.getDateTimeString();
        dateField.setText(datetimeText);
        String reason = vibeEvent.getReason();
        SocSit socSit = vibeEvent.getSocSit();
        String reasonImagePath = vibeEvent.getImage();

        if (reasonImagePath != null){
            loadImageFirebase(reasonImage, reasonImagePath);
        }

        vibeImage.setImageResource(vibeEvent.getVibe().getEmoticon());
        toolbar.setBackgroundResource(vibeEvent.getVibe().getColor());

        if (reason == null || reason.equals("")) {  // Reason is optional
            reasonLabel.setVisibility(TextView.GONE);
            reasonField.setVisibility(TextView.GONE);
        } else {
            reasonField.setText(reason);
        }

        if (socSit == null) { // Social Situation is optional
            socialSituationLabel.setVisibility(TextView.GONE);
            socialSituationField.setVisibility(TextView.GONE);
        } else {
            socialSituationField.setText(socSit.toString());
        }

    }

    /**
     * This will load an image from Firebase Storage into an ImageView
     * @param imageView
     *      The destination ImageView
     * @param imagePath
     *      Path to the image in Firebase Storage
     */
    public void loadImageFirebase(ImageView imageView, String imagePath){
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        // Get the path to the image
        StorageReference imageRef = storageRef.child(imagePath);

        // Get the download URL for Glide
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext() /* context */)
                        .load(uri) // Load the image
                        .into(imageView); // Destination to load image into
            }
        });
    }

    @Override
    public void onMapFragmentReady() {
        //if the vibe has a location, show it on the map
        if (vibeEvent.getLatitude() != 0 && vibeEvent.getLongitude() != 0) {
            mapFragment.setToLocation(new LatLng(vibeEvent.getLatitude(), vibeEvent.getLongitude()));
        } else {
            mapFragment.hideMap();
        }
    }
}
