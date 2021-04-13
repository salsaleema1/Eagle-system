package com.workos.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.workos.R;
import com.workos.adapters.UserSessionManager;
import com.workos.api.UserController;


public class LoginActivity extends AppCompatActivity {
    private EditText userName;
    private EditText pass;
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
        userName = (TextInputEditText) findViewById(R.id.username);
        pass = (TextInputEditText) findViewById(R.id.password);
        Button loginButton = (Button) findViewById(R.id.login);
        assert loginButton != null;
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateLogin(userName.getText().toString(), pass.getText().toString());
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

    void validateLogin(String username, String password)//
    {
        // UserController user = new UserController();
        UserController user = UserController.getIns();
        UserController.init(this);
        if (username.isEmpty()) {
            userName.setError("User name is required!");

        }
        if (password.isEmpty()) {
            pass.setError("Password is required!");


        } else {
            boolean loginResult = user.login(progressDialog, username, password);//get log in result from the username and password from the server
            if (loginResult) {
                Log.i(TAG, "log in successful");
                //    Log.i(tag, "the resulting token is restored from preferences" + user.getToken());

                session.createUserLoginSession(username);
                Intent in = new Intent(context, MainPageActivity.class);
                startActivity(in);
                finish();
            } else {
                pass.setText("");
                Log.i(TAG, "log in is unsuccessful");

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                this.finish();

            }
        }
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

