package com.coupon.go.util;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.NotificationCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.coupon.go.R;
import com.coupon.go.dialog.DialogProgress;
import com.coupon.go.module.base.CouponApplication;
import com.coupon.go.module.detail.ClueDetailsActivity;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.enums.SnackbarType;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import pl.droidsonroids.gif.GifImageView;


/**
 * Created by zabingo on 21/3/16.
 */
public abstract class Util {
    public static String TAG = "CouponGo";


    public static enum Operation {
        INSERT_CLUE, FETCH_CLUE, INSERT_ALL_CLUE, FETCH_ALL_CLUE,
        INSERT_COUPON, FETCH_COUPON, INSERT_ALL_COUPON, FETCH_ALL_COUPON
    }


    public static void showLog(String tag, String msg) {
        Log.e("CouponGo " + tag, msg);
    }

    public static boolean isMobileInternetOn(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            } else {
                return false;
            }

        }
        return false;
    }

    public static void showToast(Context context, String msg){
        try{
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*public static void showAlert(Activity activity, String msg) {
        final DialogConfirm dialogConfirm = new DialogConfirm(activity);

        dialogConfirm.content(msg)//
                .isTitleShow(false)//
                        //.showAnim(mContext.mBasIn)//
                        //.dismissAnim(mContext.mBasOut)//
                .btnNum(1)
                .btnText("OK")
                .show();

        dialogConfirm.setOnBtnClickL(
                new OnBtnClickL() {//left btn click listener
                    @Override
                    public void onBtnClick() {
                        dialogConfirm.dismiss();
                    }
                }
        );

    }*/


   /* public static DialogConfirm showReloadDialog(final Activity activity, String msg, String leftBtnTxt, String rightBtnTxt) {
        final DialogConfirm dialogConfirm = new DialogConfirm(activity);
        try {
            dialogConfirm.content(msg)//
                    .isTitleShow(false)//
                            //.showAnim(mContext.mBasIn)//
                            //.dismissAnim(mContext.mBasOut)//
                    .btnText(leftBtnTxt, rightBtnTxt)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialogConfirm;
    }*/



    /*public static void disPlayPicassoImageWithLoader(Context mContext, final ImageView imageView, final String imageUri, final GifImageView progressBar) {
        try {
            setProgressShow(progressBar);
            if(imageUri != null && !imageUri.equals("")){
                Picasso.with(mContext)
                        .load(imageUri)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(imageView, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                setProgressGone(progressBar);
                            }

                            @Override
                            public void onError() {
                                Util.showLog("error img loading : ", imageUri + " ???????");
                                setProgressGone(progressBar);
                            }
                        });
            }else {
                setProgressGone(progressBar);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


    /*public static void disPlayPicassoImageCenterCropWithLoader(FluentApplication mContext, final ImageView imageView, final String imageUri, final ProgressBar progressBar) {
        try {
            setProgressShow(progressBar);
            Picasso.with(mContext)
                    .load(imageUri)
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(imageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            setProgressGone(progressBar);
                        }

                        @Override
                        public void onError() {
                            Util.showLog("error img loading : ", imageUri + " ???????");
                            setProgressGone(progressBar);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


    public static void disPlayPicassoImageCenterCropWithLoader(Context mContext, final ImageView imageView, final String imageUri, final GifImageView progressBar) {
        try {
            setProgressShow(progressBar);
            if(imageUri != null && !imageUri.equals("")){
                Picasso.with(mContext)
                        .load(imageUri)
                        .fit()
                        .centerCrop()
                                //.placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(imageView, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                setProgressGone(progressBar);
                            }

                            @Override
                            public void onError() {
                                Util.showLog("error img loading : ", imageUri + " ???????");
                                setProgressGone(progressBar);
                            }
                        });
            } else {
                setProgressGone(progressBar);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void disPlayPicassoImageCenterCropWithLoader(Context mContext, final ImageView imageView, final String imageUri, final CircularProgressView progressBar) {
        try {
            setProgressShow(progressBar);
            if(imageUri != null && !imageUri.equals("")){
                Picasso.with(mContext)
                        .load(imageUri)
                        .fit()
                        .centerCrop()
                                //.placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(imageView, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                setProgressGone(progressBar);
                            }

                            @Override
                            public void onError() {
                                Util.showLog("error img loading : ", imageUri + " ???????");
                                setProgressGone(progressBar);
                            }
                        });
            } else {
                setProgressGone(progressBar);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void disPlayImageWithLoader(CouponApplication mContext, ImageView imageView, final ArrayList<String> imagesUri, final CircularProgressView progressBar, final IOnBlinkingComplete iOnBlinkingComplete) {
        try {

            //Util.showLog("Display imageURL = ", imageUri + " ??????");
            //imageView = new ImageView(mContext);

            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    //.showImageOnLoading(R.drawable.ic_stub)
                    //.showImageForEmptyUri(R.drawable.ic_empty)
                    //.showImageOnFail(R.drawable.ic_error)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .displayer(new RoundedBitmapDisplayer(0))
                    .build();

            if (imagesUri != null && imagesUri.size() > 0) {
                for (int i = 0; i < imagesUri.size(); i++) {
                    // Load image, decode it to Bitmap and display Bitmap in ImageView (or any other view
                    //  which implements ImageAware interface)
                    final int finalI = i;
                    mContext.imageLoader.displayImage(imagesUri.get(i), imageView, options, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            setProgressShow(progressBar);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            setProgressGone(progressBar);
                            if (finalI == (imagesUri.size() - 1) && iOnBlinkingComplete != null)
                                iOnBlinkingComplete.onComplete();
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            setProgressGone(progressBar);
                            if (finalI == (imagesUri.size() - 1) && iOnBlinkingComplete != null)
                                iOnBlinkingComplete.onComplete();
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                            setProgressGone(progressBar);
                            if (finalI == (imagesUri.size() - 1) && iOnBlinkingComplete != null)
                                iOnBlinkingComplete.onComplete();
                        }
                    }, new ImageLoadingProgressListener() {
                        @Override
                        public void onProgressUpdate(String imageUri, View view, int current, int total) {
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static String getDeviceId(Activity activity) {
        String deviceId = "";
        try {
            deviceId = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return deviceId;
    }


    public static String getLanguageShortCode() {
        String language_id = "";
        try {
            language_id = Locale.getDefault().getLanguage();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return language_id;
    }


    public static double parseStringToDouble(String txt) {
        double value = 0.0;
        try {
            if (txt != null && !txt.equals("")) {
                value = Double.parseDouble(txt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }


    public interface IOnBlinkingComplete {
        public void onComplete();
    }


    public static void cancelAllPendingApiCall(Activity activity) {
        try {
            //Ion.getDefault(activity).cancelAll(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static DialogProgress showProgressHud(Activity activity, String msg) {
        try {
            DialogProgress dialogProgress = new DialogProgress(activity);
            dialogProgress.show();
            if (msg != null || !msg.equals("")) {
                msg = "Please wait";
                //msg = null;
            }

            dialogProgress.setMessage(msg);

            return dialogProgress;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void dismissProgress(DialogProgress progressHud) {
        try {
            if (progressHud != null && progressHud.isShowing())
                progressHud.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*public static KProgressHUD showProgressHud(Activity activity, String msg) {
        try {
            if (msg != null && !msg.equals("")) {

            } else {
                msg = "Please wait";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            return KProgressHUD.create(activity)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel(msg)
                    .setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/


   /* public static void dismissProgress(KProgressHUD progressHud ) {
        try {
            if (progressHud != null && progressHud.isShowing())
                progressHud.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


    /*public static void checkGPSEnable(Activity activity) {
        try {
            LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            boolean gps_enabled = false;
            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (!gps_enabled) {
                showGPSAlert(activity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/


    /*public static void showGPSAlert(final Activity activity) {
        try {
            final DialogConfirm dialogConfirm = new DialogConfirm(activity);

            dialogConfirm.content("Location is not Enable.")//
                    .isTitleShow(false)//
                            //.showAnim(mContext.mBasIn)//
                            //.dismissAnim(mContext.mBasOut)//
                    .btnText("NO NEED", "ENABLE GPS")
                    .show();
            dialogConfirm.setOnBtnClickL(
                    new OnBtnClickL() {//left btn click listener
                        @Override
                        public void onBtnClick() {
                            dialogConfirm.dismiss();


                        }
                    },
                    new OnBtnClickL() {//right btn click listener
                        @Override
                        public void onBtnClick() {
                            dialogConfirm.dismiss();
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            activity.startActivity(intent);
                            //turnGPSOn(activity);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


    /*public static void disPlayImageWithLoader(CouponApplication mContext, final ImageView imageView, final String imageUri, final GifImageView giv_progress) {
        try {
            //Util.showLog("Display imageURL = ", imageUri + " ??????");
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    //.showImageOnLoading(R.drawable.placeholder)
                    .showImageForEmptyUri(R.drawable.placeholder)
                            //.showImageOnFail(R.drawable.ic_error)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .displayer(new RoundedBitmapDisplayer(0))
                    .build();

            // Load image, decode it to Bitmap and display Bitmap in ImageView (or any other view
            //  which implements ImageAware interface)
            mContext.imageLoader.displayImage(imageUri, imageView, options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    setProgressShow(giv_progress);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    setProgressGone(giv_progress);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    setProgressGone(giv_progress);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    setProgressGone(giv_progress);
                }
            }, new ImageLoadingProgressListener() {
                @Override
                public void onProgressUpdate(String imageUri, View view, int current, int total) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/


    private static void setProgressShow(View giv_progress) {
        try {
            if (giv_progress != null)
                giv_progress.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setProgressGone(View giv_progress) {
        try {
            if (giv_progress != null)
                giv_progress.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void turnGPSOn(Context context) {
        try {
            String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

            if (!provider.contains("gps")) { //if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                context.sendBroadcast(poke);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void turnGPSOff(Context context) {
        String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (provider.contains("gps")) { //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            context.sendBroadcast(poke);
        }
    }


    public interface INoInternet {
        void onError(String msg);
    }


    /*public static DialogContinueScan showNoInternetMSG(Activity activity) {
        final DialogContinueScan dialogContinueScan = new DialogContinueScan(activity);
        try {


            if (activity != null)
                dialogContinueScan.show();
            dialogContinueScan.setTexts(AppConstant.NO_INTERNET_ALERT, "", "OK");
            dialogContinueScan.setBodyTxtColor("#7a7776");
            dialogContinueScan.setButtonColor("#000000", null);

            dialogContinueScan.tv_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogContinueScan.dismiss();
                    try {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

        return dialogContinueScan;
    }*/


    /*public static DialogContinueScan showMSGDialog(Activity activity, String msg, String left_btn_txt, String right_btn_txt, String bodyTxtColor, String buttonColor) {

        final DialogContinueScan dialogContinueScan = new DialogContinueScan(activity);
        try {
            if (bodyTxtColor != null || bodyTxtColor.equals(""))
                bodyTxtColor = "#7a7776";

            if (buttonColor != null || buttonColor.equals(""))
                buttonColor = "#afacab";

            if (activity != null)
                dialogContinueScan.show();
            dialogContinueScan.setTexts(msg, left_btn_txt, right_btn_txt);
            dialogContinueScan.setBodyTxtColor(bodyTxtColor);
            dialogContinueScan.setButtonColor(buttonColor, buttonColor);

            dialogContinueScan.tv_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogContinueScan.dismiss();
                    try {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dialogContinueScan;
    }*/


    public static int convertStringToInt(String str){
        int number = 0;
        try{
            if(str != null && !str.equals("")){
                number = Integer.parseInt(str);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return number;
    }

    public static Double convertStringToDouble(String str){
        double number = 0;
        try{
            if(str != null && !str.equals("")){
                number = Double.parseDouble(str);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return number;
    }


    public static int roundNumber(String noStr){
        int noInt = 0;
        double number = convertStringToDouble(noStr);
        try{
            number = Math.ceil(number);
            noInt = (int) number;
        }catch (Exception e){
            e.printStackTrace();
        }
        return noInt;
    }


    /*****
     * Programmatically Selector
     */

    public static void setSelector(Activity activity,ImageView imageView, int normal, int pressed){
        try{
            StateListDrawable states = new StateListDrawable();
            states.addState(new int[] {android.R.attr.state_pressed},getResource(pressed,activity));
            states.addState(new int[] {android.R.attr.state_focused},getResource(normal, activity));
            states.addState(new int[]{}, getResource(normal, activity));
            if(imageView != null)
                imageView.setImageDrawable(states);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static Drawable getResource(int id, Context mContext){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return mContext.getResources().getDrawable(id, mContext.getTheme());
        } else {
            return mContext.getResources().getDrawable(id);
        }
    }



    public  static String getLocalDate(String utcTime, String dateFormat){
        showLog("utcTime : ", utcTime + " ???????");

        String formattedDate = "";
        try{
            SimpleDateFormat df = new SimpleDateFormat(dateFormat);
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = df.parse(utcTime);
            df.setTimeZone(TimeZone.getDefault());
            formattedDate = df.format(date);
        }catch (Exception e){
            e.printStackTrace();
        }
        showLog("utc to local : ", formattedDate + " ???????");
        return formattedDate;
    }

    public static Date getDateFromString(String dateStr, String formatterStr){
        Date date = null;
        try{
            if(dateStr != null && !dateStr.equals("")
                    && formatterStr != null && !formatterStr.equals("")){
                SimpleDateFormat formatter = new SimpleDateFormat(formatterStr);
                return formatter.parse(dateStr);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return date;
    }

    public static Date getCurrentDate(String formatterStr){
        Date date = null;
        try{
            if(formatterStr != null && !formatterStr.equals("")){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                date = new Date();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return date;
    }

    public static long getMillisecondFromDate(Date date){
        long mlliSec = 0;
        try{
            if(date != null ){
                mlliSec =  date.getTime();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return mlliSec;
    }


    public static int dayDifference(String startDateStr, String endDateStr, String dateFormat){
        int dateDifference = 0;
        try{
            showLog("startDateStr : ", startDateStr + " ?????");
            showLog("endDateStr : ", endDateStr + " ?????");

            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
            Date startDate = formatter.parse(startDateStr);
            Date endDate = formatter.parse(endDateStr);
            /*long diff = endDate.getTime() - startDate.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000);*/
            dateDifference = (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
            showLog("dayDifference : ", dateDifference + " ?????");
        }catch (Exception e){
            e.printStackTrace();
        }
        return dateDifference;
    }

    public static String getCurrentDateString(String dateFormatStr){
        String curDate = "";
        try{
            Date date = null;
            if(dateFormatStr != null && !dateFormatStr.equals("")){
                SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatStr);
                date = new Date();
                curDate = dateFormat.format(date);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        showLog("curDate : ", curDate + " ????");
        return curDate;

    }



    public static long getUTCCurrentDifference(String expireTime){
        long diffMilliSec = 0;
        try{
            Date startDate;
            Date curdate;
            long startMilliSec;
            long curMilliSec;

            String localTime = Util.getLocalDate(expireTime,AppConstant.DATE_FORMATTER_CLUE_START);
            startDate = Util.getDateFromString(localTime, AppConstant.DATE_FORMATTER_CLUE_START);
            startMilliSec = Util.getMillisecondFromDate(startDate);
            curdate = Util.getCurrentDate(AppConstant.DATE_FORMATTER_CLUE_START);
            curMilliSec = Util.getMillisecondFromDate(curdate);
            diffMilliSec = startMilliSec - curMilliSec;
            showLog("Util current diffMilliSec ", diffMilliSec + " ??????");
        }catch (Exception e){
            e.printStackTrace();
        }
        return diffMilliSec;
    }


    public static Snackbar showSnackBar(Activity activity, String msg, String btnTxt,boolean shouldShowBtn){
        Snackbar snackbar = null;
        try{
            snackbar = Snackbar.with(activity);
            snackbar.type(SnackbarType.MULTI_LINE);
            snackbar.text(msg); // text to be displayed
            snackbar.duration(Snackbar.SnackbarDuration.LENGTH_SHORT); // make it shorter
            snackbar.animation(true) ;// don't animate it
            snackbar.swipeToDismiss(true);
            if(shouldShowBtn){
                snackbar.actionLabel(btnTxt); // action button label
                snackbar.actionColor(Color.RED); // action button label color
            }
            snackbar.show(activity);

        }catch (Exception e){
            e.printStackTrace();
        }

        return snackbar;
    }
    public static void setTextViewScrollable(TextView textView){
        try{
            /*****
             * To make TextView scrollable after set max line.
             */
            textView.setMovementMethod(new ScrollingMovementMethod());
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public static void createLocalNotification(Context context){
        try{
            Intent notificationIntent = new Intent(context, ClueDetailsActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(ClueDetailsActivity.class);
            stackBuilder.addNextIntent(notificationIntent);

            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

            Notification notification = builder.setContentTitle("ic_folder_special_black_36dp")
                    .setContentText("New Notification From Demo App..")
                    .setTicker("New Message Alert!")
                    .setSmallIcon(R.drawable.ic_folder_special_black_36dp)
                    .setContentIntent(pendingIntent).build();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notification);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void createLocalNotification1(Context context){
        try{
            Intent notificationIntent = new Intent(context, ClueDetailsActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(ClueDetailsActivity.class);
            stackBuilder.addNextIntent(notificationIntent);

            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

            Notification notification = builder.setContentTitle("coupon icon ")
                    .setContentText("New Notification From Demo App..")
                    .setTicker("New Message Alert!")
                    .setSmallIcon(R.drawable.icon_notification)
                    .setContentIntent(pendingIntent).build();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notification);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static Bitmap takeScreenShot(Activity activity) {
        try{
            View view = activity.getWindow().getDecorView();
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();


            Bitmap b1 = view.getDrawingCache();
            Rect frame = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;

            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;


            Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
            view.destroyDrawingCache();
            return b;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
