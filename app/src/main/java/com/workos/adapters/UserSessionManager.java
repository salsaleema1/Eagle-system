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

    private static final String IS_USER_LOGGED_IN = "IsUserLoggedIn";

    private static final String PREF_NAME = "profilePref";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_FULLNAME = "fullname";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_TOKEN = "authkey";
    public static final String KEY_DATE = "date";
    public static final String KEY_IMAGE_URL = "image";

    public UserSessionManager(Context con) {
        context = con;
        shpref = con.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = shpref.edit();
    }

    public void createUserLoginSession(String name, String fullname, String email, String phone, String password, String date, String photoUrl) {

        editor.putBoolean(IS_USER_LOGGED_IN, true);
        editor.putString(KEY_USERNAME, name);
        editor.putString(KEY_FULLNAME, fullname);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_DATE, date);
        editor.putString(KEY_IMAGE_URL, photoUrl);

        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {

        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_USERNAME, shpref.getString(KEY_USERNAME, null));
        user.put(KEY_FULLNAME, shpref.getString(KEY_FULLNAME, null));
        user.put(KEY_EMAIL, shpref.getString(KEY_EMAIL, null));
        user.put(KEY_PHONE, shpref.getString(KEY_PHONE, null));
        user.put(KEY_PASSWORD, shpref.getString(KEY_PASSWORD, null));
        user.put(KEY_DATE, shpref.getString(KEY_DATE, null));
        user.put(KEY_IMAGE_URL, shpref.getString(KEY_IMAGE_URL, null));

        return user;
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

    public void logoutUser() {

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent loginIntent = new Intent(context, LoginActivity.class);

        // Closing all the Activities
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        context.startActivity(loginIntent);
    }
}
