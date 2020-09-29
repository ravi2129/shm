package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    ImageView flash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flash=findViewById(R.id.flash);
        Animation myanim= AnimationUtils.loadAnimation(this,R.anim.splashscreen);
        flash.startAnimation(myanim);
        final Intent i=new Intent(this,login.class);
        Thread timer=new Thread(){
            public void run(){
                try{
                    sleep(5000);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }
}