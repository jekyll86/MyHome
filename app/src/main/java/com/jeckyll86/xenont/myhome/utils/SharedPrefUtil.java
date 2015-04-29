package com.jeckyll86.xenont.myhome.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by XenonT on 15/04/2015.
 */
public class SharedPrefUtil {

    private static final String TAG = SharedPrefUtil.class.getName();

    public static final String PROPERTY_REG_ID = "registration_id";
    public static final String PROPERTY_EDISON_IP = "edison_ip";
    private static final String PROPERTY_APP_VERSION = "appVersion";

    private SharedPrefUtil(){
        //Utility class, do not instantiate it
    }



    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param value registration ID
     */
    public static void storeRegistrationParameters(Context context, String property, String value) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving property " + property +  " on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(property, value);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private static SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the registration ID in your app is up to you.
        return PreferenceManager.getDefaultSharedPreferences(context);
    }



    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }


    /**
     * Gets the current registration ID for application on GCM service.
     * <p/>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     */
    public static String getProperty(Context context, String property) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String propertyValue = prefs.getString(property, "");
        if (propertyValue.isEmpty()) {
            Log.d(TAG, "Registration of " + property + " not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return propertyValue;
    }

}
