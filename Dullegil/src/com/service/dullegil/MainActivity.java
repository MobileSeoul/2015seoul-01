package com.service.dullegil;

import java.util.ArrayList;
import java.util.Map;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.service.dullegil.R;
import com.service.dullegil.geofencing.Constants;
import com.service.dullegil.geofencing.GeofenceTransitionsIntentService;
import com.service.dullegil.model.Stamp;
import com.service.dullegil.navigation.GpsInfo;
import com.service.dullegil.navigation.NotificationCall;
import com.service.dullegil.view.NfcActivity;
import com.service.dullegil.view.SplashActivity;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnClickListener,
		ConnectionCallbacks, OnConnectionFailedListener, ResultCallback<Status> {

	private static final String TAG = "MainActivity";
	private static final int MODE_CORSE = 0;
	private static final int MODE_STAMPBOOK = 1;
	private static final int MODE_THIRD = 2;
	
	DrawerLayout drawerLayout;
	RelativeLayout drawerPane;
	
	ActionBarDrawerToggle actionBarDrawerToggle;

	/**
	 * Related with ViewPager
	 */

	Button btn[] = new Button[2];
	ViewPager viewPager = null;
	Thread thread = null;
	Handler handler = null;

	/**
	 * Related with NFC Tag
	 */

	private NfcAdapter mAdapter;
	private PendingIntent pIntent;
	private IntentFilter[] mFilters;
	private String[][] mTechLists;

	private int stamp_id;

	/**
	 * Related with GPS provided by Google Play services.
	 */
	private boolean stamp_navi_check = false;
	private boolean navi_alarm_check = false;
	protected GoogleApiClient mGoogleApiClient;
	protected ArrayList<Geofence> mGeofenceList;
	private boolean mGeofencesAdded;
	private PendingIntent mGeofencePendingIntent;
	private SharedPreferences mSharedPreferences;
	private LocationManager locationM;

	/**
	 * Related with Navigation
	 */
	private GpsInfo gps;
	private double latitude, longitude;
	private int test = 0;
	private boolean stopped = true;
	
	Switch switchStamp, switchNavi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slide_main);
		startActivity(new Intent(this, SplashActivity.class));
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerPane = (RelativeLayout) findViewById(R.id.drawer_pane);
		
		drawerLayout.closeDrawer(drawerPane);
		
		actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, 
				R.string.drawer_opened, R.string.drawer_closed)
		{
			@Override
			public void onDrawerOpened(View drawerView) {
				// TODO Auto-generated method stub
				invalidateOptionsMenu();
				super.onDrawerOpened(drawerView);
			}
			
			@Override
			public void onDrawerClosed(View drawerView) {
				// TODO Auto-generated method stub
				invalidateOptionsMenu();
				super.onDrawerClosed(drawerView);
			}
		};
		
		drawerLayout.setDrawerListener(actionBarDrawerToggle);
			
		
		// viewPager
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		MyViewPagerAdapter adapter = new MyViewPagerAdapter(
				getSupportFragmentManager());

		viewPager.setAdapter(adapter);
		viewPager.setOffscreenPageLimit(2);
	
		btn[0] = (Button) findViewById(R.id.btn_a);
		btn[1] = (Button) findViewById(R.id.btn_b);

		for (int i = 0; i < btn.length; i++) {
			btn[i].setOnClickListener(this);
		}

		/** NFC 관련 */

		mAdapter = NfcAdapter.getDefaultAdapter(this);
		if (mAdapter == null) {
			System.out.println("지원하지 않는 기기입니다.");
			finish();
		}
		
		if(!mAdapter.isEnabled()){
			AlertDialog.Builder ad = new AlertDialog.Builder(this);
			ad.setTitle("NFC 알림");
			ad.setMessage("스탬프북 이용에 필요한 NFC 기능을 사용하시겠습니까?");
			ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					startActivity(new Intent(android.provider.Settings.ACTION_NFC_SETTINGS));
					
				}
			}).setNegativeButton("NO", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					
				}
			});
			ad.create();
			ad.show();			
		}

		mTechLists = new String[][] { new String[] { NfcA.class.getName() } };
		getNfcData(getIntent());

		String deviceId = getDeviceId(this);

		// Empty list for storing geofences.
		// geofence의 위치를 저장하는 리스트 초기화
		mGeofenceList = new ArrayList<Geofence>();
		mGeofencePendingIntent = null;
		mSharedPreferences = getSharedPreferences(
				Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
		mGeofencesAdded = mSharedPreferences.getBoolean(
				Constants.GEOFENCES_ADDED_KEY, false);
		populateGeofenceList();

		buildGoogleApiClient();

		// GPS수신 설정
		locationM = (LocationManager) getSystemService(LOCATION_SERVICE);

		gps = new GpsInfo(MainActivity.this);
		
		switchStamp = (Switch) findViewById(R.id.switchStamp);
		switchNavi = (Switch) findViewById(R.id.switchNav);
		
		switchStamp.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton cb, boolean isChecking) {
				if(isChecking){
					addGeofencesButtonHandler();
				}else{
					removeGeofencesButtonHandler();
				}
				
			}
			
		});
		switchNavi.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener(){

			Location location = gps.getLocation();

			@Override
			public void onCheckedChanged(CompoundButton cb, boolean isChecking) {
				if(isChecking){
					Toast.makeText(MainActivity.this, "네비게이션 기능이 활성화 되었습니다",
							Toast.LENGTH_SHORT).show();
					if (!locationM.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
						createGpsDisableAlert();
					} else if (locationM
							.isProviderEnabled(LocationManager.GPS_PROVIDER)
							&& gps.isGetLocation()) {
						stopped = true;
						if (String.valueOf(location) != null) {
							new MyNaviThread().start();
						} else {
							Toast.makeText(MainActivity.this,
									"GPS 수신 상태가 좋지 않습니다.", Toast.LENGTH_SHORT)
									.show();
						}
					}
				}else{
					Toast.makeText(MainActivity.this, "네비게이션 기능이 비활성화 되었습니다",
							Toast.LENGTH_SHORT).show();
					stopped = false;
				}
				
			}
			
		});

	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);
		actionBarDrawerToggle.syncState();
	}
	
	private String getDeviceId(MainActivity mainActivity) {
		TelephonyManager teleMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		Log.i(TAG, "deviceID: " + teleMgr.getDeviceId());
		return teleMgr.getDeviceId();
	}

	private void getNfcData(Intent intent) {

		if (intent != null) {
			String action = intent.getAction();

			if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)
					|| NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
					|| NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {

				Parcelable[] rawMsgs = intent
						.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

				NdefMessage[] msgs;
				if (rawMsgs != null) {
					msgs = new NdefMessage[rawMsgs.length];
					for (int i = 0; i < rawMsgs.length; i++) {
						msgs[i] = (NdefMessage) rawMsgs[i];
					}

				} else {
					byte[] empty = new byte[] {};
					NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN,
							empty, empty, empty);
					NdefMessage msg = new NdefMessage(
							new NdefRecord[] { record });
					msgs = new NdefMessage[] { msg };
				}

				doAction(msgs);
			}
		}

	}

	private void doAction(NdefMessage[] msgs) {

		String payload = new String(msgs[0].getRecords()[0].getPayload());

		// 선호도 조사 알림 기능 필요

		if (payload.startsWith("stamp")) {
			
			viewPager.setCurrentItem(MODE_STAMPBOOK);

			stamp_id = Integer.valueOf(payload.split(":")[1]);

			if (stamp_id != 0) {

				Intent iNfc = new Intent(this, NfcActivity.class);
				iNfc.putExtra("stamp_id", stamp_id);
				startActivity(iNfc);
			}

		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_a:
			viewPager.setCurrentItem(MODE_CORSE);
			break;
		case R.id.btn_b:
			viewPager.setCurrentItem(MODE_STAMPBOOK);
			break;
		case R.id.btn_c:
			viewPager.setCurrentItem(MODE_THIRD);
			break;
		default:
			break;
		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.i(TAG, "onNewIntent");
		Log.e("MainActivity", "stampList: " + intent.getIntegerArrayListExtra("stampList"));
		if(intent.getStringExtra("stamp_result")!=null){
			setContentView(MODE_STAMPBOOK);
		}

		setIntent(intent);
		getNfcData(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();

		Intent i = new Intent(this, MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		pIntent = PendingIntent.getActivity(this, 0, i, 0);

		IntentFilter filter = new IntentFilter(
				NfcAdapter.ACTION_NDEF_DISCOVERED);

		try {
			filter.addDataType("*/*");
		} catch (IntentFilter.MalformedMimeTypeException e) {
			e.printStackTrace();
		}

		mFilters = new IntentFilter[] { filter };
		mAdapter.enableForegroundDispatch(this, pIntent, mFilters, mTechLists);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mAdapter.disableForegroundDispatch(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

	//	getMenuInflater().inflate(R.menu.menu, menu);

		return true;
		// return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if(actionBarDrawerToggle.onOptionsItemSelected(item)){
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
	 * LocationServices API.
	 */
	protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();
	}

	/**
	 * Runs when a GoogleApiClient object successfully connects.
	 */
	@Override
	public void onConnected(Bundle connectionHint) {
		Log.i(TAG, "Connected to GoogleApiClient");
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
				+ result.getErrorCode());
	}

	@Override
	public void onConnectionSuspended(int cause) {
		Log.i(TAG, "Connection suspended");

	}

	/**
	 * Builds and returns a GeofencingRequest. Specifies the list of geofences
	 * to be monitored. Also specifies how the geofence notifications are
	 * initially triggered.
	 */
	private GeofencingRequest getGeofencingRequest() {
		GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

		builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
		builder.addGeofences(mGeofenceList);

		return builder.build();
	}

	/**
	 * Adds geofences, which sets alerts to be notified when the device enters
	 * or exits one of the specified geofences. Handles the success or failure
	 * results returned by addGeofences().
	 */
	public void addGeofencesButtonHandler() {

		if (!locationM.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			createGpsDisableAlert();
		} else {

			if (!mGoogleApiClient.isConnected()) {
				Toast.makeText(this, getString(R.string.not_connected),
						Toast.LENGTH_SHORT).show();
				switchStamp.setChecked(false);
				return;
			}

			try {
				LocationServices.GeofencingApi.addGeofences(mGoogleApiClient,
						getGeofencingRequest(), getGeofencePendingIntent())
						.setResultCallback(this); // Result
													// processed
													// in
													// onResult().
				Toast.makeText(this, "스탬프 위치 알람이 활성화되었습니다.", Toast.LENGTH_SHORT)
						.show();

			} catch (SecurityException securityException) {
				logSecurityException(securityException);
			}
		}
	}

	private void createGpsDisableAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("GPS를 켜시겠습니까?")
				.setCancelable(false)
				.setPositiveButton("GPS on",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int id) {
								showGpsOption();
							}
						})
				.setNegativeButton("GPS off",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void showGpsOption() {
		Intent gpsI = new Intent(
				android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(gpsI);
	}

	/**
	 * Removes geofences, which stops further notifications when the device
	 * enters or exits previously registered geofences.
	 */
	public void removeGeofencesButtonHandler() {
		if (!mGoogleApiClient.isConnected()) {
			Toast.makeText(this, getString(R.string.not_connected),
					Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			// Remove geofences.
			LocationServices.GeofencingApi.removeGeofences(mGoogleApiClient,
					getGeofencePendingIntent()).setResultCallback(this); // Result
																			// processed
																			// in
																			// onResult().
			// stopped = false;
			Toast.makeText(this, "스탬프 위치 알람이 비활성화되었습니다.", Toast.LENGTH_SHORT)
					.show();
		} catch (SecurityException securityException) {
			logSecurityException(securityException);
		}
	}

	private void logSecurityException(SecurityException securityException) {
		Log.e(TAG, "Invalid location permission. "
				+ "You need to use ACCESS_FINE_LOCATION with geofences",
				securityException);
	}

	/**
	 * Gets a PendingIntent to send with the request to add or remove Geofences.
	 * Location Services issues the Intent inside this PendingIntent whenever a
	 * geofence transition occurs for the current list of geofences.
	 *
	 * @return A PendingIntent for the IntentService that handles geofence
	 *         transitions.
	 */
	private PendingIntent getGeofencePendingIntent() {
		if (mGeofencePendingIntent != null) {
			return mGeofencePendingIntent;
		}

		Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
		return PendingIntent.getService(this, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

	}

	/**
	 * This sample hard codes geofence data. A real app might dynamically create
	 * geofences based on the user's location.
	 */
	public void populateGeofenceList() {
		for (Map.Entry<String, LatLng> entry : Constants.BAY_AREA_LANDMARKS
				.entrySet()) {

			mGeofenceList.add(new Geofence.Builder()
					.setRequestId(entry.getKey())

					.setCircularRegion(entry.getValue().latitude,
							entry.getValue().longitude,
							Constants.GEOFENCE_RADIUS_IN_METERS)

					.setExpirationDuration(Geofence.NEVER_EXPIRE)
					.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
					.build());
		}
	}

	class MyNaviThread extends Thread {
		@Override
		public void run() {
			// Gps 사용유무 가져오기
			while (stopped) {
				Location location = gps.getLocation();
				if (gps.isGetLocation()) {
					latitude = location.getLatitude();
					longitude = location.getLongitude();
					// latitude = 37.546907;
					// longitude = 126.876614;
					Log.i("위도", String.valueOf(latitude));
					Log.i("경도", String.valueOf(longitude));
					//
					// Log.i("결과값", String.valueOf(gps.compareLoad()));

					if (gps.compareLoad() == true) {
						test = 1;
					} else if (gps.compareLoad() == false) {
						test = 2;
					}
					Message myMes = myHandler.obtainMessage(test);
					myHandler.sendMessage(myMes);
				} else {
					gps.showSettingAlert();
				}
				try {
					Thread.sleep(5000);// 테스트 5초
					// Thread.sleep(3000);//테스트 3초
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				// Toast.makeText(MainActivity.this,"위도 : " +
				// String.valueOf(latitude) + "\n경도 : " +
				// String.valueOf(longitude), Toast.LENGTH_SHORT).show();
				break;
			case 2:
				// Toast.makeText(MainActivity.this,
				// "위도 : " + String.valueOf(latitude) + "\n경도 : " +
				// String.valueOf(longitude), Toast.LENGTH_SHORT).show();
				Intent service = new Intent(getApplicationContext(),
						NotificationCall.class);
				startService(service);
				stopService(service);
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void onResult(Status arg0) {
		// TODO Auto-generated method stub

	}
}
