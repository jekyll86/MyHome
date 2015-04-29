package com.jeckyll86.xenont.myhome.frontend.setting;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.jeckyll86.xenont.myhome.R;


public class SettingsActivity extends PreferenceActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    private void doPositiveClick(){

    }

    private void doNegativeClick(){
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Negative click!");
    }


    public static class MyPreferenceFragment extends PreferenceFragment {
        Preference reloadGcmId;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences_connection);
            reloadGcmId =  findPreference("reloadGcmId");
            reloadGcmId.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    showDialog();
                    return false;
                }
            });
        }

        void showDialog() {
            DialogFragment newFragment = ComfirmDialogFragment.newInstance("Do you want regenerate ID? ");
            newFragment.show(getFragmentManager(), "dialog");
        }
    }


    public static class ComfirmDialogFragment extends DialogFragment {

        public static ComfirmDialogFragment newInstance(String title) {
            ComfirmDialogFragment frag = new ComfirmDialogFragment();
            Bundle args = new Bundle();
            args.putString("title", title);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String title = getArguments().getString("title");

            return new AlertDialog.Builder(getActivity())
                    //.setIcon(R.drawable.icon)
                    .setTitle(title)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ((SettingsActivity) getActivity()).doPositiveClick();
                                }
                            }
                    )
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ((SettingsActivity) getActivity()).doNegativeClick();
                                    dialog.cancel();
                                }
                            }
                    )
                    .create();
        }
    }

}