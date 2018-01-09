package com.coupon.go.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.coupon.go.R;
import com.coupon.go.dialog.DialogEnterCoupon;
import com.coupon.go.dialog.DialogMessage;
import com.coupon.go.dialog.DialogSuccessCoupon;
import com.coupon.go.dialog.DialogUseCoupon;
import com.coupon.go.model.Coupon;
import com.coupon.go.model.response_wrapper.CluesResponse;
import com.coupon.go.module.base.CouponApplication;
import com.coupon.go.module.coupon.request_handler.RedeemCouponRequestHandler;
import com.coupon.go.util.AppMessages;
import com.coupon.go.util.ParamConstant;
import com.coupon.go.util.Util;
import com.coupon.go.view.TextViewRegular;
import com.coupon.go.webservice.interfaces.IServerResponse;
import com.google.gson.Gson;
import com.nispok.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;

import pl.droidsonroids.gif.GifImageView;


public class CouponAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<Coupon> lists;
    private ViewHolder viewHolder;
    private Activity mActivity;
    private CouponApplication mContext;


    public CouponAdapter(Activity mActivity, ArrayList<Coupon> lists) {
        super();
        layoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.lists = lists;
        this.mActivity = mActivity;
        mContext = (CouponApplication) mActivity.getApplicationContext();
    }


    @Override
    public int getCount() {
        if (lists != null && lists.size() > 0)
            return this.lists.size();
        else
            return 0;

        //return 15;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.row_clue_coupon, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_title.setText(lists.get(position).coupon_title);

            if(lists.get(position).coupon_status.equals("1")){
                viewHolder.iv_center.setImageResource(R.drawable.use_now);
            }else if(lists.get(position).coupon_status.equals("2")){
                viewHolder.iv_center.setImageResource(R.drawable.success);
            }else {
                viewHolder.iv_center.setImageResource(R.drawable.expired);
            }


            Util.disPlayPicassoImageCenterCropWithLoader(mContext, viewHolder.iv_image, lists.get(position).coupon_photo, viewHolder.giv_progress);

            viewHolder.iv_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if(lists.get(position).coupon_status.equals("1")){
                            final DialogUseCoupon dialog = DialogUseCoupon.showDialog(mActivity);
                            dialog.setCoupon(lists.get(position));
                            dialog.iv_go.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try{
                                        dialog.dismiss();
                                        final DialogEnterCoupon dialogEnterCoupon = DialogEnterCoupon.showDialog(mActivity);
                                        dialogEnterCoupon.setCoupon(lists.get(position));
                                        dialogEnterCoupon.iv_go.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                redeemCouponTask(dialogEnterCoupon,lists.get(position));
                                            }
                                        });
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            viewHolder.iv_right_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*PopupWindow  popupWindowDogs  = popupWindowInfo(lists.get(position));
                    popupWindowDogs.showAsDropDown(v, -5, 0);*/
                    showInformation(lists.get(position));
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private class ViewHolder {
        //private CircularProgressView giv_progress;
        private GifImageView giv_progress;
        private ImageView iv_image,iv_center,iv_right_icon;
        private TextViewRegular tv_title, tv_go;

        public ViewHolder(View v) {
            //giv_progress = (CircularProgressView) v.findViewById(R.id.giv_progress);
            giv_progress = (GifImageView) v.findViewById(R.id.giv_progress);
            iv_image = (ImageView) v.findViewById(R.id.iv_image);
            iv_center = (ImageView) v.findViewById(R.id.iv_center);
            iv_right_icon = (ImageView) v.findViewById(R.id.iv_right_icon);
            tv_title = (TextViewRegular) v.findViewById(R.id.tv_main_clue);
            tv_go = (TextViewRegular) v.findViewById(R.id.tv_go);
            tv_go.setVisibility(View.GONE);
            Util.setSelector(mActivity,iv_right_icon,R.drawable.info_top_h,R.drawable.info_top);
        }
    }



    private void redeemCouponTask(final DialogEnterCoupon dialogEnterCoupon, final Coupon coupon ){
        try{
            if(coupon != null){
                if(!dialogEnterCoupon.getCouponCode().equals("")){
                    if (Util.isMobileInternetOn(mActivity)){
                        RedeemCouponRequestHandler redeemCouponRequestHandler = new RedeemCouponRequestHandler(false, true, mActivity, mContext);
                        String coupon_id = coupon.coupon_id;
                        String device_id = Util.getDeviceId(mActivity);
                        String coupon_code = dialogEnterCoupon.getCouponCode();
                        HashMap<String, String> values = new HashMap<>();
                        values.put(ParamConstant.PARAM_COUPON_ID, coupon_id + "");
                        values.put(ParamConstant.PARAM_DEVICE_ID, device_id + "");
                        values.put(ParamConstant.PARAM_COUPON_CODE, coupon_code + "");
                        redeemCouponRequestHandler.callService(values, new IServerResponse() {
                            @Override
                            public void onSuccess(String response) {
                                try{
                                    dialogEnterCoupon.dismiss();
                                    CluesResponse cluesResponse = new Gson().fromJson(response, CluesResponse.class);
                                    if(cluesResponse.response_code.equals("200")){
                                        coupon.coupon_status = "2";
                                        notifyDataSetChanged();
                                        //final DialogMessage dialogSuccessCoupon = DialogMessage.showDialog(mActivity);
                                        final DialogSuccessCoupon dialogSuccessCoupon = DialogSuccessCoupon.showDialog(mActivity);
                                        dialogSuccessCoupon.setMessage(cluesResponse.response_msg, true);
                                        dialogSuccessCoupon.iv_back.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialogSuccessCoupon.dismiss();
                                            }
                                        });
                                    }else {
                                        //final DialogSuccessCoupon dialogSuccessCoupon = DialogSuccessCoupon.showDialog(mActivity);
                                        final DialogMessage dialogSuccessCoupon = DialogMessage.showDialog(mActivity);
                                        dialogSuccessCoupon.setMessage(cluesResponse.response_msg, true);
                                        dialogSuccessCoupon.iv_back.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialogSuccessCoupon.dismiss();
                                            }
                                        });
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(String message, String errorcode) {

                            }
                        });
                    }else {
                        try{
                            Snackbar snackbar = Util.showSnackBar(mActivity, AppMessages.NO_INTERNET, "RETRY", false);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }else {
                    //final DialogSuccessCoupon dialogSuccessCoupon = DialogSuccessCoupon.showDialog(mActivity);
                    final DialogMessage dialogSuccessCoupon = DialogMessage.showDialog(mActivity);
                    dialogSuccessCoupon.setMessage("Coupon Code shouldn't Balnk !", true);
                    dialogSuccessCoupon.iv_back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogSuccessCoupon.dismiss();
                        }
                    });
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void showInformation(Coupon coupon){
        try{
            DialogMessage dialog = DialogMessage.showDialog(mActivity);
            dialog.setMessage( "Terms And Conditions :\n" + coupon.terms_conditions,true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /*public PopupWindow popupWindowInfo(Coupon coupon) {

        // initialize a pop up window type
        PopupWindow popupWindow = new PopupWindow(mActivity);
        try{
            // some other visual settings
            popupWindow.setFocusable(true);
            popupWindow.setWidth(250);
            popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            View layout = mActivity.getLayoutInflater().inflate(R.layout.pop_up_terms, null);
            TextView tv_txt = (TextView) layout.findViewById(R.id.tv_txt);
            tv_txt.setText("Terms and Conditions:\n" + coupon.terms_conditions);
            popupWindow.setContentView(layout);
        }catch (Exception e){
            e.printStackTrace();
        }

        return popupWindow;
    }*/


}
