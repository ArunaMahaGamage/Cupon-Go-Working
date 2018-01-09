package com.coupon.go.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.coupon.go.R;
import com.coupon.go.model.Coupon;
import com.coupon.go.module.base.CouponApplication;
import com.coupon.go.util.Util;


/**
 * Created by maz on 19-Jun-15.
 */
public class DialogEnterCoupon extends Dialog implements View.OnClickListener {

    private Activity activity;
    private CouponApplication mContext;
    public ImageView iv_go,iv_back;
    private EditText et_coupon_code;
    private Coupon coupon;



    public DialogEnterCoupon(Activity activity) {
        super(activity, R.style.normal_dialog);
        this.activity = activity;
        mContext = (CouponApplication) activity.getApplicationContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //Always call before setContentView
        //Full screen is set for the Window
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.dialog_enter_coupon);
        //initView();
        //setClickListeners();
        //setSelectors();
    }


    private void initView() {
        iv_go = (ImageView) findViewById(R.id.iv_go);
        iv_back = (ImageView) findViewById(R.id.iv_back);
//        et_coupon_code = (EditText) findViewById(R.id.et_coupon_code);
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
                //redeemCouponTask();
                break;
            case R.id.iv_back:
                dismiss();
                break;
        }
    }


    public String getCouponCode(){
        try{
            if(et_coupon_code.getText().toString() != null
                    && !et_coupon_code.getText().toString().equals("")){
                return et_coupon_code.getText().toString();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }


    /*private void redeemCouponTask(){
        try{
            if(coupon != null){
                if(!getCouponCode().equals("")){
                    RedeemCouponRequestHandler redeemCouponRequestHandler = new RedeemCouponRequestHandler(false, true, activity, mContext);
                    String coupon_id = coupon.coupon_id;
                    String device_id = Util.getDeviceId(activity);
                    String coupon_code = getCouponCode();
                    HashMap<String, String> values = new HashMap<>();
                    values.put(ParamConstant.PARAM_COUPON_ID, coupon_id + "");
                    values.put(ParamConstant.PARAM_DEVICE_ID, device_id + "");
                    values.put(ParamConstant.PARAM_COUPON_CODE, coupon_code + "");
                    redeemCouponRequestHandler.callService(values, new IServerResponse() {
                        @Override
                        public void onSuccess(String response) {
                            try{
                                DialogEnterCoupon.this.dismiss();
                                CluesResponse cluesResponse = new Gson().fromJson(response, CluesResponse.class);
                                if(cluesResponse.response_code.equals("200")){
                                    final DialogSuccessCoupon dialogSuccessCoupon = DialogSuccessCoupon.showDialog(activity);
                                    dialogSuccessCoupon.setMessage(cluesResponse.response_msg, false);
                                    dialogSuccessCoupon.iv_back.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogSuccessCoupon.dismiss();
                                        }
                                    });
                                }else {
                                    final DialogSuccessCoupon dialogSuccessCoupon = DialogSuccessCoupon.showDialog(activity);
                                    dialogSuccessCoupon.setMessage(cluesResponse.response_msg, false);
                                    dialogSuccessCoupon.iv_back.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogSuccessCoupon.dismiss();
                                        }
                                    });
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(String message, String errorcode) {

                        }
                    });
                }else {
                    final DialogSuccessCoupon dialogSuccessCoupon = DialogSuccessCoupon.showDialog(activity);
                    dialogSuccessCoupon.setMessage("Coupon Code shouldn't Balnk !", false);
                    dialogSuccessCoupon.iv_back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogSuccessCoupon.dismiss();
                        }
                    });
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/


    public static DialogEnterCoupon showDialog(Activity activity){
        DialogEnterCoupon dialog = new DialogEnterCoupon(activity);
        dialog.show();
        return dialog;
    }

}
