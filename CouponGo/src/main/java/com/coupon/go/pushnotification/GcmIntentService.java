/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coupon.go.pushnotification;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.coupon.go.R;
import com.coupon.go.model.Message;
import com.coupon.go.module.base.CouponApplication;
import com.coupon.go.util.AppConstant;
import com.coupon.go.util.Util;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;


/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService {
    public static int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    //NotificationCompat.Builder builder;
    int REQUEST_CODE;
    Intent intent;
    CouponApplication mContext;
    Bundle extras;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {

            extras = intent.getExtras();
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
            // The getMessageType() intent parameter must be the intent you received
            // in your BroadcastReceiver.
            String messageType = gcm.getMessageType(intent);
            mContext = (CouponApplication) getApplicationContext();
            REQUEST_CODE = (int) System.currentTimeMillis();
            NOTIFICATION_ID = REQUEST_CODE;

            if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
                    /*
    	             * Filter messages based on message request_type. Since it is likely that GCM will be
    	             * extended in the future with new message types, just ignore any message types you're
    	             * not interested in, or that you don't recognize.
    	             */

                if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                    try{
                        sendNotification(extras.getString("message"));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                    try{
                        sendNotification(extras.getString("message"));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    // If it's a regular GCM message, do some work.
                } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                    try{
                        sendNotification(extras.getString("message"));
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
            // Release the wake lock provided by the WakefulBroadcastReceiver.
            GcmBroadcastReceiver.completeWakefulIntent(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        try{
            Util.showLog("get msg : ", msg.toString() + " ??????");
            mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            intent = new Intent(this, PushReceiverIntentService.class);
            intent.putExtra(AppConstant.NOTIFICATION_ID_KEY, NOTIFICATION_ID);
            intent.putExtra(AppConstant.PUSH_KEY, msg);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent contentIntentDriver = PendingIntent.getService(getApplicationContext(), REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            try {
                Message msgObject = new Gson().fromJson(msg, Message.class);
                Util.showLog("notification main clue : ", msgObject.main_clue);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(getNotificationIcon())
                                //.setLargeIcon(getNotificationIcon())
                                //.setContent(remoteViews)
                                .setContentText(msgObject.main_clue)
                        .setContentTitle("CouponGo");
                //.setStyle(new NotificationCompat.BigTextStyle().bigText(txt))
                mBuilder.setContentIntent(contentIntentDriver);
                Notification notification = mBuilder.build();
                // Play default notification sound
                // notification.defaults |= Notification.DEFAULT_SOUND;
                // Vibrate if vibrate is enabled
                notification.defaults |= Notification.DEFAULT_VIBRATE;
                mNotificationManager.notify(NOTIFICATION_ID, notification);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.icon_notification : R.drawable.icon_notification;
        //return useWhiteIcon ? R.drawable.app_icon : R.drawable.app_icon;
    }
}
