package com.coupon.go.webservice.interfaces;

import java.util.Map;

public interface IRequestCaller {

	public String getWebServiceMethod();

	public String[] getKeys();

	public Map<String,String> getValues();
}
