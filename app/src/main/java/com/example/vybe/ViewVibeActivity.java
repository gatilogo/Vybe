package com.example.vybe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * This Activity displays the screen for a vibe event and all its available details
 */
public class ViewVibeActivity extends AppCompatActivity {

    private static final String TAG = "ViewVibeActivity";

    private VibeEvent vibeEvent;

    private ImageView vibeImage;
    private TextView dateField;
    private TextView reasonField;
    private TextView reasonLabel;
    private TextView socialSituationField;
    private TextView socialSituationLabel;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_vibe);
        Log.d(TAG, "onCreate: In View vibes");

        vibeImage = findViewById(R.id.view_vibe_image_view);
        dateField = findViewById(R.id.view_date_text_view);
        reasonField = findViewById(R.id.view_reason_text_view);
        reasonLabel = findViewById(R.id.view_reason_label);
        socialSituationField = findViewById(R.id.view_social_situation_text_view);
        socialSituationLabel = findViewById(R.id.view_social_situation_label);
        toolbar = findViewById(R.id.view_vibes_toolbar);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras.containsKey("vibeEvent")) {
            vibeEvent = (VibeEvent) extras.getSerializable("vibeEvent");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" d, YYYY h:mm a", Locale.ENGLISH);
            LocalDateTime dateTime = vibeEvent.getDateTime();
            String month = dateTime.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.ENGLISH);
            dateField.setText(month + dateTime.format(formatter));
            String reason = vibeEvent.getReason();
            String socialSituation = vibeEvent.getSocialSituation();
            // TODO: missing location - do that later once done
            // TODO: missing reason photo

            if (vibeEvent.getVibe() != null) {
                vibeImage.setImageResource(vibeEvent.getVibe().getEmoticon());
                toolbar.setBackgroundColor(getColor(vibeEvent.getVibe().getColor()));
            }

            if (reason == null || reason.equals("")) {  // Reason is optional
                reasonLabel.setVisibility(TextView.GONE);
                reasonField.setVisibility(TextView.GONE);
            } else {
                reasonField.setText(reason);
            }

            if (socialSituation == null || socialSituation.equals("")) { // Social Situation is optional
                socialSituationLabel.setVisibility(TextView.GONE);
                socialSituationField.setVisibility(TextView.GONE);
            } else {
                socialSituationField.setText(socialSituation);
            }
        }

    }
}
