package com.coupon.go.webservice;



import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.coupon.go.webservice.interfaces.IRequestCaller;
import com.coupon.go.webservice.interfaces.IServerResponse;


public abstract class RequestHandler implements IRequestCaller,IServerResponse {

	private Activity mActivity;
    private Context context;
	
	
	public RequestHandler(Activity activity) {
		mActivity = activity;
	}
	
	public RequestHandler(FragmentActivity activity) {
		mActivity = activity;
	}
	
	public RequestHandler(FragmentActivity activity, Fragment fragment) {
		mActivity = activity;
	}


    public RequestHandler(Context context) {
        this.context = context;
    }
	
	
	
	@Override
	public void onFailure(String message,String errorCode) {
		showMessage(message);
	}
	
	private void showMessage(String message){
        //DialogUtility.showMessageWithOk(message, mActivity);
	}

}
