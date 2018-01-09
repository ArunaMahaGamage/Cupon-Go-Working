package com.coupon.go.api_interface;

import com.coupon.go.util.ParamConstant;
import com.coupon.go.util.UrlConstant;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface CluesDetailInterface {

    @FormUrlEncoded
    @POST(UrlConstant.GET_CLUE_DETAIL)
    void requestRegister(@Field(ParamConstant.PARAM_PROMO_ID) String promo_id,
                         Callback<Response> cb);


}
