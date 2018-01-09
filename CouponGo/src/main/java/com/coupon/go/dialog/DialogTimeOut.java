package com.coupon.go.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.coupon.go.R;


/**
 * Created by maz on 19-Jun-15.
 */
public class DialogTimeOut extends Dialog implements View.OnClickListener {

    private Activity activity;
    public ImageView iv_go,iv_clues;


    public DialogTimeOut(Activity activity) {
        super(activity, R.style.normal_dialog);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //Always call before setContentView
        //Full screen is set for the Window
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.dialog_time_out);
        initView();
        setClickListeners();

    }


    private void initView() {
        iv_go = (ImageView) findViewById(R.id.iv_go);
        iv_clues = (ImageView) findViewById(R.id.iv_clues);
    }

    private void setClickListeners() {
        iv_go.setOnClickListener(this);
        iv_clues.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_go:
                dismiss();
                break;
            case R.id.iv_clues:
                dismiss();
                break;
        }

    }


    public static DialogTimeOut showDialog(Activity activity){
        DialogTimeOut dialog = new DialogTimeOut(activity);
        dialog.show();
        return dialog;
    }

}
