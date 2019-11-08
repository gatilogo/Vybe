package com.example.vybe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

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
