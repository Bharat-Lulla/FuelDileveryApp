package com.example.fueldeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private EditText txtemail,txtpass,txtname,txtphone;
    private Button btnregister;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        changeStatusBarColor();
        txtname=findViewById(R.id.editTextName);
        txtpass=findViewById(R.id.editTextPassword);
        txtphone=findViewById(R.id.editTextMobile);
        txtemail=findViewById(R.id.editTextEmail);
        btnregister=findViewById(R.id.cirRegisterButton);
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),txtname.getText().toString(),Toast.LENGTH_LONG).show();
                String email, password,name,phone;
                email = txtemail.getText().toString();
                password = txtpass.getText().toString();
                name = txtname.getText().toString();
                phone = txtphone.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter email!!",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                if (TextUtils.isEmpty(txtpass.getText())) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter password!!",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                if (TextUtils.isEmpty(txtname.getText())) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter name!!",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                if (TextUtils.isEmpty(txtphone.getText())) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter phone number!!",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                firebaseAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //Log.d(TAG, "New user registration: " + task.isSuccessful());

                                if (task.isSuccessful()) {
                                    //RegisterActivity.this.showToast("Authentication failed. " + task.getException());
                                    Toast.makeText(getApplicationContext(),"Registered",Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(RegisterActivity.this,MainMenuActivity.class));
                                } else {
                                    Toast.makeText(getApplicationContext(),"failed"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }
    public void onLoginClick(View view){
        startActivity(new Intent(this,LoginActivty.class));

    }
}