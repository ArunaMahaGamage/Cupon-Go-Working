package com.coupon.go.table_manager;

import android.app.Activity;
import android.os.AsyncTask;

import com.coupon.go.dialog.DialogProgress;
import com.coupon.go.model.Clue;
import com.coupon.go.model.Coupon;
import com.coupon.go.module.base.CouponApplication;
import com.coupon.go.util.AppMessages;
import com.coupon.go.util.Util;
import com.coupon.go.util.Util.Operation;

import java.util.ArrayList;

/**
 * Created by zabingo on 22/8/16.
 */
public class CouponTableManagerTask extends AsyncTask {

    private Activity activity;
    private CouponApplication mContext;
    private Operation operation;
    private ICouponOperation iCouponOperation;
    private IClueOperation iClueOperation;
    private DialogProgress progress;
    private boolean shouldShowDialog = true;
    private ArrayList<Clue> clueList;
    private Clue clue;
    private ArrayList<Coupon> couponList;
    private Coupon coupon;


    public interface IClueOperation {
        void onFetchComplete(ArrayList<Clue> objectList);
        void onFetchComplete(Clue object);
    }

    public interface ICouponOperation {
        void onFetchComplete(ArrayList<Coupon> objectList);
        void onFetchComplete(Coupon object);
    }

    public CouponTableManagerTask(Activity activity, Operation operation,boolean shouldShowDialog) {
        this.activity = activity;
        this.operation = operation;
        this.shouldShowDialog = shouldShowDialog;
        mContext = (CouponApplication) activity.getApplicationContext();
    }

    public void initClue(Clue object){
        this.clue = object;
    }
    public void initClueList(ArrayList<Clue> objectList){
        this.clueList = objectList;
    }
    public void initCoupon(Coupon object){
        this.coupon = object;
    }
    public void initCouponList(ArrayList<Coupon> objectList){
        this.couponList = objectList;
    }

    public void setClueInterface(IClueOperation iOperation){
        this.iClueOperation = iOperation;
    }
    public void setCouponInterface(ICouponOperation iOperation){
        this.iCouponOperation = iOperation;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            if(shouldShowDialog){
                progress = DialogProgress.showProgressHud(activity, AppMessages.WAIT_MSG);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            Util.showLog("operation : ", operation + " ??????");
            switch (operation) {

                /***
                 * Clue Area
                 */
                case INSERT_CLUE:
                    try {
                        if(clue != null){
                            ClueTableManager.insertIntoClue(clue, activity, mContext);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case INSERT_ALL_CLUE:
                    try {
                        if(clueList != null && clueList.size() > 0){
                            ClueTableManager.insertIntoClue(clueList, activity, mContext);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case FETCH_CLUE:
                    try {
                        clue = ClueTableManager.getClue(activity, mContext, clue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case FETCH_ALL_CLUE:
                    try {
                        clueList = ClueTableManager.getClueList(activity, mContext);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;


                /****
                 * Coupon Area
                 */

                case INSERT_COUPON:
                    try {
                        if(coupon != null){
                            CouponTableManager.insertIntoCoupon(coupon, activity, mContext);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case INSERT_ALL_COUPON:
                    try {
                        if(couponList != null && couponList.size() > 0){
                            CouponTableManager.insertIntoCoupon(couponList, activity, mContext);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case FETCH_COUPON:
                    try {
                        coupon = CouponTableManager.getCoupon(activity, mContext, coupon);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case FETCH_ALL_COUPON:
                    try {
                        couponList = CouponTableManager.getCouponList(activity, mContext);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        try {
            DialogProgress.dismissProgress(progress);
            if(iClueOperation != null){
                iClueOperation.onFetchComplete(clueList);
            }
            if(iCouponOperation != null){
                iCouponOperation.onFetchComplete(couponList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
