package com.workos.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class RequestHandler {
    private final static String TAG = "RequestHandler";
    private String localhost = "http://192.168.1.115:3001";
    private boolean isConncted;

    public boolean isConncted() {
        return isConncted;
    }

    public static RequestHandler requestHandlerInstance = new RequestHandler();
    private static Context context;

    public static void init(Context contex) {

        context = contex;
    }

    public static RequestHandler getRequestHandlerInstance() {
        return requestHandlerInstance;
    }

    private RequestHandler() {
    }

    public String login(final ProgressDialog progressDialog, final String username, final String pass) {

        final String url = localhost + "/api/authenticate";

        Log.i(TAG, "log in req-- " + username + pass);
        Object responce;
        responce = null;

        try {
            responce = new AsyncTask<Void, Void, String>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialog.setCancelable(true);
                    progressDialog.show();
                }

                @Override
                // Making a request to url and getting response
                protected String doInBackground(Void... params) {
                    String jsonStr;
                    HttpHandler sh = HttpHandler.getInstance();
                    final JSONObject jsonParam = new JSONObject();
                    try {
                        jsonParam.put("password", pass);
                        jsonParam.put("username", username);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonStr = sh.postCall(url, jsonParam);
                    //   Log.i(TAG, jsonStr);

                    return jsonStr;
                }

                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                    progressDialog.dismiss();
                    System.out.println("result==" + result);
                }
            }.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if (responce != null)
            return responce.toString();
        else
            return null;
    }

}
