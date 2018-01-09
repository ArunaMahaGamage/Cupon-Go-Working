package com.coupon.go.util.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.coupon.go.util.AppConstant;


public class PushNotifyReceiver extends BroadcastReceiver {
    public interface IPushNotifyListener {
        public void onPushCome(Bundle extras);
    }
    private IPushNotifyListener iPushNotifyListener;
    private Bundle extras;

    @Override
    public void onReceive(Context ctx, Intent intent) {
        if(intent != null){
            if(this.iPushNotifyListener != null){
                extras = intent.getExtras().getBundle(AppConstant.PUSH_KEY);
                iPushNotifyListener.onPushCome(extras);
            }
        }

    }
    public void setPushListener(IPushNotifyListener iPushNotifyListener) {
        this.iPushNotifyListener = iPushNotifyListener;
    }
}