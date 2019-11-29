package com.example.vybe;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * The Purpose of this controller is to take all the database shit from:
 * Main Activity and Create Account Activity
 * put it in here
 */
public class LoginController {
//    private static LoginController instance;
//
//    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
//    private Activity activity;
//
//    public interface loginControllerListener {
//
//
//    private LoginController() {
//    }
//
//    public static LoginController getInstance(Activity activity) {
//        if (instance == null)
//            instance = new LoginController();
//
//        return instance;
//    }
//
//    public void login(String email, String password) {
//        // Sign in using Firebase
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener((@NonNull Task<AuthResult> task) -> {
//                            // If the task succeed, login passed and go to My Vibes Activity
//                            if (task.isSuccessful()) {
//                                Intent intent = new Intent(MainActivity.this, MyVibesActivity.class);
//                                startActivity(intent);
//
//                            } else {
//                                Toast.makeText(getApplicationContext(), "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                            }
//                        }
//                );
//    }
//
//}
}
