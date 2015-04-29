package com.jeckyll86.xenont.myhome.frontend.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


import com.jeckyll86.xenont.myhome.R;
import com.jeckyll86.xenont.myhome.datamodel.Home;
import com.jeckyll86.xenont.myhome.dbfacilities.HomesDataSource;
import com.jeckyll86.xenont.myhome.frontend.fragment.home.HomeDetailFragment;
import com.jeckyll86.xenont.myhome.frontend.fragment.home.HomeListFragment;

import java.util.List;

/**
 * An activity representing a list of Homes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link HomeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link HomeListFragment} and the item details
 * (if present) is a {@link HomeDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link HomeListFragment.HomeListListener} interface
 * to listen for item selections.
 */
public class HomeListActivity extends ActionBarActivity
        implements HomeListFragment.HomeListListener, HomeDetailFragment.HomeDetailListener, View.OnClickListener {

    private static final int CREATE_REQUEST = 1;
    private static final String TAG = HomeListActivity.class.getSimpleName();
    HomesDataSource homesDataSource;
    ImageButton createNewHomeButton;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homesDataSource = new HomesDataSource(this);
       // homesDataSource.open();

        setContentView(R.layout.activity_home_list);


        createNewHomeButton = (ImageButton) findViewById(R.id.create_new_home_fab);
        createNewHomeButton.setOnClickListener(this);


        if (findViewById(R.id.home_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((HomeListFragment) getFragmentManager()
                    .findFragmentById(R.id.home_list))
                    .setActivateOnItemClick(true);
        }

    }



   /* @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "OnResume reopen datasource");
        homesDataSource.open();
    }

    @Override
    public void onPause() {
        homesDataSource.close();
        Log.d(TAG, "OnPause close datasource");
        super.onPause();
    }*/

    /**
     * Callback method from {@link HomeListFragment.HomeListListener}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(long id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putLong(HomeDetailFragment.ARG_HOME_ID, id);
            HomeDetailFragment fragment = new HomeDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.home_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, HomeDetailActivity.class);
            detailIntent.putExtra(HomeDetailFragment.ARG_HOME_ID, id);
            startActivity(detailIntent);
        }
    }

    @Override
    public List<Home> getAllHomes() {
        return homesDataSource.getAllHomes();
    }


    @Override
    public Home getHomeById(long id) {
       return homesDataSource.getHomeById(id);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        switch (viewId){
            case R.id.create_new_home_fab:
                Intent goToEditOrCreateActivityIntent = new Intent(this, EditOrCreateActivity.class);
                startActivityForResult(goToEditOrCreateActivityIntent, CREATE_REQUEST);
                break;
            default:
                break;

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult");
        String message = "";
        if (requestCode == CREATE_REQUEST){

            if(resultCode == RESULT_OK){
                message = getString(R.string.home_created);
                reloadDataList();
            } else if (resultCode == RESULT_CANCELED){
                message = getString(R.string.home_not_created);
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }


    }

    private void reloadDataList(){
        Log.d(TAG, "reloadList");
                ((HomeListFragment) getFragmentManager()
                        .findFragmentById(R.id.home_list)).dataSetChanged();
    }
}
