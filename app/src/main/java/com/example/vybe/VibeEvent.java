package com.example.vybe;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

public class VibeEvent implements Serializable {
    private Vibe vibe;
    private LocalDateTime dateTime;
    private String reason;
    private String socialSituation;

    public VibeEvent() {

    }

    public VibeEvent(Vibe vibe, LocalDateTime dateTime, String reason, String socialSituation) {
        this.vibe = vibe;
        this.dateTime = dateTime;
        this.reason = reason;
        this.socialSituation = socialSituation;
    }

    public Vibe getVibe() {
        return vibe;
    }

    public void setVibe(Vibe vibe) {
        this.vibe = vibe;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSocialSituation() {
        return socialSituation;
    }

    public void setSocialSituation(String socialSituation) {
        this.socialSituation = socialSituation;
    }
}
