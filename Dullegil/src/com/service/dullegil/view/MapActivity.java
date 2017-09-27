package com.service.dullegil.view;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.service.dullegil.R;
import com.service.dullegil.model.Stamp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import net.daum.android.map.openapi.search.Item;
import net.daum.android.map.openapi.search.OnFinishSearchListener;
import net.daum.android.map.openapi.search.Searcher;
import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPoint.GeoCoordinate;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;
import net.daum.mf.map.api.MapView.CurrentLocationEventListener;
import floatingactionbutton.FloatingActionButton;

public class MapActivity extends Activity implements OnClickListener, CurrentLocationEventListener {

	private static final String DAUM_API_KEY = "974dbdbea9790d34f615462ab5a253ee";
	private static final String COORDINATE_FILE_NAME = "sample.json";
	private static boolean BUTTON_FLAG = true; 
	private static boolean COV_FLAG = false; 
	private static boolean STAMP_FLAG = false;
	private static boolean MY_LOCATION_FLAG = false;
	

	private MapView mapView;
	private ViewGroup mapViewContainer;

	private ArrayList<MapPolyline> polylineList = new ArrayList<MapPolyline>();
	private HashMap<Integer, Item> mTagItemMap = new HashMap<Integer, Item>();

	private JSONParser jsonParser;
	private File coordiFile;
	private int mapLoadResult;
	private FloatingActionButton rootButton;
	private FloatingActionButton myLocationButton;
	private FloatingActionButton convenientButton;
	private FloatingActionButton stampButton;
	private Stamp stamp;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		
		Intent intent = getIntent();
		mapLoadResult = intent.getIntExtra("course", FragmentCourse.MAP_COURSE_WHOLE);
		
		int detailStampShow = intent.getIntExtra("showStamp", 0);
		if(detailStampShow != 0){
			mapLoadResult = detailStampShow;
		}

		displayMap();
		checkMarker();

		coordiFile = getFileStreamPath(COORDINATE_FILE_NAME);

		if (coordiFile.isFile() == false) {
			String strBuf = readJsonFile(COORDINATE_FILE_NAME);
			addPolyline(strBuf);
		}
		
		rootButton = (FloatingActionButton) findViewById(R.id.rootButton);
		myLocationButton = (FloatingActionButton) findViewById(R.id.myLocationButton);
		convenientButton = (FloatingActionButton) findViewById(R.id.convenientButton);
		stampButton = (FloatingActionButton) findViewById(R.id.stampButton);
		
		rootButton.setOnClickListener(this);
		myLocationButton.setOnClickListener(this);
		convenientButton.setOnClickListener(this);
		stampButton.setOnClickListener(this);
		
		convenientButton.setImageResource(R.drawable.toilet);
		stampButton.setImageResource(R.drawable.stamp);
		myLocationButton.setImageResource(R.drawable.location);
		
		
		mapView.setCurrentLocationEventListener(this);

