package com.jeckyll86.xenont.myhome.utils;

/**
 * Created by XenonT on 19/04/2015.
 */
public interface AppConstants {


    /*
    JSON constants
     */
    String SENSOR_TAG = "sensors";
    String TEMPERATURE_JSON_TAG = "temperature";
    String LIGHT_JSON_TAG = "light";
    String AIR_QUALITY_JSON_TAG = "air_quality";
    String PPM_JSON_TAG = "ppm";

    /*
    Bundle ARG CONST
     */
    String ARG_URL = "url";
    String ARG_TEMPERATURE = "arg_temp";
    String ARG_LIGHT = "arg_light";
    String ARG_AIR = "arg_air";
    String ARG_PPM = "arg_ppm";

    String ARG_ROOM = "room";
    String ARG_HOME = "home";
    String ARG_DATE = "timestamp";

    String ARG_ID = "arg_id";

    String ARG_ACTION = "action";
    /*
    App specific constants
     */

    String DATA_FROM_SENSORS = "data_from_sensors";
    String CONNECTION_ERROR = "connection_error";
    String GOOGLE_CONNECTION_ERROR = "google_connection_error";
    String GO_TO_HOME_LIST = "go_to_home_list";


    int DEFAULT_PORT = 6666;

    /*
    Preferences
     */

    String EDISON_IP_PREFERENCE = "edison_ip";
    String GCM_ID_PREFERENCE = "registration_id";
}
