package com.jeckyll86.xenont.myhome.frontend.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.jeckyll86.xenont.myhome.R;
import com.jeckyll86.xenont.myhome.adapter.HomeSpinnerAdapter;
import com.jeckyll86.xenont.myhome.datamodel.Home;
import com.jeckyll86.xenont.myhome.dbfacilities.HomesDataSource;
import com.jeckyll86.xenont.myhome.utils.AppConstants;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeScreenFragment extends Fragment implements View.OnClickListener {
private static final String TAG = HomeScreenFragment.class.getSimpleName();

    private Button getDataFromSensorButton;
    private Button showHomeList;
    private Spinner homeSpinner;
    private HomesDataSource homesDataSource;

    public interface SelectionListener {
        void onHomeFragmentInteraction(Bundle bundle);
    }


    private SelectionListener mCallback;

    public HomeScreenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        homesDataSource = new HomesDataSource(getActivity());
        View fragmentView = inflater.inflate(R.layout.fragment_home_screen, container, false);

        homeSpinner = (Spinner) fragmentView.findViewById(R.id.home_spinner);
        loadHomes();

        getDataFromSensorButton = (Button)fragmentView.findViewById(R.id.button_get_data);
        getDataFromSensorButton.setOnClickListener(this);

        showHomeList = (Button)fragmentView.findViewById(R.id.button_view_homes);
        showHomeList.setOnClickListener(this);
        return fragmentView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Make sure that the hosting Activity has implemented
        // the SelectionListener callback interface. We need this
        // because when an item in this ListFragment is selected,
        // the hosting Activity's onItemSelected() method will be called.

        try {

            mCallback = (SelectionListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement SelectionListener");
        }
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "Button Clicked " + v.getId());
        Bundle bundle = new Bundle();

        switch (v.getId()){
                case R.id.button_get_data:
                    bundle.putString(AppConstants.ARG_ACTION,AppConstants.DATA_FROM_SENSORS );
                    bundle.putString(AppConstants.ARG_URL, getAddressAndPortFromSelectedSpinner());
                    break;
                case R.id.button_view_homes:
                    bundle.putString(AppConstants.ARG_ACTION,AppConstants.GO_TO_HOME_LIST );
                    break;
                default:
                    break;
            }
        mCallback.onHomeFragmentInteraction(bundle);
    }

    //This is the customize adapter binding method as you can see
    //there is only a slight different if you compare with functino loadStudent_Simple.
    private void loadHomes(){
        HomeSpinnerAdapter homeAdapter;
        List<Home> homes = homesDataSource.getAllHomes();
        homeAdapter = new HomeSpinnerAdapter(getActivity(),
                R.layout.home_spinner_item , homes );
        homeSpinner.setAdapter(homeAdapter);

        homeAdapter.setDropDownViewResource(R.layout.home_spinner_item);

    }

    public String getAddressAndPortFromSelectedSpinner(){

        Home selectedHome = (Home) homeSpinner.getSelectedItem();
        String address = selectedHome.getAddress();
        int port = selectedHome.getPort();
        String addressAndPort = address + ":" + port;
        Log.d(TAG, "address from spinner: " + addressAndPort);
        return addressAndPort;

    }
}