		if(detailStampShow != 0){
			searchStamp();
		}

	}

	private void drawPolyline(int result, int lineNumber) {

		MapPolyline polyline;
		
		if (result == 1) {
			polyline = polylineList.get(0);
		} else {
			polyline = polylineList.get(lineNumber - 1);
		}
		switch (lineNumber) {
		case 1:
			polyline.setLineColor(Color.argb(255, 255, 127, 36));
			

			break;
		case 2:
			polyline.setLineColor(Color.argb(255, 240, 128, 128));

			break;
		case 3:
			polyline.setLineColor(Color.argb(255, 30, 180, 255));

			break;
		case 4:
			polyline.setLineColor(Color.argb(255, 60, 240, 100));

			break;
		case 5:
			polyline.setLineColor(Color.argb(255, 192, 62, 255));

			break;
		case 6:
			polyline.setLineColor(Color.argb(255, 238, 48, 48));

			break;
		case 7:
			polyline.setLineColor(Color.argb(255, 139, 101, 8));

			break;
		case 8:
			polyline.setLineColor(Color.argb(255, 0, 0, 255));

			break;

		default:
			break;
		}
		polyline.setTag(1000);
		mapView.addPolyline(polyline);
		// MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
		// int padding = 100; // px
		// mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));

	}

	private void addPolyline(String strBuf) {

		jsonParser = new JSONParser();

		try {
			Object obj = jsonParser.parse(strBuf);
			JSONObject jsonObject = (JSONObject) obj;

			if (mapLoadResult == FragmentCourse.MAP_COURSE_WHOLE) {

				for (int i = 1; i <= jsonObject.size(); i++) {
					MapPolyline polyline = new MapPolyline();
					JSONArray coordinateArray = (JSONArray) jsonObject.get(i + "C");
					// Log.e("result", i+"C");

					for (int j = 0; j < coordinateArray.size(); j++) {
						JSONObject coordinate = (JSONObject) coordinateArray.get(j);

						double lat = (Double) coordinate.get("latitude");
						double lng = (Double) coordinate.get("logitute");
						polyline.addPoint(MapPoint.mapPointWithGeoCoord(lat, lng));
					}

					polylineList.add(polyline);
					drawPolyline(FragmentCourse.MAP_COURSE_WHOLE, i);
					
					mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.57123110634069, 127.01453452531665), true);
					mapView.setZoomLevel(7, true);

				}

			} else {

				MapPolyline polyline = new MapPolyline();

				JSONArray coordinateArray = (JSONArray) jsonObject.get(mapLoadResult + "C");

				for (int j = 0; j < coordinateArray.size(); j++) {
					JSONObject coordinate = (JSONObject) coordinateArray.get(j);

					double lat = (Double) coordinate.get("latitude");
					double lng = (Double) coordinate.get("logitute");
					polyline.addPoint(MapPoint.mapPointWithGeoCoord(lat, lng));
				}

				polylineList.add(polyline);
				drawPolyline(1, mapLoadResult);
				
				MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
				int padding = 100; // px
				mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));

			}

			// if(polylineList.size()==1){
			// drawPolyline(mapLoadResult);
			// }else{
			// for(int i = 1; i<=jsonObject.size();i++){
			// }
			// }

		} catch (ParseException e) {

			e.printStackTrace();
		}

	}

	private String readJsonFile(String strFileName) {

		String text = null;
		try {

			InputStream is = getAssets().open(strFileName);
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();

			text = new String(buffer);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return text;
	}

	private void checkMarker() {
		
		MapPOIItem marker = new MapPOIItem();
		marker.setItemName("Default Marker");
		marker.setTag(0);
		marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
		marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
		mapView.addPOIItem(marker);

	}

	private void displayMap() {
		mapView = new MapView(this);
		mapView.setDaumMapApiKey(DAUM_API_KEY);
		mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
		mapViewContainer.addView(mapView);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rootButton:
			
			if(BUTTON_FLAG){
				
//				myLocationButton.setShadow(false);
//				convenientButton.setShadow(false);
//				stampButton.setShadow(false);
				myLocationButton.hide(true,1);
				convenientButton.hide(true,2);
				stampButton.hide(true,3);
				
				BUTTON_FLAG = false;
				
			}else{
				myLocationButton.show(true,1);
				convenientButton.show(true,2);
				stampButton.show(true,3);
				
//				myLocationButton.setShadow(true);
//				convenientButton.setShadow(true);
//				stampButton.setShadow(true);
				
				BUTTON_FLAG = true;
				
			}
			
			
			break;
		case R.id.myLocationButton:
			
			if (MY_LOCATION_FLAG) {
				mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
				MY_LOCATION_FLAG=false;
			}else{
				mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
				MY_LOCATION_FLAG=true;
			}
			
			break;
		case R.id.convenientButton:
			if(COV_FLAG){
				mapView.removeAllPOIItems();
				COV_FLAG=false;
			}else{
				searchConvenient();
				COV_FLAG=true;
			}
			
			break;
		case R.id.stampButton:
			
			if(STAMP_FLAG){
				mapView.removeAllPOIItems();
				STAMP_FLAG=false;
				
			}else{
				searchStamp();
				STAMP_FLAG=true;
			}
			
			
			break;

		default:
			break;
		}
		
	}

	private void searchStamp() {
		
		ArrayList<Stamp> stampList = new ArrayList<Stamp>();
		stamp = new Stamp("����ö 1,7ȣ�� �����꿪 2�� �ⱸ",37.68933518,127.0469804);
		stampList.add(stamp);
		stamp = new Stamp("ȭ���� 4�� �ⱸ �� ����",37.62049005,127.0851012);
		stampList.add(stamp);
		stamp = new Stamp("�븶�� ����� ����",37.5880727621941,127.106859604227);
		stampList.add(stamp);
		stamp = new Stamp("������ �����繫�� ��",37.5525188942512,127.099832);
		stampList.add(stamp);
		stamp = new Stamp("������ ����",37.546075683335,127.108700700501);
		stampList.add(stamp);
		stamp = new Stamp("���ڻ� ����",37.5552473745104,127.157137908518);
		stampList.add(stamp);
		stamp = new Stamp("���̵� ���°���������� �繫�� ��",37.5123898673045,127.140395656605);
		stampList.add(stamp);
		stamp = new Stamp("źõ ���κ�",37.4882960340308,127.106861480807);
		stampList.add(stamp);
		stamp = new Stamp("���� ����",37.4867370657176,127.102019882302);
		stampList.add(stamp);
		stamp = new Stamp("����ù��ǽ� �ȳ��� ��",37.46928891,127.0372054);
		stampList.add(stamp);
		stamp = new Stamp("���� ���κ�",37.4735625270202,126.986411416951);
		stampList.add(stamp);
		stamp = new Stamp("���ǻ� ���κ�",37.4339651421683,126.906562769735);
		stampList.add(stamp);
		stamp = new Stamp("���Ͽ� ��",37.4958961376295,126.870711739964);
		stampList.add(stamp);
		stamp = new Stamp("Ȳ�ݳ� �ٸ����� ȭ��� ��",37.56177956,126.8646502);
		stampList.add(stamp);
		stamp = new Stamp("����ü������ ȭ��� ��",37.58492316,126.9028364);
		stampList.add(stamp);
		stamp = new Stamp("������ ��",37.62789871,126.9368843);
		stampList.add(stamp);
		stamp = new Stamp("������ ������",37.61174766,126.9395765);
		stampList.add(stamp);
		stamp = new Stamp("���� ������",37.61291506,126.9822408);
		stampList.add(stamp);
		stamp = new Stamp("�򱸸��� ������",37.62248222,127.0099993);
		stampList.add(stamp);
		stamp = new Stamp("�սǹ��� ������",37.66167656,127.0165242);
		stampList.add(stamp);
		
		showStamp(stampList);
		
	}

	private void showStamp(ArrayList<Stamp> stampList ) {
		for (int i = 0; i < stampList.size(); i++) {
			Stamp stamp = stampList.get(i);

			MapPOIItem poiItem = new MapPOIItem();
			poiItem.setItemName(stamp.getName());
			poiItem.setTag(i);
			MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(stamp.getLat(), stamp.getLon());
			poiItem.setMapPoint(mapPoint);
			poiItem.setMarkerType(MapPOIItem.MarkerType.CustomImage);
			poiItem.setCustomImageResourceId(R.drawable.map_pin_blue);
			poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
			poiItem.setCustomSelectedImageResourceId(R.drawable.map_pin_red);
			poiItem.setCustomImageAutoscale(false);
			poiItem.setCustomImageAnchor(0.5f, 1.0f);
			
			mapView.addPOIItem(poiItem);
		}
		
	}

	private void searchConvenient() {
		GeoCoordinate geoCoordinate = mapView.getMapCenterPoint().getMapPointGeoCoord();
		double latitude = geoCoordinate.latitude; // ����
		double longitude = geoCoordinate.longitude; // �浵
		int radius = 10000; // �߽� ��ǥ������ �ݰ�Ÿ�. Ư�� ������ �߽����� �˻��Ϸ��� �� ��� ���. meter ���� (0 ~ 10000)
		int page = 1; // ������ ��ȣ (1 ~ 3). ���������� 15��
		
		String query =  "ȭ���";
		
		Searcher searcher = new Searcher(); // net.daum.android.map.openapi.search.Searcher
		searcher.searchKeyword(getApplicationContext(), query, latitude, longitude, radius, page, DAUM_API_KEY, new OnFinishSearchListener() {
			@Override
			public void onSuccess(List<Item> itemList) {
				mapView.removeAllPOIItems(); // ���� �˻� ��� ����
				showResult(itemList); // �˻� ��� ������ 
			}
			

			@Override
			public void onFail() {
				System.out.println("����!!!!");
			}
		});
		
	}
	
	private void showResult(List<Item> itemList) {
MapPointBounds mapPointBounds = new MapPointBounds();
		
		for (int i = 0; i < itemList.size(); i++) {
			Item item = itemList.get(i);

			MapPOIItem poiItem = new MapPOIItem();
			poiItem.setItemName(item.title);
			poiItem.setTag(i);
			MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(item.latitude, item.longitude);
			poiItem.setMapPoint(mapPoint);
			mapPointBounds.add(mapPoint);
			poiItem.setMarkerType(MapPOIItem.MarkerType.CustomImage);
			poiItem.setCustomImageResourceId(R.drawable.map_pin_blue);
			poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
			poiItem.setCustomSelectedImageResourceId(R.drawable.map_pin_red);
			poiItem.setCustomImageAutoscale(false);
			poiItem.setCustomImageAnchor(0.5f, 1.0f);
			
			mapView.addPOIItem(poiItem);
			mTagItemMap.put(poiItem.getTag(), item);
		}
		
		mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds));
		
		MapPOIItem[] poiItems = mapView.getPOIItems();
		if (poiItems.length > 0) {
			mapView.selectPOIItem(poiItems[0], false);
		}
		
		
	}

	@Override
	public void onCurrentLocationDeviceHeadingUpdate(MapView arg0, float arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float accuracyInMeters) {
		MapPoint.GeoCoordinate mapPointGeo = currentLocation.getMapPointGeoCoord();
		mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(mapPointGeo.latitude, mapPointGeo.longitude), 3, true);

	}

	@Override
	public void onCurrentLocationUpdateCancelled(MapView arg0) {
		
	}

	@Override
	public void onCurrentLocationUpdateFailed(MapView arg0) {
		
	}

}
