package com.coupon.go.module.dashboard;

import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.coupon.go.R;
import com.coupon.go.dialog.DialogMessage;
import com.coupon.go.module.arcamera.ARCameraActivity;
import com.coupon.go.module.clues.CluesFragment;
import com.coupon.go.module.coupon.CouponFragment;
import com.coupon.go.util.AppConstant;

/**
 * Created by zabingo on 2/8/16.
 */
public class DashBoardActivity extends FragmentActivity {
    private TextView tv_clues, tv_coupons;
    private boolean shouldShowClues = true;
    protected LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_dashboard);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        initView();
        setClickListeners();
        getObject();
        if(shouldShowClues){
            setTabSelection(tv_clues, tv_coupons);
            pushFragments(new CluesFragment(), false, AppConstant.TAG_CLUE_FRAGMENT, false);
        }else {
            setTabSelection(tv_coupons, tv_clues);
            pushFragments(new CouponFragment(), false, AppConstant.TAG_COUPON_FRAGMENT, false);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    try{
                        //final DialogSuccessCoupon dialog = DialogSuccessCoupon.showDialog(this, true);
                        final DialogMessage dialog = DialogMessage.showDialog(DashBoardActivity.this, true);
                        dialog.setMessage("YOU NEED TO ENABLE GPS!",true);
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.rl_go.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    dialog.dismiss();
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        dialog.iv_back.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                DashBoardActivity.this.finish();
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        },3000);


    }


    private void getObject(){
        try{
            shouldShowClues = getIntent().getBooleanExtra("shouldShowClues", true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    private void initView() {
        tv_clues = (TextView) findViewById(R.id.tv_main_clue);
        tv_coupons = (TextView) findViewById(R.id.tv_coupons);
    }

    private void setClickListeners() {
        tv_clues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabSelection(tv_clues,tv_coupons);
                pushFragments(new CluesFragment(),false, AppConstant.TAG_CLUE_FRAGMENT,false);
            }
        });
        tv_coupons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabSelection(tv_coupons,tv_clues);
                pushFragments(new CouponFragment(),false, AppConstant.TAG_COUPON_FRAGMENT,false);
            }
        });
    }

    private void setTabSelection(TextView selectedTextView,TextView deSelectedTextView){
        try{
            if(selectedTextView != null && deSelectedTextView != null){
                selectedTextView.setTextColor(Color.parseColor("#FFFFFF"));
                selectedTextView.setBackgroundColor(Color.parseColor("#E80703"));
                deSelectedTextView.setTextColor(Color.parseColor("#E80703"));
                deSelectedTextView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        }catch (Exception e){
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
            ft.replace(R.id.realtabcontent, fragment, Tag);
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
}
