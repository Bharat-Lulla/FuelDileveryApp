package com.example.fueldeliveryapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.fueldeliveryapp.R;
import com.example.fueldeliveryapp.model.FuelModal;

public class OrderSucceessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_succeess);
        FuelModal fuelModal = getIntent().getParcelableExtra("fuelmodel");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(fuelModal.getName());
        actionBar.setSubtitle(fuelModal.getAddress());
        actionBar.setDisplayHomeAsUpEnabled(false);


        TextView buttonDone = findViewById(R.id.buttonDone);
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}