package com.coupon.go.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coupon.go.R;
import com.coupon.go.model.Clue;
import com.coupon.go.util.Util;

/**
 * Created by zabingo on 24/8/16.
 */
public class MoreClueAdapter extends PagerAdapter {

    private Activity activity;
    private Clue clue;
    private LayoutInflater layoutInflater;

    public MoreClueAdapter(Activity activity, Clue coupon) {
        this.activity = activity;
        this.clue = coupon;
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (clue != null && clue.more_clues != null)
            return clue.more_clues.size();
        else
            return 0;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = layoutInflater.inflate(R.layout.row_more_clue, container, false);
        try {
            TextView tv_clues_detail = (TextView) itemView.findViewById(R.id.tv_clues_detail);
            Util.showLog("more clue : ", clue.more_clues.get(position).clues + " ???????");
            tv_clues_detail.setText(clue.more_clues.get(position).clues);
            //Util.setTextViewScrollable(tv_clues_detail);
            container.addView(itemView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }
}
