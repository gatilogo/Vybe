package com.example.vybe;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.vybe.Models.Vibe;

/**
 * Allows the user to filter the vibes they would like to view
 */
public class VibeFilterFragment extends Fragment {

    private Context context;
    private OnVibeSelectedListener onVibeSelectedListener;
    private Spinner vibeFilterDropdown;

    public interface OnVibeSelectedListener {
        void onVibeSelected(Vibe vibe);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;

        if (context instanceof OnVibeSelectedListener) {
            onVibeSelectedListener = (OnVibeSelectedListener) context;

        } else {
            throw new RuntimeException(context.toString() + " must implement OnVibeSelectedListener");

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vibe_filter, container, false);

        vibeFilterDropdown = view.findViewById(R.id.vibe_filter_dropdown);

        ArrayAdapter<String> vibesAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, Vibe.getNames());
        vibeFilterDropdown.setAdapter(vibesAdapter);
        vibeFilterDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Vibe selectedVibe = Vibe.values()[position];
                onVibeSelectedListener.onVibeSelected(selectedVibe);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        return view;
    }
}
