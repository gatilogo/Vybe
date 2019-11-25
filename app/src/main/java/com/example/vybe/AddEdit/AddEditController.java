package com.example.vybe.AddEdit;

import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.vybe.Models.SocSit;
import com.example.vybe.Models.Vibe;
import com.example.vybe.Models.VibeEvent;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class AddEditController {
    private static AddEditController instance;
    private AddEditVibeEventActivity activity;

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
        Vibe vibe = vibeEvent.getVibe();
        String reason = vibeEvent.getReason();
        SocSit socSit = vibeEvent.getSocSit();

        activity.setVibe(vibe);
        if (reason != null) activity.setReason(reason);
        if (socSit != null) activity.setSocSit(socSit);
    }
}