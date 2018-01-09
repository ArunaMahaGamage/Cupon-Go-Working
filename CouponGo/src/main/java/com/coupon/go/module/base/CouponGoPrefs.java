package com.coupon.go.module.base;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class CouponGoPrefs {

    private CouponApplication mContext;

    public static String KEY_FIRST_LOGIN = "key_first_login";
    public static String KEY_DEVICE_REG = "key_device_reg";
    public static String KEY_LANGUAGE = "key_selected_language";
    public static String KEY_CURRENCY = "key_selected_currency";
    public static String KEY_GCM_ID = "key_gcm_id";

	/*------------------------------------------------------------------------*/

    public SharedPreferences mSharedPreferences;
    public SharedPreferences.Editor mSharedPreferenceEditor;

    public CouponGoPrefs(CouponApplication contex) {
        mContext = contex;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mSharedPreferenceEditor = mSharedPreferences.edit();
    }


    public void saveFirstLogin(boolean buy) {
        mSharedPreferenceEditor.putBoolean(KEY_FIRST_LOGIN, buy);
        mSharedPreferenceEditor.commit();
    }


    public boolean isLoggedIn() {
        return mSharedPreferences.getBoolean(KEY_FIRST_LOGIN, false);
    }


    public void saveFirstLoggedIn(boolean isReg) {
        mSharedPreferenceEditor.putBoolean(KEY_DEVICE_REG, isReg);
        mSharedPreferenceEditor.commit();
    }

    public boolean isFirstLoggedIn() {
        return mSharedPreferences.getBoolean(KEY_DEVICE_REG, false);
    }


    /***
     * To clear all preference values.
     */
    public void clearPref() {
        mSharedPreferenceEditor.clear();
        mSharedPreferenceEditor.commit();
    }



//    public void saveSelectedCurrency(Currency currency) {
//        try {
//            mSharedPreferenceEditor.putString(KEY_CURRENCY, currency != null ? ObjectSerializer.serialize(currency) : null);
//            mSharedPreferenceEditor.commit();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public Currency getSelectedCurrency() {
//        try {
//            String serializeString = mSharedPreferences.getString(KEY_CURRENCY, null);
//            try {
//                if (serializeString != null) {
//                    Currency currency = (Currency) ObjectSerializer.deserialize(serializeString);
//                    Util.showLog("get cur : ", currency.toString());
//                    return (Currency) ObjectSerializer.deserialize(serializeString);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


}

