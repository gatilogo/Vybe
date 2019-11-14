package com.example.vybe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.vybe.util.APIKey;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

public class LocationSelectionDialog extends DialogFragment {

    private static final String TAG = "LocationSelector";

    private Button openLocationAutofill;
    private Button useCurrentLocation;
    private Button exitButton;
    private Button confirmButton;
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the SDK
        Places.initialize(getContext(), APIKey.API_KEY);

        // Create a new Places client instance
        PlacesClient placesClient = Places.createClient(getContext());

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_location_selector, null);

        openLocationAutofill = view.findViewById(R.id.btn_find_location);
        useCurrentLocation = view.findViewById(R.id.btn_current_location);
        exitButton = view.findViewById(R.id.btn_back);
        confirmButton = view.findViewById(R.id.btn_confirm);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final AlertDialog dialog = builder
                .setCustomTitle(customTitle())
                .setView(view)
                .create();

        openLocationAutofill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int AUTOCOMPLETE_REQUEST_CODE = 1;

                // Set the fields to specify which types of place data to
                // return after the user has made a selection.
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

                // Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(getContext());
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

            }
        });

        useCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pass
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pass
            }
        });
        
        return dialog;
    }

    public interface OnFragmentInteractionListener {
        void onOkPressed();
    }

    private TextView customTitle() {
        TextView title = new TextView(getContext());
        title.setText("Add a Location");
        title.setGravity(Gravity.CENTER);
        title.setTextColor(getResources().getColor(R.color.colorPrimary));
        title.setTextSize(22);

        return title;
    }
}
