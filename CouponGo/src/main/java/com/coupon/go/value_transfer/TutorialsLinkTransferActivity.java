package com.coupon.go.value_transfer;

import android.app.Activity;
import android.content.Intent;

import com.coupon.go.model.TutorialLink;
public class TutorialsLinkTransferActivity {
    public static String TRANS_KEY = "tutorial_key";

    public static void sendObject(Intent intent, TutorialLink object){
        try{
            intent.putExtra(TRANS_KEY, object);
        }catch (Exception e){
            e.printStackTrace();
        }
        //return intent;
    }

    public static TutorialLink getObject(Activity activity){
        TutorialLink object = null;
        try{
            object = (TutorialLink) activity.getIntent().getSerializableExtra(TRANS_KEY);
        } catch (Exception e){
            e.printStackTrace();
        }
        return object;
    }
}
