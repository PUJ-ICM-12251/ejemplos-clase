package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getName();
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    EditText emailEdit, passEdit;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEdit = findViewById(R.id.loginEmail);
        passEdit = findViewById(R.id.loginPass);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    private void updateUI(FirebaseUser currentUser) {
        if(currentUser != null) {
            Intent intent = new Intent(getBaseContext(), HomeActivity.class);
            intent.putExtra("user", currentUser.getEmail());
            startActivity(intent);
        } else {
            emailEdit.setText("");
            passEdit.setText("");
        }
    }


    private boolean validateForm() {
        boolean valid = true;
        String email = emailEdit.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailEdit.setError("Required");
            valid = false;
        } else {
            emailEdit.setError(null);
        }

        String password = passEdit.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passEdit.setError("Required");
            valid = false;
        } else {
            passEdit.setError(null);
        }
        return valid;
    }


    private void signInUser(String email, String password) {
        if (validateForm()) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI
                        Log.d(TAG, "signInWithEmail: Success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If Sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail: Failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                }
            });
        }
    }


    public static boolean isEmailValid(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }


    public void login(View view) {
        String email = emailEdit.getText().toString();
        String pass = passEdit.getText().toString();

        if (!isEmailValid(email)) {
            Toast.makeText(LoginActivity.this, "Email is not a valid format", Toast.LENGTH_SHORT).show();
            return;
        }

        signInUser(email, pass);
    }


    public void signUp(View view) {
        String email = emailEdit.getText().toString();
        String pass = passEdit.getText().toString();

        if (!isEmailValid(email)) {
            Toast.makeText(LoginActivity.this, "Email is not a valid format", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(LoginActivity.this, String.format("The user %s is successfully registered", user.getEmail()), Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    public void forgotPassword(View view) {
        String email = emailEdit.getText().toString();

        if (!isEmailValid(email)) {
            Toast.makeText(LoginActivity.this, "Email is not a valid format", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(LoginActivity.this, "Email instructions hace been sent, please check your email", Toast.LENGTH_SHORT).show();
            }
        });
    }


}