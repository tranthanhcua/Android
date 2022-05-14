package com.course.labs.dailyselfie;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class SfRecordAdapter extends BaseAdapter {

    private ArrayList<SfRecord> mRecordList = new ArrayList<SfRecord>();
    private static LayoutInflater inflater = null;
    private Context mContext;

    public SfRecordAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);

        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (storageDir != null) {
            File[] selfieFiles = storageDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File file, String name) {
                    return name.endsWith(".jpg");
                }
            });

            for (File file : selfieFiles) {
                SfRecord selfieRecord = new SfRecord(file.getAbsolutePath(), file.getName());
                mRecordList.add(selfieRecord);
            }
        }
    }

    public int getCount() {
        return mRecordList.size();
    }

    public Object getItem(int position) {
        return mRecordList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View newView = convertView;
        final SfRecordView sfRecordView;

        final SfRecord currentRecord = mRecordList.get(position);

        if (null == convertView) {
            sfRecordView = new SfRecordView();
            newView = inflater.inflate(R.layout.sf_listitem, parent, false);
            sfRecordView.checkBoxSelected = (CheckBox) newView.findViewById(R.id.checkbox_selected);
            sfRecordView.thumbnail = (ImageView) newView.findViewById(R.id.thumbnail);
            sfRecordView.sfDate = (TextView) newView.findViewById(R.id.sf_date);
            newView.setTag(sfRecordView);
        }
        else {
            sfRecordView = (SfRecordView) newView.getTag();
        }

        sfRecordView.checkBoxSelected.setChecked(currentRecord.getSelected());
        sfRecordView.checkBoxSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                currentRecord.setSelected(isChecked);
            }
        });

        ImgHelper.setImgFromFilePath(currentRecord.getPath(), sfRecordView.thumbnail);
        sfRecordView.sfDate.setText(currentRecord.getDisplayName());

        return newView;
    }

    static class SfRecordView {
        CheckBox checkBoxSelected;
        ImageView thumbnail;
        TextView sfDate;
    }

    public void add(SfRecord sfRecord) {
        mRecordList.add(sfRecord);
        notifyDataSetChanged();
    }

    public ArrayList<SfRecord> getAllRecords() {
        return mRecordList;
    }

    public ArrayList<SfRecord> getSelectedRecords() {
        ArrayList<SfRecord> mSelectedRecordList = new ArrayList<>();
        for (SfRecord record : mRecordList) {
            if (record.getSelected()) {
                mSelectedRecordList.add(record);
            }
        }
        return mSelectedRecordList;
    }

    public void clearAll() {
        mRecordList.clear();
        notifyDataSetChanged();
    }

    public void clearSelected() {
        mRecordList.removeAll(getSelectedRecords());
        notifyDataSetChanged();
    }
}
