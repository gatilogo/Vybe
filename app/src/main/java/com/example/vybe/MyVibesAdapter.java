package com.example.vybe;

import android.content.Context;
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
    private TextView reasonField;
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
        reasonField = view.findViewById(R.id.my_reason_text_view);
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" d, YYYY h:mm a", Locale.ENGLISH);
        LocalDateTime dateTime = vibeEvent.getDateTime();
        String month = dateTime.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.ENGLISH);
        String dateFieldText = month + dateTime.format(formatter);
        dateField.setText(dateFieldText);
        vibeImage.setImageResource(vibeEvent.getVibe().getEmoticon());

        // TODO: DO WE WANT TO DISPLAY REASON?
        // KEN SAID ON FORUM DATETIME AND EMOTICON IS ENOUGH.
        if (vibeEvent.getReason() == null) {
            reasonField.setVisibility(View.GONE);
        } else {
            reasonField.setText(vibeEvent.getReason());
        }

    }
}
