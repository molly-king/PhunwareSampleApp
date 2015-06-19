package com.phunware.phunwaresampleapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

public class Venue {
    private long mId;
    private int mPcode;
    private long mLatitude;
    private long mLongitude;
    private String mName;
    private String mAddress;
    private String mCity;
    private String mState;
    private String mZip;
    private String mPhone;

    private String mTollFreePhone;
    private String mDescription;
    private String mTicketLink;
    private String mImageUrl;
    private ArrayList<ScheduleItem> mSchedule;

    public Venue(JSONObject jsonObject) throws JSONException {
        mId = jsonObject.getLong("id");
        mPcode = jsonObject.getInt("pcode");
        mLatitude = jsonObject.getLong("latitude");
        mLongitude = jsonObject.getLong("longitude");
        mName = jsonObject.getString("name");
        mAddress = jsonObject.getString("address");
        mCity = jsonObject.getString("city");
        mState = jsonObject.getString("state");
        mZip = jsonObject.getString("zip");
        mPhone = jsonObject.getString("phone");

        mImageUrl = jsonObject.getString("image_url");
        mTollFreePhone = jsonObject.getString("tollfreephone");
        mDescription = jsonObject.getString("description");
        mTicketLink = jsonObject.getString("ticket_link");

        ArrayList<ScheduleItem> scheduleItems = new ArrayList<>();
        JSONArray scheduleArray = jsonObject.getJSONArray("schedule");
        for (int i = 0; i < scheduleArray.length(); i++) {
            JSONObject scheduleObject = scheduleArray.getJSONObject(i);
            String startDate = scheduleObject.getString("start_date");
            String endDate = scheduleObject.getString("end_date");
            try {
                ScheduleItem scheduleItem = new ScheduleItem(startDate, endDate);
                scheduleItems.add(scheduleItem);
            } catch (ParseException e) {
                Log.e("ScheduleItem", e.getLocalizedMessage());
            }
        }
        mSchedule = scheduleItems;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getTicketLink() {
        return mTicketLink;
    }

    public ArrayList<ScheduleItem> getSchedule() {
        return mSchedule;
    }

    public String getTollFreePhone() {
        return mTollFreePhone;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public String getAddress() {
        return mAddress;
    }

    public String getCity() {
        return mCity;
    }

    public String getState() {
        return mState;
    }

    public String getZip() {
        return mZip;
    }

    public String getPhone() {
        return mPhone;
    }

    public long getLatitude() {
        return mLatitude;
    }

    public long getLongitude() {
        return mLongitude;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Venue && ((Venue) o).getId() == mId) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Long.valueOf(mId).hashCode();
    }

    public int getPcode() {
        return mPcode;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

}
