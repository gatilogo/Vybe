package com.example.vybe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

/**
 * This Activity displays the screen for a user to log in. This file will be updated
 * to reflect the above in the future
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    Button loginButton;
    Button signupButton;
    EditText emailField;
    EditText passwordField;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.login_button);
        signupButton = findViewById(R.id.signup_button);
        emailField = findViewById(R.id.email_edit_text);
        passwordField = findViewById(R.id.password_edit_text);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                // User is logged in
                if (user != null){
                    Intent intent = new Intent(MainActivity.this, MyVibesActivity.class);
                    startActivity(intent);
                }
            }
        };

        loginButton.setOnClickListener(view -> {
            // Check that the email and password fields are entered
            if (!isEmpty(emailField) && !isEmpty(passwordField)){
                // Get the credentials that were entered
                User user = new User(getTrimmedString(emailField));
                String password = getTrimmedString(passwordField);
                // Sign in using Firebase
                mAuth.signInWithEmailAndPassword(user.getEmail(), password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If the task succeed, login passed and go to My Vibes Activity
                        if (task.isSuccessful()){
                            Intent intent = new Intent(MainActivity.this, MyVibesActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

        });

        signupButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
            startActivity(intent);
        });
    }

    // Add the authentication listener on start
    // This can be used that if the user is already logged in then to redirect to My Vibes Activity
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    // Remove the listener on stop as we don't want to redirect? not sure
    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /**
     * Helper function to check if an edit text field is empty
     * @param editText
     *      EditText that we want to check
     * @return Returns true if the field is empty, false it is filled
     */
    public static boolean isEmpty(EditText editText){
        return editText.getText().toString().trim().length() == 0;
    }

    /**
     * Helper function to get trimmed string from edit text field
     * @param editText
     *      EditText that we want to get the string from
     * @return Returns the string from the EditText trimmed
     */
    public static String getTrimmedString(EditText editText){
        return editText.getText().toString().trim();
    }
}
