package com.coupon.go.value_transfer;

import android.app.Activity;
import android.content.Intent;

import com.coupon.go.model.Coupon;

public class CouponTransferActivity {
    public static String TRANS_KEY = "couponl_key";

    public static void sendObject(Intent intent, Coupon object){
        try{
            intent.putExtra(TRANS_KEY, object);
        }catch (Exception e){
            e.printStackTrace();
        }
        //return intent;
    }

    public static Coupon getObject(Activity activity){
        Coupon object = null;
        try{
            object = (Coupon) activity.getIntent().getSerializableExtra(TRANS_KEY);
        } catch (Exception e){
            e.printStackTrace();
        }
        return object;
    }
}
