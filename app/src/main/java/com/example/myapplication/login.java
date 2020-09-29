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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class login extends AppCompatActivity {
TextView SignUp;
    EditText txtEmail,txtPassword;
    Button btn_student_Login;
    private TextView Student_signup;
    private FirebaseAuth mAuth;
    private TextView forgotPassword;
    TextView ForgotPassword;
    private FirebaseDatabase database;
    private DatabaseReference rootRef, userRef, useridRef;
    private String userEmail, sameEmail, loginEmail;

    FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser != null) {
                String userid=firebaseUser.getUid();
                userEmail = firebaseUser.getEmail();
                userRef=rootRef.child("records");
                useridRef = userRef.child(userid);


                useridRef.child("username").child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        sameEmail = (String) dataSnapshot.getValue();
                        if (Objects.equals(userEmail, sameEmail)) {
                            Intent intent = new Intent(login.this,Dashboard.class);
                            startActivity(intent);

                            finish();
                        }
                    }



                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // error message
                    }
                });

            }
            else{
                Toast.makeText(login.this, "It Seems that You haven't logged in!", Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SignUp=findViewById(R.id.Sign_Up);
        txtEmail=(EditText) findViewById(R.id.Email);
        txtPassword=(EditText)findViewById(R.id.Pass);
        ForgotPassword=findViewById(R.id.ForgotPassword);
        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent fpass=new Intent(login.this,ForgotPassword.class);
             startActivity(fpass);
            }
        });
        btn_student_Login   = (Button) findViewById(R.id.Login);
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SignUp=new Intent(login.this,registration.class);
                startActivity(SignUp);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        rootRef = database.getReference();
        btn_student_Login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final String email =txtEmail.getText().toString().trim();
                String password=txtPassword.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(login.this, "Please enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(login.this, "Please enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.length()<6){
                    Toast.makeText(login.this, "Password is Very Short", Toast.LENGTH_SHORT).show();
                }
                mAuth .signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    if(mAuth.getCurrentUser().isEmailVerified()) {
                                        String userid = mAuth.getCurrentUser().getUid();
                                        userRef = rootRef.child("PARENTS");
                                        useridRef = userRef.child(userid);
                                        useridRef.child("username").child("username");
                                        useridRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                loginEmail = (String) dataSnapshot.getValue();
                                                if (Objects.equals(email, loginEmail)) {
                                                    startActivity(new Intent(login.this, Dashboard.class));

                                                } /*else {
                                                    FirebaseAuth.getInstance().signOut();
                                                    Toast.makeText(login.this, "", Toast.LENGTH_SHORT).show();
                                                    finish();

                                                    startActivity(new Intent(login.this,login.class));
                                                }*/
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                //error message
                                            }
                                        });
                                    }else{
                                        Toast.makeText(login.this," Please Verify Your Email Address First",Toast.LENGTH_SHORT).show();


                                    }
                                } else {
                                    Toast.makeText(login.this, task.getException().getMessage(),Toast.LENGTH_LONG).show();


                                }

                                // ...
                            }
                        });
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);




    }
}