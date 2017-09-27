package com.service.dullegil.navigation;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

public class GpsInfo extends Service implements LocationListener {
	protected LocationManager locationM;
	private final Context mContext;

	// GPS 반경을 구할때
	// private static final double area = 0.0005;
	private static final double area = 0.00038;
//	private static final double area = 0.00038;
	private double gilLa, gilLo, Lamin, Lamax, Lomin, Lomax;

	// 현재 GPS 사용 유무
	boolean isGPSEnabled = false;
	// 네트워크 사용 유무
	boolean isNetworkEnabled = false;
	// GPS 상태값
	boolean isGetlocation = false;

	Location location;
	// 위도 경도
	double lat;
	double lon;
	// 최소 GPS 정보 업데이트 거리 3미터
	// private static final long MIN_DISTANCE_CHANGE_FOR_UPDATE = 3;
	// 가만히 서 있어도 GPS 정보 업데이트(거리 0미터)
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATE = 0;
	// 최소 GPS 정보 업데이트 시간(밀리세컨이므로 3분)
	// private static final long MIN_TIME_BW_UPDATES = 1000 * 180 * 1;
	// 테스트 1초
	private static final long MIN_TIME_BW_UPDATES = 1000 * 1 * 1;

	public GpsInfo(Context context) {
		this.mContext = context;
		getLocation();
	}

	public Location getLocation() {
		locationM = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
		// GPS 정보 가져오기
		isGPSEnabled = locationM.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// 현재 네트워크 상태값 알아오기
		isNetworkEnabled = locationM.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (!isGPSEnabled && !isNetworkEnabled) {
			// 네트워크와 GPS 사용이 불가능할 때
		} else {
			this.isGetlocation = true;
			// 네트워크 정보로부터 위치값 가져오기
			if (isNetworkEnabled) {
				locationM.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
						MIN_DISTANCE_CHANGE_FOR_UPDATE, this);

				if (locationM != null) {
					location = locationM.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

					if (location != null) {
						// 위도 경도 저장
						lat = location.getLatitude();
						lon = location.getLongitude();
					}
				}

			}

			if (isGPSEnabled) {
				if (location == null) {
					locationM.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATE, this);

					if (locationM != null) {
						location = locationM.getLastKnownLocation(LocationManager.GPS_PROVIDER);
						if (location != null) {
							lat = location.getLatitude();
							lon = location.getLongitude();
						}
					}
				}
			}
		}

		return location;

	}

	// GPS 종료
	public void stopUsingGps() {
		if (locationM != null) {
			locationM.removeUpdates(GpsInfo.this);
		}
	}

	// GPS 혹은 인터넷 켜져있는지 확인
	public boolean isGetLocation() {
		return this.isGetlocation;
	}

	// Gps정보를 가져오지 못했을 때 설정값으로 갈지 물어보는 alert창
	public void showSettingAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

		alertDialog.setTitle("GPS 사용유무 셋팅");
		alertDialog.setMessage("GPS 셋팅이 되지 않았을 수도 있습니다. \n 설정창으로 가시겠습니까?");

		// OK������
		alertDialog.setPositiveButton("Setting", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				mContext.startActivity(intent);
			}
		});
		alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
	}

	@Override
	public void onLocationChanged(Location location) {
	}

	public double getLatitude() {
		if (location != null) {
			lat = location.getLatitude();
		}
		return lat;
	}

	public double getLongitude() {
		if (location != null) {
			lon = location.getLongitude();
		}
		return lon;
	}

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
	public IBinder onBind(Intent intent) {
		return null;
	}

	public int calLatitude() {
		return 0;
	}

	public int calLongitude() {
		return 0;
	}

	// Gps좌표와 내가 입력한 위치를 비교한다.
	public boolean compareLoad() {
		ArrayList<Double> gilLati = new ArrayList<Double>();
		ArrayList<Double> gilLongi = new ArrayList<Double>();
		boolean distinction = true;

		gilLati.add(37.545237);
		gilLati.add(37.545278);
		gilLati.add(37.545328);
		gilLati.add(37.545386);
		gilLati.add(37.545519);
		gilLati.add(37.545606);
		gilLati.add(37.545685);
		gilLati.add(37.545755);
		gilLati.add(37.545866);
		gilLati.add(37.545974);
		gilLati.add(37.546113);
		gilLati.add(37.546277);
		gilLati.add(37.546429);
		gilLati.add(37.546552);
		gilLati.add(37.546655);

		gilLongi.add(126.880627);
		gilLongi.add(126.880737);
		gilLongi.add(126.880884);
		gilLongi.add(126.881028);
		gilLongi.add(126.881404);
		gilLongi.add(126.881617);
		gilLongi.add(126.881785);
		gilLongi.add(126.881916);
		gilLongi.add(126.881779);
		gilLongi.add(126.881656);
		gilLongi.add(126.881488);
		gilLongi.add(126.881306);
		gilLongi.add(126.881129);
		gilLongi.add(126.88099);
		gilLongi.add(126.880837);

		if (isGetlocation) {

			for (int i = 0; i < gilLati.size(); i++) {
				gilLa = gilLati.get(i);
				Lamin = gilLa - area;
				Lamax = gilLa + area;

				gilLo = gilLongi.get(i);
				Lomin = gilLo - area;
				Lomax = gilLo + area;
				// 범위 밖에 있을 경우
				if (lat > Lamax || lat < Lamin || lon > Lomax || lon < Lomin) {
					distinction = false;
					gilLa = 0;
					gilLo = 0;
					Lamin = 0;
					Lamax = 0;
				}
				// 범위 안에 있을 경우
				else {
					distinction = true;
					gilLa = 0;
					gilLo = 0;
					Lamin = 0;
					Lamax = 0;
					return distinction;
				}
				// Log.i("결과", String.valueOf(distinction));
			}
		}

		return distinction;
	}
}
