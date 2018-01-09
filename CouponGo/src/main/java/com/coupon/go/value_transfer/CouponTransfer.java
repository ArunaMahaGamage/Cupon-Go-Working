package com.coupon.go.value_transfer;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.coupon.go.model.Coupon;

public class CouponTransfer {
    public static final String TRANSFER_KEY = "CouponTransfer";

    public static Fragment sendObject(Fragment fragment, Coupon object){
        try{
            Bundle bundle = new Bundle();
            bundle.putSerializable(TRANSFER_KEY, object);
            fragment.setArguments(bundle);
        }catch (Exception e){
            e.printStackTrace();
        }
        return fragment;
    }

    public static Coupon getObject(Fragment fragment){
        Coupon object = null;
        try{
            object = (Coupon) fragment.getArguments().getSerializable(TRANSFER_KEY);
        }catch (Exception e){
            e.printStackTrace();
        }
        return object;
    }
}
