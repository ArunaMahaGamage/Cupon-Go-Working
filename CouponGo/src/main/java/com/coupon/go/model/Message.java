package com.coupon.go.model;

import java.io.Serializable;

/**
 * Created by zabingo on 27/8/16.
 */
public class Message implements Serializable {
    public String msg = "";
    public int id;
    public String pre_launch_clue = "";
    public String main_clue = "";

    @Override
    public String toString() {
        return "Message{" +
                "msg='" + msg + '\'' +
                ", id='" + id + '\'' +
                ", pre_launch_clue='" + pre_launch_clue + '\'' +
                ", main_clue='" + main_clue + '\'' +
                '}';
    }
}
