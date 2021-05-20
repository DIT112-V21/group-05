package com.example.androidappcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText fullName,email,password;
    Button registerBtn,goToLogin;
    CheckBox isStaffBox, isPatientBox;
    boolean valid = true;

    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullName = findViewById(R.id.registerName);
        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);
        registerBtn = findViewById(R.id.registerBtn);
        goToLogin = findViewById(R.id.gotoLogin);

        isStaffBox = findViewById(R.id.isStaff);
        isPatientBox = findViewById(R.id.isPatient);

        isPatientBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isChecked()) {
                isStaffBox.setChecked(false);
            }
        });

        isStaffBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isChecked()) {
                isPatientBox.setChecked(false);
            }
        });

        registerBtn.setOnClickListener(v -> {
            checkField(fullName);
            checkField(email);
            checkField(password);

            if (!(isStaffBox.isChecked() || isPatientBox.isChecked())) {
                Toast.makeText(RegisterActivity.this, "You have to choose a user type", Toast.LENGTH_SHORT).show();
                return;
            }

            if(valid) {
                fAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(authResult -> {
                    FirebaseUser user = fAuth.getCurrentUser();
                    Toast.makeText(RegisterActivity.this,"Account is created successfully",Toast.LENGTH_SHORT).show();
                    DocumentReference df = fStore.collection("Users").document(user.getUid());
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("FullName", fullName.getText().toString());
                    userInfo.put("Email", email.getText().toString());

                    if (isStaffBox.isChecked()) {
                        userInfo.put("isStaff", "1");
                    } else {
                        userInfo.put("isPatient", "1");
                    }

                    df.set(userInfo);

                    if (isStaffBox.isChecked()) {
                        startActivity(new Intent(getApplicationContext(), StaffMainActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(getApplicationContext(), PatientMainActivity.class));
                        finish();
                    }

                }).addOnFailureListener(e -> Toast.makeText(RegisterActivity.this,"Failed to create account",Toast.LENGTH_SHORT).show());
            }
        });

        goToLogin.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
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
}