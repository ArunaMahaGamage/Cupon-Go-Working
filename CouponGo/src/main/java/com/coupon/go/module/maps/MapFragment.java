package com.coupon.go.module.maps;

import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.coupon.go.R;
import com.coupon.go.model.Clue;
import com.coupon.go.util.Util;
import com.coupon.go.value_transfer.ClueTransfer;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zabingo on 11/5/16.
 */
public class MapFragment extends Fragment {
    private MapView mapView;
    private GoogleMap map;
    private Location currentLocation = null;
    private LatLng destLatLng = null;
    private LatLng currentLatLng = null;
    private GPSTracker gpsTracker;
    private boolean shouldClickable = true;

    private Clue coupon;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gpsTracker = new GPSTracker(getActivity());
        getObject();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mapview, container, false);

        try {
            currentLocation = gpsTracker.getLocation();

            if(coupon == null){
                coupon = new Clue();
                coupon.latitude = currentLocation.getLatitude() + "";
                coupon.longitude = currentLocation.getLongitude() + "";
            }

            try {
                MapsInitializer.initialize(getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }

            switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity())) {
                case ConnectionResult.SUCCESS:
                    mapView = (MapView) v.findViewById(R.id.map);
                    mapView.onCreate(savedInstanceState);
                    // Gets to GoogleMap from the MapView and does initialization stuff
                    if (mapView != null) {
                        map = mapView.getMap();
                        map.getUiSettings().setMyLocationButtonEnabled(false);
                        map.setMyLocationEnabled(true);

                        map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                            @Override
                            public void onMyLocationChange(Location location) {
                                currentLocation = location;
                                //currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                                //drawRoute();
                            }
                        });


                        addMarkers(coupon);

                        currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        //drawRoute(currentLatLng, destLatLng);

                    }
                    break;
                case ConnectionResult.SERVICE_MISSING:
                    Toast.makeText(getActivity(), "GOOGLE PLAY SERVICE MISSING", Toast.LENGTH_SHORT).show();
                    break;
                case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                    Toast.makeText(getActivity(), "GOOGLE PLAY SERVICE UPDATE REQUIRED", Toast.LENGTH_SHORT).show();
                    break;
            }



            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    try{
                        currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        destLatLng = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                        //drawRoute(currentLatLng, destLatLng);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return false;
                }
            });

            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng point) {
                    /*Util.showLog("Map", "Map clicked");
                    try {
                        if (shouldClickable) {
                            DashboardActivity mActivity = (DashboardActivity) getActivity();
                            MapFragment mapViewFragment = createNewMapFragment();
                            mapViewFragment.enableClickable(false);
                            RecommendedTransfer.sendObject(mapViewFragment, coupon);
                            mActivity.pushTopChildFragments(mapViewFragment, false, "MAPVIEWFRAGMENT", false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/


                }
            });


            map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                @Override
                public void onInfoWindowClick(Marker marker) {
                    try {
                        /*if (shouldClickable) {
                            DashboardActivity mActivity = (DashboardActivity) getActivity();
                            MapFragment mapViewFragment = createNewMapFragment();
                            mapViewFragment.enableClickable(false);
                            RecommendedTransfer.sendObject(mapViewFragment, coupon);
                            mActivity.pushTopChildFragments(mapViewFragment, false, "MAPVIEWFRAGMENT", false);
                        }*/

                        currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        destLatLng = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                        //drawRoute(currentLatLng, destLatLng);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return v;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }



    private void getObject() {
        try {
            coupon = ClueTransfer.getObject(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enableClickable(boolean isClickable) {
        shouldClickable = isClickable;
    }


    private MapFragment createNewMapFragment() {
        MapFragment mapViewFragment = null;
        try {
            mapViewFragment = new MapFragment();
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapViewFragment;
    }


    public void drawRoute() {
        try {


            currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            destLatLng = new LatLng(Util.parseStringToDouble(coupon.latitude), Util.parseStringToDouble(coupon.longitude));
            //addPolyLines(currentLocation);
            // Getting URL to the Google Directions API
            Util.showLog("currentLatLng >>>>>> ", currentLatLng + " ??????");
            Util.showLog("destLatLng >>>>>> ", destLatLng + " ??????");
            String url = getDirectionsUrl(currentLatLng, destLatLng);
            DownloadTask downloadTask = new DownloadTask();
            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawRoute(LatLng currentLatLng, LatLng destLatLng) {
        try {
            //addPolyLines(currentLocation);
            // Getting URL to the Google Directions API
            Util.showLog("currentLatLng >>>>>> ", currentLatLng + " ??????");
            Util.showLog("destLatLng >>>>>> ", destLatLng + " ??????");
            String url = getDirectionsUrl(currentLatLng, destLatLng);
            DownloadTask downloadTask = new DownloadTask();
            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    public void addMarkers(Clue coupon) {
        Log.d("MapFragment","coupon details: "+coupon.longitude+", "+coupon.latitude);
        try{
            this.coupon = coupon;
            Util.showLog("coupon : ", coupon.toString());
            if (map != null)
                map.clear();

            createMarker(map, coupon);
            double lat = Util.parseStringToDouble(coupon.latitude);
            double lng = Util.parseStringToDouble(coupon.longitude);
            destLatLng = new LatLng(lat, lng);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(destLatLng, 15));
        }catch (Exception e){
            e.printStackTrace();
        }

        /*try {
            for (int i = 0; i < coupon.address_records.size(); i++) {
                createMarker(map, coupon, coupon.address_records.get(i));
            }

            double lat = Util.parseStringToDouble(coupon.address_records.get(0).latitude);
            double lng = Util.parseStringToDouble(coupon.address_records.get(0).longitude);
            destLatLng = new LatLng(lat, lng);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(destLatLng, 15));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }


    private Marker createMarker(GoogleMap map,Clue store) {

        try{
            double lat = Util.parseStringToDouble(store.latitude);
            double lng = Util.parseStringToDouble(store.longitude);
            LatLng latlng = new LatLng(lat, lng);
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(latlng)
                    .title(store.promo_company)
                            //.snippet(store.short_description)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin_orange)));
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }


    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Building the url to the web service
        String url = "";

        try {
            // Origin of route
            String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

            // Destination of route
            String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

            // Sensor enabled
            String sensor = "sensor=false";

            // Building the parameters to the web service
            String parameters = str_origin + "&" + str_dest + "&" + sensor;

            // Output format
            String output = "json";

            // Building the url to the web service
            url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return url;
    }


    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            try {
                ArrayList<LatLng> points = null;
                PolylineOptions lineOptions = null;
                MarkerOptions markerOptions = new MarkerOptions();

                // Traversing through all the routes
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(10);
                    lineOptions.color(Color.parseColor("#346dFF"));
                }

                // Drawing polyline in the Google Map for the i-th route
                if (map != null)
                    map.addPolyline(lineOptions);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}