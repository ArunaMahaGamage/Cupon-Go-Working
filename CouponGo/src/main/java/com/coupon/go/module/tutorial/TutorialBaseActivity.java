package com.coupon.go.module.tutorial;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.coupon.go.R;
import com.coupon.go.model.TutorialLink;
import com.coupon.go.model.response_wrapper.TutorialResponse;
import com.coupon.go.module.base.CouponApplication;
import com.coupon.go.module.dashboard.DashBoardActivity;
import com.coupon.go.module.tutorial.request_handler.TutorialRequestHandler;
import com.coupon.go.util.Util;
import com.coupon.go.value_transfer.TutorialsLinkTransferActivity;
import com.coupon.go.webservice.interfaces.IServerResponse;
import com.google.gson.Gson;

import java.util.HashMap;

import pl.droidsonroids.gif.GifImageView;


/**
 * Created by zabingo on 22/3/16.
 */
public class TutorialBaseActivity extends FragmentActivity {
    private final String TAG = "TutorialBaseActivity ";
    private CouponApplication mContext;
    private TutorialLink tutorialLink;
    private VideoView videoView;
    //private CircularProgressView giv_progress;
    private GifImageView giv_progress;
    private TextView btn_skip;
    private LinearLayout ll_fluent_logo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        try {
            mContext = (CouponApplication) getApplicationContext();
            setContentView(R.layout.activity_tutorial_container);
            initView();
            setVideoViewListeners();
            getObject();
            showVideoView(false);
            setFullScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mContext.couponGoPrefs.saveFirstLoggedIn(true);
        getTutorialTask();
    }

    private void getObject() {
        try {
            tutorialLink = TutorialsLinkTransferActivity.getObject(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initView() {
        ll_fluent_logo = (LinearLayout) findViewById(R.id.ll_fluent_logo);
        videoView = (VideoView) findViewById(R.id.videoView);
        //giv_progress = (CircularProgressView) findViewById(R.id.giv_progress);
        giv_progress = (GifImageView) findViewById(R.id.giv_progress);
        btn_skip = (TextView) findViewById(R.id.btn_skip);
    }


    private void setVideoViewListeners() {
        try {
            videoView.setOnInfoListener(onInfoToPlayStateListener);

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                // Close the progress bar and play the video
                public void onPrepared(MediaPlayer mp) {
                    showVideoView(true);
                    videoView.start();
                }
            });


            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Intent i = new Intent(TutorialBaseActivity.this, DashBoardActivity.class);
                    startActivity(i);
                    finish();
                }
            });

            btn_skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(TutorialBaseActivity.this, DashBoardActivity.class);
                    startActivity(i);
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private final MediaPlayer.OnInfoListener onInfoToPlayStateListener = new MediaPlayer.OnInfoListener() {

        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            if (MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START == what) {
                showVideoView(true);
            }
            if (MediaPlayer.MEDIA_INFO_BUFFERING_START == what) {
                showVideoView(false);
            }
            if (MediaPlayer.MEDIA_INFO_BUFFERING_END == what) {
                showVideoView(true);
            }
            return false;
        }
    };


    private void showVideoView(boolean shouldShowVideo) {
        if (videoView != null) {
            if (shouldShowVideo) {
                //videoView.setVisibility(View.VISIBLE);
                giv_progress.setVisibility(View.GONE);
                ll_fluent_logo.setVisibility(View.GONE);
            } else {
                //videoView.setVisibility(View.GONE);
                giv_progress.setVisibility(View.VISIBLE);
            }
        }
    }


    private void showTutorialVideo() {
        try {
            mContext.couponGoPrefs.saveFirstLogin(true);
            Util.showLog("Tutorial Link : ", tutorialLink.tutorial_link + " ?????????");
            // Insert your Video URL
            String VideoURL = "http://www.androidbegin.com/tutorial/AndroidCommercial.3gp";
            //String VideoURL = "http://devs.zabingo.com/SRV/SS/Change_Store_Logo.mp4";
            //String VideoURL = "http://bitcast-a.v1.bom1.bitgravity.com/saregamawsa/cross_synergy/video/LeaderSpeak-Conclave-MPEG-4_480x270.mp4";

            //String VideoURL = tutorialLink.tutorial_link;
            // Start the MediaController
            MediaController mediacontroller = new MediaController(TutorialBaseActivity.this);
            mediacontroller.setAnchorView(videoView);
            // Get the URL from String VideoURL
            Uri video = Uri.parse(VideoURL);
            videoView.setMediaController(mediacontroller);
            videoView.setVideoURI(video);

            videoView.setMediaController(null);

            videoView.requestFocus();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFullScreen() {
        try {
            ViewGroup.LayoutParams params = videoView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            videoView.requestLayout();
            videoView.setLayoutParams(params);
            /*DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            android.widget.FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) mLayout.getLayoutParams();
            params.width =  metrics.widthPixels;
            params.height = metrics.heightPixels;
            params.leftMargin = 0;
            videoView.setLayoutParams(params);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private void getTutorialTask() {
        try {
            TutorialRequestHandler tutorialRequestHandler = new TutorialRequestHandler(false,true,this,mContext);

            HashMap<String, String> value = new HashMap<>();
            value.put("","");

            tutorialRequestHandler.callService(value, new IServerResponse() {
                @Override
                public void onSuccess(String response) {
                    try {

                        TutorialResponse tutorialServerResponse = new Gson().fromJson(response, TutorialResponse.class);

                        if (tutorialServerResponse != null && tutorialServerResponse.response_code.equals("200")) {
                            if (tutorialServerResponse.response_data != null
                                    && tutorialServerResponse.response_data.size() > 0) {
                                tutorialLink = tutorialServerResponse.response_data.get(0);
                                if(tutorialLink != null){
                                    showTutorialVideo();
                                }else {
                                    showVideoView(true);
                                    Toast.makeText(mContext, "No Tutorial available for Selected Language.\nPlease try another language.",Toast.LENGTH_LONG).show();
                                }

                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String message, String errorcode) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (videoView != null) {
                videoView.suspend();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
