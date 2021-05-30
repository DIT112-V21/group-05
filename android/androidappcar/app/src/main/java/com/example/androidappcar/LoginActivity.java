package com.example.androidappcar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button loginBtn, gotoRegister;
    boolean valid = true;

    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseFirestore  fStore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.loginBtn);
        gotoRegister = findViewById(R.id.gotoRegister);

        loginBtn.setOnClickListener(v -> {
            checkField(email);
            checkField(password);

            if (valid) {
                fAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(authResult -> {
                    Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                    checkUserAccessLevel(authResult.getUser().getUid());
                }).addOnFailureListener(e -> {
                    Toast.makeText(LoginActivity.this, "Failed to log in", Toast.LENGTH_SHORT).show();
                });
            }
        });

        gotoRegister.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            finish();
        });
    }

    private void checkUserAccessLevel(String uid) {
        DocumentReference df = fStore.collection("Users").document(uid);

        df.get().addOnSuccessListener(documentSnapshot -> {
            Log.d("TAG", "onSuccess" + documentSnapshot.getData());

            if (documentSnapshot.getString("isStaff") != null) {
                Toast.makeText(LoginActivity.this, "Welcome back " + documentSnapshot.getString("FullName"), Toast.LENGTH_SHORT).show();

                startActivity(new Intent(getApplicationContext(), RegisterDeliveryActivity.class));
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Welcome back " + documentSnapshot.getString("FullName"), Toast.LENGTH_SHORT).show();

                startActivity(new Intent(getApplicationContext(), DeliveryActivity.class));
                finish();
            }
        });
    }

    public boolean checkField(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }else {
            valid = true;
        }

        return valid;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (fAuth.getCurrentUser() != null) {
            DocumentReference df = fStore.collection("Users").document(fAuth.getCurrentUser().getUid());

            df.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.getString("isStaff") != null) {
                    Toast.makeText(LoginActivity.this, "Welcome back " + documentSnapshot.getString("FullName"), Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(), RegisterDeliveryActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Welcome back " + documentSnapshot.getString("FullName"), Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(), DeliveryActivity.class));
                    finish();
                }
            }).addOnFailureListener(e -> {
                fAuth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            });
        }
    }
}