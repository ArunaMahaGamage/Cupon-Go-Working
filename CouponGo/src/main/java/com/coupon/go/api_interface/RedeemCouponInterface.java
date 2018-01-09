package com.coupon.go.api_interface;

import com.coupon.go.util.ParamConstant;
import com.coupon.go.util.UrlConstant;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface RedeemCouponInterface {

    @FormUrlEncoded
    @POST(UrlConstant.REDEEM_COUPON)
    void redeemCoupons(@Field(ParamConstant.PARAM_COUPON_ID) String coupon_id,
                        @Field(ParamConstant.PARAM_DEVICE_ID) String device_id,
                        @Field(ParamConstant.PARAM_COUPON_CODE) String coupon_code,
                        Callback<Response> cb);


}
