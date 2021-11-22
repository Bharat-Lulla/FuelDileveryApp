package com.example.fueldeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.fueldeliveryapp.R;
import com.example.fueldeliveryapp.adapters.FuelListAdapter;
import com.example.fueldeliveryapp.model.FuelModal;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

import io.paperdb.Paper;

public class MainMenuActivity extends AppCompatActivity implements FuelListAdapter.FuelListClickListener{
    String user;
    Button btn_logout;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Fuel List");
        List<FuelModal> fuelModalList =  getFuelData();
        btn_logout=findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();

                Intent intent= new Intent(MainMenuActivity.this, LoginActivty.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
//        navigationView=findViewById(R.id.navigation);
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                Fragment fragment=null;
//                int id=item.getItemId();
//                if(id==R.id.first)
//                    startActivity(new Intent(MainMenuActivity.this,OrderActivity.class));
//                return true;
//            }
//        });
        initRecyclerView(fuelModalList);
    }

    private void initRecyclerView(List<FuelModal> fuelModalList) {
        RecyclerView recyclerView =  findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FuelListAdapter adapter = new FuelListAdapter(fuelModalList, this);
        recyclerView.setAdapter(adapter);
    }

    private List<FuelModal> getFuelData() {
        InputStream is = getResources().openRawResource(R.raw.fuel_station);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try{
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while(( n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0,n);
            }
        }catch (Exception e) {

        }

        String jsonStr = writer.toString();
        Gson gson = new Gson();
        FuelModal[] fuelModals =  gson.fromJson(jsonStr, FuelModal[].class);
        List<FuelModal> restList = Arrays.asList(fuelModals);

        return  restList;

    }

    @Override
    public void onItemClick(FuelModal fuelModal) {
        Intent intent = new Intent(getApplicationContext(), MainFuelActivity.class);
        intent.putExtra("fuelmodel", fuelModal);
        startActivity(intent);
    }
}