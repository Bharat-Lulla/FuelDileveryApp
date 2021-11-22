package com.example.fueldeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.ProgressDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import io.paperdb.Paper;


public class LoginActivty extends AppCompatActivity {
    private EditText edLoginEmail,edLoginPass;
    private ProgressDialog notifBox;
    Button btlogin;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activty);
        getSupportActionBar().hide();
        Paper.init(this);
        firebaseAuth = FirebaseAuth.getInstance();
        edLoginEmail=findViewById(R.id.editTxtEmail);
        notifBox= new ProgressDialog(this);
        edLoginPass=findViewById(R.id.editTxtPassword);
        btlogin=findViewById(R.id.cirLoginButton);
        String rememberedPhoneKey = Paper.book().read("userName");
        String rememberedPassKey= Paper.book().read("password");
        if (rememberedPhoneKey!= "" && rememberedPassKey!= "")
        {
            if(!TextUtils.isEmpty(rememberedPhoneKey) && !TextUtils.isEmpty(rememberedPassKey))
            {
                notifBox.setTitle("User remembered");
                notifBox.setMessage("Please wait, you are already logged In .....");
                notifBox.setCanceledOnTouchOutside(true);
                notifBox.show();

                AllowUserAccess(rememberedPhoneKey, rememberedPassKey);


            }
        }
        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edLoginEmail.getText().toString();
                String password = edLoginPass.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Paper.book().write("userName", email);
                Paper.book().write("password", password);
                //authenticate user
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivty.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    Toast.makeText(LoginActivty.this, "Authentication failed"+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(LoginActivty.this, "Successfully logined",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                                    startActivity(intent);
                                }
                            }
                        });

            }
        });


        //for changing status bar icon colors
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }
    private void AllowUserAccess(final String email, final String password){
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivty.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            // there was an error
                            Toast.makeText(LoginActivty.this, "Authentication failed"+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivty.this, "Successfully logined",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }
    public void onLogLoginClick(View view){
        startActivity(new Intent(this,RegisterActivity.class));

    }

}