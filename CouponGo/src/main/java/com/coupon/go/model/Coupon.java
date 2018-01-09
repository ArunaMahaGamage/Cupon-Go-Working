package com.coupon.go.model;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by zabingo on 2/8/16.
 */
public class Coupon implements Serializable {

    @DatabaseField(id = true, canBeNull = false)
    public String coupon_id = "";
    public String coupon_title = "";
    public String coupon_photo = "";
    public String terms_conditions = "";
    public String coupon_status = "";
    public String expire_date = "";


    @DatabaseField
    public String coupon_str = "";

    @Override
    public String toString() {
        return "Coupon{" +
                "coupon_id='" + coupon_id + '\'' +
                ", coupon_title='" + coupon_title + '\'' +
                ", coupon_photo='" + coupon_photo + '\'' +
                ", terms_conditions='" + terms_conditions + '\'' +
                ", coupon_status='" + coupon_status + '\'' +
                ", expire_date='" + expire_date + '\'' +
                ", coupon_str='" + coupon_str + '\'' +
                '}';
    }
}
