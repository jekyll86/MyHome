package com.jeckyll86.xenont.myhome.frontend.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jeckyll86.xenont.myhome.network.ConnectionManager;
import com.jeckyll86.xenont.myhome.R;
import com.jeckyll86.xenont.myhome.utils.AppConstants;
import com.jeckyll86.xenont.myhome.utils.SharedPrefUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

public class RegisterActivity extends ActionBarActivity {

    /**
     * Tag used on log messages.
     */
    private static final String TAG = RegisterActivity.class.getSimpleName();

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    private static final String SENDER_ID = "1094633352356";
    private GoogleCloudMessaging gcm;
    private Context context;
    private static final int PORT = 6666;
    private String edisonIpAddress;
    private String regid;

    private EditText mEditTextIp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        //mDisplay = (TextView) findViewById(R.id.display);
        mEditTextIp = (EditText) findViewById(R.id.ip_address);

        context = getApplicationContext();

        // Check device for Play Services APK. If check succeeds, proceed with
        //  GCM registration.
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = SharedPrefUtil.getProperty(context, SharedPrefUtil.PROPERTY_REG_ID);
            Log.d(TAG, "RegID: " + regid);
            Log.d(TAG, "EdisonIP: " + edisonIpAddress);
            edisonIpAddress = getRegiteredEdisonIP();

            if (!regid.isEmpty() && !edisonIpAddress.isEmpty()) {

                goToHomeScreen();
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
    }

    // You need to do the Play Services APK check here too.
    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    public void register(View buttonRegister) {
        edisonIpAddress = mEditTextIp.getText().toString();
        registerInBackground();
    }

    /**
     * Switch to the next activity
     */
    private void goToHomeScreen() {
        Intent intent = new Intent(this, HomeScreenActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
    /**
     * Registers the application with GCM servers asynchronously.
     * <p/>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, String, String>() {
            private ProgressDialog pDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                pDialog = new ProgressDialog(RegisterActivity.this);
                pDialog.setMessage("Registering...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }

            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }

                    if (regid.isEmpty()) {
                        regid = gcm.register(SENDER_ID);
                        msg = "Device registered, registration ID=" + regid;
                    }

                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                        try {
                            sendRegistrationIdToBackend();
                            // Persist the IP of edison - no need to register again.
                            SharedPrefUtil.storeRegistrationParameters(context, SharedPrefUtil.PROPERTY_EDISON_IP, edisonIpAddress);

                        } catch (Exception e) {
                            Log.e(TAG, "Error connecting to server");
                            Log.e(TAG, e.getMessage());
                            publishProgress(AppConstants.CONNECTION_ERROR);

                        }

                    // Persist the registration ID - no need to register again.
                    SharedPrefUtil.storeRegistrationParameters(context, SharedPrefUtil.PROPERTY_REG_ID, regid);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();

                    // If there is an error with, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                    //Display an error message to user
                    publishProgress(AppConstants.GOOGLE_CONNECTION_ERROR);
                }
                return msg;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
                String errorMessage = "";

                switch (values[0]){
                    case AppConstants.CONNECTION_ERROR:
                        errorMessage = getString(R.string.error_connecting_to_server);
                        break;
                    case AppConstants.GOOGLE_CONNECTION_ERROR:
                        errorMessage = "Error getting ID from Google Server";
                        break;
                    default:
                        errorMessage = "Network Error";
                        break;
                }

                Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onPostExecute(String msg) {
                pDialog.dismiss();
                Log.d(TAG, msg);
                if (!regid.isEmpty() && !getRegiteredEdisonIP().isEmpty()) {
                    goToHomeScreen();
                }
            }
        }.execute(null, null, null);
    }
    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
     * or CCS to send messages to your app.
     */
    private void sendRegistrationIdToBackend() throws Exception {

        String registrationUri = "http://"+edisonIpAddress+":"+PORT+"/register";

        //I use ConnectionManager class to send gcm RegId to Edison board with a http post request
        ConnectionManager connectionManager = new ConnectionManager(registrationUri);
        connectionManager.addParam("regID", regid);
        String response = connectionManager.sendRequest();

        if (response != null) {
            Log.d(TAG, response);
        }
    }

    /**
     *Get Ip address of Edison board from shared preferences
     */
    private String getRegiteredEdisonIP() {
        return SharedPrefUtil.getProperty(context, SharedPrefUtil.PROPERTY_EDISON_IP);
    }
}