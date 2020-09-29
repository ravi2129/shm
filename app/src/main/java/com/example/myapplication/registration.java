package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registration extends AppCompatActivity {
    EditText txtUser, txtEmail, txtPassword, txtRetypePassword;
    private FirebaseDatabase database;
    private DatabaseReference rootRef, userRef, useridRef;
    private Button SignUp;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        txtUser = findViewById(R.id.rEmail);
        txtEmail = findViewById(R.id.cEmail);
        txtPassword=findViewById(R.id.rPass);
        txtRetypePassword=findViewById(R.id.cPass);
        SignUp = findViewById(R.id.Register);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        rootRef = database.getReference();
        userRef = rootRef.child("records");
        SignUp.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          final String username = txtUser.getText().toString().trim();
                                          String email = txtEmail.getText().toString().trim();
                                          String password = txtPassword.getText().toString().trim();
                                          String confirmPassword = txtRetypePassword.getText().toString().trim();
                                          if (TextUtils.isEmpty(username)) {
                                              Toast.makeText(registration.this, "Username is Empty", Toast.LENGTH_SHORT).show();
                                              return;
                                          }
                                          if (TextUtils.isEmpty(email)) {
                                              Toast.makeText(registration.this, "Email is Empty", Toast.LENGTH_SHORT).show();
                                              return;
                                          }
                                          if (TextUtils.isEmpty(password)) {
                                              Toast.makeText(registration.this, "You have to enter password First to register", Toast.LENGTH_SHORT).show();
                                              return;
                                          }
                                          if (TextUtils.isEmpty(confirmPassword)) {
                                              Toast.makeText(registration.this, "Confirm Password field is Empty", Toast.LENGTH_SHORT).show();
                                              return;
                                          }

                                          if (password.length() < 6) {
                                              Toast.makeText(registration.this, "Weak Password", Toast.LENGTH_SHORT).show();
                                          }


                                          if (password.equals(confirmPassword)) {
                                              firebaseAuth.createUserWithEmailAndPassword(email, password)
                                                      .addOnCompleteListener(registration.this, new OnCompleteListener<AuthResult>() {
                                                          @Override
                                                          public void onComplete(@NonNull Task<AuthResult> task) {

                                                              if (task.isSuccessful()) {
                                                                  firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                      @Override
                                                                      public void onComplete(@NonNull Task<Void> task) {
                                                                          if (task.isSuccessful()) {
                                                                              Toast.makeText(registration.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                                                              txtUser.setText("");
                                                                              txtEmail.setText("");
                                                                              txtPassword.setText("");
                                                                              txtRetypePassword.setText("");
                                                                              String userId = firebaseAuth.getCurrentUser().getUid();
                                                                              useridRef = userRef.child(userId);
                                                                              useridRef.child("username").child("username").setValue(username);
                                                                          } else {
                                                                              Toast.makeText(registration.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                          }
                                                                      }
                                                                  });
                                                                  startActivity(new Intent(registration.this, login.class));

                                                              } else {
                                                                  Toast.makeText(registration.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                              }

                                                              // ...
                                                          }
                                                      });

                                          }


                                      }

                                  }


        );


    }
}