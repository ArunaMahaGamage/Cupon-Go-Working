package com.coupon.go.value_transfer;

import android.app.Activity;
import android.content.Intent;

import com.coupon.go.model.Clue;

public class ClueTransferActivity {
    public static String TRANS_KEY = "couponl_key";

    public static void sendObject(Intent intent, Clue object){
        try{
            intent.putExtra(TRANS_KEY, object);
        }catch (Exception e){
            e.printStackTrace();
        }
        //return intent;
    }

    public static Clue getObject(Activity activity){
        Clue object = null;
        try{
            object = (Clue) activity.getIntent().getSerializableExtra(TRANS_KEY);
        } catch (Exception e){
            e.printStackTrace();
        }
        return object;
    }
}
