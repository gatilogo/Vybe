package com.example.vybe.AddEdit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.vybe.LocationController;
import com.example.vybe.R;
import com.example.vybe.util.APIKey;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

import static com.example.vybe.util.Constants.*;

/**
 * This dialog allows the user to select a location
 * to be added to the vibe
 */
public class LocationSelectionDialog extends DialogFragment {

    private static final String TAG = "LocationSelector";

    private OnLocationSelectedListener onLocationSelectedListener;
    private Button openLocationAutofill;
    private Button useCurrentLocation;
    private View view;
    private AlertDialog dialog;

    public interface OnLocationSelectedListener {
        void onLocationSelected(double latitude, double longitude);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        Activity activity = (Activity) context;
        onLocationSelectedListener = (OnLocationSelectedListener) activity;

    }

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

        //create dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder
                .setCustomTitle(customTitle())
                .setView(view)
                .create();

        //opens the find location fragment
        openLocationAutofill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = buildAutocompleteIntent();
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });


        //sets location to current location
        useCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location location = LocationController.getUserLocation(getContext());
                if (location == null) {
                    Toast.makeText(getContext(), "We were unable to retrieve your location", Toast.LENGTH_SHORT);
                    return;
                }
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                onLocationSelectedListener.onLocationSelected(latitude, longitude);
                dialog.dismiss();

            }
        });


        return dialog;
    }

    /**
     * Creates custom title for the dialog
     * @return
     */
    private TextView customTitle() {
        TextView title = new TextView(getContext());
        title.setText("Add a Location");
        title.setGravity(Gravity.CENTER);
        title.setTextColor(getResources().getColor(R.color.colorPrimary));
        title.setTextSize(22);

        return title;
    }

    /**
     * Checks that the autocomplete fragment worked properly
     * @param requestCode
     * activity that requested this method
     * @param resultCode
     * code that was returned after finishing
     * @param data
     * data retrieved from the activity
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: result retrieved " + resultCode);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                LatLng coordinates = place.getLatLng();
                onLocationSelectedListener.onLocationSelected(coordinates.latitude, coordinates.longitude);
                dialog.dismiss();
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            }
        }
    }

    /**
     * builds the google autocomplete fragment
     * @return
     * the intent of the autocomplete fragment to be started
     */
    private Intent buildAutocompleteIntent() {
        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

        //get location for bias area
        Location currentLoc = LocationController.getUserLocation(getContext());
        if (currentLoc != null) {
            //if user has location, add bias
            Log.d(TAG, "onClick: user has location, adding search bias");
            double currentLat = currentLoc.getLatitude();
            double currentLng = currentLoc.getLongitude();
            double biasOffset = 0.1;

            // Start the autocomplete intent with bias
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.OVERLAY, fields)
                    .setLocationBias(RectangularBounds.newInstance(
                            new LatLng(currentLat - biasOffset, currentLng - biasOffset),
                            new LatLng(currentLat + biasOffset, currentLng + biasOffset)))
                    .build(getContext());
            return intent;
        }

        // Intent without bias
        Log.d(TAG, "onClick: location not found, no bias added");
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields)
                .build(getContext());
        return intent;
    }
}
