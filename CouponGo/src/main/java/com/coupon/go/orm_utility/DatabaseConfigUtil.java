package com.coupon.go.orm_utility;

import com.coupon.go.model.Coupon;
import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.IOException;
import java.sql.SQLException;


public class DatabaseConfigUtil extends OrmLiteConfigUtil {

	private static final Class<?> classes[] = new Class[]{Coupon.class};
	
	public static void main(String arg[]) throws SQLException, IOException {
		writeConfigFile("ormlite_config.txt", classes);
	}
	
}
