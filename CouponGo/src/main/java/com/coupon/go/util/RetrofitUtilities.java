package com.coupon.go.util;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.client.Response;

/**
 * Created by zabingo on 22/6/16.
 */
public abstract class RetrofitUtilities {

    public static RestAdapter getRetrofitAdapter() {
        RestAdapter restAdapter = null;
        try {
            RequestInterceptor requestInterceptor = new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    request.addHeader(UrlConstant.API_HEADER_KEY, UrlConstant.API_HEADER_VALUE);
                }
            };

            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(UrlConstant.BASE_URL)
                    .setRequestInterceptor(requestInterceptor)
                    .setLogLevel(RestAdapter.LogLevel.BASIC)
                    .setLog(new AndroidLog("Api info"))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return restAdapter;
    }


    /*****
     * Convert Response object to string .
     * @param response
     * @return
     */
    public static String createStringFromResponse(Response response) {
        try {
            //Try to get response body
            BufferedReader reader = null;
            StringBuilder sb = new StringBuilder();
            try {
                reader = new BufferedReader(new InputStreamReader(response.getBody().in()));
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isValidJson(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


}
