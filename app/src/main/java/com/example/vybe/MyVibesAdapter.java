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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public class MyVibesAdapter extends ArrayAdapter<VibeEvent> {

    private static final String TAG = "MyVibesAdapter";
    private Context context;
    private ArrayList<VibeEvent> vibeEventList;
    private int resource;

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

        TextView dateField = view.findViewById(R.id.view_date_text_view);
        TextView reasonField = view.findViewById(R.id.my_reason_text_view);
        ImageView vibeImage = view.findViewById(R.id.imageView);

        VibeEvent vibeEvent = vibeEventList.get(position);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" MM, YYYY h:mm a", Locale.ENGLISH);
        LocalDateTime dateTime = vibeEvent.getDateTime();
        String month = dateTime.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.ENGLISH);

        dateField.setText(month + dateTime.format(formatter));

        if (vibeEvent.getVibe() != null) {
            vibeImage.setImageResource(vibeEvent.getVibe().getEmoticon());
        } else {
            vibeImage.setImageResource(R.drawable.ic_vibeless);
        }

        if (vibeEvent.getReason() == null) {
            reasonField.setVisibility(View.GONE);
        } else {
            reasonField.setText(vibeEvent.getReason());
        }

        return view;
    }
}
