package com.example.vybe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

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
        TextView socialSituationField = view.findViewById(R.id.my_social_situation_text_view);

        VibeEvent vibeEvent = vibeEventList.get(position);

        dateField.setText(vibeEvent.getDateTime().toString());
        reasonField.setText(vibeEvent.getReason());
        socialSituationField.setText(vibeEvent.getSocialSituation());

        return view;
    }
}
