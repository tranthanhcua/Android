package com.course.labs.dailyselfie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class SfNotification extends BroadcastReceiver {

    public static final int SELFIE_NOTIFICATION_ID = 1;

    private Intent mNotificationIntent;
    private PendingIntent mPendingIntent;
    private final Uri soundURI = Uri
            .parse("android.resource://course.examples.Alarms.AlarmCreate/"
                    + R.raw.alarm_rooster);

    @Override
    public void onReceive(Context context, Intent intent) {
        mNotificationIntent = new Intent(context, ActiMain.class);
        mPendingIntent = PendingIntent.getActivity(context, 0, mNotificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification.Builder notificationBuilder = new Notification.Builder(context)
                .setTicker("Mình cùng selfie nào!!!")
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText("Mình cùng selfie nào!!!")
                .setContentIntent(mPendingIntent)
                .setSound(soundURI);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(SELFIE_NOTIFICATION_ID, notificationBuilder.build());
    }
}
