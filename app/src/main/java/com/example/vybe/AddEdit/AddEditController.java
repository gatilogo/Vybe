package com.example.vybe.AddEdit;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.vybe.Models.SocSit;
import com.example.vybe.Models.Vibe;
import com.example.vybe.Models.VibeEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

/**
 * This class sets initial Vibe Event information in the AddEditVibeEventActivity
 * and uploads and saves the new Vibe Event information to Firestore
 * This class also implements the singleton pattern to ensure there is only
 * ever one instance of the class throughout application use
 */
public class AddEditController {
    private static final String TAG = "AddEditController";
    private static AddEditController instance;

    private AddEditVibeEventActivity activity;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    private String vibeEventDBPath;
    private VibeEvent vibeEvent;

    private AddEditController() {
    }

    private void setActivity(AddEditVibeEventActivity activity) {
        vibeEventDBPath = "Users/" + mAuth.getCurrentUser().getUid() + "/VibeEvents";
        this.activity = activity;
    }

    public static AddEditController getInstance(AddEditVibeEventActivity activity) {
        if (instance == null)
            instance = new AddEditController();


        instance.setActivity(activity);
        return instance;
    }

    /**
     * This method takes in a VibeEvent and uses it to set initial
     * fields in the AddEditVibeEventActivity
     * @param vibeEvent the Vibe Event to edit/set values for
     */
    public void editVibeEvent(VibeEvent vibeEvent) {
        this.vibeEvent = vibeEvent;
        Vibe vibe = vibeEvent.getVibe();
        String reason = vibeEvent.getReason();
        SocSit socSit = vibeEvent.getSocSit();
        String imagePath = vibeEvent.getImage();
        Double latitude = vibeEvent.getLatitude();
        Double longitude = vibeEvent.getLongitude();
        assert vibeEvent.getID() != null : "Editing a VibeEvent that doesn't have an ID";

        activity.setVibe(vibe);
        if (reason != null) activity.setReason(reason);
        if (socSit != null) activity.setSocSit(socSit);
        if (imagePath != null) {
            loadAndSetImage(imagePath);
        }
    }

    /**
     * This method creates a new vibe event and initializes it
     * in Firestore by assigning an ID
     */
    public void addVibeEvent() {
        vibeEvent = new VibeEvent();

        String id = db.collection(vibeEventDBPath).document().getId();
        vibeEvent.setID(id);
    }

    /**
     * This method saves the Vibe Event using all user inputs from
     * editText fields
     * @param vibe
     * @param reason
     * @param socSit
     * @param image
     * @param latitude
     * @param longitude
     */
    public void saveVibeEvent(Vibe vibe, String reason, SocSit socSit, Bitmap image, Double latitude, Double longitude) {
        vibeEvent.setOwner(mAuth.getCurrentUser().getDisplayName());
        vibeEvent.setVibe(vibe.toString());
        vibeEvent.setReason(reason);

        if (socSit != null)
            vibeEvent.setSocSit(socSit.toString());
        else
            vibeEvent.setSocSit(null);

        String imagePath = vibeEvent.getImage();
        if (imagePath != null)
            deleteImageFromDB(imagePath);

        if (image != null)
            vibeEvent.setImage(uploadImage(image));
        else
            vibeEvent.setImage(null);

        vibeEvent.setLatitude(latitude);
        vibeEvent.setLongitude(longitude);

        uploadVibeEvent();
    }

    /**
     * This method initializes the Map Fragment nested inside the
     * AddEditVibeEvent activity page, if the Vibe Event that the user
     * is modifying has a location
     */
    public void onMapFragmentReady() {
        if (vibeEvent.getLatitude() != null && vibeEvent.getLongitude() != null) {
            activity.setLocation(vibeEvent.getLatitude(), vibeEvent.getLongitude());

        } else {
            activity.clearLocation();
        }
    }

    /**
     * This method loads and sets an image if available and/or part of the current
     * Vibe Event being added/edited
     * @param imagePath URL path to Firebase storage for images
     */
    private void loadAndSetImage(String imagePath) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child(imagePath);

        imageRef.getDownloadUrl().addOnSuccessListener((Uri uri) -> {
            Glide.with(activity)
                    .asBitmap()
                    .load(uri)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            activity.setImage(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });

        });
    }

    /**
     * This method takes in a bitmap of an image to return a
     * path to the image in Firestore
     * @param bitmap image to upload
     * @return image path/URL to image in Firestore
     */
    private String uploadImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArray = baos.toByteArray();

        String imagePath = "reasons/" + vibeEvent.getID() + ".jpg";
        StorageReference imageRef = storageRef.child(imagePath);

        UploadTask uploadTask = imageRef.putBytes(byteArray);
        uploadTask.addOnFailureListener((@NonNull Exception exception) -> {
            Toast.makeText(activity, "Image Upload Failed", Toast.LENGTH_LONG).show();

        }).addOnSuccessListener((UploadTask.TaskSnapshot taskSnapshot) -> {
            Toast.makeText(activity, "Image Upload Successful", Toast.LENGTH_LONG).show();
        });

        return imagePath;

    }

    /**
     * This method deletes an image from Firestore
     * @param imagePath takes in URL/image path to an image in Firestore
     */
    private void deleteImageFromDB(String imagePath) {
        StorageReference imageRef = storageRef.child(imagePath);
        imageRef.delete();
    }

    /**
     * This method uploads a Vibe Event to Firestoreß
     */
    private void uploadVibeEvent() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("ID", vibeEvent.getID());
        data.put("vibe", vibeEvent.getVibe());
        data.put("datetime", vibeEvent.getDateTime());
        data.put("reason", vibeEvent.getReason());
        data.put("socSit", vibeEvent.getSocSit());
        data.put("image", vibeEvent.getImage());
        data.put("latitude", vibeEvent.getLatitude());
        data.put("longitude", vibeEvent.getLongitude());
        data.put("owner", mAuth.getCurrentUser().getDisplayName());

        db.collection(vibeEventDBPath).document(vibeEvent.getID()).set(data);
    }
}