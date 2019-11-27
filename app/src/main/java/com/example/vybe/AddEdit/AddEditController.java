package com.example.vybe.AddEdit;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.vybe.Models.SocSit;
import com.example.vybe.Models.Vibe;
import com.example.vybe.Models.VibeEvent;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class AddEditController {
    private static AddEditController instance;

    private AddEditVibeEventActivity activity;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String vibeEventDBPath = "Users/" + mAuth.getCurrentUser().getUid() + "/VibeEvents";
    private VibeEvent vibeEvent;

    private AddEditController() {
    }

    private void setActivity(AddEditVibeEventActivity activity) {
        this.activity = activity;
    }

    public static AddEditController getInstance(AddEditVibeEventActivity activity) {
        if (instance == null)
            instance = new AddEditController();

        instance.setActivity(activity);
        return instance;
    }

    public void editVibeEvent(VibeEvent vibeEvent) {
        this.vibeEvent = vibeEvent;
        Vibe vibe = vibeEvent.getVibe();
        String reason = vibeEvent.getReason();
        SocSit socSit = vibeEvent.getSocSit();
        String imagePath = vibeEvent.getImage();
        Double latitude = vibeEvent.getLatitude();
        Double longitude = vibeEvent.getLongitude();
        assert vibeEvent.getId() != null : "Editing a VibeEvent that doesn't have an ID";

        activity.setVibe(vibe);
        if (reason != null) activity.setReason(reason);
        if (socSit != null) activity.setSocSit(socSit);
        if (latitude == null && longitude == null) {
            activity.hideDeleteLocationBtn();
        }
        if (imagePath != null) {
            loadAndSetImage(imagePath);
        }
    }

    public void addVibeEvent() {
        vibeEvent = new VibeEvent();

        String id = db.collection(vibeEventDBPath).document().getId();
        vibeEvent.setId(id);

        activity.hideDeleteLocationBtn();
    }

    public void saveVibeEvent(Vibe vibe, String reason, SocSit socSit, Bitmap image, Double latitude, Double longitude) {
        vibeEvent.setVibe(vibe.toString());
        vibeEvent.setReason(reason);
        vibeEvent.setSocSit(socSit);
        if (image != null) {
            vibeEvent.setImage(uploadImage(image));
        } else {
            vibeEvent.setImage(null);
        }
        vibeEvent.setLatitude(latitude);
        vibeEvent.setLongitude(longitude);

        uploadVibeEvent();
    }

    public void onMapFragmentReady() {
        if (vibeEvent.getLatitude() != null && vibeEvent.getLongitude() != null) {
            activity.setLocation(vibeEvent.getLatitude(), vibeEvent.getLongitude());

        } else {
            activity.hideMap();
        }
    }

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

    private String uploadImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArray = baos.toByteArray();

        String imagePath = "reasons/" + vibeEvent.getId() + ".jpg";
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child(imagePath);

        UploadTask uploadTask = imageRef.putBytes(byteArray);
        uploadTask.addOnFailureListener((@NonNull Exception exception) -> {
            Toast.makeText(activity, "Image Upload Failed", Toast.LENGTH_LONG).show();

        }).addOnSuccessListener((UploadTask.TaskSnapshot taskSnapshot) -> {
            Toast.makeText(activity, "Image Upload Successful", Toast.LENGTH_LONG).show();
        });

        return imagePath;

    }

    private void uploadVibeEvent() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("ID", vibeEvent.getId());
        data.put("vibe", vibeEvent.getVibe());
        data.put("datetime", vibeEvent.getDateTime());
        data.put("reason", vibeEvent.getReason());
        data.put("socSit", vibeEvent.getSocSit());
        data.put("image", vibeEvent.getImage());
        data.put("latitude", vibeEvent.getLatitude());
        data.put("longitude", vibeEvent.getLongitude());

        db.collection(vibeEventDBPath).document(vibeEvent.getId()).set(data);
    }
}