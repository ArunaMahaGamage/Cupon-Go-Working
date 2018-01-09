package com.coupon.go.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.coupon.go.R;
import com.coupon.go.model.Coupon;
import com.coupon.go.util.Util;


/**
 * Created by maz on 19-Jun-15.
 */
public class DialogUseCoupon extends Dialog implements View.OnClickListener {

    private Activity activity;
    public ImageView iv_go,iv_back;
    private TextView tv_shop_name;
    private Coupon coupon;


    public DialogUseCoupon(Activity activity) {
        super(activity, R.style.normal_dialog);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //Always call before setContentView
        //Full screen is set for the Window
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.dialog_use_coupon);
        initView();
        setClickListeners();
        setSelectors();
    }


    private void initView() {
        iv_go = (ImageView) findViewById(R.id.iv_go);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_shop_name = (TextView) findViewById(R.id.tv_shop_name);
    }

    private void setClickListeners() {
        iv_go.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    private void setSelectors(){
        Util.setSelector(activity, iv_back, R.drawable.back_ass, R.drawable.back_red);
    }


    public void setCoupon(Coupon coupon){
        try{
            this.coupon = coupon;
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public Coupon getCoupon(){
        return coupon;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_go:
                dismiss();
                break;
            case R.id.iv_back:
                dismiss();
                break;
        }

    }


    public static DialogUseCoupon showDialog(Activity activity){
        DialogUseCoupon dialog = new DialogUseCoupon(activity);
        dialog.show();
        return dialog;
    }

}
