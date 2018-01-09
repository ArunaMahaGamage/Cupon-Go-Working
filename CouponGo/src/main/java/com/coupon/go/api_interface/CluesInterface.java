package com.coupon.go.api_interface;

import com.coupon.go.util.ParamConstant;
import com.coupon.go.util.UrlConstant;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface CluesInterface {

    @FormUrlEncoded
    @POST(UrlConstant.GET_CLUE_LIST)
    void requestRegister(@Field(ParamConstant.PARAM_LATITUDE) String latitude,
                         @Field(ParamConstant.PARAM_LONGITUDE) String longitude,
                         @Field(ParamConstant.PARAM_DEVICE_ID) String device_id,
                         Callback<Response> cb);


}
