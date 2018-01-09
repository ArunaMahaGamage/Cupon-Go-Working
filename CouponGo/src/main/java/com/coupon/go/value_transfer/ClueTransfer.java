package com.coupon.go.value_transfer;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.coupon.go.model.Clue;

public class ClueTransfer {
    public static final String TRANSFER_KEY = "CouponTransfer";

    public static Fragment sendObject(Fragment fragment, Clue object){
        try{
            Bundle bundle = new Bundle();
            bundle.putSerializable(TRANSFER_KEY, object);
            fragment.setArguments(bundle);
        }catch (Exception e){
            e.printStackTrace();
        }
        return fragment;
    }

    public static Clue getObject(Fragment fragment){
        Clue object = null;
        try{
            object = (Clue) fragment.getArguments().getSerializable(TRANSFER_KEY);
        }catch (Exception e){
            e.printStackTrace();
        }
        return object;
    }
}
