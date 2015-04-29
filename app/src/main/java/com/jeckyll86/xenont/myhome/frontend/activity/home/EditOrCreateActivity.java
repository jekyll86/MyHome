package com.jeckyll86.xenont.myhome.frontend.activity.home;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.jeckyll86.xenont.myhome.R;
import com.jeckyll86.xenont.myhome.datamodel.Home;
import com.jeckyll86.xenont.myhome.dbfacilities.HomesDataSource;
import com.jeckyll86.xenont.myhome.utils.AppConstants;

public class EditOrCreateActivity extends ActionBarActivity {

    private HomesDataSource homesDataSource;
    private EditText nameText;
    private EditText addressText;
    private EditText portText;

    Home myHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_or_create_actvity);
        Intent createOrEditIntent = getIntent();
        long homeId = createOrEditIntent.getLongExtra(AppConstants.ARG_ID, -1);

    if (homeId != -1){
        myHome = homesDataSource.getHomeById(homeId);
    } else {
        myHome = new Home();
    }

        nameText = (EditText) findViewById(R.id.home_name_edit);
        nameText.setText(myHome.getName());
        addressText = (EditText) findViewById(R.id.home_address_edit);
        addressText.setText(myHome.getAddress());
        portText = (EditText) findViewById(R.id.home_port_edit);
        if (myHome.getPort() != null) {
            portText.setText(myHome.getPort());
        }

        homesDataSource = new HomesDataSource(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_or_create_actvity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_save){

            if (isValid()){
                Intent returnIntent = new Intent();
                //returnIntent.putExtra("result",result);
                createOrUpdateHome();
                setResult(RESULT_OK,returnIntent);
                finish();
            }
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

/*    @Override
    public void onResume() {
        super.onResume();
        homesDataSource.open();
    }

    @Override
    public void onPause() {
        homesDataSource.close();
        super.onPause();
    }*/

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        finish();
    }

    private void createOrUpdateHome(){

        if (isValid()) {
            String homeName = nameText.getText().toString();
            myHome.setName(homeName);

            String homeaddress = addressText.getText().toString();
            myHome.setAddress(homeaddress);

            if (!portText.getText().toString().trim().isEmpty()){
                int homePort = Integer.valueOf(portText.getText().toString());
                myHome.setPort(homePort);
        }

            if (myHome.getId() != null) {
                homesDataSource.updateHome(myHome);
            } else {
                homesDataSource.createHome(myHome);
            }
        }
    }

    private boolean isValid(){
        boolean isValid = true;
        if( nameText.getText().toString().trim().equals(""))
        {
            isValid = false;
            nameText.setError( getString(R.string.home_name_required) );
            //You can Toast a message here that the Username is Empty
        }

        if( addressText.getText().toString().trim().equals(""))
        {
            isValid = false;
            addressText.setError( getString(R.string.address_required) );
            //You can Toast a message here that the Username is Empty
        }
        return isValid;
    }
}
