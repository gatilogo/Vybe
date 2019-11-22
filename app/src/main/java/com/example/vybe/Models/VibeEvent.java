package com.example.vybe.Models;

import android.icu.text.SimpleDateFormat;

import com.example.vybe.Models.vibefactory.Vibe;
import com.example.vybe.Models.vibefactory.VibeFactory;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * A class that implements Serializable and keeps track of vibe events. A vibe event is an event
 * triggered by a particular vibe in the form of a {@link Vibe} at a particular Date and Time,
 * in the form of a {@link java.time.LocalDate} object. Every vibe event is kept track of through
 * a unique ID which is passed into and from the FireStore database and also optionally
 * keeps track of other details including a reasoning (in the form of text or a photograph) for
 * the event, and the social situation in which the event occurred in.
 */
public class VibeEvent implements Serializable {
    private Vibe vibe;
    private Date datetime;
    private String reason;
    private String socialSituation;
    private String id;
    private String image;
    private double latitude;
    private double longitude;

    /**
     * Constructor called for serialization. Always requires
     * to have the current date and time initialized.
     */
    public VibeEvent() {

        this.datetime = new Date();
        this.latitude = 0;
        this.longitude = 0;
    }

    /**
     * Default constructor for VibeEvent with provided vibe event parameters
     * @param vibe
     *      This is the name of the user selected vibe
     * @param datetime
     *      This is the timestamp in which a vibe event occurs/occurred
     * @param reason
     *      This is the reason a vibe event occurred
     * @param socialSituation
     *      This is the social situation in which a vibe event occurred
     * @param id
     *      This is the unique identifier for a particular instance of a vibe event
     * @param image
     *      This is a photograph expressing the reason a vibe event occurred
     * @param latitude
     *      This is the latitude coordinate of where a vibe event occurred
     * @param longitude
     *      This is the longitude coordinate of where a vibe event occurred
     */
    public VibeEvent(String vibe, Date datetime, String reason, String socialSituation, String id, String image, double latitude, double longitude) {
        this.vibe = VibeFactory.getVibe(vibe);
        this.datetime = datetime;
        this.reason = reason;
        this.socialSituation = socialSituation;
        this.id = id;
        this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * This gets the vibe of the VibeEvent
     * @return The vibe
     */
    public Vibe getVibe() {
        return vibe;
    }

    /**
     * This sets the vibe of a VibeEvent
     * @param vibeString The string representation of a vibe
     */
    public void setVibe(String vibeString) {
        this.vibe = VibeFactory.getVibe(vibeString);
    }
    
//    /**
//     * This sets the vibe of a VibeEvent
//     * @param vibeEmoticon The integer representing the emoticon of a vibe
//     */
//    public void setVibe(int vibeEmoticon) {
//        this.vibe = VibeFactory.getVibe(vibeEmoticon);
//    }
//
//    /**
//     * This sets the vibe of a VibeEvent
//     * @param vibe The vibe to set
//     */
//    public void setVibe(Vibe vibe) {
//        this.vibe = vibe;
//    }

    /**
     * This gets the date and time of the VibeEvent
     * @return The date and time of the event as a LocalDateTime object
     */
    public Date getDateTime() {
        return datetime;
    }

    /**
     * This sets the date and time in which a VibeEvent occurred
     * @param datetime The date and time of the event
     */
    public void setDateTime(Date datetime) {
        this.datetime = datetime;
    }

    public String getDateTimeString() {
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy hh:mm a");
        Date datetime = this.getDateTime();
        return formatter.format(datetime);
    }

    /**
     * This gets the textual reason for the VibeEvent occurring
     * @return The reasoning for a vibe event
     */
    public String getReason() {
        return reason;
    }

    /**
     * This sets the textual reasoning behind a VibeEvent
     * @param reason The event reasoning
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * This gets the social situation of the VibeEvent
     * @return The social situation which a vibe event occurred
     */
    public String getSocialSituation() {
        return socialSituation;
    }

    /**
     * This sets the social situation of the VibeEvent
     * @param socialSituation The social situation in which the event occurred
     */
    public void setSocialSituation(String socialSituation) {
        this.socialSituation = socialSituation;
    }

    /**
     * This gets the unique ID for the VibeEvent
     * @return The unique ID for a vibe event
     */
    public String getId() { return id; }

    /**
     * This sets the unique ID for the VibeEvent
     * @param id The ID number of an event
     */
    public void setId(String id) { this.id = id; }

    /**
     * This gets the path to the image for the VibeEvent
     * @return The path to the image for the event
     */
    public String getImage() { return image; }

    /**
     * This sets the path to the image for the VibeEvent
     * @param image The path to the image for the event
     */
    public void setImage(String image) { this.image = image; }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
