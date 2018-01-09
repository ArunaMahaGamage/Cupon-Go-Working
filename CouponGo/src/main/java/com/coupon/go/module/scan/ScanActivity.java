package com.coupon.go.module.scan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.coupon.go.R;
import com.coupon.go.dialog.DialogMessage;
import com.coupon.go.dialog.DialogMoreClues;
import com.coupon.go.dialog.DialogTimeOut;
import com.coupon.go.model.Clue;
import com.coupon.go.model.response_wrapper.CluesResponse;
import com.coupon.go.module.base.CouponApplication;
import com.coupon.go.module.coupon.request_handler.SetCouponRequestHandler;
import com.coupon.go.module.dashboard.DashBoardActivity;
import com.coupon.go.util.AppConstant;
import com.coupon.go.util.Config;
import com.coupon.go.util.CountDownTimerWithPause;
import com.coupon.go.util.ParamConstant;
import com.coupon.go.util.Util;
import com.coupon.go.value_transfer.ClueTransferActivity;
import com.coupon.go.webservice.interfaces.IServerResponse;
import com.craftar.CraftARActivity;
import com.craftar.CraftARCloudRecognition;
import com.craftar.CraftARContent;
import com.craftar.CraftARContentImage;
import com.craftar.CraftARError;
import com.craftar.CraftARItem;
import com.craftar.CraftARItemAR;
import com.craftar.CraftARResult;
import com.craftar.CraftARSDK;
import com.craftar.CraftARSDKException;
import com.craftar.CraftARSearchResponseHandler;
import com.craftar.CraftARTracking;
import com.craftar.ImageRecognition;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zabingo on 3/8/16.
 */
public class ScanActivity extends CraftARActivity implements CraftARSearchResponseHandler, ImageRecognition.SetCollectionListener {

    private final String TAG = "ScanActivity";

    private CouponApplication mContext;
    private ImageView iv_back;
    private CountDownTimerWithPause timerWithPause;
    private boolean isTimeUp = false;
    private CraftARSDK mCraftARSDK;
    private CraftARTracking mTracking;
    private CraftARCloudRecognition mCloudIR;
    private RelativeLayout rl_flash;
    private int count = 0;
    private String scannedResult = null;
    private String languageCode = "kr";
    private boolean isHistoryTaskCalling = false;
    private Clue clue;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mContext = (CouponApplication) getApplicationContext();

