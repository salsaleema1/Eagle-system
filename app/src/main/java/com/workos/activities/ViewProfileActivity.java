package com.workos.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.profiletoolbar);
        setSupportActionBar(toolbar);

        ImageView profileimage = (ImageView) findViewById(R.id.profileUserImage);
        TextView usernameview = (TextView) findViewById(R.id.profileUserName);
        TextView fullName = (TextView) findViewById(R.id.profileFullName);
        TextView mobileview = (TextView) findViewById(R.id.profilePhoneNumber);
        TextView emailview = (TextView) findViewById(R.id.profileEmail);
        TextView backToHome = (TextView) findViewById(R.id.backToHome);
        UserSessionManager user = new UserSessionManager(getApplicationContext());


        assert usernameview != null;
        assert mobileview != null;
        assert emailview != null;
        assert profileimage != null;

        Intent intent = getIntent();
        if (intent.getStringExtra(UserSessionManager.KEY_USERNAME) == null) {
            usernameview.setText(String.format("@%s", intent.getStringExtra(UserSessionManager.KEY_USERNAME)));
            mobileview.setText(intent.getStringExtra(UserSessionManager.KEY_PHONE));
            emailview.setText(intent.getStringExtra(UserSessionManager.KEY_EMAIL));
//            String decodedImage=userdata.get(UserSessionManager.KEY_IMAGE);
//            Log.i(TAG,"decodedImage"+decodedImage);
//            if(decodedImage!= null) {
//                byte[] b = Base64.decode(decodedImage, Base64.DEFAULT);
//                Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
//
//                //compress bitmap
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                final byte[] byteArray = stream.toByteArray();
//
//                profileimage.setImageBitmap(bitmap);
//
//                profileimage.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent;
//                        intent = new Intent(getApplicationContext(), ImageViewAcivity.class);
//                        intent.putExtra("bitmap",byteArray);
//                        getApplicationContext().startActivity(intent);
//                    }
//                });
//            }
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(intent.getStringExtra(UserSessionManager.KEY_FULLNAME));
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
        } else {
            usernameview.setText(getIntent().getStringExtra(UserSessionManager.KEY_USERNAME));
            fullName.setText(getIntent().getStringExtra(UserSessionManager.KEY_FULLNAME));
            mobileview.setText(getIntent().getStringExtra(UserSessionManager.KEY_PHONE));
            emailview.setText(getIntent().getStringExtra(UserSessionManager.KEY_EMAIL));
//            getBitmapFromURL(getIntent().getStringExtra("image"), profileimage);
            profileimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;
                    intent = new Intent(getApplicationContext(), ImageViewAcivity.class);
                    intent.putExtra("image", getIntent().getStringExtra("image"));
                    getApplicationContext().startActivity(intent);
                }
            });
            profileimage.setImageResource(R.drawable.profile);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(getIntent().getStringExtra(UserSessionManager.KEY_FULLNAME));
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
            backToHome.setOnClickListener(v -> onBackPressed());
        }
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

    public void getBitmapFromURL(final String src, final ImageView profileImage) {

        try {
            new AsyncTask<Void, Void, Bitmap>() {
                @Override
                protected void onPreExecute() {

                    super.onPreExecute();
                }

                @Override
                // Making a request to url and getting response
                protected Bitmap doInBackground(Void... params) {
                    Bitmap myBitmap = null;
                    Log.e("src", src);
                    if (src == null)
                        return null;
                    HttpURLConnection connection;
                    try {
                        URL url = new URL(src);
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        myBitmap = BitmapFactory.decodeStream(input);
                        Log.e("Bitmap", "returned");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return myBitmap;
                }

                @Override
                protected void onPostExecute(Bitmap result) {
                    super.onPostExecute(result);
                    if (result != null)
                        profileImage.setImageBitmap(result);
                    else
                        profileImage.setImageResource(R.drawable.profile);
                    System.out.println("result==" + result);
                }
            }.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


    }
}