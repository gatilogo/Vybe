package com.example.vybe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SocialActivity extends AppCompatActivity {

    private static final String TAG = "SocialActivity";

    private Button searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);
        Log.d(TAG, "onCreate: In social");

        searchBtn = findViewById(R.id.search_btn);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SocialActivity.this, SearchProfilesActivity.class));
            }
        });
    }
}
