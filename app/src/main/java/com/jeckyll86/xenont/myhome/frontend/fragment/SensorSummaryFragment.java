package com.jeckyll86.xenont.myhome.frontend.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jeckyll86.xenont.myhome.R;
import com.jeckyll86.xenont.myhome.frontend.fragment.abstracts.AbstractRightDataFragment;
import com.jeckyll86.xenont.myhome.network.JSONParser;
import com.jeckyll86.xenont.myhome.network.JSONToMapDeserializer;
import com.jeckyll86.xenont.myhome.utils.AppConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SensorSummaryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SensorSummaryFragment extends AbstractRightDataFragment {
    private static final String TAG = SensorSummaryFragment.class.getSimpleName();
    private OnFragmentInteractionListener mListener;
    private Gson gson;
    private String url;
    private JSONArray sensors = null;


    /*
    Text views in witch we are showing values from sensors
     */
    TextView temperatureTextView;
    TextView ligthTextView;
    TextView airTextView;
    TextView ppmTextView;
    TextView humanReadableAirQualityTextView;
    private String temperature;
    private String light;
    private String airQuality;
    private String ppm;


    public SensorSummaryFragment() {
        // Required empty public constructor
    }


    public static SensorSummaryFragment newInstance(String urlParam) {
        SensorSummaryFragment fragment = new SensorSummaryFragment();
        Bundle args = new Bundle();
        args.putString(AppConstants.ARG_URL, urlParam);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        url = getArguments().getString(AppConstants.ARG_URL);

        // Configure Gson
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(new TypeToken<Map<String, String>>() {}.getType(), new JSONToMapDeserializer());
        gson = gsonBuilder.create();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_refresh_data, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.refresh_item:
                this.updateFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View sensorSummaryView = inflater.inflate(R.layout.fragment_sensor_summary, container, false);
        temperatureTextView = (TextView) sensorSummaryView.findViewById(R.id.text_temperature);
        ligthTextView = (TextView) sensorSummaryView.findViewById(R.id.text_light);
        airTextView = (TextView) sensorSummaryView.findViewById(R.id.text_Air_Aquality);
        ppmTextView = (TextView) sensorSummaryView.findViewById(R.id.text_ppm);
        humanReadableAirQualityTextView = (TextView) sensorSummaryView.findViewById(R.id.text_Air_Aquality_human_readable);

        if (savedInstanceState != null){
            temperature = savedInstanceState.getString(AppConstants.ARG_TEMPERATURE);
            light = savedInstanceState.getString(AppConstants.ARG_LIGHT);
            airQuality = savedInstanceState.getString(AppConstants.ARG_AIR);
            ppm = savedInstanceState.getString(AppConstants.ARG_PPM);

            updateValuesOnScreen();
        }

        return sensorSummaryView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(AppConstants.ARG_TEMPERATURE, temperature);
        outState.putString(AppConstants.ARG_AIR, airQuality);
        outState.putString(AppConstants.ARG_LIGHT, light);
        outState.putString(AppConstants.ARG_PPM, ppm);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog((Activity) mListener);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser jParser = new JSONParser();
            // Getting JSON from URL
            JSONObject json = null;
            try {
                json = jParser.getJSONFromUrl(args[0]);
            } catch (IOException e) {
                //Toast.makeText(getActivity().getApplicationContext(), getString(R.string.error_connecting_to_server), Toast.LENGTH_SHORT);
                publishProgress(AppConstants.CONNECTION_ERROR);
                Log.e(TAG, "Error connecting to " + url);
                e.printStackTrace();
            }
            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();

            if (json != null){

                try {
                    // Getting JSON Array from URL
                    sensors = json.getJSONArray(AppConstants.SENSOR_TAG);
                    Log.d(TAG, "JSON object " + sensors.toString());

                    Map<String, String> sensorValues = gson.fromJson(sensors.toString(), new TypeToken<Map<String, String>>() {
                    }.getType());
                    Log.d(TAG, sensorValues.toString());

                    temperature = sensorValues.get(AppConstants.TEMPERATURE_JSON_TAG);
                    light = sensorValues.get(AppConstants.LIGHT_JSON_TAG);
                    airQuality = sensorValues.get(AppConstants.AIR_QUALITY_JSON_TAG);
                    ppm = sensorValues.get(AppConstants.PPM_JSON_TAG);
                    
                    updateValuesOnScreen();


                } 
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }}

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if(AppConstants.CONNECTION_ERROR.equals(values[0])) {
                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.error_connecting_to_server), Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private void updateValuesOnScreen(){
        temperatureTextView.setText(temperature);
        ligthTextView.setText(light);
        airTextView.setText(airQuality);
        ppmTextView.setText(ppm);
    }

    @Override
    public void updateFragment(){
        new JSONParse().execute(url);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String id);
    }

    public void setUrl(String url){
        this.url = url;
    }

}
