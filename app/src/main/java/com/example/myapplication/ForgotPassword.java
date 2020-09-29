package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
Button button;
EditText editText;
FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        button=findViewById(R.id.btn);
        editText=findViewById(R.id.ForgotPasswordEdittext);
        mAuth=FirebaseAuth.getInstance();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail=editText.getText().toString();
                if(TextUtils.isEmpty(userEmail)) {
                    Toast.makeText(ForgotPassword.this, "Please Enter Email First", Toast.LENGTH_SHORT).show();
                }
                else{
                    mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ForgotPassword.this, "Kindly Check Your Email Account for Resetting Your Password", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ForgotPassword.this,login.class));
                            }
                            else{
                                String msg=task.getException().getMessage();
                                Toast.makeText(ForgotPassword.this, "This Error occured"+msg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}