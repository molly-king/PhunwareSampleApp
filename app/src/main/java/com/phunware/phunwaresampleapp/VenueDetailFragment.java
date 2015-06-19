package com.phunware.phunwaresampleapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by mollyrand on 6/18/15.
 */
public class VenueDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";

    private static DisplayImageOptions mImageOptions = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .build();

    private Venue mItem;

    public VenueDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(ARG_ITEM_ID)) {
            mItem = DataManager.getVenueForId(getArguments().getLong(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_venue_detail, container, false);

        if (mItem != null) {
            ImageView imageView = (ImageView)rootView.findViewById(R.id.venue_image);
            ImageLoader.getInstance().displayImage(mItem.getImageUrl(), imageView, mImageOptions);
            TextView venueName = (TextView) rootView.findViewById(R.id.venue_detail_name);
            TextView venueAddress = (TextView) rootView.findViewById(R.id.venue_detail_address);
            TextView venueCityState = (TextView) rootView.findViewById(R.id.venue_detail_city);
            ListView venueSchedule = (ListView) rootView.findViewById(R.id.venue_schedule_list);

            venueName.setText(mItem.getName());
            venueAddress.setText(mItem.getAddress());
            venueCityState.setText(mItem.getCity() + ", " + mItem.getState() + " " + mItem.getZip());
            venueSchedule.setDividerHeight(0);
            venueSchedule.setAdapter(new ScheduleAdapter(getActivity(), R.layout.item_schedule, mItem.getSchedule()));
            setHasOptionsMenu(true);
            if (getActivity() instanceof ActionBarActivity) {
                ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("Details");
            }
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.details_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, mItem.getName() + " " + mItem.getAddress());
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class ScheduleAdapter extends ArrayAdapter<ScheduleItem> {
        private Context mContext;
        private ArrayList<ScheduleItem> mScheduleItems;
        private int mResId;

        public ScheduleAdapter(Context context, int resId, ArrayList<ScheduleItem> scheduleItems) {
            super(context, resId, scheduleItems);
            mContext = context;
            mScheduleItems = scheduleItems;
            mResId = resId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ScheduleHolder holder = null;

            if(row == null)
            {
                LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
                row = inflater.inflate(mResId, parent, false);

                holder = new ScheduleHolder();
                holder.scheduleTextView = (TextView)row.findViewById(R.id.schedule_text);

                row.setTag(holder);
            }
            else
            {
                holder = (ScheduleHolder)row.getTag();
            }

            ScheduleItem item = mScheduleItems.get(position);
            holder.scheduleTextView.setText(item.getFullDateString());

            return row;
        }

        private class ScheduleHolder {
            TextView scheduleTextView;
        }
    }
}
