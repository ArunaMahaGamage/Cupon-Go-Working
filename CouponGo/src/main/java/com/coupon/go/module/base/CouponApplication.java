package com.coupon.go.module.base;

import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.support.multidex.MultiDexApplication;

import com.coupon.go.model.Clue;
import com.coupon.go.model.Coupon;
import com.coupon.go.model.LocationObj;
import com.coupon.go.module.maps.FusedLocationService;
import com.coupon.go.orm_utility.DatabaseManager;
import com.coupon.go.orm_utility.IDatabaseHelper;
import com.coupon.go.util.Util;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


/**
 * Created by visni on 10/12/2015.
 */

public class CouponApplication extends MultiDexApplication implements IDatabaseHelper, FusedLocationService.LocationUpdateListener {

    private static final String TAG = "CouponGoApplication";
    public static final String DATABASE_NAME = "CouponGo.db";
    public static final int DATABASE_VERSION = 1;
    public DatabaseManager databaseManager;
    public CouponGoPrefs couponGoPrefs;
    public ImageLoader imageLoader;
    public FusedLocationService fusedLocationService;
    public LocationObj location;
    //public double latitude = 0.0;
    //public double longitude = 0.0;
    public String device_token = "";

    @Override
    public void onCreate() {
        super.onCreate();
        //PsiMethod:Fabric.with(this, new Crashlytics());
        databaseManager = DatabaseManager.getDatabaseManager(this, this, DATABASE_NAME, DATABASE_VERSION);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));

        location = new LocationObj();
        fusedLocationService = new FusedLocationService(this);
        fusedLocationService.setLocUpdateListener(this);
        couponGoPrefs = new CouponGoPrefs(this);

    }


    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Clue.class);
            TableUtils.createTableIfNotExists(connectionSource, Coupon.class);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Clue.class, true);
            TableUtils.dropTable(connectionSource, Coupon.class, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void close() {

    }


    @Override
    public void onLocationUpdated(Location newLoc, long newTime, long gps_time, float accuracy, double alt, float distance) {
        try {
            //Toast.makeText(this, "Lat : " + latitude + " Long : " + longitude, Toast.LENGTH_SHORT ).show();
            if(location == null)
                location = new LocationObj();
            location.latitude = newLoc.getLatitude();
            location.longitude = newLoc.getLongitude();
            Util.showLog(TAG + "latitude : ", location.latitude + " ??????");
            Util.showLog(TAG + "longitude : ", location.longitude + " ??????");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        if (databaseManager != null) {
            databaseManager.releaseHelper(databaseManager.getHelper(this));
        }
    }

}