package com.example.vybe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

public class SearchProfilesActivity extends AppCompatActivity {

    private static final String TAG = "SearchProfilesActivity";

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_profiles);
        Log.d(TAG, "onCreate: In search profiles");

        searchView = findViewById(R.id.search_view);
    }
}
