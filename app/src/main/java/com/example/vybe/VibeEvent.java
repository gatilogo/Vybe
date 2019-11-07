package com.example.vybe;

import com.example.vybe.vibefactory.Vibe;
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
    private String id;

    public VibeEvent() {

    }

    public VibeEvent(String vibe, LocalDateTime dateTime, String reason, String socialSituation) {
        this.vibe = VibeFactory.getVibe(vibe);
        this.dateTime = dateTime;
        this.reason = reason;
        this.socialSituation = socialSituation;
    }

    public Vibe getVibe() {
        return vibe;
    }

    public void setVibe(String vibe) {
        this.vibe = VibeFactory.getVibe(vibe);
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

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public Date getDateTimeFormat(){
        long seconds = this.dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        Date date = new Date(seconds);
        return date;
    }
}
