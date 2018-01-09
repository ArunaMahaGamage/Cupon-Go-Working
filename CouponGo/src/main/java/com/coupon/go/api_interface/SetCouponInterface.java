package com.coupon.go.api_interface;

import com.coupon.go.util.ParamConstant;
import com.coupon.go.util.UrlConstant;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface SetCouponInterface {

    @FormUrlEncoded
    @POST(UrlConstant.SET_COUPON)
    void setCoupon(@Field(ParamConstant.PARAM_DEVICE_ID) String device_id,
                        @Field(ParamConstant.PARAM_IR_CODE) String ir_code,
                        Callback<Response> cb);


}
