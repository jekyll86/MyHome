package com.jeckyll86.xenont.myhome.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.jeckyll86.xenont.myhome.R;
import com.jeckyll86.xenont.myhome.frontend.activity.AlertActivity;
import com.jeckyll86.xenont.myhome.frontend.activity.RegisterActivity;
import com.jeckyll86.xenont.myhome.receiver.GcmBroadcastReceiver;
import com.jeckyll86.xenont.myhome.utils.AppConstants;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by XenonT on 10/04/2015.
 */
public class GcmIntentService extends IntentService {
    private static final String TAG = GcmIntentService.class.getName();
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent");
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                sendNotification(intent.getExtras());
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, RegisterActivity.class), 0);


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_gcm)
                        .setContentTitle("GCM Notification")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg)
                        .setAutoCancel(true);


        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void sendNotification(Bundle extras) {
        String room = extras.getString(AppConstants.ARG_ROOM);
        String dateString = extras.getString(AppConstants.ARG_DATE);
        String home = extras.getString(AppConstants.ARG_HOME);


        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent toAlertActivityIntent = new Intent(this, AlertActivity.class);
        toAlertActivityIntent.putExtras(extras);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, toAlertActivityIntent
                , PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.alert_icon_sma)
                        .setContentTitle(getString(R.string.intrusion_detected))
                        .setColor(Color.BLACK)
                        .setContentText(getString(R.string.intrusion_detected))
                        .setAutoCancel(true)
                        .setLights(Color.BLUE, 500, 500);
        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(getString(R.string.details));
        inboxStyle.addLine(getString(R.string.home) + home);
        inboxStyle.addLine(getString(R.string.room) + room);
        inboxStyle.addLine(getString(R.string.date) + dateString);
        mBuilder.setStyle(inboxStyle);
        long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
        mBuilder.setVibrate(pattern);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

}
