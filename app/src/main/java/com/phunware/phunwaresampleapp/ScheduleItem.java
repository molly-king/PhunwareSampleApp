package com.phunware.phunwaresampleapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ScheduleItem {
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss Z", Locale.getDefault());
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(
            "EEEE M/dd", Locale.US);
    private static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat(
            "h:mmaa", Locale.US);

    private Date mStartDate;
    private Date mEndDate;

    public ScheduleItem() {
        DATE_FORMATTER.setTimeZone(TimeZone.getDefault());
        TIME_FORMATTER.setTimeZone(TimeZone.getDefault());
    }

    public ScheduleItem(String startDate, String endDate) throws ParseException {
        this();
        mStartDate = FORMATTER.parse(startDate);
        mEndDate = FORMATTER.parse(endDate);
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public Date getEndDate() {
        return mEndDate;
    }

    public String getStartDateString() {
        return FORMATTER.format(mStartDate);
    }

    public String getEndDateString() {
        return FORMATTER.format(mEndDate);
    }

    public String getFullDateString() {
        String startDay = DATE_FORMATTER.format(mStartDate);
        String endDay = DATE_FORMATTER.format(mEndDate);
        String startTime = TIME_FORMATTER.format(mStartDate).toLowerCase();
        String endTime = TIME_FORMATTER.format(mEndDate).toLowerCase();
        if (!startDay.equalsIgnoreCase(endDay)) {
            endTime = endDay + " " + endTime;
        }
        return startDay + " " + startTime + " to " + endTime;
    }

    @Override
    public boolean equals(Object o) {
        boolean result = false;
        if (o instanceof ScheduleItem) {
            result = mStartDate.equals(((ScheduleItem) o).getStartDate())
                    && mEndDate.equals(((ScheduleItem) o).getEndDate());
        }
        return result;
    }

    @Override
    public int hashCode() {
        String s = getStartDateString() + getEndDateString();
        return s.hashCode();
    }

}
