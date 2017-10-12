package com.example.rajivtiwari.cms;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class signUp extends AppCompatActivity implements View.OnClickListener{

    private EditText inputEmail, inputPassword;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mPhoneNo;
    private Spinner mLoginType;

    private String email;
    private String password;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String loginType;
    private double latitude;
    private double longitude;
    private DatabaseReference firebasDatabase;
    private UserDetails userDetails;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        sharedPreferences = getSharedPreferences("com.example.rajivtiwari.cms", Context.MODE_PRIVATE);

        inputEmail = (EditText) findViewById(R.id.edittextEmail);
        inputPassword = (EditText) findViewById(R.id.edittextPassword);
        btnSignIn = (Button) findViewById(R.id.buttonSignIn);
        btnSignUp = (Button) findViewById(R.id.buttonSignUp);
        btnResetPassword = (Button) findViewById(R.id.buttonResetPassword);

        firebasDatabase = FirebaseDatabase.getInstance().getReference("Users");

        mFirstName = (EditText)findViewById(R.id.viewFirstName);
        mLastName = (EditText)findViewById(R.id.viewLastName);
        mPhoneNo = (EditText)findViewById(R.id.viewPhoneNumber);
        mLoginType = (Spinner)findViewById(R.id.spinner_loginType);


        btnSignIn.setOnClickListener(this);
        btnResetPassword.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    private void registerUser() {
        email = inputEmail.getText().toString().trim();
        password = inputPassword.getText().toString().trim();
        firstName = mFirstName.getText().toString().trim();
        lastName = mLastName.getText().toString().trim();
        phoneNumber = mPhoneNo.getText().toString().trim();
        loginType = mLoginType.getSelectedItem().toString();
        latitude = 0.0;
        longitude = 0.0;

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter Your Email ID", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length()<6) {
            Toast.makeText(this, "Password too short", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(firstName)) {
            Toast.makeText(this, "Enter Your First Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(lastName)) {
            Toast.makeText(this, "Enter Your Last Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(this, "Enter Your Phone Number", Toast.LENGTH_SHORT).show();
            return;
        }
        if(phoneNumber.length() != 10) {
            Toast.makeText(this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(signUp.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()) {

                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(firstName + " " + lastName).build();
                    user.updateProfile(profileUpdates);

                    Toast.makeText(signUp.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                    userDetails = new UserDetails(firstName,lastName,phoneNumber,loginType);
                    firebasDatabase.child(phoneNumber).child("Details").setValue(userDetails);
                    Toast.makeText(signUp.this, "Details Updated", Toast.LENGTH_SHORT).show();

                    Gson gson = new Gson();
                    String json = gson.toJson(userDetails);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(email, json);
                    editor.apply();

                    if(loginType.equals("Parent")) {
                        finish();
                        startActivity(new Intent(signUp.this, MapParent.class));
                    }
                    else {
                        finish();
                        startActivity(new Intent(signUp.this, Maps.class));
                    }
                }
                else {
                    Toast.makeText(signUp.this, "Registration Failed!!! Enter Correct Email And Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view == btnSignUp) {
            registerUser();
        }

        if(view == btnSignIn) {
            finish();
            startActivity(new Intent(signUp.this,Login.class));
        }
    }
}
