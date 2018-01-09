package com.coupon.go.module.arcamera;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.coupon.go.R;
import com.coupon.go.model.Clue;
import com.coupon.go.value_transfer.ClueTransferActivity;
import com.inglobetechnologies.armedia.math.Vector3f;
import com.inglobetechnologies.armedia.sdk.rendering.ARMediaLocationTrackerActivity;
import com.inglobetechnologies.armedia.sdk.rendering.ARModel;
import com.inglobetechnologies.armedia.sdk.rendering.ARObject;
import com.inglobetechnologies.armedia.sdk.rendering.IARMediaLocationListener;
import com.inglobetechnologies.armedia.sdk.rendering.IARMediaRenderingListener;
import com.inglobetechnologies.armedia.sdk.tracking.ARMediaTrackerUtilities;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by RansikaDeSilva on 1/4/18.
 */

public class ARCameraActivity extends ARMediaLocationTrackerActivity implements IARMediaRenderingListener, IARMediaLocationListener {

    private static final String DEBUG_TAG = "ARMedia Debug";

    // copy here your app's SDK key...
    private static final String AR_SDK_KEY = "16E4017DF4FADC87FA03A00E5F149A1CD373349A";

    private static final boolean USE_HIGH_RES = true;

    private String models_folder;

    private static final String MODEL_TO_LOAD = "axes.osg";

    private boolean configuration_changed;

    private Clue clue;

    Location[] locations;
    ArrayList arraylist;

