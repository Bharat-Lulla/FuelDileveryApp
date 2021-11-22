package com.example.fueldeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.fueldeliveryapp.adapters.TargetDataAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    private static final String TAG = "CodeExa.Com";

    ArrayList<TargetClass> mTargetData = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private TargetDataAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        fetchData();
        initView();
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        System.out.println("Hello" + mTargetData);
        mAdapter = new TargetDataAdapter(mTargetData);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void fetchData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Get Post object and use the values to update the UI
                mTargetData.clear();
                for (DataSnapshot single : snapshot.getChildren()) {
                    TargetClass target =  single.getValue(TargetClass.class);
                    mTargetData.add(target);
                }
                mAdapter.notifyDataSetChanged();
                Log.e(TAG, "Data received:" + mTargetData.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting Post failed, log a message
                Log.w(TAG, "fetchData onCancelled");
            }
        };

        myRef.addListenerForSingleValueEvent(postListener);
    }
}