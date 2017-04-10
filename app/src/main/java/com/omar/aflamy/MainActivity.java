package com.omar.aflamy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private MoviesFragment moviesFragment;
    private SharedPreferences sharedPref;
    private FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        // ensure that pref is set with default values, the false arg means only call this first time and never override the user pref
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        // if there is savedInstanceState before (when the screen rotated), we get the instance of the fragment from it
        // instead of creating new one
        if(savedInstanceState==null)
            moviesFragment = new MoviesFragment();
        else
            moviesFragment = (MoviesFragment) getSupportFragmentManager().getFragment(savedInstanceState,"F");
        moviesFragment.setSort(sharedPref.getString(Aflamy.SORT_PREF_KEY, Aflamy.SORT_POPULARITY));
        // save if tablet or mobile mode
        Aflamy.TABLET_MODE = findViewById(R.id.fragment_tablet) != null;
    }

    @Override
    public void onResume() {
        super.onResume();
        try{
            if(sharedPref.getString(Aflamy.SORT_PREF_KEY, Aflamy.SORT_POPULARITY).equals("FAVORITES"))
                moviesFragment = new MoviesFragment();
            moviesFragment.setSort(sharedPref.getString(Aflamy.SORT_PREF_KEY, Aflamy.SORT_POPULARITY));
            fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.movies_fragment, moviesFragment);
            fragmentTransaction.commit();
        } catch (RuntimeException e){

        }
    }

    @Override
    public void onSaveInstanceState(Bundle state) {

        // save current fragment instance before rotating the screen to get it back in activity recreation
        getSupportFragmentManager().putFragment(state,"F",moviesFragment);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(state);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPref, String key) {
        moviesFragment = new MoviesFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.sort_criteria:{
                startActivity(new Intent(this, SettingActivity.class));
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
