package com.coupon.go.module.splash;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.coupon.go.R;
import com.coupon.go.module.base.CouponApplication;
import com.coupon.go.module.dashboard.DashBoardActivity;
import com.coupon.go.module.splash.request_handler.DeviceRegistrationRequestHandler;
import com.coupon.go.module.tutorial.AppGuideActivity;
import com.coupon.go.module.tutorial.TutorialBaseActivity;
import com.coupon.go.pushnotification.DeviceRegistration;
import com.coupon.go.pushnotification.IDeviceRegistration;
import com.coupon.go.util.ParamConstant;
import com.coupon.go.util.Util;
import com.coupon.go.webservice.interfaces.IServerResponse;
import com.craftar.CraftARSDK;

import java.util.HashMap;

public class SplashActivity extends AppCompatActivity {

    private CouponApplication mContext;
    protected LocationManager locationManager;
    private String device_token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = (CouponApplication) getApplicationContext();
        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        deviceRegistration();
        CraftARSDK.Instance().init(getApplicationContext());
        goToNextActivity();
    }

    private void goToNextActivity(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!mContext.couponGoPrefs.isFirstLoggedIn()){
                    startActivity(new Intent(SplashActivity.this, AppGuideActivity.class));
                    SplashActivity.this.finish();
                }else {
                    startActivity(new Intent(SplashActivity.this, DashBoardActivity.class));
                    SplashActivity.this.finish();
                }
            }
        }, 2000);
    }



    private void getDeviceRegistrationTask() {
        try {
            DeviceRegistrationRequestHandler deviceRegistrationRequestHandler = new DeviceRegistrationRequestHandler(false, false, this, mContext);
            String android_id = Util.getDeviceId(this);

            HashMap<String,String> values = new HashMap<>();
            //values.put(ParamConstant.PARAM_AGENT_ID, "");
            values.put(ParamConstant.PARAM_DEVICE_ID, android_id);
            values.put(ParamConstant.PARAM_DEVICE_TOKEN, device_token);
            values.put(ParamConstant.PARAM_DEVICE_TYPE, "Android");
            deviceRegistrationRequestHandler.callService(values, new IServerResponse() {

                @Override
                public void onSuccess(String response) {
                    try {
                        //mContext.couponGoPrefs.saveFirstLoggedIn(true);
                        //startActivity(new Intent(SplashActivity.this, TutorialBaseActivity.class));
                        //SplashActivity.this.finish();

//                        if (response != null && !"".equals(response)) {
//                            GetHistoryServerResponse getHistoryServerResponse = new Gson().fromJson((response), GetHistoryServerResponse.class);
//                            if (getHistoryServerResponse.response_code.equals("200")) {
//                                mContext.fluentPrefs.saveFirstLoggedIn(true);
//                            }
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String message, String errorcode) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deviceRegistration() {
        DeviceRegistration.registerInBackground(mContext, SplashActivity.this, new IDeviceRegistration() {
            @Override
            public void onRegComplete(String regId) {
                device_token = regId;
                Util.showLog("Splash GCM Reg : ", device_token + "<><><><><>");
                getDeviceRegistrationTask();
            }
        });


    }




}
