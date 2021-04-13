package com.workos.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

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
    public boolean login(ProgressDialog progressDialog, String username, String pass) {
        boolean result = false;

        //use the request handler to connect to the server and get the response of the login
        String response = requestHandler.login(progressDialog, username, pass);

        try {
            if (response != null) {
                //jason objet to parse the jason response string
                JSONObject jsonResponce = new JSONObject(response);
                // from the return value of the response, get the value of success (true or false)
                if ((Boolean) jsonResponce.get("success")) {
                    //   Log.i(tag, "the logged in user ID :" + jsonResponce.get("userID").toString());
                    //get the authintication token and save it in the sharedpreferences
                    UserSessionManager sessionManager = new UserSessionManager(context);
                    sessionManager.saveToken(jsonResponce.get("token").toString());

                    JSONObject userinfo = jsonResponce.getJSONObject("userinfo");
                    Log.i(tag, "the logged in user ID :" + userinfo.getString("userID"));
                    sessionManager.saveUserID(userinfo.getString("userID"));

                    sessionManager.saveUserinfo(userinfo.getString("email"), userinfo.getString("first"),
                            userinfo.getString("last"), userinfo.getString("gender"), userinfo.getString("phone")
                            , userinfo.getString("first") + " " + userinfo.getString("last"));
                    //sessionManager.saveUserID(jsonResponce.get("userID").toString());
                    Log.i(tag,"user frofile image "+userinfo.getString("image"));

                    saveImageAsString(userinfo.getString("image"));
                    result = true;

                } else {
                    Toast.makeText(context, jsonResponce.get("message").toString(), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
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


    public void saveImageAsString(final String src ) {

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
                    if(result!=null) {
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
