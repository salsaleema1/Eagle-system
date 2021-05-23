package com.workos.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.workos.activities.MainPageActivity;
import com.workos.adapters.UserSessionManager;
import com.workos.models.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


/**
 * Created by Murad on 07/09/2017.
 */

public class UserController {
    private String tag = "UserController";
    private static SharedPreferences mSharedPreferences;
    static Context context;
    static UserController userInstance = new UserController(context);

    RequestHandler requestHandler = RequestHandler.getRequestHandlerInstance();

    public static UserController getIns() {
        return userInstance;
    }

    public UserController(Context context1) {
        RequestHandler.init(context1);

    }

    public static void init(Context contex) {
        context = contex.getApplicationContext();
        mSharedPreferences = context.getSharedPreferences("AuthToken", Context.MODE_PRIVATE);

    }

    //get user info forom server through request handler
    public boolean login(ProgressDialog progressDialog, TextInputLayout userName, TextInputLayout password) {

        String userEnteredUsername = userName.getEditText().getText().toString().trim();
        String userEnteredPassword = password.getEditText().getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        final boolean[] result = {false};
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
                        result[0] = true;
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


        return result[0];
    }

    //now you can get the authentication token from User Controller
    public String getToken() {
        String result = null;
        UserSessionManager session = new UserSessionManager(context);
        HashMap<String, String> user = session.getUserDetails();
        ///   UserController userController=UserController.getIns();
        // get name
        String token = user.get(UserSessionManager.KEY_TOKEN);
        return token;
    }


    public void saveImageAsString(final String src) {

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
                    UserSessionManager sessionManager = new UserSessionManager(context);
                    if (result != null) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        result.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] b = baos.toByteArray();

                        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                        sessionManager.saveUserImage(encodedImage);
                        Log.i(tag, "encoded profile image " + encodedImage);
                    }
                    System.out.println("result==" + result);
                }
            }.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

    public boolean deleteUser(UserModel user) {
        return true;
    }

    //check that the update is valid at first then modify
    public boolean updateProfile() {

        return true;
    }

    public void saveGCMToken(ProgressDialog progressDialog, String token) {
        //requestHandler.saveGCMToken(progressDialog,token);
    }

    public void socketConnect() {
    }

    public void socketDisconnect() {
    }

}
