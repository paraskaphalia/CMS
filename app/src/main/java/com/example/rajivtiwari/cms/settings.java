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

import static android.view.View.GONE;

public class settings extends AppCompatActivity implements View.OnClickListener{

    private Button mChangeEmail,mChangePassword,mShowDetails,mUpdatePhoneNumber,mUpdateLinkedNumber,mBack;
    private SharedPreferences sharedPreferences;
    private FirebaseAuth firebaseAuth;
    private UserDetails userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        firebaseAuth = FirebaseAuth.getInstance();

        mChangeEmail = (Button) findViewById(R.id.change_email);
        mChangePassword = (Button) findViewById(R.id.change_password);
        mShowDetails = (Button) findViewById(R.id.user_details);
        mUpdatePhoneNumber = (Button) findViewById(R.id.update_phone_number);
        mUpdateLinkedNumber = (Button) findViewById(R.id.update_linked_number);
        mBack = (Button) findViewById(R.id.go_back);

        mChangeEmail.setOnClickListener(this);
        mChangePassword.setOnClickListener(this);
        mShowDetails.setOnClickListener(this);
        mUpdatePhoneNumber.setOnClickListener(this);
        mUpdateLinkedNumber.setOnClickListener(this);
        mBack.setOnClickListener(this);

        sharedPreferences = getSharedPreferences("com.example.rajivtiwari.cms", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(firebaseAuth.getCurrentUser().getEmail(), " ");
        userDetails = gson.fromJson(json, UserDetails.class);

        if(userDetails.getLoginType().equalsIgnoreCase("Child")) {
            mUpdateLinkedNumber.setVisibility(GONE);
            mUpdatePhoneNumber.setVisibility(GONE);
        }

    }

    @Override
    public void onClick(View view) {
        if(view == mChangeEmail) {
            startActivity(new Intent(settings.this,changeEmail.class));
        }
        if(view == mChangePassword) {
            startActivity(new Intent(settings.this,changePassword.class));
        }
        if(view == mShowDetails) {
            startActivity(new Intent(settings.this,showDetails.class));
        }
        if(view == mUpdatePhoneNumber) {
            startActivityForResult(new Intent(settings.this,changePhoneNumber.class),12);
        }
        if(view == mUpdateLinkedNumber) {
            startActivityForResult(new Intent(settings.this,userInfo.class),12);
        }
        if(view == mBack) {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==123 && requestCode==12){
            setResult(123);
            finish();
        }
    }
}
