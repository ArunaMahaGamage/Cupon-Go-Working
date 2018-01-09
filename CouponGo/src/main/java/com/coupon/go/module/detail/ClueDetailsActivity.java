package com.coupon.go.module.detail;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coupon.go.R;
import com.coupon.go.dialog.DialogClueAvailable;
import com.coupon.go.model.Clue;
import com.coupon.go.model.Message;
import com.coupon.go.model.response_wrapper.CluesDetailResponse;
import com.coupon.go.module.arcamera.ARCameraActivity;
import com.coupon.go.module.base.CouponApplication;
import com.coupon.go.module.dashboard.DashBoardActivity;
import com.coupon.go.module.detail.request_handler.CluesDetailRequestHandler;
import com.coupon.go.module.maps.MapFragment;
import com.coupon.go.module.scan.ScanActivity;
import com.coupon.go.table_manager.CouponTableManagerTask;
import com.coupon.go.util.AppConstant;
import com.coupon.go.util.ParamConstant;
import com.coupon.go.util.Util;
import com.coupon.go.util.Util.Operation;
import com.coupon.go.util.reciever.PushNotifyReceiver;
import com.coupon.go.value_transfer.ClueTransfer;
import com.coupon.go.value_transfer.ClueTransferActivity;
import com.coupon.go.webservice.interfaces.IServerResponse;
import com.google.gson.Gson;

import java.util.Date;
import java.util.HashMap;

import static com.coupon.go.value_transfer.ClueTransferActivity.TRANS_KEY;

/**
 * Created by zabingo on 2/8/16.
 */
public class ClueDetailsActivity extends FragmentActivity implements PushNotifyReceiver.IPushNotifyListener {
    private CouponApplication mContext;
    private RelativeLayout rl_root;
    //private ImageView iv_blur;
    private TextView tv_main_clue;
    private ImageView iv_back, iv_direction, iv_go, iv_ar_camera;
    private Clue clue;
    private MapFragment mapFragment;

    private Date startDate = Util.getDateFromString("2016-08-11 21:01:18", AppConstant.DATE_FORMATTER_CLUE_START);
    private Date curdate = Util.getCurrentDate(AppConstant.DATE_FORMATTER_CLUE_START);
    private long startMilliSec;
    private long curMilliSec;
    private long diffMilliSec;

