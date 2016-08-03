package com.study.momo.safeapplicationtest.engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AddressDao {
	private static final String tag = "AddressDao";
	//1、指定访问数据库的路径
	public static String path = "data/data/com.study.momo.safeapplicationtest/files/address.db";
	public static String address = "未知号码";


	/**
	 * 传递一个电话号码，开启数据库连接进行访问，返回一个归属地
	 *
	 * @param phone 查询电话号码
	 * @return
	 */
	public static void getAddress(String phone) {
		phone = phone.substring(0,7);
		//2、开启数据库连接，进行访问
		SQLiteDatabase db =
				SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);

		//3、数据库查询
		Cursor cursor =
				db.query("data1", new String[]{"outkey"}, "id = ?", new String[]{phone}, null, null, null);
		//4、查询
		if (cursor.moveToNext()) {
			String outkey = cursor.getString(0);
			Log.i("tag", "outkey = " + outkey);

			//5、通过data1查询到的结果作为外键查询data2
			Cursor indexCursor = db.query(
					"data2", new String[]{"location"}, "id = ?", new String[]{outkey}, null, null, null);
			if (indexCursor.moveToNext()) {
				String address = indexCursor.getString(0);
				Log.i(tag, "location = " + address);

			}
		}
	}
}
