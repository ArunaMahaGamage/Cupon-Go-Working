package com.coupon.go.pushnotification;

import android.app.Activity;
import android.os.AsyncTask;

import com.coupon.go.module.base.CouponApplication;
import com.coupon.go.util.AppConstant;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by Ahmed on 1/30/2015.
 */
public class DeviceRegistration {
    public IDeviceRegistration iDeviceRegistration;

    public static void registerInBackground(final CouponApplication context, final Activity activity, final IDeviceRegistration iDeviceRegistration) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                GoogleCloudMessaging gcm = null;
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    String regid = msg = context.device_token = gcm.register(AppConstant.GCM_SENDER_ID);
                    //Util.showLog("Reg id : ", regid + "<><><><><>");
                    msg = "Device registered, registration ID=" + regid;
                    return regid;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (iDeviceRegistration != null)
                    iDeviceRegistration.onRegComplete(context.device_token);
            }
        }.execute(null, null, null);
    }

}
