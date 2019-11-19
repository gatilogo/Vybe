package com.example.vybe;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.vybe.Models.VibeEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * MyVibesAdapter is a CustomList used for the ListAdapter in the main activity that serves as the
 * list for storing vibe events
 */
public class MyVibesAdapter extends ArrayAdapter<VibeEvent> {

    private static final String TAG = "MyVibesAdapter";
    private Context context;
    private ArrayList<VibeEvent> vibeEventList;
    private int resource;
    private TextView dateField;
    private TextView vibeNameField;
    private ImageView vibeImage;

    public MyVibesAdapter(@NonNull Context context, int resource, ArrayList<VibeEvent> vibeEventList) {
        super(context, resource, vibeEventList);
        this.resource = resource;
        this.context = context;
        this.vibeEventList = vibeEventList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater
                    .from(this.context)
                    .inflate(this.resource, parent, false);
        }

        dateField = view.findViewById(R.id.view_date_text_view);
        vibeNameField = view.findViewById(R.id.vibe_name_text_view);
        vibeImage = view.findViewById(R.id.image_view);

        VibeEvent vibeEvent = vibeEventList.get(position);
        populateVibeAdapterFields(vibeEvent);

        return view;
    }

    /**
     * This will populate the appropriate fields for displaying Vibe
     * Event details on the VibesAdapter view
     * @param vibeEvent
     *      The Vibe Event object containing details to be displayed
     */
    // TODO:
    // this can be moved to a controller class and could potentially be used in common with
    // the ViewVibeActivity method: populateVibeEventDetails
    public void populateVibeAdapterFields(VibeEvent vibeEvent) {
        String datetimeText = vibeEvent.getDateTimeString();
        dateField.setText(datetimeText);
        vibeImage.setImageResource(vibeEvent.getVibe().getEmoticon());
        vibeNameField.setText(vibeEvent.getVibe().getName());
    }
}
