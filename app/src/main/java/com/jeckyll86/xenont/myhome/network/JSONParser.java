package com.jeckyll86.xenont.myhome.network;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by XenonT on 15/04/2015.
 */
public class JSONParser {
    private final static String TAG = JSONParser.class.getSimpleName();

    static JSONObject jObj = null;
    static String json = "";
    String response;
    // constructor
    public JSONParser() {
    }
    public JSONObject getJSONFromUrl(String url) throws IOException {
        // Making HTTP request
        try {
            ConnectionManager connectionManager = new ConnectionManager(url);
            response = connectionManager.sendGetRequest();

        } catch (IOException e) {
            //Log.d(TAG, e.printStackTrace());
            throw e;
        }
        json = response;

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        // return JSON String
        return jObj;
    }
}