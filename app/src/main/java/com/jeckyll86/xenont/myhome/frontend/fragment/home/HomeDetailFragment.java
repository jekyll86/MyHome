package com.jeckyll86.xenont.myhome.frontend.fragment.home;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeckyll86.xenont.myhome.R;

import com.jeckyll86.xenont.myhome.datamodel.Home;
import com.jeckyll86.xenont.myhome.frontend.activity.home.HomeDetailActivity;
import com.jeckyll86.xenont.myhome.frontend.activity.home.HomeListActivity;


/**
 * A fragment representing a single Home detail screen.
 * This fragment is either contained in a {@link HomeListActivity}
 * in two-pane mode (on tablets) or a {@link HomeDetailActivity}
 * on handsets.
 */
public class HomeDetailFragment extends Fragment {



    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_HOME_ID = "home_id";

    /**
     * The Home content this fragment is presenting.
     */
    private Home mHome;


    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private HomeDetailListener mCallbacks = null;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HomeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if (getArguments().containsKey(ARG_HOME_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mHome = mCallbacks.getHomeById(getArguments().getLong(ARG_HOME_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mHome != null) {
            ((TextView) rootView.findViewById(R.id.home_detail)).setText(mHome.getName());
        }

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof HomeDetailListener)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (HomeDetailListener) activity;
    }


    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface HomeDetailListener {
        /**
         * Callback for when an item has been selected.
         */
        public Home getHomeById(long id);

    }

}
