package com.coupon.go.model;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zabingo on 19/8/16.
 */
public class Clue implements Serializable {
    /**
     * Clue OR Promo Part
     */
    @DatabaseField(id = true, canBeNull = false)
    public String promo_id = "";
    public String promo_company = "";
    public String pre_launch_clue = "";
    public String promo_terms_conditions = "";
    public String utc_active_time = "";
    public String utc_expire_time = "";
    public String main_clue = "";
    public ArrayList<Clue> more_clues;


    /***
     * Coupon Part
     */
    public String coupon_id = "";
    public String coupon_title = "";
    public String coupon_availabilty = "";
    public String coupon_photo = "";
    public String coupon_terms_conditions = "";
    public String latitude = "";
    public String longitude = "";
    public String clues = "";

    @DatabaseField
    public String clue_str = "";


    @Override
    public String toString() {
        return "Clue{" +
                "promo_id='" + promo_id + '\'' +
                ", promo_company='" + promo_company + '\'' +
                ", pre_launch_clue='" + pre_launch_clue + '\'' +
                ", promo_terms_conditions='" + promo_terms_conditions + '\'' +
                ", utc_active_time='" + utc_active_time + '\'' +
                ", utc_expire_time='" + utc_expire_time + '\'' +
                ", main_clue='" + main_clue + '\'' +
                ", more_clues=" + more_clues +
                ", coupon_id='" + coupon_id + '\'' +
                ", coupon_title='" + coupon_title + '\'' +
                ", coupon_availabilty='" + coupon_availabilty + '\'' +
                ", coupon_photo='" + coupon_photo + '\'' +
                ", coupon_terms_conditions='" + coupon_terms_conditions + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", clues='" + clues + '\'' +
                ", clue_str='" + clue_str + '\'' +
                '}';
    }
}
