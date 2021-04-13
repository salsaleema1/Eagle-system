package com.workos.api;

/**
 * Created by Murad on 07/10/2017.
 * <p>
 * Created by z3roc0ol on 6/19/2017.
 */

/**
 * Created by z3roc0ol on 6/19/2017.
 */

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HttpHandler {

    private static final String TAG = "HttpHandler";
    private BufferedOutputStream printout;
    UserController userController=UserController.getIns();
    static HttpHandler httpHandlerIns=new HttpHandler();
    public static final HttpHandler getInstance()
    {
        return httpHandlerIns;
    }
    public HttpHandler() {
    }




    public String postCall(String reqUrl,JSONObject jsonParam) {
        String response = null;
        HttpURLConnection conn = null;
        Log.i(TAG, " " +jsonParam.toString());
        try {

            URL url = new URL(reqUrl);

            ///open connection from the url
           conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            //place the request headers
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            conn.setConnectTimeout(5000);
            conn.connect();

            //pbut request body
            printout = new BufferedOutputStream(conn.getOutputStream());
            printout.write(jsonParam.toString().getBytes());
            printout.flush ();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);
            printout.close ();

        } catch (java.net.SocketTimeoutException e) {

            return "Connection Timeout ...";
        }  catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());

        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }finally {
            conn.disconnect();
        }
        return response;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}