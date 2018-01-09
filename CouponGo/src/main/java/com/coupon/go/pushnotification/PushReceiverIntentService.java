package com.coupon.go.pushnotification;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import com.coupon.go.model.Clue;
import com.coupon.go.model.Message;
import com.coupon.go.module.base.CouponApplication;
import com.coupon.go.module.detail.ClueDetailsActivity;
import com.coupon.go.util.AppConstant;
import com.coupon.go.value_transfer.ClueTransferActivity;
import com.google.gson.Gson;

import java.util.List;


public class PushReceiverIntentService extends IntentService {
    private int NOTIFICATION_ID;

    public PushReceiverIntentService() {
        super("PushReceiverIntentService");
    }

    public static final String TAG = "PushReceiverIntentService";
    public static CouponApplication mContext;
    public static String extras;

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            extras = intent.getExtras().getString(AppConstant.PUSH_KEY);
            mContext = (CouponApplication) getApplicationContext();
            NOTIFICATION_ID = intent.getExtras().getInt(AppConstant.NOTIFICATION_ID_KEY);
            navigateTo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void navigateTo() {
        try {
            Message msg = new Gson().fromJson(extras, Message.class);
            Clue clue = new Clue();
            clue.promo_id = msg.id + "";
            clue.main_clue = msg.main_clue;
            clue.pre_launch_clue = msg.pre_launch_clue;
            CancelNotification(mContext, NOTIFICATION_ID);
            Intent intent = new Intent(this, ClueDetailsActivity.class);
            ClueTransferActivity.sendObject(intent,clue);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(new Intent(intent));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean isApplicationForeBackground() {
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = mContext.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }


    private boolean isApplicationBackground() {
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = mContext.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    private void sendLocalNotification() {
        Intent broadcast = new Intent();
        broadcast.setAction(AppConstant.ACTION_LOCAL_PUSH);
        //broadcast.putExtra(AppConstant.PUSH_KEY, extras);
        sendBroadcast(broadcast);
    }

    private void goLogingActivity() {
        /*Intent intentDriver = new Intent(this, LoginActivity.class);
        intentDriver.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentDriver.putExtra(AppConstant.PUSH_KEY, extras);
        startActivity(intentDriver);*/
    }


    private void goDashBoardActivity() {
        /*Intent intentDriver = new Intent(this, DashBoardActivity.class);
        intentDriver.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentDriver.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentDriver.putExtra(AppConstant.PUSH_KEY, extras);
        startActivity(intentDriver);*/
    }

    public static void CancelNotification(Context mContext, int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) mContext.getSystemService(ns);
        nMgr.cancel(notifyId);
    }
}