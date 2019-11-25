package com.example.vybe.AddEdit;

import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.vybe.Models.SocSit;
import com.example.vybe.Models.Vibe;
import com.example.vybe.Models.VibeEvent;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
        assert vibeEvent.getId() != null : "Editing a VibeEvent that doesn't have an ID";

        activity.setVibe(vibe);
        if (reason != null) activity.setReason(reason);
        if (socSit != null) activity.setSocSit(socSit);
    }

    public void addVibeEvent() {
        vibeEvent = new VibeEvent();

        String id = db.collection(vibeEventDBPath).document().getId();
        vibeEvent.setId(id);
    }
}