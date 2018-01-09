package com.coupon.go.api_interface;

import com.coupon.go.util.ParamConstant;
import com.coupon.go.util.UrlConstant;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface TutorialInterface {

    @FormUrlEncoded
    @POST(UrlConstant.GET_TUTORIAL)
    void requestRegister(@Field("") String agent_id,
                         Callback<Response> cb);


}
