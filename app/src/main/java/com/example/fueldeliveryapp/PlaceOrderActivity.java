package com.example.fueldeliveryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fueldeliveryapp.R;
import com.example.fueldeliveryapp.adapters.PlaceYourOrderAdapter;
import com.example.fueldeliveryapp.model.Menu;
import com.example.fueldeliveryapp.model.FuelModal;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

public class PlaceOrderActivity extends AppCompatActivity {
    private EditText inputName, inputAddress, inputCity, inputState, inputZip,inputCardNumber, inputCardExpiry, inputCardPin ;
    private RecyclerView cartItemsRecyclerView;
    private TextView tvSubtotalAmount, tvDeliveryChargeAmount, tvDeliveryCharge, tvTotalAmount, buttonPlaceYourOrder;
    private SwitchCompat switchDelivery;
    private boolean isDeliveryOn;
    private PlaceYourOrderAdapter placeYourOrderAdapter;
    float subTotalAmount = 0f;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myref = database.getReference("UserDetails");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        FuelModal fuelModal = getIntent().getParcelableExtra("fuelmodel");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(fuelModal.getName());
        actionBar.setSubtitle(fuelModal.getAddress());
        actionBar.setDisplayHomeAsUpEnabled(true);

        inputName = findViewById(R.id.inputName);
        inputAddress = findViewById(R.id.inputAddress);
        inputCity = findViewById(R.id.inputCity);
        inputState = findViewById(R.id.inputState);
        inputZip = findViewById(R.id.inputZip);
        tvSubtotalAmount = findViewById(R.id.tvSubtotalAmount);
        tvDeliveryChargeAmount = findViewById(R.id.tvDeliveryChargeAmount);
        tvDeliveryCharge = findViewById(R.id.tvDeliveryCharge);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        buttonPlaceYourOrder = findViewById(R.id.buttonPlaceYourOrder);
        switchDelivery = findViewById(R.id.switchDelivery);

        cartItemsRecyclerView = findViewById(R.id.cartItemsRecyclerView);
        buttonPlaceYourOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlaceOrderButtonClick(fuelModal);
            }
        });

        switchDelivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    inputAddress.setVisibility(View.VISIBLE);
                    inputCity.setVisibility(View.VISIBLE);
                    inputState.setVisibility(View.VISIBLE);
                    inputZip.setVisibility(View.VISIBLE);
                    tvDeliveryChargeAmount.setVisibility(View.VISIBLE);
                    tvDeliveryCharge.setVisibility(View.VISIBLE);
                    isDeliveryOn = true;
                    calculateTotalAmount(fuelModal);
                } else {
                    inputAddress.setVisibility(View.GONE);
                    inputCity.setVisibility(View.GONE);
                    inputState.setVisibility(View.GONE);
                    inputZip.setVisibility(View.GONE);
                    tvDeliveryChargeAmount.setVisibility(View.GONE);
                    tvDeliveryCharge.setVisibility(View.GONE);
                    isDeliveryOn = false;
                    calculateTotalAmount(fuelModal);
                }
            }
        });
        initRecyclerView(fuelModal);
        calculateTotalAmount(fuelModal);
    }

    private void calculateTotalAmount(FuelModal fuelModal) {

        subTotalAmount = 0;
        for(Menu m : fuelModal.getMenus()) {
            subTotalAmount += m.getPrice() * m.getTotalInCart();
        }

        tvSubtotalAmount.setText("Rs "+String.format("%.2f", subTotalAmount));
        if(isDeliveryOn) {
            tvDeliveryChargeAmount.setText("Rs "+String.format("%.2f", fuelModal.getDelivery_charge()));
            subTotalAmount += fuelModal.getDelivery_charge();
        }
        tvTotalAmount.setText("Rs "+String.format("%.2f", subTotalAmount));
    }

    private void onPlaceOrderButtonClick(FuelModal fuelModal) {
        if(TextUtils.isEmpty(inputName.getText().toString())) {
            inputName.setError("Please enter name ");
            return;
        } else if(isDeliveryOn && TextUtils.isEmpty(inputAddress.getText().toString())) {
            inputAddress.setError("Please enter address ");
            return;
        }else if(isDeliveryOn && TextUtils.isEmpty(inputCity.getText().toString())) {
            inputCity.setError("Please enter city ");
            return;
        }else if(isDeliveryOn && TextUtils.isEmpty(inputState.getText().toString())) {
            inputState.setError("Please enter zip ");
            return;
        }
        HelperClass h=new HelperClass(inputName.getText().toString(),inputAddress.getText().toString(),inputCity.getText().toString(),
                inputState.getText().toString(),inputZip.getText().toString());
        myref.child(inputName.getText().toString()).setValue(h);
//        int subparts=1;
        for(Menu m : fuelModal.getMenus()) {
//            subparts=subparts+1;
            myref.child(inputName.getText().toString()).child("cart").child(m.getName()).setValue(m.getTotalInCart());
        }
        myref.child(inputName.getText().toString()).child("cart").child("Total Price").setValue(subTotalAmount);

        //start success activity..
        Intent i = new Intent(PlaceOrderActivity.this, PaymentActivity.class);
        i.putExtra("amounttotal", subTotalAmount);
        startActivity(i);
    }

    private void initRecyclerView(FuelModal fuelModal) {
        cartItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        placeYourOrderAdapter = new PlaceYourOrderAdapter(fuelModal.getMenus());
        cartItemsRecyclerView.setAdapter(placeYourOrderAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == 1000) {
            setResult(Activity.RESULT_OK);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
            default:
                //do nothing
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        finish();
    }



}