package com.example.rajivtiwari.cms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

public class Front extends AppCompatActivity implements View.OnClickListener{

    private Button mLogin;
    private Button mSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);

        mLogin = (Button) findViewById(R.id.button_login);
        mSignup = (Button) findViewById(R.id.button_signup);

        mLogin.setOnClickListener(this);
        mSignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if(view == mLogin) {
            startActivity(new Intent(this,Login.class));
        }

        if(view == mSignup) {
            startActivity(new Intent(this,signUp.class));
        }
    }
}
