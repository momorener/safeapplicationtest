package com.study.momo.safeapplicationtest.receiver;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;

public class LocationService extends Service {
	protected static final String tag = "LocationService";
	private MyLocationListener mLocationListener;
	@Override
	public void onCreate() {
		super.onCreate();
		//获取手机的经纬度坐标
		//1、获取位置管理者对象
		LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		//以最优的方式获取经纬度坐标
		Criteria criteria = new Criteria();
		//允许花费流量
		criteria.setCostAllowed(true);
		criteria.setAccuracy(Criteria.ACCURACY_FINE);//指定获取经纬度的精确度
		String bestProvider  = lm.getBestProvider(criteria, true);
		mLocationListener = new MyLocationListener();
		//在一定时间间隔，移动一定距离后获取经纬度坐标
		lm.requestLocationUpdates(bestProvider, 0, 0,mLocationListener);
	}
	class MyLocationListener implements LocationListener{
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			
		}
		
		@Override
		public void onProviderDisabled(String provider) {

		}
		
		@Override
		public void onLocationChanged(Location location) {
			double longitude = location.getLongitude();
			double latitude = location.getLatitude();
			//获取经纬度
			SmsManager sms = SmsManager.getDefault();
			sms.sendTextMessage("5556", null, "longitude = "+longitude+","+"latitude = "+latitude, null, null);
		}
	}
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onDestroy() {
		if(mLocationListener!=null){
			mLocationListener = null;
		}
		super.onDestroy();
	}
}
