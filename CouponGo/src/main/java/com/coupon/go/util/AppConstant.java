package com.coupon.go.util;

/**
 * Created by zabingo on 2/8/16.
 */
public abstract class AppConstant {
    //------------- fragment TAG ------------------/
    public static final String TAG_CLUE_FRAGMENT = "clueFragment";
    public static final String TAG_COUPON_FRAGMENT = "couponFragment";
    public static final String TAG_MAP_FRAGMENT = "mapFragment";

    //-------------- Scan TimeUp --------------//
    public static final long SCAN_TIME_LIMIT = 30000;
    public static final long SCAN_TIME_INTERVAL = 1000;


    //------------- Date Formatter ---------//
    //2016-08-11 21:01:18
    public static final String DATE_FORMATTER_CLUE_START = "yyyy-MM-dd HH:mm:ss";
    //2016-08-31
    public static final String DATE_FORMATTER_EXPIRE = "yyyy-MM-dd";


    //--------------------- Push Notification ---------------------//
    public static final String  ACTION_LOCAL_PUSH = "com.coupongo.actionlocalpush";
    public static final String NOTIFICATION_ID_KEY = "notification_id";
    public static final String PUSH_KEY = "push_type";
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "coupon_go_registration_id";
    public static final String PROPERTY_APP_VERSION = "coupon_go_appVersion";
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static String  GCM_SENDER_ID = "851526500180";
    public static final String PUSH_TYPE_KEY = "action";

}
