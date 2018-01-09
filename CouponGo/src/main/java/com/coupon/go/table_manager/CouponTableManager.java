package com.coupon.go.table_manager;

import android.app.Activity;

import com.coupon.go.model.Coupon;
import com.coupon.go.module.base.CouponApplication;
import com.coupon.go.orm_utility.Orm_SQLManager;
import com.coupon.go.util.AppConstant;
import com.coupon.go.util.Util;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;


public class CouponTableManager {

    public static void insertIntoCoupon(Coupon object, Activity activity, CouponApplication application) {
        try {
            if(object != null){
                object.coupon_str = new Gson().toJson(object);
                //Util.showLog("insert coupon_str " + " = ", object.coupon_str);
                Orm_SQLManager.insertIntoTable(Coupon.class, object, activity, application.databaseManager);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertIntoCoupon(ArrayList<Coupon> objectList, Activity activity, CouponApplication application) {
        try {
            if(objectList != null && objectList.size() > 0){
                for(int i = 0; i < objectList.size(); i++){
                    objectList.get(i).coupon_str = new Gson().toJson(objectList.get(i));
                    Util.showLog("insert coupon_str " + i + " = ", objectList.get(i).coupon_str);
                }
                //getCouponList(activity,application,Operation.);
            }
            Orm_SQLManager.insertCollectionIntoTable(Coupon.class, objectList, activity, application.databaseManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Coupon> getCouponList(Activity activity, CouponApplication application) {
        ArrayList<Coupon> objectList = new ArrayList<>();
        try {
            List<Coupon> templist1 = (List<Coupon>) Orm_SQLManager.getAllTableObjects(
                    Coupon.class,
                    activity,
                    application.databaseManager
            );
            if(templist1 != null && templist1.size() > 0){
                for(int i = 0; i < templist1.size(); i++){
                    Coupon coupon = new Gson().fromJson(templist1.get(i).coupon_str,Coupon.class);
                    Util.showLog("get coupon_str " + i + " = ", templist1.get(i).coupon_str);
                    /***
                     * Checking whether expire or not
                     * Delete if expire
                     */
                    if(Util.dayDifference(Util.getCurrentDateString(AppConstant.DATE_FORMATTER_EXPIRE),coupon.expire_date, AppConstant.DATE_FORMATTER_EXPIRE) >= 0){
                        objectList.add(coupon);
                    }else{
                        //Delete record from local data base
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objectList;

    }

    public static Coupon getCoupon(Activity activity, CouponApplication application, Coupon coupon) {
        try {
            return (Coupon) Orm_SQLManager.getSelectedColumn(application.databaseManager, activity,"coupon_id",coupon.coupon_id, Coupon.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coupon;

    }

}
