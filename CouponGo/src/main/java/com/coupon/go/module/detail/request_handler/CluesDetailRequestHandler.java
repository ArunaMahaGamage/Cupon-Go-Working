package com.coupon.go.module.detail.request_handler;

import android.app.Activity;
import android.util.Log;

import com.coupon.go.api_interface.CluesDetailInterface;
import com.coupon.go.dialog.DialogProgress;
import com.coupon.go.module.base.CouponApplication;
import com.coupon.go.util.AppMessages;
import com.coupon.go.util.ParamConstant;
import com.coupon.go.util.RetrofitUtilities;
import com.coupon.go.util.UrlConstant;
import com.coupon.go.util.Util;
import com.coupon.go.webservice.RequestHandler;
import com.coupon.go.webservice.interfaces.IServerResponse;

import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CluesDetailRequestHandler extends RequestHandler implements IServerResponse {
    private static final String TAG = "CluesDetailRequestHandler ";

    private Map<String,String> values;
    private Activity activity;
    private IServerResponse iLoginSuccess;
    private boolean isJSONObj = false;
    private boolean shouldShowDialog = true;
    private CouponApplication context;
    private DialogProgress progress;

    public CluesDetailRequestHandler(boolean isJSONObj, boolean shouldShowDialog, Activity activity, CouponApplication context) {
        super(activity);
        this.activity = (Activity) activity;
        this.context = context;
        this.isJSONObj = isJSONObj;
        this.shouldShowDialog = shouldShowDialog;
    }

    @Override
    public String getWebServiceMethod() {
        return UrlConstant.GET_CLUE_DETAIL;
    }

    @Override
    public String[] getKeys() {
        String[] keys = {"promo_id"};
        return keys;
    }

    @Override
    public Map<String, String> getValues() {
        return values;
    }


    public void callService(final Map<String,String> values, final IServerResponse iLoginSuccess) {
        try {
            if(Util.isMobileInternetOn(activity)){
                this.values = values;
                this.iLoginSuccess = iLoginSuccess;
                progress = Util.showProgressHud(activity, AppMessages.WAIT_MSG);

                RestAdapter adapter = RetrofitUtilities.getRetrofitAdapter();
                CluesDetailInterface clueInterface = adapter.create(CluesDetailInterface.class);

                Util.showLog("URL : ", UrlConstant.BASE_URL + UrlConstant.GET_CLUE_DETAIL + " ?????");

                String promo_id = "";


                if(values != null && values.size() > 0){
                    try{
                        promo_id = values.get(ParamConstant.PARAM_PROMO_ID);
                        for (String name: values.keySet()){
                            String key = name.toString();
                            String value = values.get(name).toString();
                            Util.showLog(TAG + "param :" + key, value + " ?????");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }


                clueInterface.requestRegister(promo_id, new Callback<Response>() {
                    @Override
                    public void success(Response s, Response response) {

                        try {
                            String result = RetrofitUtilities.createStringFromResponse(response);
                            Util.showLog("response : ", result.toString() + " ???????");
                            Util.dismissProgress(progress);
                            if (iLoginSuccess != null) {
                                iLoginSuccess.onSuccess(result);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("error response : ", error.toString() + " ???????");
                        Util.dismissProgress(progress);
                        if (iLoginSuccess != null) {
                            iLoginSuccess.onSuccess(null);
                        }
                    }


                });
            }else {
//                final DialogConfirm dialogConfirm = Util.showReloadDialog(activity, AppConstant.NO_INTERNET_ALERT, "CANCEL", "RELOAD");
//                if(dialogConfirm != null){
//                    try{
//                        dialogConfirm.setOnBtnClickL(
//                                new OnBtnClickL() {//left btn click listener
//                                    @Override
//                                    public void onBtnClick() {
//                                        dialogConfirm.dismiss();
//                                    }
//                                },
//                                new OnBtnClickL() {//right btn click listener
//                                    @Override
//                                    public void onBtnClick() {
//                                        dialogConfirm.dismiss();
//                                        callService(values,iLoginSuccess);
//                                    }
//                                }
//                        );
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            //Util.dismissProgress(progress);
        }


    }

    @Override
    public void onSuccess(String response) {

        if (response != null) {
            iLoginSuccess.onSuccess(response);
        }
    }

    private void showErrorMessage(String message) {
        //ShowDialogError.showDialog(activity, "Alert !", message);
    }

}
