package com.workos.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.workos.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class ImageViewAcivity extends AppCompatActivity {

    public static final String TAG="ViewProfileActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.viewimage);

        Toolbar toolbar = (Toolbar) findViewById(R.id.ImageViewtoolbar);
        setSupportActionBar(toolbar);
        ImageView profileimage =(ImageView)findViewById(R.id.imageView) ;

        if(getIntent().getByteArrayExtra("bitmap")==null) {
            getBitmapFromURL(getIntent().getStringExtra("image"), profileimage);
        }else{
            byte[] byteArray =getIntent().getByteArrayExtra("bitmap");
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            if (profileimage != null) {
                profileimage.setImageBitmap(bmp);
            }
        }
        if (getSupportActionBar() != null) {
            //getSupportActionBar().setTitle(getIntent().getStringExtra("fullname"));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.i(TAG, "onBackPressed " );
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
