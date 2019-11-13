package com.example.vybe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This Activity displays the screen for a user to log in. This file will be updated
 * to reflect the above in the future
 */
public class MainActivity extends AppCompatActivity {

    Button loginButton;
    Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.login_button);
        signupButton = findViewById(R.id.signup_button);

        loginButton.setOnClickListener(view -> {

            Intent intent = new Intent(MainActivity.this, MyVibesActivity.class);
            startActivity(intent);
        });

        signupButton.setOnClickListener(view -> {

            Intent intent = new Intent(MainActivity.this, MyVibesActivity.class);
            startActivity(intent);
        });
    }
}
