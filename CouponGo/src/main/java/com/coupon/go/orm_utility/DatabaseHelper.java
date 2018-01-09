package com.coupon.go.orm_utility;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.coupon.go.R;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;



public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	public DatabaseHelper(Context context) {
		super(context,DatabaseManager.DATABASE_NAME, null, DatabaseManager.DATABASE_VERSION, R.raw.ormlite_config);
	}

	/**
	 * This is called when the database is first created. Usually you should
	 * call createTable statements here to create the tables that will store
	 * your data.
	 */
	@Override
	public void onCreate(SQLiteDatabase db,
			ConnectionSource connectionSource) {
		if(DatabaseManager.iDatabaseHelper != null)
			DatabaseManager.iDatabaseHelper.onCreate(db, connectionSource);
	}

	/**
	 * This is called when your application is upgraded and it has a higher
	 * version number. This allows you to adjust the various data to match
	 * the new version number.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db,
			ConnectionSource connectionSource, int oldVersion,
			int newVersion) {
		if(DatabaseManager.iDatabaseHelper != null)
		DatabaseManager.iDatabaseHelper.onUpgrade(db, connectionSource, oldVersion,newVersion);
	}

	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
		if(DatabaseManager.iDatabaseHelper != null)
		DatabaseManager.iDatabaseHelper.close();
	}

}
