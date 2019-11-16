package com.example.vybe.AddEdit;

import android.app.Activity;
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

import com.example.vybe.R;

public class VibeFieldFragment extends Fragment {

    private static final String TAG = "VibeFieldFragment";

    private Spinner vibeDropdown;
    private Context context;
    private String[] vibes;
    private VibeSelectedListener vibeSelectedListener;

    interface VibeSelectedListener {
        void onVibeSelected(String selectedVibe);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        Activity activity = (Activity) context;
        vibeSelectedListener = (VibeSelectedListener) activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vibe_field_fragment, container, false);

        vibeDropdown = view.findViewById(R.id.vibe_dropdown);

        vibes = new String[]{"Select a vibe", "angry", "disgusted", "happy", "sad", "scared", "surprised"};
        ArrayAdapter<String> vibesAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, vibes);
        vibeDropdown.setAdapter(vibesAdapter);
        vibeDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                vibeSelectedListener.onVibeSelected(vibes[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        return view;
    }

    public void setDefaultVibe(Bundle args) {
        if (args.containsKey("vibe")) {
            String defaultVibe = (String) args.getSerializable("vibe");

            Integer idx = getVibeIndex(defaultVibe);
            if (idx != null) {
                vibeDropdown.setSelection(idx);
            }

        }
    }

    private Integer getVibeIndex(String vibe) {
        for (int i = 0; i < vibes.length; ++i) {
            if (vibes[i].equals(vibe)) {
                return i;
            }
        }

        return null;
    }
}
