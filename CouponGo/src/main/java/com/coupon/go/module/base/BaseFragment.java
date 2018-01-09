package com.coupon.go.module.base;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.coupon.go.module.dashboard.DashBoardActivity;


public class BaseFragment extends Fragment{
	public DashBoardActivity mActivity;
	public CouponApplication mContext ;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity		= (DashBoardActivity) getActivity();
		mContext 		= (CouponApplication)mActivity.getApplicationContext();
	
	}
}
