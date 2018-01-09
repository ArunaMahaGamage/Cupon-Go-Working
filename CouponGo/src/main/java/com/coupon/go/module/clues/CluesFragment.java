package com.coupon.go.module.clues;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.coupon.go.R;
import com.coupon.go.adapter.ClueAdapter;
import com.coupon.go.model.Clue;
import com.coupon.go.model.response_wrapper.CluesResponse;
import com.coupon.go.module.arcamera.ARCameraActivity;
import com.coupon.go.module.base.BaseFragment;
import com.coupon.go.module.clues.request_handler.CluesRequestHandler;
import com.coupon.go.module.dashboard.DashBoardActivity;
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
import java.util.List;

/**
 * Created by zabingo on 2/8/16.
 */
public class CluesFragment extends BaseFragment {

    private View view;
    private ListView lv_list;
    private ClueAdapter adapter;
    private ArrayList<Clue> list;
    private SwipeRefreshLayout swipeContainer;
    private static final String TAG = "CluesFragment";
    private ImageView iv_ar_camera;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list, container, false);
        initView(view);
        setOnClickListeners();
        //Util.createLocalNotification(mContext);
        //Util.createLocalNotification1(mContext);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getCluesListTask();
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
                if(list != null)
                    list.clear();
                adapter = null;
                getCluesListTask();
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

//                CluesFragment fragment = new CluesFragment();
//                Bundle bundle = new Bundle();
//                mBundle.putParcelableArrayList("arraylist", (ArrayList<? extends Parcelable>) list);
//                fragment.setArguments(bundle);

                mBundle.putParcelableArrayList("arraylist", (ArrayList) list);

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


            adapter = new ClueAdapter(mActivity, list);
            lv_list.setAdapter(adapter);

            /*Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("arraylist", (ArrayList)list);
            fragment.setArguments(bundle);*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getCluesListTask() {
        Log.d(TAG, "getCluesListTask1:");
        try {
            Log.d(TAG, "getCluesListTask2:");
            swipeContainer.setRefreshing(false);
            if (list == null || list.size() == 0) {
                Log.d(TAG, "getCluesListTask3:");
                if (Util.isMobileInternetOn(mContext)) {
                    Log.d(TAG, "getCluesListTask4:");
                    CluesRequestHandler cluesRequestHandler = new CluesRequestHandler(false, true, getActivity(), mContext);
                    HashMap<String, String> values = new HashMap<>();
                    values.put(ParamConstant.PARAM_LATITUDE, mContext.location.latitude + "");
                    values.put(ParamConstant.PARAM_LONGITUDE, mContext.location.longitude + "");
                    values.put(ParamConstant.PARAM_DEVICE_ID,mContext.device_token);

                    Log.d(TAG, "getCluesListTask4: ValuesLat: "+values.get(ParamConstant.PARAM_LATITUDE));
                    Log.d(TAG, "getCluesListTask4: ValuesLng: "+values.get(ParamConstant.PARAM_LONGITUDE));
                    Log.d(TAG, "getCluesListTask4: ValuesDeviceId: "+values.get(ParamConstant.PARAM_DEVICE_ID));


                    cluesRequestHandler.callService(values, new IServerResponse() {
                        @Override
                        public void onSuccess(String response) {
                            try {
                                Log.d(TAG, "getCluesListTask5: Response: "+response);
                                CluesResponse cluesResponse = new Gson().fromJson(response, CluesResponse.class);
                                if (cluesResponse.response_code.equals("200")) {
                                    list = cluesResponse.response_data;
                                    Log.d(TAG, "getCluesListTask6: "+cluesResponse.response_code+" data: "+list.toString());
                                    CouponTableManagerTask couponTableManagerTask = new CouponTableManagerTask(mActivity, Operation.INSERT_ALL_CLUE, true);
                                    couponTableManagerTask.initClueList(list);
                                    couponTableManagerTask.execute();
                                    setAdapter();
                                }
                                else{
                                    Log.d(TAG, "getCluesListTask7:");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.d(TAG, "getCluesListTask8:");
                            }
                        }

                        @Override
                        public void onFailure(String message, String errorcode) {

                        }
                    });
                } else {
                    CouponTableManagerTask couponTableManagerTask = new CouponTableManagerTask(mActivity, Operation.FETCH_ALL_CLUE, true);

                    couponTableManagerTask.setClueInterface(new CouponTableManagerTask.IClueOperation() {
                        @Override
                        public void onFetchComplete(ArrayList<Clue> objectList) {
                            Util.showLog("objectList", objectList.size() + "??????");
                            list = objectList;
                            setAdapter();
                        }

                        @Override
                        public void onFetchComplete(Clue object) {

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
                                getCluesListTask();
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
