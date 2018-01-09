package com.coupon.go.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coupon.go.R;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by maz on 19-Jun-15.
 */
public class DialogProgress extends Dialog {

    private Activity activity;
    private LinearLayout ll_progress_txt;
    private TextView tv_msg, tv_dot;
    //private CircularProgressView giv_progress;
    private GifImageView giv_progress;
    private String txt = ".";
    private boolean hasMSG;
    private Thread t;

    public DialogProgress(Activity activity) {
        super(activity, R.style.full_screen_dialog);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //Always call before setContentView
        //Full screen is set for the Window
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.dialog_progress);


        ll_progress_txt = (LinearLayout) findViewById(R.id.ll_progress_txt);
        //giv_progress = (CircularProgressView) findViewById(R.id.giv_progress);
        giv_progress = (GifImageView) findViewById(R.id.giv_progress);
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        tv_dot = (TextView) findViewById(R.id.tv_dot);

       /* try {
            final int totalProgressTime = 10;
            t = new Thread() {
                @Override
                public void run() {
                    int jumpTime = 1;
                    while (jumpTime < totalProgressTime + 1) {
                        try {
                            sleep(300);
                            jumpTime += 1;

                            switch (jumpTime) {
                                case 1:
                                    txt = ".";
                                    break;
                                case 2:
                                    txt = "..";
                                    break;
                                case 3:
                                    txt = "...";
                                    break;
                                case 4:
                                    txt = "....";
                                    break;
                                case 5:
                                    txt = ".....";
                                    break;
                                case 6:
                                    txt = "......";
                                    break;
                                case 7:
                                    txt = ".......";
                                    break;
                                case 8:
                                    txt = "........";
                                    break;
                                case 9:
                                    txt = ".........";
                                    break;
                                case 10:
                                    txt = "..........";
                                    break;
                            }

                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        tv_dot.setText(txt);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            });


                            if (jumpTime > totalProgressTime) {
                                jumpTime = 1;
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            txt = ".";
                                            tv_dot.setText(txt);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });

                            }
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            };
            if (hasMSG)
                t.start();
        } catch (Exception e) {
            e.printStackTrace();
        }*/


    }

    public void setMessage(String msg) {
        try {
            if (msg == null || msg.equals("")) {
                hasMSG = false;
                ll_progress_txt.setVisibility(View.GONE);
                tv_msg.setVisibility(View.GONE);
            } else {
                hasMSG = true;
                tv_msg.setText(msg);
                if(t != null)
                    t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public static DialogProgress showProgressHud(Activity activity, String msg) {
        try {
            DialogProgress dialogProgress = new DialogProgress(activity);
            dialogProgress.show();
            if (msg != null || !msg.equals("")) {
                msg = "Please wait";
                //msg = null;
            }

            dialogProgress.setMessage(msg);

            return dialogProgress;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void dismissProgress(DialogProgress progressHud) {
        try {
            if (progressHud != null && progressHud.isShowing())
                progressHud.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
