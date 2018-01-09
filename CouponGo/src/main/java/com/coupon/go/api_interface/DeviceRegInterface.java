package com.coupon.go.api_interface;

import com.coupon.go.util.AppConstant;
import com.coupon.go.util.ParamConstant;
import com.coupon.go.util.UrlConstant;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface DeviceRegInterface {

    @FormUrlEncoded
    @POST(UrlConstant.DEVICE_REGISTRATION)
    void requestRegister(@Field(ParamConstant.PARAM_AGENT_ID) String agent_id,
                         @Field(ParamConstant.PARAM_DEVICE_ID) String device_id,
                         @Field(ParamConstant.PARAM_DEVICE_TOKEN) String device_token,
                         @Field(ParamConstant.PARAM_DEVICE_TYPE) String device_type,
                         Callback<Response> cb);


}
