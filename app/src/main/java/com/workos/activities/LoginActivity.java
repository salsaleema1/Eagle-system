package com.workos.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.workos.R;
import com.workos.adapters.UserSessionManager;

import java.util.Date;


public class LoginActivity extends AppCompatActivity {
    private TextInputLayout userName, password;
    private Context context = this;
    private String TAG = "LoginActivity";
    private ProgressDialog progressDialog;
    UserSessionManager session;

    private SignInButton signInWithGoogle;
    GoogleSignInClient mGoogleSignInClient;

    int RC_SIGN_IN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.atoolbar);
        setSupportActionBar(toolbar);
        session = new UserSessionManager(getApplicationContext());
        userName = findViewById(R.id.username);
        signInWithGoogle = findViewById(R.id.signInWithGoogle);
        password = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login);
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
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.signInWithGoogle:
                        signIn();
                        break;
                }
            }
        });
    }

    public void onLoginClick(View View) {
        startActivity(new Intent(this, SignupActivity.class));
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    private void updateUI(GoogleSignInAccount account) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            loginUsingGoogle(task);
        }
    }

    private void loginUsingGoogle(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            Uri personPhoto = acct.getPhotoUrl();
            session.createUserLoginSession(personName, personGivenName + ":" + personFamilyName, personEmail, "", "", "", String.valueOf(personPhoto));

            // Signed in successfully, show authenticated UI.
            redirectToMainPage();
            updateUI(account);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void validateLogin(TextInputLayout username, TextInputLayout password) {

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
            loginUsingUsernameAndPass();
        }
    }

    private void loginUsingUsernameAndPass() {

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
                        session.createUserLoginSession(usernameFromDB, fullNameFromDB, emailFromDB, phoneNumberFromDB, passwordFromDB, String.valueOf(new Date()), null);
                        redirectToMainPage();
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
    }

    private void redirectToMainPage() {
        Intent mainPage = new Intent(getApplicationContext(), MainPageActivity.class);
        startActivity(mainPage);
        finish();
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

