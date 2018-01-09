package com.coupon.go.orm_utility;

import android.app.Activity;
import android.content.Context;
import android.database.SQLException;

import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

public class Orm_SQLManager {

	public static int insertIntoTable(Class<?> clazz, Object object,Activity activity, DatabaseManager databaseManager) {
		RuntimeExceptionDao<Object, Integer> runtimeExceptionDao = (RuntimeExceptionDao<Object, Integer>) databaseManager.getHelper(activity).getRuntimeExceptionDao(clazz);
		CreateOrUpdateStatus createOrUpdateStatus = runtimeExceptionDao.createOrUpdate(object);
		int status = createOrUpdateStatus.getNumLinesChanged();
		return status;
	}


	public static int insertIntoTable(Class<?> clazz, Object object,Context activity, DatabaseManager databaseManager) {
		RuntimeExceptionDao<Object, Integer> runtimeExceptionDao = (RuntimeExceptionDao<Object, Integer>) databaseManager.getHelper(activity).getRuntimeExceptionDao(clazz);
		CreateOrUpdateStatus createOrUpdateStatus = runtimeExceptionDao.createOrUpdate(object);
		int status = createOrUpdateStatus.getNumLinesChanged();
		return status;
	}


	public static void insertCollectionIntoTable(Class<?> clazz,final List<?> objects, Activity activity,DatabaseManager databaseManager) {
		final RuntimeExceptionDao<Object, Integer> runtimeExceptionDao = (RuntimeExceptionDao<Object, Integer>) databaseManager.getHelper(activity).getRuntimeExceptionDao(clazz);
		runtimeExceptionDao.callBatchTasks(new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				for (Object object : objects) {
					runtimeExceptionDao.createOrUpdate(object);
				}
				return null;
			}

		});
	}
	
	public static void insertCollectionIntoTable(Class<?> clazz,
			final Collection<?> objects, Activity activity,
			DatabaseManager databaseManager) {
		final RuntimeExceptionDao<Object, Integer> runtimeExceptionDao = (RuntimeExceptionDao<Object, Integer>) databaseManager
				.getHelper(activity).getRuntimeExceptionDao(clazz);
		runtimeExceptionDao.callBatchTasks(new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				for (Object object : objects) {
					runtimeExceptionDao.createOrUpdate(object);
				}
				return null;
			}

		});
	}
	

	public static Object getAllTableObjects(Class<?> clazz,Activity activity, DatabaseManager databaseManager) {
		RuntimeExceptionDao<Object, Integer> runtimeExceptionDao = (RuntimeExceptionDao<Object, Integer>) databaseManager.getHelper(activity).getRuntimeExceptionDao(clazz);
		return runtimeExceptionDao.queryForAll();
	}
	
	public static void truncateTable(Class<?> clazz,Context context, DatabaseManager databaseManager) throws SQLException {
		RuntimeExceptionDao<Object, Integer> runtimeExceptionDao = (RuntimeExceptionDao<Object, Integer>) databaseManager.getHelper(context).getRuntimeExceptionDao(clazz);
		DeleteBuilder<Object, Integer> deleteBuilder = runtimeExceptionDao.deleteBuilder();
		int index;
		try {
			index = deleteBuilder.delete();
			System.out.println("deleted index "+index);
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
		}
		
		}
	
	public static Object getAllTableObjectsByOrder(
			Class<?> clazz,
			Activity activity,
			String orderByColumName,
			boolean isAscending,
			DatabaseManager databaseManager
			) {
		Object response = null;
		try {
			RuntimeExceptionDao<Object, Integer> objRuntimeExceptionDao = (RuntimeExceptionDao<Object, Integer>) databaseManager.getHelper(activity).getRuntimeExceptionDao(clazz);
			QueryBuilder<Object, Integer> queryBuilder = objRuntimeExceptionDao
					.queryBuilder();
			queryBuilder.orderBy(orderByColumName, isAscending/**isAscending*/);
			response = queryBuilder.query();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public static Object getTableObjectsByOrder(
			DatabaseManager databaseManager, Activity activity, String column,String orderByColumName,boolean isAscending,
			int id, Class<?> clazz) {
		Object response = null;
		try {
			RuntimeExceptionDao<Object, Integer> objRuntimeExceptionDao = (RuntimeExceptionDao<Object, Integer>) databaseManager.getHelper(activity).getRuntimeExceptionDao(clazz);
			QueryBuilder<Object, Integer> queryBuilder = objRuntimeExceptionDao
					.queryBuilder();
			Where where = queryBuilder.where();
			where.eq(column, id);
			queryBuilder.orderBy(orderByColumName, isAscending/**isAscending*/);
			response = queryBuilder.query();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}



	public static Object getTableObjectsByOrder(
			Class<?> clazz,
			Activity activity,
			String column,
			String orderByColumName,
			boolean isAscending,
			String id,
			DatabaseManager databaseManager) {
		Object response = null;
		try {
			RuntimeExceptionDao<Object, Integer> objRuntimeExceptionDao = (RuntimeExceptionDao<Object, Integer>) databaseManager.getHelper(activity).getRuntimeExceptionDao(clazz);
			QueryBuilder<Object, Integer> queryBuilder = objRuntimeExceptionDao
					.queryBuilder();
			Where where = queryBuilder.where();
			where.eq(column, id);
			queryBuilder.orderBy(orderByColumName, isAscending/**isAscending*/);
			response = queryBuilder.query();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}


	/**
	* Remove those rows that id not supplied in rowIdContainner
	* 
	* @param rowIdContainner Hold all the id with separator
	* @param separator separate title
	* @param columnName
	* @param clazz
	* @param activity
	* @param databaseManager
	*/
	public static void keepSeletedIdRemoveOther(String rowIdContainner,
			String separator, String columnName, Class<?> clazz,
			Activity activity, DatabaseManager databaseManager) {
		try {
			String[] strArray = rowIdContainner.split(",");
			List<Object> keepsIdArray = new ArrayList<Object>();
			if (strArray != null && strArray.length > 0) {
				for (String id : strArray)
					keepsIdArray.add(id);
				RuntimeExceptionDao<Object, Integer> runtimeExceptionDao = (RuntimeExceptionDao<Object, Integer>) databaseManager
						.getHelper(activity).getRuntimeExceptionDao(clazz);
				DeleteBuilder<Object, Integer> deleteBuilder = runtimeExceptionDao
						.deleteBuilder();
				Where where = deleteBuilder.where();
				where.notIn(columnName, keepsIdArray);
				System.out.println("Query "
						+ deleteBuilder.prepareStatementString());
				deleteBuilder.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	* Remove those rows that id supplied in rowIdContainner
	* 
	* @param rowIdContainner Hold all the id with separator
	* @param separator separate title
	* @param columnName
	* @param clazz
	* @param activity
	* @param databaseManager
	*/
	public static void removeSeletedId(String rowIdContainner,
			String separator, String columnName, Class<?> clazz,
			Activity activity, DatabaseManager databaseManager) {
		try {
			String[] strArray = rowIdContainner.split(",");
			List<Object> keepsIdArray = new ArrayList<Object>();
			if (strArray != null && strArray.length > 0) {
				for (String id : strArray)
					keepsIdArray.add(id);
				RuntimeExceptionDao<Object, Integer> runtimeExceptionDao = (RuntimeExceptionDao<Object, Integer>) databaseManager
						.getHelper(activity).getRuntimeExceptionDao(clazz);
				DeleteBuilder<Object, Integer> deleteBuilder = runtimeExceptionDao
						.deleteBuilder();
				Where where = deleteBuilder.where();
				where.in(columnName, keepsIdArray);
				System.out.println("Query "	+ deleteBuilder.prepareStatementString());
				deleteBuilder.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * Remove those rows that playlist_id supplied in rowIdContainner
	 *
	 * @param rowIdContainner Hold all the playlist_id with separator
	 * @param columnName
	 * @param clazz
	 * @param activity
	 * @param databaseManager
	 */
	public static void removeSeletedId(
			Class<?> clazz,
			String rowIdContainner,
			String columnName,
			Activity activity,
			DatabaseManager databaseManager) {
		try {
			String[] strArray = rowIdContainner.split(",");
			List<Object> keepsIdArray = new ArrayList<Object>();
			if (strArray != null && strArray.length > 0) {
				for (String id : strArray)
					keepsIdArray.add(id);
				RuntimeExceptionDao<Object, Integer> runtimeExceptionDao = (RuntimeExceptionDao<Object, Integer>) databaseManager
						.getHelper(activity).getRuntimeExceptionDao(clazz);
				DeleteBuilder<Object, Integer> deleteBuilder = runtimeExceptionDao.deleteBuilder();
				Where where = deleteBuilder.where();
				where.in(columnName, keepsIdArray);
				System.out.println("Query "+ deleteBuilder.prepareStatementString());
				deleteBuilder.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static Object getSelectedColumn(
			DatabaseManager databaseManager,
			Activity activity, String column,
			int id, Class<?> clazz) {
		List<Object> response = null;
		try {
			RuntimeExceptionDao<Object, Integer> objRuntimeExceptionDao = (RuntimeExceptionDao<Object, Integer>) databaseManager
					.getHelper(activity).getRuntimeExceptionDao(clazz);
			QueryBuilder<Object, Integer> queryBuilder = objRuntimeExceptionDao
					.queryBuilder();
			Where where = queryBuilder.where();
			where.eq(column, id);
			System.out.println("Query: " + queryBuilder.prepareStatementString().toString());
			response = queryBuilder.query();
			if(response != null && response.size() > 0){
				return response.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public static Object getSelectedColumn(
			DatabaseManager databaseManager, Activity activity, String column,
			String id, Class<?> clazz) {
		List<Object> response = null;
		try {
			RuntimeExceptionDao<Object, String> objRuntimeExceptionDao = (RuntimeExceptionDao<Object, String>) databaseManager
					.getHelper(activity).getRuntimeExceptionDao(clazz);
			QueryBuilder<Object, String> queryBuilder = objRuntimeExceptionDao
					.queryBuilder();
			Where where = queryBuilder.where();
			where.eq(column, id);
			//System.out.println("Query: " + queryBuilder.prepareStatementString().toString());
			response = queryBuilder.query();
			if(response != null && response.size() > 0){
				return response.get(0);
			}else{
				response = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public static Object getSelectedColumnWithAnd(
			DatabaseManager databaseManager, Activity activity, String column,
			String id, String column1,
			String id1,Class<?> clazz) {
		List<Object> response = null;
		try {
			RuntimeExceptionDao<Object, String> objRuntimeExceptionDao = (RuntimeExceptionDao<Object, String>) databaseManager
					.getHelper(activity).getRuntimeExceptionDao(clazz);
			QueryBuilder<Object, String> queryBuilder = objRuntimeExceptionDao
					.queryBuilder();
			Where where = queryBuilder.where();
			where.and(where.eq(column, id), where.eq(column1, id1));
			//where.eq(column, id);
			//System.out.println("Query: " + queryBuilder.prepareStatementString().toString());
			response = queryBuilder.query();
			if(response != null && response.size() > 0){
				return response.get(0);
			}else{
				response = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}



	public static Object getSelectedColumnListOrderBy(
			Class<?> clazz,
			Activity activity,
			String column,
			String id,
			String orderByColumName,
			boolean isAscending,
			DatabaseManager databaseManager
	) {
		Object response = null;
		try {
			RuntimeExceptionDao<Object, String> objRuntimeExceptionDao = (RuntimeExceptionDao<Object, String>) databaseManager
					.getHelper(activity).getRuntimeExceptionDao(clazz);
			QueryBuilder<Object, String> queryBuilder = objRuntimeExceptionDao
					.queryBuilder();
			Where where = queryBuilder.where();
			where.eq(column, id);
			queryBuilder.orderBy(orderByColumName, isAscending/**isAscending*/);
			System.out.println("Query: " + queryBuilder.prepareStatementString().toString());
			response = queryBuilder.query();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}


	public static Object getSelectedColumnListWithAndOrderBy(
			Class<?> clazz,
			Activity activity,
			String column,
			String id,
			String column1,
			String id1,
			String orderByColumName,
			boolean isAscending,
			DatabaseManager databaseManager
	) {
		Object response = null;
		try {
			RuntimeExceptionDao<Object, String> objRuntimeExceptionDao = (RuntimeExceptionDao<Object, String>) databaseManager
					.getHelper(activity).getRuntimeExceptionDao(clazz);
			QueryBuilder<Object, String> queryBuilder = objRuntimeExceptionDao
					.queryBuilder();
			//Where where = queryBuilder.where();
			//where.eq(column, id);
			//queryBuilder.orderBy(orderByColumName, isAscending/**isAscending*/);

			Where where = queryBuilder.where();
			where.and(where.eq(column, id), where.eq(column1, id1));
			queryBuilder.orderBy(orderByColumName, isAscending/**isAscending*/);

			System.out.println("Query: " + queryBuilder.prepareStatementString().toString());
			response = queryBuilder.query();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}


	public static Object getSelectedColumn(
			DatabaseManager databaseManager, Activity activity, String column,
			boolean id, Class<?> clazz) {
		List<Object> response = null;
		try {
			RuntimeExceptionDao<Object, String> objRuntimeExceptionDao = (RuntimeExceptionDao<Object, String>) databaseManager
					.getHelper(activity).getRuntimeExceptionDao(clazz);
			QueryBuilder<Object, String> queryBuilder = objRuntimeExceptionDao
					.queryBuilder();
			Where where = queryBuilder.where();
			where.eq(column, id);
			//System.out.println("Query: " + queryBuilder.prepareStatementString().toString());
			response = queryBuilder.query();
			if(response != null && response.size() > 0){
				return response.get(0);
			}else{
				response = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}



	public static Object getSelectedColumnList(
			Class<?> clazz,
			Activity activity,
			String column,
			String id,
			DatabaseManager databaseManager) {
		Object response = null;
		try {
			RuntimeExceptionDao<Object, String> objRuntimeExceptionDao = (RuntimeExceptionDao<Object, String>) databaseManager
					.getHelper(activity).getRuntimeExceptionDao(clazz);
			QueryBuilder<Object, String> queryBuilder = objRuntimeExceptionDao
					.queryBuilder();
			Where where = queryBuilder.where();
			where.eq(column, id);
			response = queryBuilder.query();
			System.out.println("Query: " + queryBuilder.prepareStatementString().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	public static Object getSelectedColumnList(
			Class<?> clazz,Activity activity,
			String column,boolean id,
			DatabaseManager databaseManager) {
		Object response = null;
		try {
			RuntimeExceptionDao<Object, String> objRuntimeExceptionDao = (RuntimeExceptionDao<Object, String>) databaseManager
					.getHelper(activity).getRuntimeExceptionDao(clazz);
			QueryBuilder<Object, String> queryBuilder = objRuntimeExceptionDao
					.queryBuilder();
			Where where = queryBuilder.where();
			where.eq(column, id);
			System.out.println("Query: " + queryBuilder.prepareStatementString().toString());
			response = queryBuilder.query();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}


	public static Object getSelectedColumnListWithAnd(
			Class<?> clazz,
			Activity activity,
			String column,
			String id,
			String column1,
			String id1,
			DatabaseManager databaseManager
	) {
		Object response = null;
		try {
			RuntimeExceptionDao<Object, String> objRuntimeExceptionDao = (RuntimeExceptionDao<Object, String>) databaseManager
					.getHelper(activity).getRuntimeExceptionDao(clazz);
			QueryBuilder<Object, String> queryBuilder = objRuntimeExceptionDao
					.queryBuilder();
			Where where = queryBuilder.where();
			where.and(where.eq(column, id), where.eq(column1, id1));
			System.out.println("Query: " + queryBuilder.prepareStatementString().toString());
			response = queryBuilder.query();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}


	
	
//	public static void innerJOINGROUPS(Activity activity, DatabaseManager databaseManager){
//		String SQLquery = "SELECT `Group`.* FROM `Group`INNER JOIN `Image` ON `Group`.`id` = `Image`.`image_id`";
//		Cursor cursor = databaseManager.getHelper(activity).getWritableDatabase().rawQuery(SQLquery, null);
//		System.out.println("Val: " + cursor.getCount());
//		if(cursor.moveToFirst()){
//			System.out.println("Val: " + cursor.getColumnIndex("title"));
//		}
//	}

}
