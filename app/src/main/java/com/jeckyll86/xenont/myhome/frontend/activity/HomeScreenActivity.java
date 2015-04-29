package com.jeckyll86.xenont.myhome.frontend.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.jeckyll86.xenont.myhome.R;
import com.jeckyll86.xenont.myhome.frontend.activity.home.HomeListActivity;
import com.jeckyll86.xenont.myhome.frontend.fragment.HomeScreenFragment;
import com.jeckyll86.xenont.myhome.frontend.fragment.SensorSummaryFragment;
import com.jeckyll86.xenont.myhome.frontend.setting.SettingsActivity;
import com.jeckyll86.xenont.myhome.utils.AppConstants;

public class HomeScreenActivity extends ActionBarActivity implements HomeScreenFragment.SelectionListener, SensorSummaryFragment.OnFragmentInteractionListener {

    private final static String TAG = HomeScreenActivity.class.getSimpleName();

    //home fragment
    private HomeScreenFragment leftHomeScreenFragment;
    //Fragment that will contain data from edison board
    private SensorSummaryFragment rightDataFragment;

    private final int PORT = 6666;
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.home_fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
            // Create a new Fragment to be placed in the activity layout
            leftHomeScreenFragment = new HomeScreenFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            //leftHomeScreenFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getFragmentManager().beginTransaction().add(R.id.home_fragment_container, leftHomeScreenFragment).commit();
        }
        //Check if we are using layout for large screens with two panels
        if (isInTwoPaneMode()) {
            url = getActivatedUrl();
            rightDataFragment = SensorSummaryFragment.newInstance(url);
            getFragmentManager().beginTransaction().add(R.id.right_home_fragment_container, rightDataFragment).commit();
            getFragmentManager().executePendingTransactions();

        }

    }

    // If there is right_home_fragment_container ID, then the application is in
    // two-pane mode
    private boolean isInTwoPaneMode() {
        return findViewById(R.id.right_home_fragment_container) != null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //if id == action_settings, start Setting activity
        if (id == R.id.action_settings) {
            Intent settingIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //handle interaction with right fragment
    @Override
    public void onHomeFragmentInteraction(Bundle extra) {
        String action = extra.getString(AppConstants.ARG_ACTION);
        switch (action){
            case AppConstants.DATA_FROM_SENSORS:
                String address = extra.getString(AppConstants.ARG_URL);
                showRightFragment();
                break;
            case AppConstants.GO_TO_HOME_LIST:
                switchToHomeList();
                break;
            default:
                break;
        }

    }

    //handle interaction with right fragment
    @Override
    public void onFragmentInteraction(String id) {

    }

    @Override
    public void onBackPressed() {

        Log.d(TAG, "Fragments on back stack" + getFragmentManager().getBackStackEntryCount());
        if (getFragmentManager().getBackStackEntryCount() != 0) {
            //if there is a fragment on stack, go to the fragment on top of the stack
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else if (getFragmentManager().getBackStackEntryCount() == 0) {
            // or just go back to main activity
            super.onBackPressed();
        }
    }

    private void showRightFragment(){
        url = getActivatedUrl();
        Log.i(TAG, "callback " + "two pane " + isInTwoPaneMode());
        if (!isInTwoPaneMode()) {
            Log.i(TAG, "Is not in two pane mode");
            rightDataFragment = SensorSummaryFragment.newInstance(url);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.home_fragment_container, rightDataFragment);
            ft.addToBackStack("Home_fragment");
            ft.commit();
            getFragmentManager().executePendingTransactions();

        }
        rightDataFragment.setUrl(url);
        rightDataFragment.updateFragment();

    }

    private void switchToHomeList(){
        Intent goToHomeListIntent = new Intent(this, HomeListActivity.class);
        startActivity(goToHomeListIntent);
    }


    private String getActivatedUrl(){

        String edisonAddress = leftHomeScreenFragment.getAddressAndPortFromSelectedSpinner();
        url = new StringBuilder("http://").append(edisonAddress).append("/rest/sensors").toString();
        Log.d(TAG, "GET " + url);
        return url;
    }

}
