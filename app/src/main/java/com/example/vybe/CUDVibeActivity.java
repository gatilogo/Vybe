package com.example.vybe;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;

public class CUDVibeActivity extends AppCompatActivity {

    private static final String TAG = "CUDVibeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cudvibe);

        Log.d(TAG, "onCreate: In CUD vibes");
    }
}
