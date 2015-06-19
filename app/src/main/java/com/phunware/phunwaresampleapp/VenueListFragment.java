package com.phunware.phunwaresampleapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class VenueListFragment extends Fragment {
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    private ListCallbacks mCallbacks;
    private int mActivatedPosition = ListView.INVALID_POSITION;
    private VenueViewAdapter mAdapter = new VenueViewAdapter(new ArrayList<Venue>());
    private RecyclerView mListRecyclerView;

    public interface ListCallbacks {
        void onItemSelected(long position);
    }

    public VenueListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View listView = inflater.inflate(R.layout.fragment_venue_list, container, false);

        mListRecyclerView = (RecyclerView)listView.findViewById(R.id.venues_recyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListRecyclerView.setLayoutManager(layoutManager);
        mListRecyclerView.setAdapter(mAdapter);

        return listView;
    }

    public void setVenues(List<Venue> venues) {
        mAdapter = new VenueViewAdapter(venues);
        mAdapter.notifyDataSetChanged();
        mListRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof ListCallbacks)) {
            throw new IllegalStateException("Activity must implement list fragment's callbacks.");
        }
        mCallbacks = (ListCallbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    private void setActivatedPosition(int position) {
        mActivatedPosition = position;
        mAdapter.notifyDataSetChanged();
    }

    private static final class VenueViewHolder extends RecyclerView.ViewHolder {
        TextView mVenueName;
        TextView mVenueAddress;

        public VenueViewHolder(View itemView) {
            super(itemView);
            mVenueName = (TextView)itemView.findViewById(R.id.venue_name);
            mVenueAddress = (TextView)itemView.findViewById(R.id.venue_address);
        }
    }

    private class VenueViewAdapter extends RecyclerView.Adapter<VenueViewHolder> {
        private List<Venue> mVenues;

        public VenueViewAdapter(List<Venue> venues) {
            if (venues == null) {
                throw new IllegalArgumentException("Venues list can't be null");
            }
            mVenues = venues;
        }

        @Override
        public VenueViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_venue, viewGroup, false);
            return new VenueViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(VenueViewHolder venueViewHolder, int i) {
            Venue venue = mVenues.get(i);

            String address = venue.getAddress() + ", " + venue.getCity() + ", " + venue.getState();
            venueViewHolder.mVenueAddress.setText(address);
            venueViewHolder.mVenueName.setText(venue.getName());
            venueViewHolder.itemView.setTag(venue.getId());
            final int position = i;
            venueViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallbacks.onItemSelected((long) view.getTag());
                    setActivatedPosition(position);
                }
            });

            if (i == mActivatedPosition) {
                venueViewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_200));
            }
        }

        @Override
        public void onViewRecycled(VenueViewHolder holder) {
            super.onViewRecycled(holder);
            holder.mVenueAddress.setText(null);
            holder.mVenueName.setText(null);
            holder.itemView.setTag(null);
            holder.itemView.setBackgroundColor(getResources().getColor(R.color.background_material_light));
        }

        @Override
        public int getItemCount() {
            return mVenues.size();
        }
    }
}
