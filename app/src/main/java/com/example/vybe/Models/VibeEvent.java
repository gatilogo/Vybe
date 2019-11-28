package com.example.vybe.Models;

import android.icu.text.SimpleDateFormat;

import java.io.Serializable;
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
    private SocSit socSit;
    private String id;
    private String image;
    private Double latitude;
    private Double longitude;
    private String owner = "";

    /**
     * Constructor called for serialization. Always requires
     * to have the current date and time initialized.
     */
    public VibeEvent() {

        this.datetime = new Date();
    }

    /**
     * Default constructor for VibeEvent with provided vibe event parameters
     * @param vibe
     *      This is the name of the user selected vibe
     * @param datetime
     *      This is the timestamp in which a vibe event occurs/occurred
     * @param reason
     *      This is the reason a vibe event occurred
     * @param socSit
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

    public VibeEvent(Vibe vibe, Date datetime, String reason, SocSit socSit, String id, String image, Double latitude, Double longitude) {
        this.vibe = vibe;
        this.datetime = datetime;
        this.reason = reason;
        this.socSit = socSit;
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
     * @param name The string representation of a vibe
     */
    public void setVibe(String name) {
        this.vibe = Vibe.ofName(name);
    }

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
    public SocSit getSocSit() {
        return socSit;
    }

    /**
     * This sets the social situation of the VibeEvent
     * @param socSit The social situation in which the event occurred
     */
    public void setSocSit(String socSit) {
        this.socSit = SocSit.of(socSit);
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getOwner() { return owner; }

    public void setOwner(String owner) { this.owner = owner; }
}
