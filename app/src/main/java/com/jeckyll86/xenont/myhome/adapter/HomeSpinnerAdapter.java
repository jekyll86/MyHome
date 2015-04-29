package com.jeckyll86.xenont.myhome.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeckyll86.xenont.myhome.datamodel.Home;

import java.util.List;

/**
 * Created by XenonT on 28/04/2015.
 */

public class HomeSpinnerAdapter extends ArrayAdapter<Home> {

    private Context mContext;
    private List<Home> mHomes;

    public HomeSpinnerAdapter(Context context,
                                 int textViewResourceId, List<Home> objects) {
        super(context, textViewResourceId, objects);
        this.mContext = context;
        this.mHomes = objects;
    }

    @Override
    public int getCount(){
        return mHomes.size();
    }

    @Override
    public Home getItem(int position){
        return mHomes.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //This is for the first item before dropdown or default state.
        TextView label = new TextView(mContext);
        label.setTextColor(Color.BLACK);
        label.setTextSize(18);
        label.setText(" " + mHomes.get(position).getName());
        label.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        label.setGravity(Gravity.LEFT | Gravity.CENTER);
        label.setPadding(0,20,0,20);
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        //This is when user click the spinner and list of item display
        // beneath it
        TextView label = new TextView(mContext);
        label.setTextColor(Color.BLACK);
        label.setTextSize(18);
        label.setText(" " + mHomes.get(position).getName());
        label.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        label.setGravity(Gravity.LEFT | Gravity.CENTER );
        label.setPadding(0,0,0,20);

        return label;
    }
}
