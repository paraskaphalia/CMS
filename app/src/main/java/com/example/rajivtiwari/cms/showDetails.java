package com.example.rajivtiwari.cms;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class showDetails extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private SharedPreferences sharedPreferences;
    private UserDetails userDetails;
    private TextView email,loginType,phoneNumber,firstName,lastName,linkedNumber;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        firebaseAuth=FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences("com.example.rajivtiwari.cms", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(firebaseAuth.getCurrentUser().getEmail(), "");
        userDetails = gson.fromJson(json, UserDetails.class);

        //getting edittext references
        email=(TextView) findViewById(R.id.updateEmail);
        firstName=(TextView) findViewById(R.id.updateFirstName);
        lastName=(TextView) findViewById(R.id.updateLastName);
        phoneNumber=(TextView) findViewById(R.id.updatePhoneNumber);
        linkedNumber=(TextView) findViewById(R.id.updateLinkedNumber);
        loginType=(TextView) findViewById(R.id.updateLoginType);
        layout=(LinearLayout) findViewById(R.id.parentCase);

        if(firebaseAuth.getCurrentUser()!=null){
            email.setText(firebaseAuth.getCurrentUser().getEmail());
            firstName.setText(userDetails.getFirstName());
            lastName.setText(userDetails.getLastName());
            phoneNumber.setText(userDetails.getPhoneNumber());
            linkedNumber.setText(userDetails.getLinkedNumber());
            if(userDetails.getLoginType().equalsIgnoreCase("Child"))
                layout.setVisibility(LinearLayout.GONE);
            loginType.setText(userDetails.getLoginType());
        }
    }
}
