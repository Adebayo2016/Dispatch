package com.example.dispatch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    final String MY_PREFS_NAME = "details";
    ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        progressBar = new ProgressDialog(this);
    }

    public void Login(View view) {
        TextInputLayout emailField = findViewById(R.id.et_email);
        TextInputLayout passwordField = findViewById(R.id.et_password);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        String email = emailField.getEditText().getText().toString();
        String password = passwordField.getEditText().getText().toString();

        if (email.isEmpty()) {
            emailField.requestFocus();
            emailField.setError("please enter your mail");
            return;
        }

        if (password.length() < 8) {
            passwordField.requestFocus();
            passwordField.setError("password must be at least 8 characters");
            return;
        }
        if ((!Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            emailField.requestFocus();
            emailField.setError("enter Valid email address");
            return;
        }

        Loading();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressBar.dismiss();
                SaveDetails();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                progressBar.dismiss();
                Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void SaveDetails() {
        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean("firstTimeUser", false);
//        editor.putInt("idName", 12);
        editor.apply();
    }

    private void Loading() {
        progressBar.setMessage("Please Wait...");
        progressBar.show();
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.setCancelable(false);
    }

    public void ForgotPassword(View view) {

    }
}