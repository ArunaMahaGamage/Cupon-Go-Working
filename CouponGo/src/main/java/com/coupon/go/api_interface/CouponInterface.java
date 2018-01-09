package com.coupon.go.api_interface;

import com.coupon.go.util.ParamConstant;
import com.coupon.go.util.UrlConstant;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface CouponInterface {

    @FormUrlEncoded
    @POST(UrlConstant.GET_COUPON_LIST)
    void requestCoupons(@Field(ParamConstant.PARAM_DEVICE_ID) String device_id,
                         @Field(ParamConstant.PARAM_LATITUDE) String latitude,
                         @Field(ParamConstant.PARAM_LONGITUDE) String longitude,
                         Callback<Response> cb);


}
