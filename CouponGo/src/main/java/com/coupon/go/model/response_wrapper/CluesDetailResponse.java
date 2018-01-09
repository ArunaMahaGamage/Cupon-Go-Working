package com.coupon.go.model.response_wrapper;

import com.coupon.go.model.Clue;

import java.io.Serializable;

/**
 * Created by AHMED on 11/08/16.
 */
public class CluesDetailResponse implements Serializable {
    public String response_code = "";
    public String response_msg = "";
    public Clue response_data;
}
