 package com.jeckyll86.xenont.myhome.frontend.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.jeckyll86.xenont.myhome.R;

 public class AlertActivity extends ActionBarActivity {
    private static final String TAG = AlertActivity.class.getSimpleName();

     private TextView dateText;
     private TextView roomText;
     private String room;
     private String dateString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        Bundle extrasFromNotification = getIntent().getExtras();
        Log.d(TAG, "extra " + extrasFromNotification.toString());


        dateText = (TextView) findViewById(R.id.date_of_intrusion);
        roomText = (TextView) findViewById(R.id.room_of_intrusion);

        room = extrasFromNotification.getString("room");
        dateString = extrasFromNotification.getString("timestamp");
             dateText.setText(dateString);
             roomText.setText(room);


     }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alert, menu);
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

        return super.onOptionsItemSelected(item);
    }
}
