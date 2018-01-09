package com.coupon.go.table_manager;

import android.app.Activity;

import com.coupon.go.model.Clue;
import com.coupon.go.module.base.CouponApplication;
import com.coupon.go.orm_utility.Orm_SQLManager;
import com.coupon.go.util.Util;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;


public class ClueTableManager {

    public static void insertIntoClue(Clue object, Activity activity, CouponApplication application) {
        try {
            if (object != null) {
                object.clue_str = new Gson().toJson(object);
                Util.showLog("insert clue_str " + " = ", object.clue_str);
                Orm_SQLManager.insertIntoTable(Clue.class, object, activity, application.databaseManager);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertIntoClue(ArrayList<Clue> objectList, Activity activity, CouponApplication application) {
        try {
            if (objectList != null && objectList.size() > 0) {
                for (int i = 0; i < objectList.size(); i++) {
                    objectList.get(i).clue_str = new Gson().toJson(objectList.get(i));
                    Util.showLog("insert clue_str " + i + " = ", objectList.get(i).clue_str);
                }
                //getCouponList(activity,application,Operation.);
            }
            Orm_SQLManager.insertCollectionIntoTable(Clue.class, objectList, activity, application.databaseManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Clue> getClueList(Activity activity, CouponApplication application) {
        ArrayList<Clue> objectList = new ArrayList<>();
        try {

            List<Clue> templist = (List<Clue>) Orm_SQLManager.getAllTableObjects(
                    Clue.class,
                    activity,
                    application.databaseManager
            );
            if (templist != null && templist.size() > 0) {
                for (int i = 0; i < templist.size(); i++) {
                    Clue object = new Gson().fromJson(templist.get(i).clue_str, Clue.class);
                    Util.showLog("get clue_str " + i + " = ", templist.get(i).clue_str);
                    /***
                     * Checking whether expire or not
                     * Delete if expire
                     */
                    if (Util.getUTCCurrentDifference(object.utc_expire_time) > 0) {
                        objectList.add(object);
                    } else {
                        //Delete record from local data base
                    }
                }
            }

//                case FETCH_ALL_COUPON:
//                    List<Coupon> templist1 = (List<Coupon>) Orm_SQLManager.getSelectedColumnList(
//                            Coupon.class,
//                            activity,
//                            "isClue",
//                            "false" + "",
//                            application.databaseManager
//                    );
//                    if(templist1 != null && templist1.size() > 0){
//                        for(int i = 0; i < templist1.size(); i++){
//                            Clue object = new Gson().fromJson(templist1.get(i).coupon_str,Clue.class);
//                            Util.showLog("get coupon_str " + i + " = ", templist1.get(i).coupon_str);
//                            /***
//                             * Checking whether expire or not
//                             * Delete if expire
//                             */
//                            if(Util.dayDifference(Util.getCurrentDateString(AppConstant.DATE_FORMATTER_EXPIRE),coupon.expire_date, AppConstant.DATE_FORMATTER_EXPIRE) > 0){
//                                objectList.add(object);
//                            }else{
//                                //Delete record from local data base
//                            }
//                        }
//                    }
//                    break;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return objectList;

    }

    public static Clue getClue(Activity activity, CouponApplication application, Clue object) {
        try {
            return (Clue) Orm_SQLManager.getSelectedColumn(application.databaseManager, activity, "coupon_id", object.promo_id, Clue.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;

    }

}
