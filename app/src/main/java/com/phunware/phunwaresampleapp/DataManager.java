package com.phunware.phunwaresampleapp;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataManager {

    private static ArrayList<Venue> mVenues;
    private static Map<Long, Venue> mVenueMap;
    private static DataManagerCallback mDelegate;

    public interface DataManagerCallback {
        void onDownloadCompleted(ArrayList<Venue> downloadedVenues);
        void onDownloadError(VolleyError volleyError);
    }

    public static ArrayList<Venue> getVenues(Context context) {
        if (mVenues != null) {
            return mVenues;
        } else {
            downloadVenues(context);
            if (context instanceof DataManagerCallback) {
                mDelegate = (DataManagerCallback)context;
            } else {
                Log.e("DataManager", "Must implement callback to get downloaded data");
            }
            return null;
        }
    }

    private static void downloadVenues(Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://s3.amazonaws.com/jon-hancock-phunware/nflapi-static.json";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                mVenues = processVenueDownload(s);
                if (mDelegate != null) {
                    mDelegate.onDownloadCompleted(mVenues);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Phunware", volleyError.getLocalizedMessage());
                if (mDelegate != null) {
                    mDelegate.onDownloadError(volleyError);
                }
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    private static ArrayList<Venue> processVenueDownload(String responseString) {
        ArrayList<Venue> result = new ArrayList<>();
        Map<Long, Venue> resultMap = new HashMap<>();
        try {
            JSONArray venueArray = new JSONArray(responseString);
            for (int i = 0; i < venueArray.length(); i++) {
                JSONObject venueObject = venueArray.getJSONObject(i);
                Venue venue = new Venue(venueObject);

                result.add(venue);
                resultMap.put(venue.getId(), venue);
            }
        } catch (JSONException e) {
            Log.e("Tarrytown", e.getLocalizedMessage());
        }
        mVenueMap = resultMap;
        return result;
    }

    public static Venue getVenueForId(Long id) {
        return mVenueMap.get(id);
    }
}
