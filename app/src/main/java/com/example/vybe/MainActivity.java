package com.example.vybe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

    Button loginButton;
    Button signupButton;
    EditText usernameField;
    EditText emailField;
    EditText passwordField;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.login_button);
        signupButton = findViewById(R.id.signup_button);
        usernameField = findViewById(R.id.username_edit_text);
        emailField = findViewById(R.id.email_edit_text);
        passwordField = findViewById(R.id.password_edit_text);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                // User is not logged in
                if (user == null){
                    Toast.makeText(getApplicationContext(), "Aight imma head out", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Aight imma stay in", Toast.LENGTH_LONG).show();
                }
            }
        };

        loginButton.setOnClickListener(view -> {
            if (!isEmpty(emailField) && !isEmpty(passwordField)){

                String email = getTrimmedString(emailField);
                String password = getTrimmedString(passwordField);

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
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
            if (!isEmpty(usernameField) && !isEmpty(emailField) && !isEmpty(passwordField)){
                String username = getTrimmedString(usernameField);

                db.collection("Users").whereEqualTo("username", username).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                QuerySnapshot document = task.getResult();
                                if (document.isEmpty()){
                                    String email = getTrimmedString(emailField);
                                    String password = getTrimmedString(passwordField);

                                    mAuth.createUserWithEmailAndPassword(email, password)
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()){
                                                        HashMap<String, Object> data = new HashMap<>();
                                                        data.put("username", username);
                                                        data.put("email", email);
                                                        db.collection("Users").add(data);
                                                        Intent intent = new Intent(MainActivity.this, MyVibesActivity.class);
                                                        startActivity(intent);
                                                    }
                                                    else {
                                                        Toast.makeText(getApplicationContext(), "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                } else{
                                    Toast.makeText(getApplicationContext(), "Username is already taken", Toast.LENGTH_LONG).show();
                                }

                            }
                        }
                    });
            }

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private boolean isEmpty(EditText et){
        return et.getText().toString().trim().length() == 0;
    }

    private String getTrimmedString(EditText et){
        return et.getText().toString().trim();
    }
}
