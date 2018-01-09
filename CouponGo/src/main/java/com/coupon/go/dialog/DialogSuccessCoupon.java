package com.coupon.go.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coupon.go.R;
import com.coupon.go.util.Util;


/**
 * Created by maz on 19-Jun-15.
 */
public class DialogSuccessCoupon extends Dialog implements View.OnClickListener {

    private Activity activity;
    public ImageView iv_coupon,iv_back;
    private TextView tv_shop_name;
    public RelativeLayout rl_go;


    public DialogSuccessCoupon(Activity activity) {
        super(activity, R.style.normal_dialog);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //Always call before setContentView
        //Full screen is set for the Window
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.dialog_success_coupon);
        initView();
        setClickListeners();
        setSelectors();
    }


    private void initView() {
        rl_go = (RelativeLayout) findViewById(R.id.rl_go);
        tv_shop_name = (TextView) findViewById(R.id.tv_shop_name);
        iv_coupon = (ImageView) findViewById(R.id.iv_coupon);
        iv_back = (ImageView) findViewById(R.id.iv_back);
    }

    private void setClickListeners() {
        iv_back.setOnClickListener(this);
    }

    private void setSelectors(){
        Util.setSelector(activity, iv_back, R.drawable.back_ass, R.drawable.back_red);
    }

    public void setMessage(String msg, boolean shouldShowImage){
        try{
            if(msg != null)
                tv_shop_name.setText(msg);
            if(shouldShowImage)
                iv_coupon.setVisibility(View.VISIBLE);
            else
                iv_coupon.setVisibility(View.GONE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                dismiss();
                break;
        }

    }


    public static DialogSuccessCoupon showDialog(Activity activity){
        DialogSuccessCoupon dialog = new DialogSuccessCoupon(activity);
        dialog.show();
        return dialog;
    }

    public static DialogSuccessCoupon showDialog(Activity activity, boolean shouldShowGo){
        DialogSuccessCoupon dialog = new DialogSuccessCoupon(activity);
        dialog.show();
        if(shouldShowGo){
           dialog.rl_go.setVisibility(View.VISIBLE);
        }else {
            dialog.rl_go.setVisibility(View.GONE);
        }
        return dialog;
    }

}
