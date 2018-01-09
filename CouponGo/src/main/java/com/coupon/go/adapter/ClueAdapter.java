package com.coupon.go.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.coupon.go.R;
import com.coupon.go.dialog.DialogMessage;
import com.coupon.go.model.Clue;
import com.coupon.go.module.base.CouponApplication;
import com.coupon.go.module.detail.ClueDetailsActivity;
import com.coupon.go.util.Util;
import com.coupon.go.value_transfer.ClueTransferActivity;
import com.coupon.go.view.TextViewRegular;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;


public class ClueAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<Clue> lists;
    private ViewHolder viewHolder;
    private Activity mActivity;
    private CouponApplication mContext;


    public ClueAdapter(Activity mActivity, ArrayList<Clue> lists) {
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

            Util.disPlayPicassoImageCenterCropWithLoader(mContext, viewHolder.iv_image, lists.get(position).coupon_photo, viewHolder.giv_progress);

            viewHolder.iv_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(mActivity, ClueDetailsActivity.class);
                        ClueTransferActivity.sendObject(intent, lists.get(position));
                        mActivity.startActivity(intent);
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
        private ImageView iv_image, iv_center, iv_right_icon;
        private TextViewRegular tv_title;

        public ViewHolder(View v) {
            //giv_progress = (CircularProgressView) v.findViewById(R.id.giv_progress);
            giv_progress = (GifImageView) v.findViewById(R.id.giv_progress);
            iv_image = (ImageView) v.findViewById(R.id.iv_image);
            iv_center = (ImageView) v.findViewById(R.id.iv_center);
            iv_right_icon = (ImageView) v.findViewById(R.id.iv_right_icon);
            tv_title = (TextViewRegular) v.findViewById(R.id.tv_main_clue);
            Util.setSelector(mActivity, iv_right_icon, R.drawable.info, R.drawable.info_h);
        }
    }



    public void showInformation(Clue clue){
        try{
            DialogMessage dialog = DialogMessage.showDialog(mActivity);
            dialog.setMessage( "Hurry Up!\n" + clue.coupon_availabilty + " Coupons are available.",true);
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
            tv_txt.setText(coupon.coupon_availabilty + " Coupons are available.");
            popupWindow.setContentView(layout);
        }catch (Exception e){
            e.printStackTrace();
        }

        return popupWindow;
    }*/


}
