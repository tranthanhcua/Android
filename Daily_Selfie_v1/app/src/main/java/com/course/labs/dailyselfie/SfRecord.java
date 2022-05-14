package com.course.labs.dailyselfie;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SfRecord {
    private String mPath;
    private String mName;
    private boolean mSelected;

    public SfRecord(String path, String name) {
        mPath = path;
        mName = name;
        mSelected = false;
    }

    public String getPath() {
        return mPath;
    }

    public String getDisplayName() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date date = format.parse(mName, new ParsePosition(0));
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
    }

    public boolean getSelected() {
        return mSelected;
    }

    public void setSelected(boolean selected) {
        mSelected = selected;
    }

    @Override
    public String toString() {
        return mName;
    }
}
