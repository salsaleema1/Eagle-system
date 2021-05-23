package com.workos.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.workos.R;
import com.workos.adapters.UserSessionManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class ViewProfileActivity extends AppCompatActivity {

    public static final String TAG = "ViewProfileActivity";
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        session = new UserSessionManager(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.profiletoolbar);
        setSupportActionBar(toolbar);

        ImageView profileimage = (ImageView) findViewById(R.id.profileUserImage);
        TextView usernameview = (TextView) findViewById(R.id.profileUserName);
        TextView fullName = (TextView) findViewById(R.id.profileFullName);
        TextView mobileview = (TextView) findViewById(R.id.profilePhoneNumber);
        TextView emailview = (TextView) findViewById(R.id.profileEmail);
        TextView backToHome = (TextView) findViewById(R.id.backToHome);
        backToHome.setOnClickListener(v -> {
            switch (v.getId()) {
                case (R.id.backToHome):
                    onBackPressed();
                    break;
            }
        });

        assert usernameview != null;
        assert mobileview != null;
        assert emailview != null;
        assert profileimage != null;
        HashMap<String, String> userDetails = session.getUserDetails();
        String imageUrl = userDetails.get(UserSessionManager.KEY_IMAGE_URL);
        if (imageUrl != null) {
            Glide.with(this).load(imageUrl).into(profileimage);
        }

        usernameview.setText(userDetails.get(UserSessionManager.KEY_USERNAME));
        fullName.setText(userDetails.get(UserSessionManager.KEY_FULLNAME));
        mobileview.setText(userDetails.get(UserSessionManager.KEY_PHONE));
        emailview.setText(userDetails.get(UserSessionManager.KEY_EMAIL));

        profileimage.setImageResource(R.drawable.profile);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getIntent().getStringExtra(UserSessionManager.KEY_FULLNAME));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        backToHome.setOnClickListener(v -> onBackPressed());


        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(getApplicationContext(), ImageViewAcivity.class);
                intent.putExtra("image", getIntent().getStringExtra("image"));
                getApplicationContext().startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.i(TAG, "onBackPressed ");
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}