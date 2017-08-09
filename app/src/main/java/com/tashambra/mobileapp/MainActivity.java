package com.tashambra.mobileapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private RadioButton mMaleRadio;
    private RadioButton mFemaleRadio;

    private EditText mWeightEditText;
    private EditText mAlcholPercent;
    private EditText mAlcoholVolume;
    private Button mButton;
    private String mGender;
    private EditText mTimePassed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        FragmentManager fm = getSupportFragmentManager();
        Fragment existingFragment = fm.findFragmentById(R.id.container);
        SharedPreferences defaultsSharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String checkIf = defaultsSharedPref.getString("SettingsDone", "DEFAULT");
        if (!checkIf.equals("1"))  {
            if (existingFragment == null) {
                Fragment firstTimeFragment = new FirstTimeFragment();
                fm.beginTransaction().replace(R.id.container, firstTimeFragment).commit();
//                finish();
                return;
            }
        }

        Fragment newFragment = fm.findFragmentById(R.id.container);
//        if (existingFragment == null) {
            Fragment newWFragment = new GraphActivity();
            fm.beginTransaction().replace(R.id.container, newWFragment).commit();
//                finish();
//        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_manage) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences defaultsSharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        Log.i("shared_pref", defaultsSharedPref.getString("EmergencyContact", "DEFAULT"));
    }
}
