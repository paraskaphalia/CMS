package com.example.rajivtiwari.cms;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class changePhoneNumber extends AppCompatActivity implements View.OnClickListener{

    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    private SharedPreferences sharedPreferences;
    private UserDetails userDetails;
    private EditText phoneNumber;
    private Button mUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone_number);

        firebaseAuth= FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences("com.example.rajivtiwari.cms", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(firebaseAuth.getCurrentUser().getEmail(), "");
        userDetails = gson.fromJson(json, UserDetails.class);

        reference = FirebaseDatabase.getInstance().getReference("Users");

        //getting edittext references
        phoneNumber=(EditText) findViewById(R.id.update_phone);
        mUpdate=(Button) findViewById(R.id.button_up);

        mUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String mPhoneNumber = phoneNumber.getText().toString().trim();

        if (TextUtils.isEmpty(mPhoneNumber)) {
            Toast.makeText(this, "Enter Linked Number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mPhoneNumber.length() != 10) {
            Toast.makeText(this, "Invalid Linked Number", Toast.LENGTH_SHORT).show();
            return;
        }

        setResult(123);

        Gson gson = new Gson();
        String json = sharedPreferences.getString(firebaseAuth.getCurrentUser().getEmail(), "NULL");
        userDetails = gson.fromJson(json, UserDetails.class);

        userDetails.setPhoneNumber(mPhoneNumber);

        gson = new Gson();
        json = gson.toJson(userDetails);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(firebaseAuth.getCurrentUser().getEmail(), json);
        editor.apply();

        reference.child(mPhoneNumber).child("Details").setValue(userDetails);

        finish();
    }
}
