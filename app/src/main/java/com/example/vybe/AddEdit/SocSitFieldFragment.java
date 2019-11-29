package com.example.vybe.AddEdit;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.vybe.Models.SocSit;
import com.example.vybe.R;

import java.util.List;

/**
 * This fragment represents a custom spinner to allow a user to select
 * a social situation to add to a Vibe Event
 */
public class SocSitFieldFragment extends Fragment {

    private OnSocSitSelectedListener onSocSitSelectedListener;
    private Context context;
    private Spinner socSitDropdown;
    private ImageButton clearBtn;

    interface OnSocSitSelectedListener {
        void onSocSitSelected(SocSit socSit);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        Activity activity = (Activity) context;
        onSocSitSelectedListener = (OnSocSitSelectedListener) activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_social_situation_field, container, false);

        socSitDropdown = view.findViewById(R.id.soc_sit_dropdown);
        clearBtn = view.findViewById(R.id.clear_btn);

        ArrayAdapter<String> socSitAdapter = createSocSitAdapter();
        socSitDropdown.setAdapter(socSitAdapter);

        int hintPosition = socSitAdapter.getCount();
        socSitDropdown.setSelection(hintPosition);

        socSitDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if (position != hintPosition) {
                    onSocSitSelectedListener.onSocSitSelected(SocSit.at(position));

                } else {
                    onSocSitSelectedListener.onSocSitSelected(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        clearBtn.setOnClickListener((View v) -> {
            socSitDropdown.setSelection(hintPosition);
        });

        return view;
    }


    /**
     * This method initializes the social situation adapter which will display
     * a spinner/list for selecting a type of social situation
     * @return returns a custom adapter
     */
    private ArrayAdapter<String> createSocSitAdapter() {
        // List of all the social situation options
        List<String> options = SocSit.stringValues();

        // Add hint to the end of the options list
        options.add("Select a Social Situation");

        // return an array adapter that doesn't show the last option (aka the hint)
        return new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, options) {
            // Don't show the last item - don't show the hint
            @Override
            public int getCount() {
                int count = super.getCount();
                return count > 0 ? count - 1 : count;
            }
        };
    }

    /**
     * Set the default Social Situation.
     * @param socSit
     */
    public void setDefaultSocSit(SocSit socSit) {
        if (socSit != null) {
            int position = socSit.ordinal();
            socSitDropdown.setSelection(position);
        }

    }
}
