package com.example.rajivtiwari.cms;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

public class getNoChild extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private FirebaseAuth firebaseAuth;
    private EditText phoneNumber;
    private Button Enter;
    private UserDetails userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_no_child);

        sharedPreferences = getSharedPreferences("com.example.rajivtiwari.cms", Context.MODE_PRIVATE);
        firebaseAuth = FirebaseAuth.getInstance();

        phoneNumber = (EditText) findViewById(R.id.phone_Number);
        Enter = (Button) findViewById(R.id.btn_enter);

        Enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pno = phoneNumber.getText().toString().trim();

                Gson gson = new Gson();
                String json = sharedPreferences.getString(firebaseAuth.getCurrentUser().getEmail(), "NULL");
                userDetails = gson.fromJson(json, UserDetails.class);

                userDetails.setLinkedNumber(pno);

                gson = new Gson();
                json = gson.toJson(userDetails);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(firebaseAuth.getCurrentUser().getEmail(), json);
                editor.apply();

                finish();
            }
        });
    }
}
