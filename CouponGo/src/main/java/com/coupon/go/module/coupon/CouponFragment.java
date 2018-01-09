package com.coupon.go.module.coupon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import com.coupon.go.R;
import com.coupon.go.adapter.CouponAdapter;
import com.coupon.go.model.Coupon;
import com.coupon.go.model.response_wrapper.CouponResponse;
import com.coupon.go.module.arcamera.ARCameraActivity;
import com.coupon.go.module.base.BaseFragment;
import com.coupon.go.module.coupon.request_handler.CouponRequestHandler;
import com.coupon.go.table_manager.CouponTableManagerTask;
import com.coupon.go.util.AppMessages;
import com.coupon.go.util.ParamConstant;
import com.coupon.go.util.Util;
import com.coupon.go.util.Util.Operation;
import com.coupon.go.webservice.interfaces.IServerResponse;
import com.google.gson.Gson;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.listeners.ActionClickListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zabingo on 2/8/16.
 */
public class CouponFragment extends BaseFragment {

    private View view;
    private ListView lv_list;
    private CouponAdapter adapter;
    private ArrayList<Coupon> list;
    private SwipeRefreshLayout swipeContainer;
    private static final String TAG = "CouponFragment";
    private ImageView iv_ar_camera;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list, container, false);
        initView(view);
        setOnClickListeners();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getCouponListTask();
    }

    private void initView(View view) {
        iv_ar_camera = (ImageView) view.findViewById(R.id.iv_ar_camera);
        lv_list = (ListView) view.findViewById(R.id.lv_list);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                try {
                    if (list != null)
                        list.clear();
                    adapter = null;
                    getCouponListTask();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void setOnClickListeners() {
        iv_ar_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DashboardActivity","OnClick1");
                Intent arCameraIntent = new Intent(getActivity(), ARCameraActivity.class);
                Log.d("DashboardActivity","OnClick2");

                Bundle mBundle = new Bundle();
                mBundle.putBoolean("fromDashboard", true);

                arCameraIntent.putExtras(mBundle);

                getActivity().startActivity(arCameraIntent);
                Log.d("DashboardActivity","OnClick3");
            }
        });
        /*lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Intent intent = new Intent(mActivity, ClueDetailsActivity.class);
                    CouponTransferActivity.sendObject(intent, list.get(position));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });*/

    }

    private void setAdapter() {
        try {
           /* CouponFragment fragment = new CouponFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("arraylist", (ArrayList)list);
            fragment.setArguments(bundle);*/

            adapter = new CouponAdapter(mActivity, list);
            lv_list.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getCouponListTask() {
        try {
            swipeContainer.setRefreshing(false);
            if (list == null || list.size() == 0) {
                Log.d(TAG,"getCouponListTask0");
                if (Util.isMobileInternetOn(mContext)) {
                    CouponRequestHandler cluesRequestHandler = new CouponRequestHandler(false, true, getActivity(), mContext);
                    String device_id = Util.getDeviceId(mActivity);
                    HashMap<String, String> values = new HashMap<>();
                    values.put(ParamConstant.PARAM_DEVICE_ID, device_id + "");
                    values.put(ParamConstant.PARAM_LATITUDE, mContext.location.latitude+ "");
                    values.put(ParamConstant.PARAM_LONGITUDE, mContext.location.longitude+ "");

                    Log.d(TAG,"getCouponListTask1");
                    cluesRequestHandler.callService(values, new IServerResponse() {
                        @Override
                        public void onSuccess(String response) {
                            try {
                                CouponResponse cluesResponse = new Gson().fromJson(response, CouponResponse.class);
                                Log.d(TAG,"getCouponListTask2: response: "+response);
                                if (cluesResponse.response_code.equals("200")) {
                                    Log.d(TAG,"getCouponListTask3");
                                    list = cluesResponse.response_data;
                                    CouponTableManagerTask couponTableManagerTask = new CouponTableManagerTask(mActivity, Operation.INSERT_ALL_COUPON, true);
                                    couponTableManagerTask.initCouponList(list);
                                    couponTableManagerTask.execute();
                                    setAdapter();
                                }
                                else{
                                    Log.d(TAG,"getCouponListTask4");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(String message, String errorcode) {

                        }
                    });
                } else {
                    CouponTableManagerTask couponTableManagerTask = new CouponTableManagerTask(mActivity, Operation.FETCH_ALL_COUPON, true);

                    couponTableManagerTask.setCouponInterface(new CouponTableManagerTask.ICouponOperation() {
                        @Override
                        public void onFetchComplete(ArrayList<Coupon> objectList) {
                            Util.showLog("objectList", objectList.size() + " ??????");
                            list = objectList;
                            setAdapter();
                        }

                        @Override
                        public void onFetchComplete(Coupon object) {

                        }
                    });
                    couponTableManagerTask.execute();

                    Snackbar snackbar = Util.showSnackBar(mActivity, AppMessages.NO_INTERNET, "RETRY", true);
                    snackbar.actionListener(new ActionClickListener() {
                        @Override
                        public void onActionClicked(Snackbar snackbar) {
                            try {
                                snackbar.dismiss();
                                if (list != null)
                                    list.clear();
                                getCouponListTask();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            } else {
                setAdapter();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
