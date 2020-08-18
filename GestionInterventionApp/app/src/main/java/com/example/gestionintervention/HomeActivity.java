package com.example.gestionintervention;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;

import android.view.MenuItem;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity  {

    private long backPressedTime = 0;
    Bundle savedInstance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        savedInstance = savedInstanceState;
        super.onCreate(savedInstanceState);
        setContentView(com.example.gestionintervention.R.layout.activity_main);

    }


    @Override
    public void onBackPressed() {        // to prevent irritating accidental logouts
        long t = System.currentTimeMillis();
        if (t - backPressedTime > 2000) {    // 2 secs
            backPressedTime = t;
            Toast.makeText(this, "Press back again to logout",
                    Toast.LENGTH_SHORT).show();
        } else {    // this guy is serious
            // clean up
            finish();
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.example.gestionintervention.R.menu.menu_main, menu);
        return true;
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case com.example.gestionintervention.R.id.action_logout: {
          //      Toast.makeText(this, "Log Out now ...", Toast.LENGTH_LONG).show();

            //     Navigation.findNavController(findViewById(R.id.userInteventionsFragment)).navigate(R.id.action_userInteventionsFragment_to_InterventionFrag);

                return true;}

        }
        return super.onOptionsItemSelected(item);
    }

}