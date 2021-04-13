package com.workos.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.workos.activities.LoginActivity;

import java.util.HashMap;

/**
 * Created by Murad on 07/31/2017.
 */

public class UserSessionManager {
    SharedPreferences shpref;

    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "profilePref";
    private static final String IS_USER_LOGGED_IN = "IsUserLoggedIn";
    public static final String KEY_NAME = "name";
    public static final String KEY_TOKEN = "authkey";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_FIRST = "first";
    public static final String KEY_LAST = "last";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_ID = "ID";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_FULLNAME = "fullname";
    public static final String KEY_IMAGE = "image";

    public UserSessionManager(Context con) {
        context = con;
        shpref = con.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = shpref.edit();
    }

    public void createUserLoginSession(String name) {

        editor.putBoolean(IS_USER_LOGGED_IN, true);
        editor.putString(KEY_NAME, name);
        editor.commit();
    }

    public void saveToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.commit();//commit what you want to save to sharedPreferences
    }

    public void saveUserID(String userID) {
        editor.putString(KEY_ID, userID);
        editor.commit();//commit what you want to save to sharedPreferences
    }
    public void saveUserImage(String userImage) {
        editor.putString(KEY_IMAGE, userImage);
        editor.commit();//commit what you want to save to sharedPreferences
    }

    public void saveUserinfo(String email, String first, String last, String gender, String phone,String fullname) {
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_FIRST, first);
        editor.putString(KEY_LAST, last);
        editor.putString(KEY_GENDER, gender);
        editor.putString(KEY_FULLNAME, fullname);

        editor.commit();
    }

    public boolean isUserLoggedIn() {
        return shpref.getBoolean(IS_USER_LOGGED_IN, false);
    }

    public boolean checkLogin() {
        if (!this.isUserLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(context, LoginActivity.class);

            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            context.startActivity(i);

            return true;
        }
        return false;
    }

    public HashMap<String, String> getUserDetails() {

        //Use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<String, String>();

        // user name
        user.put(KEY_NAME, shpref.getString(KEY_NAME, null));
        user.put(KEY_TOKEN, shpref.getString(KEY_TOKEN, null));
        user.put(KEY_FULLNAME, shpref.getString(KEY_FULLNAME, null));
        // user email id
        user.put(KEY_EMAIL, shpref.getString(KEY_EMAIL, null));
        user.put(KEY_PHONE, shpref.getString(KEY_PHONE, null));
        user.put(KEY_ID, shpref.getString(KEY_ID, null));
        user.put(KEY_IMAGE, shpref.getString(KEY_IMAGE, null));

        // return user
        return user;
    }

    public void logoutUser() {

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(context, LoginActivity.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        context.startActivity(i);
    }
}