    private PushNotifyReceiver pushNotifyReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mContext = (CouponApplication) getApplicationContext();
        setContentView(R.layout.activity_detail);
        getObject();
        initView();
        setClickListeners();
        startBrodcastReciever();
        //showDialog();

    }

    @Override
    public void onResume() {
        super.onResume();
        getClueDetailTask();
    }


    private void getObject() {
        try {
            clue = ClueTransferActivity.getObject(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        //iv_blur = (ImageView) findViewById(R.id.iv_blur);
        rl_root = (RelativeLayout) findViewById(R.id.rl_root);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_direction = (ImageView) findViewById(R.id.iv_direction);
        iv_go = (ImageView) findViewById(R.id.iv_go);
        tv_main_clue = (TextView) findViewById(R.id.tv_main_clue);
        iv_ar_camera = (ImageView) findViewById(R.id.iv_ar_camera);

        /*****
         * To make TextView scrollable after set max line.
         */
        Util.setTextViewScrollable(tv_main_clue);
        //tv_main_clue.setMovementMethod(new ScrollingMovementMethod());
    }

    private void setClickListeners() {
        iv_ar_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ClueDetailsActivity","OnClick1");
                Intent arCameraIntent = new Intent(ClueDetailsActivity.this, ARCameraActivity.class);

                Bundle mBundle = new Bundle();
                mBundle.putSerializable(TRANS_KEY, clue);

                arCameraIntent.putExtras(mBundle);

                Log.d("ClueDetailsActivity","OnClick2");
                ClueDetailsActivity.this.startActivity(arCameraIntent);
                Log.d("ClueDetailsActivity","OnClick3");
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (mapFragment != null)
                        mapFragment.drawRoute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                /*try{
                    String curLat = mContext.latitude + "";
                    String curLng = mContext.longitude + "";
                    String destLat = "22.565461";
                    String destLng = "88.345528";
                    String uri = "http://maps.google.com/maps?saddr=" + curLat + "," + curLng + "&daddr=" + destLat + "," + destLng;
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse(uri));
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }*/

            }
        });

        iv_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.showLog("diffMilliSec : ", diffMilliSec + " ???????");
                if (diffMilliSec > 60000) {
                    showDialog();
                } else {
                    Intent intent = new Intent(ClueDetailsActivity.this, ScanActivity.class);
                    ClueTransferActivity.sendObject(intent, clue);
                    startActivity(intent);
                }
            }
        });
    }


    private void startBrodcastReciever() {
        /***
         * Start Receiver for Push Notification
         */
        pushNotifyReceiver = new PushNotifyReceiver();
        IntentFilter pushFilter = new IntentFilter();
        pushFilter.addAction(AppConstant.ACTION_LOCAL_PUSH);
        registerReceiver(pushNotifyReceiver, pushFilter);
        pushNotifyReceiver.setPushListener(this);

    }

    private void showDialog() {
        try {
            DialogClueAvailable dialogClueAvailable = DialogClueAvailable.showDialog(this);
            dialogClueAvailable.setCancelable(false);
            dialogClueAvailable.setCanceledOnTouchOutside(false);
            dialogClueAvailable.startTimer(clue.utc_active_time, startMilliSec);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setTexts() {
        try {
            if (clue != null) {
                String localTime = Util.getLocalDate(clue.utc_active_time, AppConstant.DATE_FORMATTER_CLUE_START);
                startDate = Util.getDateFromString(localTime, AppConstant.DATE_FORMATTER_CLUE_START);
                //startDate = Util.getDateFromString(clue.utc_active_time, AppConstant.DATE_FORMATTER_CLUE_START);
                startMilliSec = Util.getMillisecondFromDate(startDate);
                curdate = Util.getCurrentDate(AppConstant.DATE_FORMATTER_CLUE_START);
                curMilliSec = Util.getMillisecondFromDate(curdate);
                diffMilliSec = startMilliSec - curMilliSec;
                Util.showLog("dtl diffMilliSec : ", diffMilliSec + "??????");
                Util.showLog("dtl pre_launch_clue : ", clue.pre_launch_clue + "??????");
                Util.showLog("dtl main_clue : ", clue.main_clue + "??????");

                if (Util.getUTCCurrentDifference(clue.utc_active_time) > 0) {
                    tv_main_clue.setText(clue.pre_launch_clue);
                } else {
                    tv_main_clue.setText(clue.main_clue);
                }

                /*if (diffMilliSec > 60000) {
                    tv_main_clue.setText(clue.pre_launch_clue);
                } else {
                    tv_main_clue.setText(clue.main_clue);
                }*/
                mapFragment = new MapFragment();
                ClueTransfer.sendObject(mapFragment, clue);
                pushFragments(mapFragment, false, AppConstant.TAG_MAP_FRAGMENT, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getClueDetailTask() {
        try {
            if (clue == null || clue.more_clues == null || clue.more_clues.size() == 0) {
                if (Util.isMobileInternetOn(mContext)) {
                    CluesDetailRequestHandler cluesRequestHandler = new CluesDetailRequestHandler(false, true, this, mContext);
                    HashMap<String, String> values = new HashMap<>();
                    values.put(ParamConstant.PARAM_PROMO_ID, clue.promo_id + "");
                    cluesRequestHandler.callService(values, new IServerResponse() {
                        @Override
                        public void onSuccess(String response) {
                            try {
                                CluesDetailResponse cluesResponse = new Gson().fromJson(response, CluesDetailResponse.class);
                                if (cluesResponse.response_code.equals("200")) {
                                    Clue couponDtl = cluesResponse.response_data;
                                    clue.more_clues = couponDtl.more_clues;
                                    clue.utc_active_time = couponDtl.utc_active_time;
                                    clue.main_clue = couponDtl.main_clue;
                                    clue.latitude = couponDtl.latitude;
                                    clue.longitude = couponDtl.longitude;
                                    Util.showLog("get clue dtl :", clue.toString());
                                    setTexts();
                                    CouponTableManagerTask couponTableManagerTask = new CouponTableManagerTask(ClueDetailsActivity.this, Operation.INSERT_CLUE, true);
                                    couponTableManagerTask.initClue(clue);
                                    couponTableManagerTask.execute();
                                }
                            } catch (Exception e) {
                                setTexts();
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(String message, String errorcode) {

                        }
                    });
                } else {
                    setTexts();
                }

            } else {
                setTexts();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * This method is used to push fragment.
     *
     * @param fragment      ----- This is the fragment to show.
     * @param shouldAnimate ----- This will use whether push with animate or not.
     * @param Tag           ------ Use TAG to uniquely identify fragment.
     * @param shouldAdd     ------ This boolean value will keep last fragment into BackStack.
     */
    public void pushFragments(Fragment fragment, boolean shouldAnimate, String Tag, boolean shouldAdd) {
        try {
            clearBackStackByTag(Tag);
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            if (shouldAnimate)
                ft.setCustomAnimations(R.anim.slideinfromright, R.anim.slideouttoleft, R.anim.slideinfromleft, R.anim.slideouttoright);
            ft.replace(R.id.fl_map_view, fragment, Tag);
            if (shouldAdd) {
                ft.addToBackStack(null);
            }
            ft.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * This method will get fragment from the BackStack
     */
    public void popFragments() {
        FragmentManager manager = getSupportFragmentManager();
        int count = manager.getBackStackEntryCount();
        if (count < 1 || count == 0) {
            finish();
        } else {
            manager.popBackStack();
        }
    }

    /**
     * Check Whether Fragment Already Added or Not
     */
    public boolean isAlreadyAdded(String TAG) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(TAG);
        if (fragment == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Get Current Visible Fragment
     */
    public boolean isCurrentlyVisible(String TAG) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(TAG);
        if (fragment == null) {
            return false;
        } else {
            if (fragment.isVisible()) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Get fragment by tag
     *
     * @param TAG
     * @return
     */
    public Fragment getFragmentByTAG(String TAG) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(TAG);
        return fragment;
    }

    /**
     * All clear Fragment Back Stack
     */
    public void clearBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    /**
     * Clear Fragment Back Stack by TAG
     *
     * @param tag
     */
    public void clearBackStackByTag(String tag) {
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onPushCome(Bundle extras) {
        try {
            Message msg = new Gson().fromJson(extras.getString("message"), Message.class);
            Util.showLog("Push come : ", msg.toString());
            if (clue == null) {
                clue = new Clue();
            }
            clue.promo_id = msg.id + "";
            getClueDetailTask();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*if(true){// if is from true load dashboard activity
            Intent intent = new Intent(ClueDetailsActivity.this, DashBoardActivity.class);
            intent.putExtra("shouldShowClues", true);
            startActivity(intent);
            ClueDetailsActivity.this.finish();
        }else {
            super.onBackPressed();
        }*/

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(pushNotifyReceiver);
    }
}
