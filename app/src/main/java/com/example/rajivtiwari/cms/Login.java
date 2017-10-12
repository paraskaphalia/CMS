package com.example.rajivtiwari.cms;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.sql.BatchUpdateException;


public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText inputEmail, inputPassword;
    FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private Button btnSignUp, btnLogin, btnReset;
    private UserDetails userDetails;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("com.example.rajivtiwari.cms", Context.MODE_PRIVATE);

        /*if(firebaseAuth.getCurrentUser()!=null) {
            Gson gson = new Gson();
            String json = sharedPreferences.getString(firebaseAuth.getCurrentUser().getEmail(), "");
            userDetails = gson.fromJson(json, UserDetails.class);

            if( userDetails.getLoginType().equals("Parent")) {
                finish();
                startActivity(new Intent(Login.this, MapParent.class));
            }
            else {
                finish();
                startActivity(new Intent(Login.this, Maps.class));
            }
        }*/

        progressDialog = new ProgressDialog(this);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnSignUp = (Button) findViewById(R.id.btn_not_member);
        btnReset = (Button) findViewById(R.id.btn_reset_password);

        btnLogin.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    private void loginUser() {
        final String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter Your Email ID", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();

                    Gson gson = new Gson();
                    String json = sharedPreferences.getString(email, " ");
                    userDetails = gson.fromJson(json, UserDetails.class);

                    if( userDetails.getLoginType().equalsIgnoreCase("Parent")) {
                        finish();
                        startActivity(new Intent(Login.this, MapParent.class));
                    }
                    else {
                        finish();
                        startActivity(new Intent(Login.this, Maps.class));
                    }

                } else {
                    Toast.makeText(Login.this, "Authentication Failed...", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

        });
    }

    @Override
    public void onClick(View view) {

        if (view == btnLogin) {
            loginUser();
        }

        if (view == btnSignUp) {
            finish();
            startActivity(new Intent(Login.this, signUp.class));
        }
    }

}