    private boolean fromDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar_camera);

        Bundle extras = getIntent().getExtras();
        arraylist = extras.getParcelableArrayList("arraylist");

        fromDashboard = getIntent().getBooleanExtra("fromDashboard",false);

        // extract the models from the assets folder first...
        models_folder = getFilesDir().getAbsolutePath()+ File.separator+"example_models";
        if (!new File(models_folder).exists()) {
            new File(models_folder).mkdirs();
        }


        ARMediaTrackerUtilities.extractAssetsFolder(models_folder, "models", getAssets());

        // NOTE: the following methods must be called before calling the "onStart" method!
        mustShowActivityIndicatorWhenBusy(true);
        setTrackingListener(this);
        setRenderingListener(this);
        setApplicationKey(AR_SDK_KEY);
        setHighResolution(USE_HIGH_RES);
        setARView((ViewGroup)findViewById(R.id.layout_frame));

        if(!fromDashboard){
            getObject();
            Log.d("ARCameraActivity","ClueDetails: "+ clue.latitude +" "+clue.longitude + " "+ clue.coupon_title);
        }


        configuration_changed = false;



        // setup events handlers...
        findViewById(R.id.btn_take_screenshot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { takeScreenshot(); }
        });

        findViewById(R.id.layout_frame).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1)
            {
                if (arg1.getActionMasked() == MotionEvent.ACTION_UP)
                {
                    pickObjectFromScreenLocation(new Point((int)arg1.getX(), (int)arg1.getY()));
                    ARObject obj = getPickedObject();
                    if (obj != null)
                    {
                        Log.i(DEBUG_TAG, "Yout picked: "+obj.getName());
                        if (obj instanceof ARModel) {
                            ((ARModel)obj).togglePauseAnimation();
                        }
                    }
                    else Log.i(DEBUG_TAG, "Nothing picked");
                }
                return true;
            }
        });
    }
    private void getObject() {
        try {
            clue = ClueTransferActivity.getObject(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * IMPORTANT: When this is called, the tracker will be initialized again!
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        configuration_changed = true;
    }

    private void loadModels() {
        if (configuration_changed) return;

//        // add some locations (NOTE: modify them with something that is near your place)...
//        Location location_0 = new Location("Eiffel Tower");
//        location_0.setLatitude(48.85883f);
//        location_0.setLongitude(2.294433f);
//        addLocationWithName(location_0.getProvider(), location_0);
//
//        Location location_1 = new Location("Colosseum");
//        location_1.setLatitude(41.890434f);
//        location_1.setLongitude(12.492193f);
//        addLocationWithName(location_1.getProvider(), location_1);
//
//
//
//        // ...eventually specifying altitude as well...
//        Location location_2 = new Location("Statue of Liberty");//provider name is unecessary
//        location_2.setLatitude(40.689477f);//your coords of course
//        location_2.setLongitude(-74.044538f);
//        location_2.setAltitude(10.5f);
//        location_2.setAccuracy(1.0f);
//        addLocationWithName(location_2.getProvider(), location_2);

        //6.8801° N, 79.8797° E
        //6.880259, 79.880215
        if (!fromDashboard){
            Log.d("ARCameraActivity","fromDashboard1");
            Location location = new Location(clue.coupon_title.toString());//provider name is unecessary
            location.setLatitude(Float.parseFloat(clue.latitude));
            location.setLongitude(Float.parseFloat(clue.longitude));
            location.setAltitude(10.5f);
            location.setAccuracy(1.0f);
            addLocationWithName(location.getProvider(), location);

            // setup 3D content/scene here...
            if (new File(models_folder+File.separator+MODEL_TO_LOAD).exists()) {
                ARModel axes = new ARModel(models_folder+File.separator+MODEL_TO_LOAD);
                axes.setScale(new Vector3f(2.0f, 2.0f, 2.0f));

                addObjectToLocationWithName(axes, location.getProvider());
            }
            else
                Log.i(DEBUG_TAG, "WARNING: could not load specified model.");
        }
        else {
            Log.d("ARCameraActivity", "fromDashboard2");

            try {

                Bundle extras = getIntent().getExtras();
                arraylist = extras.getParcelableArrayList("arraylist");
//                Log.e("arraylist", String.valueOf(arraylist.size()));

                locations = new Location[arraylist.size()];

                    String [] lat = {"6.8802449","6.8802738","6.8803487","6.8802449","6.8802738","6.8803487","6.8802449","6.8802738","6.8803487","6.8803487"};
                    String[] lon ={"79.8799442","79.8802661","79.8805243","79.8799442","79.8802661","79.8805243","79.8799442","79.8802661","79.8805243","79.8805243"};

                for (int i = 0; i < 2; i++) {

                Clue temClue = (Clue) arraylist.get(i);
                    Location location = new Location(temClue.coupon_title.toString());//provider name is unecessary
                    location.setLatitude(Float.parseFloat("6.8802449"));
                    location.setLongitude(Float.parseFloat("79.8799442"));

                   /* location.setLatitude(Float.parseFloat(temClue.latitude));
                    location.setLongitude(Float.parseFloat(temClue.longitude));*/

                    location.setAltitude(10.5f);
                    location.setAccuracy(1.0f);
                    addLocationWithName(location.getProvider(), location);

                    locations[i] = location;

                    Log.e("lat000000000", String.valueOf(locations[i].getLatitude()));
                    Log.e("longggg", String.valueOf(locations[i].getLongitude()));

                    // setup 3D content/scene here...
                    if (new File(models_folder + File.separator + MODEL_TO_LOAD).exists()) {
                        ARModel axes = new ARModel(models_folder + File.separator + MODEL_TO_LOAD);
                        axes.setScale(new Vector3f(2.0f, 2.0f, 2.0f));

                        addObjectToLocationWithName(axes, location.getProvider());
                    } else
                        Log.i(DEBUG_TAG, "WARNING: could not load specified model.");
                }

            } catch (Exception e) {
            }
        }



    }



    @Override
    public void trackingInitializedWithStatus(boolean status) {
        if(status) {
            //From now on, we can start adding our contents to the scene
            loadModels();

        } else {
            Log.i(DEBUG_TAG, "WARNING: could not init the tracker");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder a = new AlertDialog.Builder(ARCameraActivity.this);
                    a.setTitle("Initialization failed");
                    a.setMessage("Could not init the tracker.");
                    a.setNeutralButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {}
                    });
                    a.create().show();
                }
            });
        }
    }

    @Override
    public void screenshotReady() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // a screenshot is ready, just retrieve it and do whatever you want with it...
                Bitmap bmp = getScreenshot();
                ImageView v = (ImageView)findViewById(R.id.img_screenshot);
                v.setImageBitmap(bmp);
            }
        });
    }



    @Override
    public void preRenderingCallback() {
        // Do whatever you want before rendering current frame...
    }

    @Override
    public void postRenderingCallback() {
        // Do whatever you want just after have rendered current frame...
    }

    @Override
    public void locationUpdated() {
        Log.i(DEBUG_TAG, "locationUpdated...");
    }

}