        getObject();
    }

    private void getObject(){
        try{
            clue = ClueTransferActivity.getObject(this);
            Util.showLog("clue : ", clue.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPostCreate() {
        View mainLayout = getLayoutInflater().inflate(R.layout.activity_scan, null);
        setContentView(mainLayout);
        initScanning();
        initView();
        setOnClickListeners();
        setSelectors();
        startTimer();
    }


    private void initView() {
        rl_flash = (RelativeLayout) findViewById(R.id.rl_flash);
        iv_back = (ImageView) findViewById(R.id.iv_back);
    }


    private void setSelectors() {
        Util.setSelector(this, iv_back, R.drawable.back_red, R.drawable.back_ass);
    }

    private void setOnClickListeners() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void onPreviewStarted(int width, int height) {
        /**
         * Set the collection we want to search with the COLLECITON_TOKEN.
         * When the collection is ready, the collectionReady callback will be triggered.
         */
        mCloudIR.setCollection(Config.MY_COLLECTION_TOKEN, this);
    }


    @Override
    public void collectionReady() {
        /**
         * Start searching in finder mode. The searchResults() method of the
         * CraftARSearchResponseHandler previously set to the SDK will be triggered when some results
         * are found.
         */
        mCraftARSDK.startFinder();
    }

    @Override
    public void setCollectionFailed(CraftARError craftARError) {
        /**
         * This method is called when the setCollection method failed. This happens usually
         * when the token is wrong or there is no internet connection.
         */
        Util.showLog(TAG, "search failed! " + craftARError.getErrorMessage());
    }

    @Override
    public void searchResults(ArrayList<CraftARResult> results, long l, int i) {


        count += 1;
        Util.showLog(TAG + " count >>>>>> ", count + " ???????");
        Util.showLog(TAG + " count result >>>>>> ", results.size() + " ???????");


        Map<String, CraftARItemAR> arItems = new HashMap<String, CraftARItemAR>();
        if (results != null && results.size() != 0) {

            CraftARResult result = results.get(0);
            CraftARItem item = result.getItem();


            Util.showLog(TAG + " item.isAR() : ", item.isAR() + " ?????????????");
            //Util.showLog(TAG + " CraftARItem : ", item.getUrl() + " ?????????????");
            //Util.showLog("getJson : ", item.getJson() + " ?????????????");
            // if (item.isAR()) {


            if (item.isAR()) {
                try {
                    CraftARItemAR myARItem = (CraftARItemAR) item;

                    if (!arItems.containsKey(myARItem.getItemName())) {

                        String[] tmpScanResult = null;
                        try {
                            tmpScanResult = myARItem.getItemName().toString().split("_");
                            scannedResult = tmpScanResult[0];
                        } catch (Exception e) {
                            scannedResult = myARItem.getItemName().toString();
                            e.printStackTrace();
                        }

                        Util.showLog(TAG + " scannedResult : ", scannedResult + " ?????????????");


                        setHistoryTask(scannedResult);
                        showFlashView();

                        String imageUrl = "http://54.153.38.116:8080/api/thumbnails/ios/?scan_code=" + scannedResult.toUpperCase() + "&language_code=" + languageCode;
                        CraftARContentImage imageContent = new CraftARContentImage(imageUrl);
                        imageContent.setWrapMode(CraftARContent.ContentWrapMode.WRAP_MODE_SCALE_FILL);
                        // Add content to the item
                        myARItem.addContent(imageContent);

                        // Add content to the tracking SDK and start AR experience+
                        try {
                            mTracking.addItem(myARItem);
                            mTracking.startTracking();
                            arItems.put(myARItem.getItemName(), myARItem);
                            //mCraftARTracking.startTracking();
                        } catch (CraftARSDKException e) {
                            //The item could not be added
                            e.printStackTrace();
                        }
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {

                    CraftARItem myARItem = item;

                    if (!arItems.containsKey(myARItem.getItemName())) {
                        String[] tmpScanResult = null;
                        try {
                            tmpScanResult = myARItem.getItemName().toString().split("_");
                            scannedResult = tmpScanResult[0];
                        } catch (Exception e1) {
                            scannedResult = myARItem.getItemName().toString();
                            e1.printStackTrace();
                        }

                        setHistoryTask(scannedResult);
                        Util.showLog(TAG + " scannedResult : ", scannedResult + " ?????????????");
                        showFlashView();
                    } else {
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

        } else {
        }
    }


    private void setHistoryTask(String scanId) {
        try {
            if (!isHistoryTaskCalling) {
                isHistoryTaskCalling = true;
                SetCouponRequestHandler setHistoryRequestHandler = new SetCouponRequestHandler(false, false, ScanActivity.this, mContext);

                String device_id = Util.getDeviceId(this);
                //scanId = "BB01";

                /*String val = "AR programmatically";
                if(scanId.equals(val)){
                    Util.showLog("val is : ", "true");
                }else {
                    Util.showLog("val is : ", "false");
                }*/

                HashMap<String, String> values = new HashMap<>();
                values.put(ParamConstant.PARAM_DEVICE_ID, device_id + "");
                values.put(ParamConstant.PARAM_IR_CODE, scanId + "");

                setHistoryRequestHandler.callService(values, new IServerResponse() {
                    @Override
                    public void onSuccess(String response) {
                        try {
                            if (response != null && !"".equals(response)) {
                                final CluesResponse setHistoryServerResponse = new Gson().fromJson((response), CluesResponse.class);
                                if (setHistoryServerResponse.response_code.equals("200")) {
                                    Intent intent = new Intent(ScanActivity.this, DashBoardActivity.class);
                                    intent.putExtra("shouldShowClues", false);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    ScanActivity.this.finish();
                                }
                                /*else if (setHistoryServerResponse.response_code.equalsIgnoreCase("201")) {
                                    //Util.showAlert(DashboardActivity.this, "Device id field is blank");
                                } else if (setHistoryServerResponse.response_code.equalsIgnoreCase("202")) {
                                    //Util.showAlert(DashboardActivity.this, "Item scan id is blank");
                                } else if (setHistoryServerResponse.response_code.equalsIgnoreCase("203")) {
                                    //Util.showAlert(DashboardActivity.this, "Language id is blank or not numeric");
                                }*/
                                else {
                                    if(timerWithPause != null)
                                        timerWithPause.pause();
                                    //final DialogSuccessCoupon dialog = DialogSuccessCoupon.showDialog(ScanActivity.this);
                                    final DialogMessage dialog = DialogMessage.showDialog(ScanActivity.this);
                                    dialog.setCancelable(false);
                                    dialog.setCanceledOnTouchOutside(false);
                                    dialog.setMessage(setHistoryServerResponse.response_msg, true);
                                    dialog.iv_back.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            isHistoryTaskCalling = false;
                                            dialog.dismiss();
                                            startScanning();
                                            if(timerWithPause != null)
                                                timerWithPause.resume();
                                        }
                                    });
                                }
                            }else {
                                isHistoryTaskCalling = false;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            isHistoryTaskCalling = false;
                            startScanning();
                        }
                    }

                    @Override
                    public void onFailure(String message, String errorcode) {
                        isHistoryTaskCalling = false;
                    }
                });
            }

        } catch (Exception e) {
            isHistoryTaskCalling = false;
            e.printStackTrace();
        }


    }


    boolean isFlashing = false;

    private void showFlashView() {
        stopScanning();
        if (!isFlashing) {
            isFlashing = true;
            rl_flash.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideFlashView();
                    isFlashing = false;
                    if (!isHistoryTaskCalling)
                        startScanning();
                }
            }, 1000);
        }

    }

    private void hideFlashView() {
        rl_flash.setVisibility(View.GONE);
    }

    @Override
    public void searchFailed(CraftARError craftARError, int i) {
        /**
         * Called when a search fails. This happens usually when pointing the
         * device to a textureless surface or when there is connectivity issues.
         */
        Log.d(TAG, "Search failed :" + craftARError.getErrorMessage());
    }

    @Override
    public void finish() {
        /**
         * Stop Searching, Tracking and clean the AR scene
         */
        mCraftARSDK.stopFinder();
        mTracking.stopTracking();
        mTracking.removeAllItems();
        super.finish();
    }

    @Override
    public void onCameraOpenFailed() {
        Toast.makeText(getApplicationContext(), "Camera error", Toast.LENGTH_SHORT).show();
    }


    public void startTimer() {
        try {
            //set a new Timer
            timerWithPause = new CountDownTimerWithPause(AppConstant.SCAN_TIME_LIMIT,
                    AppConstant.SCAN_TIME_INTERVAL, false) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    isTimeUp = true;
                    Util.showLog("Time Up : ", "True ????????");
                    stopScanning();
                    try {
                        final DialogTimeOut dialog = DialogTimeOut.showDialog(ScanActivity.this);
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setCancelable(false);
                        dialog.iv_go.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                isTimeUp = false;
                                startTimer();
                                startScanning();
                            }
                        });

                        dialog.iv_clues.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                stopScanning();
                                final DialogMoreClues dialogMoreClues = DialogMoreClues.showDialog(ScanActivity.this);
                                dialogMoreClues.setCanceledOnTouchOutside(false);
                                dialogMoreClues.setCancelable(false);
                                dialogMoreClues.setClue(clue);
                                dialogMoreClues.iv_go.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogMoreClues.dismiss();
                                        isTimeUp = false;
                                        startTimer();
                                        startScanning();
                                    }
                                });
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            };

            timerWithPause.create();
            resumeTimer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resumeTimer() {
        try {
            //set a new Timer
            if (timerWithPause != null)
                timerWithPause.resume();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pauseTimer() {
        try {
            //set a new Timer
            if (timerWithPause != null)
                timerWithPause.pause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void initScanning() {
        try {

            /**
             * Get the CraftAR SDK instance and initialize the capture
             * passing the current activity
             *
             * When the capture is ready onPreviewStarted() will be called.
             */
            mCraftARSDK = CraftARSDK.Instance();
            mCraftARSDK.startCapture(this);


            /**
             * Get the Cloud Image Recognition instance and set this class
             * as the one to receive search responses.
             */
            mCloudIR = CraftARCloudRecognition.Instance();
            mCloudIR.setCraftARSearchResponseHandler(this);

            /**
             * Set the Search controller from the Cloud IR class.
             * The Cloud IR class knows how to perform visual searches in the CraftAR CRS Service.
             * The searchController is a helper class that receives the camera frames and pictures from the
             * SDK and manages the Single shot and the Finder mode searches.
             */
            mCraftARSDK.setSearchController(mCloudIR.getSearchController());

            /**
             * Get the Tracking instance for the AR.
             */
            mTracking = CraftARTracking.Instance();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void startCapture() {
        try {
            mCraftARSDK.getCamera().restartCapture();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopCapture() {
        try {
            mCraftARSDK.getCamera().stopCapture();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startScanning() {
        try {
            if (!isTimeUp) {
                if (mCraftARSDK != null) {
                    mCraftARSDK.startFinder();
                }
            }

            return;
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void stopScanning() {
        try {
            if (mCraftARSDK != null) {
                mCraftARSDK.stopFinder();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (timerWithPause != null) {
                timerWithPause.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
