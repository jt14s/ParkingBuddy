package com.dev.jt14s.parkingbuddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        Intent intent = new Intent(this, MapsActivity.class);
        System.out.println("In launch");
        startActivity(intent);
    }
}
