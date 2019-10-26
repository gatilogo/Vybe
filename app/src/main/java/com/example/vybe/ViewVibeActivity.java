package com.example.vybe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ViewVibeActivity extends AppCompatActivity {

    private static final String TAG = "ViewVibeActivity";

    VibeEvent vibeEvent;

    private TextView dateField;
    private TextView reasonField;
    private TextView socialSituationField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_vibe);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();


        dateField = findViewById(R.id.view_date_text_view);
        reasonField = findViewById(R.id.view_reason_text_view);
        socialSituationField = findViewById(R.id.view_social_situation_text_view);

        if (extras.containsKey("vibeEvent")) {
            vibeEvent = (VibeEvent) extras.getSerializable("vibeEvent");


            dateField.setText(vibeEvent.getDateTime().toString());
            reasonField.setText(vibeEvent.getReason());
            socialSituationField.setText(vibeEvent.getSocialSituation());
        }

    }
}
