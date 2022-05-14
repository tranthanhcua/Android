package com.course.labs.dailyselfie;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ActiMain extends Activity {

    private static final String LOG_TAG = ActiMain.class.getSimpleName();

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final long INTERVAL_TWO_MINUTES = 2 * 60 * 1000L;

    private SfRecordAdapter mAdapter;
    private String mCurrentSfName;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(LOG_TAG, "getExternalStorageState() = " + Environment.getExternalStorageState());

        ListView sfList = (ListView) findViewById(R.id.sf_list);

        mAdapter = new SfRecordAdapter(getApplicationContext());
        sfList.setAdapter(mAdapter);
        sfList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SfRecord sfRecord = (SfRecord) mAdapter.getItem(position);
                Intent intent = new Intent(ActiMain.this, ActiDetail.class);
                intent.putExtra(Intent.EXTRA_TEXT, sfRecord.getPath());
                startActivity(intent);
            }
        });
        createSfAlarm();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_camera) {
            dispatchTakePictureIntent();
            return true;
        }
        if (id == R.id.action_delete) {
            deleteSelectedSf();
            return true;
        }
        if (id == R.id.action_delete_all) {
            deleteAllSf();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private File createImgFile() throws IOException {
        mCurrentSfName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File imgFile = File.createTempFile(
                mCurrentSfName,
                ".png",
                getExternalFilesDir(null));

        mCurrentPhotoPath = imgFile.getAbsolutePath();
        return imgFile;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImgFile();
            } catch (IOException ex) {
            }

            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            File photoFile = new File(mCurrentPhotoPath);
            File sfFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), mCurrentSfName + ".jpg");
            photoFile.renameTo(sfFile);

            SfRecord sfRecord = new SfRecord(Uri.fromFile(sfFile).getPath(), mCurrentSfName);
            Log.d(LOG_TAG, sfRecord.getPath() + " - " + sfRecord.getDisplayName());
            mAdapter.add(sfRecord);
        } else {
            File photoFile = new File(mCurrentPhotoPath);
            photoFile.delete();
        }
    }

    private void deleteSelectedSf() {
        ArrayList<SfRecord> selectedSf = mAdapter.getSelectedRecords();
        for (SfRecord sfRecord : selectedSf) {
            File sfFile = new File(sfRecord.getPath());
            sfFile.delete();
        }
        mAdapter.clearSelected();
    }

    private void deleteAllSf() {
        for (SfRecord sfRecord : mAdapter.getAllRecords()) {
            File sfFile = new File(sfRecord.getPath());
            sfFile.delete();
        }
        mAdapter.clearAll();
    }

    private void createSfAlarm() {
        Intent intent = new Intent(this, SfNotification.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + INTERVAL_TWO_MINUTES,
                INTERVAL_TWO_MINUTES,
                pendingIntent);
    }
}
