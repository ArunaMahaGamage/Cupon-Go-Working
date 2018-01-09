package com.coupon.go.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coupon.go.R;
import com.coupon.go.adapter.MoreClueAdapter;
import com.coupon.go.model.Clue;


/**
 * Created by maz on 19-Jun-15.
 */
public class DialogMoreClues extends Dialog implements View.OnClickListener {

    private Activity activity;
    private TextView tv_clues_count;//tv_clues_detail
    public  ImageView iv_prev_clue,iv_next_clue, iv_go;
    private RelativeLayout rl_go;
    private Clue clue;
    private int currentPosition = 0;
    private MoreClueAdapter adapter;
    private ViewPager viewpager_clue_dtl;


    public DialogMoreClues(Activity activity) {
        super(activity, R.style.normal_dialog);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //Always call before setContentView
        //Full screen is set for the Window
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.dialog_more_clues);
        initView();
        setClickListeners();

    }


    private void initView() {
        rl_go = (RelativeLayout) findViewById(R.id.rl_go);
        tv_clues_count = (TextView) findViewById(R.id.tv_main_clue);
        //tv_clues_detail = (TextView) findViewById(R.id.tv_clues_detail);
        iv_go = (ImageView) findViewById(R.id.iv_go);
        iv_prev_clue = (ImageView) findViewById(R.id.iv_prev_clue);
        iv_next_clue = (ImageView) findViewById(R.id.iv_next_clue);
        viewpager_clue_dtl = (ViewPager) findViewById(R.id.viewpager_clue_dtl);
    }

    private void setClickListeners() {
        iv_go.setOnClickListener(this);
        iv_next_clue.setOnClickListener(this);
        iv_prev_clue.setOnClickListener(this);

        viewpager_clue_dtl.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                try {
                    currentPosition = position;

                    /***
                     * First Position
                     */
                    if(currentPosition == 0){
                        iv_prev_clue.setVisibility(View.INVISIBLE);
                        iv_next_clue.setVisibility(View.VISIBLE);
                    }else

                    /***
                     * Last Position
                     */
                    if(currentPosition == clue.more_clues.size() - 1){
                        iv_prev_clue.setVisibility(View.VISIBLE);
                        iv_next_clue.setVisibility(View.INVISIBLE);
                    }else {
                        iv_prev_clue.setVisibility(View.VISIBLE);
                        iv_next_clue.setVisibility(View.VISIBLE);
                    }

                    if (currentPosition == clue.more_clues.size() - 1) {
                        rl_go.setVisibility(View.VISIBLE);
                    } else {
                        rl_go.setVisibility(View.GONE);
                    }
                    String positionTxt = (currentPosition + 1) + "/" + (clue.more_clues.size());
                    tv_clues_count.setText(positionTxt + "");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_go:
                dismiss();
                break;
            case R.id.iv_prev_clue:
                try {
                    currentPosition = currentPosition - 1;
                    if (currentPosition < 0){
                        currentPosition = 0;
                    }
                    viewpager_clue_dtl.setCurrentItem(currentPosition);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.iv_next_clue:
                try {
                    currentPosition = currentPosition + 1;
                    if (currentPosition >= clue.more_clues.size()){
                        currentPosition = clue.more_clues.size() - 1;
                    }
                    viewpager_clue_dtl.setCurrentItem(currentPosition);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }

    }

    public void setClue(Clue clue) {
        try {
            if (clue != null) {
                this.clue = clue;
                adapter = new MoreClueAdapter(activity, clue);
                setAdapter();
                try {
                    String positionTxt = "1" + "/" + (clue.more_clues.size());
                    tv_clues_count.setText(positionTxt + "");
                    //tv_clues_detail.setText(clue.more_clues.get(0).clues);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                viewpager_clue_dtl.setCurrentItem(currentPosition);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setAdapter() {
        try {
            viewpager_clue_dtl.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static DialogMoreClues showDialog(Activity activity) {
        DialogMoreClues dialog = new DialogMoreClues(activity);
        dialog.show();
        return dialog;
    }

}
