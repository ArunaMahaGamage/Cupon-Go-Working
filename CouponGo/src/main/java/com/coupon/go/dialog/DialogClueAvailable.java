package com.coupon.go.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.coupon.go.R;
import com.coupon.go.util.AppConstant;
import com.coupon.go.util.CountDownTimerWithPause;
import com.coupon.go.util.Util;

import java.util.Date;

import jp.wasabeef.blurry.Blurry;


/**
 * Created by maz on 19-Jun-15.
 */
public class DialogClueAvailable extends Dialog implements View.OnClickListener {

    private Activity activity;
    //private LinearLayout ll_root;
    private TextView tv_title, tv_hour, tv_hour_txt, tv_minute, tv_minute_txt, tv_second, tv_second_txt;
    private ImageView iv_back;

    private String utcTime = "";
    private Date startDate = Util.getDateFromString("2016-08-11 21:01:18", AppConstant.DATE_FORMATTER_CLUE_START);
    private Date curdate = Util.getCurrentDate(AppConstant.DATE_FORMATTER_CLUE_START);
    private long startMilliSec;
    private long curMilliSec;
    private long diffMilliSec;

    private CountDownTimerWithPause timerWithPause;


    public DialogClueAvailable(Activity activity) {
        //super(activity, R.style.normal_dialog);
        super(activity, R.style.full_screen_dialog);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //Always call before setContentView
        //Full screen is set for the Window
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.dialog_clue_available);
        initView();
        setClickListeners();
        setImageSelector();
        //startTimer();
    }


    private void initView() {
        //ll_root = (LinearLayout) findViewById(R.id.ll_root);
        tv_title = (TextView) findViewById(R.id.tv_main_clue);
        tv_hour = (TextView) findViewById(R.id.tv_hour);
        tv_hour_txt = (TextView) findViewById(R.id.tv_hour_txt);
        tv_minute = (TextView) findViewById(R.id.tv_minute);
        tv_minute_txt = (TextView) findViewById(R.id.tv_minute_txt);
        tv_second = (TextView) findViewById(R.id.tv_second);
        tv_second_txt = (TextView) findViewById(R.id.tv_second_txt);
        iv_back = (ImageView) findViewById(R.id.iv_back);

    }

    private void setClickListeners() {
        iv_back.setOnClickListener(this);
    }

    private void setImageSelector() {
        Util.setSelector(activity, iv_back, R.drawable.back_ass, R.drawable.back_red);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackClick();
                break;
        }

    }

    public void setTexts(String header) {
        try {
            if (tv_title != null)
                tv_title.setText(header);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUtcTime(String utcTime) {
        this.utcTime = utcTime;
    }


    public void startTimer(String utcTime, long startMilliSec) {
        try {

            try {
                Blurry.with(activity)
                        .radius(25)
                        .sampling(2)
                        .async()
                        .animate(500)
                        .onto((ViewGroup) findViewById(R.id.rl_content));
            } catch (Exception e) {
                e.printStackTrace();
            }

            //startDate = Util.getDateFromString("2016/08/19 19:40:39", AppConstant.DATE_FORMATTER_CLUE_START);
            //startDate = Util.getDateFromString(utcTime, AppConstant.DATE_FORMATTER_CLUE_START);
            //startMilliSec = Util.getMillisecondFromDate(startDate);
            curMilliSec = Util.getMillisecondFromDate(curdate);
            diffMilliSec = startMilliSec - curMilliSec;
            //Util.showLog("startMilliSec ", startMilliSec + "");
            //Util.showLog("curMilliSec ", curMilliSec + "");
            //set a new Timer
            timerWithPause = new CountDownTimerWithPause(diffMilliSec, AppConstant.SCAN_TIME_INTERVAL, false) {
                @Override
                public void onTick(long millisUntilFinished) {
                    try {
                        //diffMilliSec = diffMilliSec - AppConstant.SCAN_TIME_INTERVAL;
                        diffMilliSec = millisUntilFinished;
                        final long diffSeconds = diffMilliSec / 1000 % 60;
                        final long diffMinutes = diffMilliSec / (60 * 1000) % 60;
                        final long diffHours = diffMilliSec / (60 * 60 * 1000) % 24;

                        long diffDays = diffMilliSec / (24 * 60 * 60 * 1000);

                        Util.showLog("diffHours : ", diffHours + " ???????");
                        Util.showLog("diffMinutes : ", diffMinutes + " ???????");


                        tv_second.setText(diffSeconds + "");
                        tv_hour.setText(diffHours + "");
                        tv_minute.setText(diffMinutes + "");

                        /*activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_second.setText(diffSeconds + "");
                                tv_hour.setText(diffHours + "");
                                tv_minute.setText(diffMinutes + "");
                            }
                        });*/

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFinish() {
                    try {

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            };

            timerWithPause.create();

            resumeTimer();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resumeTimer() {
        try {
            //set a new Timer
            if (timerWithPause != null) {
                timerWithPause.resume();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pauseTimer() {
        try {
            //set a new Timer
            if (timerWithPause != null)
                timerWithPause.pause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void onBackClick() {
        try {
            this.dismiss();
            /*if(activity != null ){
                this.dismiss();
                activity.finish();
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static DialogClueAvailable showDialog(Activity activity) {
        DialogClueAvailable dialog = new DialogClueAvailable(activity);
        dialog.show();
        return dialog;
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //onBackClick();
    }


    @Override
    protected void onStop() {
        super.onStop();
        try {
            if (timerWithPause != null) {
                timerWithPause.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
