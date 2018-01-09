package com.coupon.go.model.response_wrapper;

import com.coupon.go.model.TutorialLink;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by AHMED on 11/08/16.
 */
public class TutorialResponse implements Serializable {
    public String response_code = "";
    public String response_msg = "";
    public ArrayList<TutorialLink> response_data;
}
