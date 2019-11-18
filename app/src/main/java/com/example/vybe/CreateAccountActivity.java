package com.example.vybe;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

/**
 * This activity gets the username and password and validates said fields
 * with Firestore. Still WIP
 */
public class CreateAccountActivity extends AppCompatActivity {

    private static final String TAG = "CreateAccountActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore db;

    private EditText usernameField, emailField, passwordField;
    private Button confirmBtn;

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
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        usernameField = findViewById(R.id.username_create);
        emailField = findViewById(R.id.email_create);
        passwordField = findViewById(R.id.password_create);
        confirmBtn = findViewById(R.id.confirm_button);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        mAuthListener = (@NonNull FirebaseAuth firebaseAuth) -> {
                FirebaseUser userFB = firebaseAuth.getCurrentUser();
                // User is logged in
                if (userFB != null) {
                    Intent intent = new Intent(CreateAccountActivity.this, MyVibesActivity.class);
                    startActivity(intent);
                }
        };

        confirmBtn.setOnClickListener((View view) -> {
                // Check all the fields are filled in for sign in
                if (!isEmpty(usernameField) && !isEmpty(emailField) && !isEmpty(passwordField)) {
                    String username = getTrimmedString(usernameField);
                    String email = getTrimmedString(emailField);
                    String password = getTrimmedString(passwordField);
                    // Check Firestore if username is already taken
                    db.collection("Users").whereEqualTo("username", username).get()
                        .addOnCompleteListener((@NonNull Task<QuerySnapshot> task) -> {
                                if (task.isSuccessful()) {
                                    if (task.getResult().isEmpty()) {
                                        // If empty, then the unique username is not taken
                                        createAccount(username, email, password);
                                    } else {
                                        // If the document exists, then the unique username already exists
                                        Toast.makeText(getApplicationContext(), "Username is already taken", Toast.LENGTH_LONG).show();
                                    }
                                }

                            }
                        );
                }

            }
        );
    }

    /**
     * Helper function to check if an edit text field is empty
     *
     * @param et EditText that we want to check
     * @return Returns true if the field is empty, false it is filled
     */
    public static boolean isEmpty(EditText et) {
        return et.getText().toString().trim().length() == 0;
    }

    /**
     * Helper function to get trimmed string from edit text field
     *
     * @param et EditText that we want to get the string from
     * @return Returns the string from the EditText trimmed
     */
    public static String getTrimmedString(EditText et) {
        return et.getText().toString().trim();
    }


    /**
     * This method  will create the account in Firebase Authentication as well as add it to Firestore if
     * Authentication does not fail
     * @param username
     *      The username to be added to Firestore
     * @param email
     *      The email to be added to Authentication & Firestore
     * @param password
     *      The password to added to Authentication
     */
    public void createAccount(String username, String email, String password){
        // Create the account using Firebase authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((@NonNull Task<AuthResult> task) -> {
                        // If the account was created successfully then add the
                        // profile information to Firestore
                        if (task.isSuccessful()) {
                            HashMap<String, Object> data = new HashMap<>();
                            data.put("username", username);
                            data.put("email", email);
                            db.collection("Users").document(mAuth.getCurrentUser().getUid()).set(data);
                            // Switch to My Vibes Acitivity
                            Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            // Notify user what caused the error
                            Toast.makeText(getApplicationContext(), "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                );
    }
}
