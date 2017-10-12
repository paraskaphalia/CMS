package com.example.rajivtiwari.cms;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class userInfo extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    private SharedPreferences sharedPreferences;
    private UserDetails userDetails;
    private EditText linkedNumber;
    private Button mUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        firebaseAuth=FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences("com.example.rajivtiwari.cms", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(firebaseAuth.getCurrentUser().getEmail(), "");
        userDetails = gson.fromJson(json, UserDetails.class);

        reference = FirebaseDatabase.getInstance().getReference("Users").child(userDetails.getPhoneNumber()).child("Details");

        //getting edittext references
        linkedNumber=(EditText) findViewById(R.id.update_linked);
        mUpdate=(Button) findViewById(R.id.button_update);

        mUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String mLinkedNumber = linkedNumber.getText().toString().trim();

        if (TextUtils.isEmpty(mLinkedNumber)) {
            Toast.makeText(this, "Enter Linked Number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mLinkedNumber.length() != 10) {
            Toast.makeText(this, "Invalid Linked Number", Toast.LENGTH_SHORT).show();
            return;
        }

        setResult(123);

        Gson gson = new Gson();
        String json = sharedPreferences.getString(firebaseAuth.getCurrentUser().getEmail(), "NULL");
        userDetails = gson.fromJson(json, UserDetails.class);

        userDetails.setLinkedNumber(mLinkedNumber);

        gson = new Gson();
        json = gson.toJson(userDetails);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(firebaseAuth.getCurrentUser().getEmail(), json);
        editor.apply();

        reference.setValue(userDetails);

        finish();
    }
}

