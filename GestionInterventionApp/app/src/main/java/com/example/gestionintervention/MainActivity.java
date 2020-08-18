package com.example.gestionintervention;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 4000;
    private long backPressedTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                finish();
        }
        },SPLASH_TIME_OUT);
        setContentView(com.example.gestionintervention.R.layout.activity_home);

        System.out.println("before toolbar");

        Toolbar toolbar = findViewById(com.example.gestionintervention.R.id.toolbar);
        setSupportActionBar(toolbar);

        }






}