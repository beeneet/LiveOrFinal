package com.tashambra.mobileapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

/**
 * Created by bkoirlal on 8/8/17.
 */

public class FirstTimeFragment extends Fragment{

    private RadioButton mMaleRadio;
    private RadioButton mFemaleRadio;
    private Button mButton;
    private EditText mWeightEditText;
    private EditText mEmergencyContact;
    private EditText mEmergencyAddress;
    public SharedPreferences defaultsSharedPref;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.first_time_fragment, container, false);

//        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
//        ((ActionBarActivity)getActivity()).setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//
//        DrawerLayout drawer = (DrawerLayout) v.findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) v.findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
        defaultsSharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mMaleRadio = (RadioButton) v.findViewById(R.id.male_rb);
        mFemaleRadio = (RadioButton) v.findViewById(R.id.female_rb);
        mWeightEditText = (EditText) v.findViewById(R.id.weight_edit_text);
//        mAlcholPercent = (EditText) findViewById(R.id.alcoholp_edit_text) ;
//        mAlcoholVolume = (EditText) findViewById(R.id.volume_edit_text);
//        mTimePassed = (EditText) findViewById(R.id.time_edit_text);
        mEmergencyContact = (EditText) v.findViewById(R.id.emergencyC_edit_text) ;
        mEmergencyAddress = (EditText) v.findViewById(R.id.emergencya_edit_text);

        mButton = (Button) v.findViewById(R.id.button_1);
        mButton.setOnClickListener(new View.OnClickListener() {

            //Store to DB
            //Exit the fragment with null graph
            @Override
            public void onClick(View view){
                String mGender;
                if (mMaleRadio.isChecked()){
                     mGender = "male";
                } else {
                     mGender = "female";
                }
                SharedPreferences.Editor edit = defaultsSharedPref.edit();
                edit.clear();
                edit.putString("Gender", mGender);
                edit.putString("WeightInPounds", mWeightEditText.getText().toString());
                edit.putString("EmergencyContact", mEmergencyContact.getText().toString());
                edit.putString("EmergencyAddress", mEmergencyAddress.getText().toString());
                edit.putString("SettingsDone", "1");
                edit.putString("TotalCustomDrinks", "0");
                edit.apply();
                Toast.makeText(getContext(), "User details stored successfully", Toast.LENGTH_SHORT);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return v;
    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main2, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_manage) {
//            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
//            startActivity(intent);
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }













}
