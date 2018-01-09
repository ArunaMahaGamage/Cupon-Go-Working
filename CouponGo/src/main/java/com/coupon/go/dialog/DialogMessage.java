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
public class DialogMessage extends Dialog implements View.OnClickListener {

    private Activity activity;
    public  ImageView iv_header,iv_back;
    private TextView tv_header;
    public  RelativeLayout rl_go;


    public DialogMessage(Activity activity) {
        super(activity, R.style.normal_dialog);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //Always call before setContentView
        //Full screen is set for the Window
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.dialog_message);
        initView();
        setClickListeners();
        setSelectors();
    }


    private void initView() {
        rl_go = (RelativeLayout) findViewById(R.id.rl_go);
        tv_header = (TextView) findViewById(R.id.tv_header);
        iv_header = (ImageView) findViewById(R.id.iv_header);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        Util.setTextViewScrollable(tv_header);
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
                tv_header.setText(msg);
            if(shouldShowImage)
                iv_header.setVisibility(View.VISIBLE);
            else
                iv_header.setVisibility(View.GONE);
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


    public static DialogMessage showDialog(Activity activity){
        DialogMessage dialog = new DialogMessage(activity);
        dialog.show();
        return dialog;
    }

    public static DialogMessage showDialog(Activity activity, boolean shouldShowGo){
        DialogMessage dialog = new DialogMessage(activity);
        dialog.show();
        if(shouldShowGo){
           dialog.rl_go.setVisibility(View.VISIBLE);
        }else {
            dialog.rl_go.setVisibility(View.GONE);
        }
        return dialog;
    }



    /*public void setCancelable(boolean isCancelable){
        try{
            this.setCanceledOnTouchOutside(isCancelable);
            this.setCancelable(isCancelable);
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/

}
