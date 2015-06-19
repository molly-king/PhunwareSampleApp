package com.phunware.phunwaresampleapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;


public class SampleActivity extends ActionBarActivity
        implements VenueListFragment.ListCallbacks, DataManager.DataManagerCallback {
    private boolean mTwoPaneMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .build();
        ImageLoader.getInstance().init(config);

        setContentView(R.layout.activity_venue_list);
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getSupportFragmentManager().findFragmentById(R.id.venue_detail_container) != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                } else {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(getSupportFragmentManager().getBackStackEntryCount() > 0);
                }
            }
        });

        if (isInternetConnectionAvailable()) {
            ArrayList<Venue> downloadedVenues = DataManager.getVenues(this);
            if (downloadedVenues != null) {
                VenueListFragment listFragment = (VenueListFragment) getSupportFragmentManager().findFragmentById(R.id.venue_list);
                listFragment.setVenues(downloadedVenues);
            }
        } else {
            Toast.makeText(this, "Internet connection needed to see venues", Toast.LENGTH_LONG).show();
        }
        if (findViewById(R.id.venue_detail_container) != null) {
            mTwoPaneMode = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return getSupportFragmentManager().popBackStackImmediate();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDownloadCompleted(ArrayList<Venue> downloadedVenues) {
        VenueListFragment listFragment = (VenueListFragment)getSupportFragmentManager().findFragmentById(R.id.venue_list);
        listFragment.setVenues(downloadedVenues);
    }

    @Override
    public void onDownloadError(VolleyError volleyError) {
        Toast.makeText(this, "Download error: " + volleyError.getLocalizedMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemSelected(long id) {
        Bundle arguments = new Bundle();
        arguments.putLong(VenueDetailFragment.ARG_ITEM_ID, id);
        VenueDetailFragment fragment = new VenueDetailFragment();
        fragment.setArguments(arguments);

        if (mTwoPaneMode){
            VenueListFragment listFragment = (VenueListFragment)getSupportFragmentManager().findFragmentById(R.id.venue_list);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.venue_detail_container, fragment)
                    .commit();

        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.venue_list, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private boolean isInternetConnectionAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
