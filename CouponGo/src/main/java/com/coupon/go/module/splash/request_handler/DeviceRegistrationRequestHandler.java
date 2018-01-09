package com.coupon.go.module.splash.request_handler;

import android.app.Activity;
import android.util.Log;

import com.coupon.go.api_interface.DeviceRegInterface;
import com.coupon.go.module.base.CouponApplication;
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

public class DeviceRegistrationRequestHandler extends RequestHandler implements IServerResponse {
    private static final String TAG = "DeviceRegistrationRequestHandler ";

    private Map<String,String> values;
    //private Activity activity;
    private IServerResponse iLoginSuccess;
    private boolean isJSONObj = false;
    private boolean shouldShowDialog = true;
    private CouponApplication context;
    //private DialogProgress progress;

    public DeviceRegistrationRequestHandler(boolean isJSONObj, boolean shouldShowDialog, Activity activity, CouponApplication context) {
        super(activity);
        //this.activity = (Activity) activity;
        this.context = context;
        this.isJSONObj = isJSONObj;
        this.shouldShowDialog = shouldShowDialog;
    }

    @Override
    public String getWebServiceMethod() {
        return UrlConstant.DEVICE_REGISTRATION;
    }

    @Override
    public String[] getKeys() {
        String[] keys = {"device_id"};
        return keys;
    }

    @Override
    public Map<String, String> getValues() {
        return values;
    }


    public void callService(final Map<String,String> values, final IServerResponse iLoginSuccess) {
        try {
            if(Util.isMobileInternetOn(context)){
                this.values = values;
                this.iLoginSuccess = iLoginSuccess;
                //progress = Util.showProgressHud(activity, AppMessages.WAIT_MSG);

                RestAdapter adapter = RetrofitUtilities.getRetrofitAdapter();
                DeviceRegInterface languageInterface = adapter.create(DeviceRegInterface.class);

                Util.showLog("URL : ", UrlConstant.BASE_URL + UrlConstant.DEVICE_REGISTRATION + " ?????");

                if(values != null && values.size() > 0){
                    try{
                        for (String name: values.keySet()){

                            String key = name.toString();
                            String value = values.get(name).toString();
                            Util.showLog(TAG + "param :" + key, value + " ?????");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                String agent_id = values.get(ParamConstant.PARAM_AGENT_ID);
                String device_id = values.get(ParamConstant.PARAM_DEVICE_ID);
                String device_token = values.get(ParamConstant.PARAM_DEVICE_TOKEN);
                String device_type = values.get(ParamConstant.PARAM_DEVICE_TYPE);


                languageInterface.requestRegister(agent_id,device_id, device_token, device_type, new Callback<Response>() {
                    @Override
                    public void success(Response s, Response response) {

                        try {
                            String result = RetrofitUtilities.createStringFromResponse(response);
                            Util.showLog("response : ", result.toString() + " ???????");
                            //Util.dismissProgress(progress);
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
                        //Util.dismissProgress(progress);
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
