package com.workos.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.workos.R;
import com.workos.adapters.UserSessionManager;
import com.workos.api.UserController;


public class LoginActivity extends AppCompatActivity {
    private TextInputLayout userName, password;
    private Context context = this;
    private String TAG = "LoginActivity";
    private ProgressDialog progressDialog;
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.atoolbar);
        setSupportActionBar(toolbar);
        session = new UserSessionManager(getApplicationContext());
        userName = (TextInputLayout) findViewById(R.id.username);
        password = (TextInputLayout) findViewById(R.id.password);
        Button loginButton = (Button) findViewById(R.id.login);
        assert loginButton != null;
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateLogin(userName, password);
            }
        });
        TextView textView = (TextView) findViewById(R.id.signup);
        assert textView != null;
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SignupActivity.class);
                startActivityForResult(intent, 0);

            }
        });
        progressDialog = new ProgressDialog(LoginActivity.this);


    }

    public void onLoginClick(View View) {
        startActivity(new Intent(this, SignupActivity.class));
//        overridePendingTransition(0x7f010010,0x7f010012);

    }

    void validateLogin(TextInputLayout username, TextInputLayout password) {

        String usernameStr = username.getEditText().getText().toString();
        String passwordStr = password.getEditText().getText().toString();
        if (usernameStr.isEmpty()) {
            userName.setError("User name is required!");
        } else {
            userName.setError(null);
            userName.setErrorEnabled(false);
        }
        if (passwordStr.isEmpty()) {
            this.password.setError("Password is required!");
        } else {
            logIn();
        }
    }

    private boolean logIn() {

//        UserController userController = UserController.getIns();
//        userController.login(null, userName,password);

        String userEnteredUsername = userName.getEditText().getText().toString().trim();
        String userEnteredPassword = password.getEditText().getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        Query checkUser = reference.orderByChild("username").equalTo(userEnteredUsername);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    userName.setError(null);
                    userName.setErrorEnabled(false);

                    String passwordFromDB = snapshot.child(userEnteredUsername).child("password").getValue(String.class);

                    if (passwordFromDB != null && passwordFromDB.equals(userEnteredPassword)) {

                        userName.setError(null);
                        userName.setErrorEnabled(false);

                        String usernameFromDB = snapshot.child(userEnteredUsername).child("username").getValue(String.class);
                        String emailFromDB = snapshot.child(userEnteredUsername).child("email").getValue(String.class);
                        String phoneNumberFromDB = snapshot.child(userEnteredUsername).child("phoneNumber").getValue(String.class);
                        String fullNameFromDB = snapshot.child(userEnteredUsername).child("fullName").getValue(String.class);

                        Intent mainPage = new Intent(getApplicationContext(), ViewProfileActivity.class);
                        mainPage.putExtra(UserSessionManager.KEY_USERNAME, usernameFromDB);
                        mainPage.putExtra(UserSessionManager.KEY_FULLNAME, fullNameFromDB);
                        mainPage.putExtra(UserSessionManager.KEY_EMAIL, emailFromDB);
                        mainPage.putExtra(UserSessionManager.KEY_PHONE, phoneNumberFromDB);
                        startActivity(mainPage);

                    } else {
                        password.setError("Wrong password");
                        password.requestFocus();
                    }
                } else {
                    userName.setError("No such User exist");
                    userName.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        Log.i(TAG, "log in is unsuccessful");
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }
}

