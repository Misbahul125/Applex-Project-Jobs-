package com.example.jobs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class DaysActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days);
        getSupportActionBar().setTitle("Create Batch");
    }

    public void btnCreate(View view) {
        Toast.makeText(this, "Batch created successfully!!", Toast.LENGTH_LONG).show();
    }
}