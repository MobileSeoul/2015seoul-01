package net.daum.android.map.openapi.search;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Searcher {
    // http://dna.daum.net/apis/local
	public static final String DAUM_MAPS_LOCAL_KEYWORD_SEARCH_API_FORMAT = "https://apis.daum.net/local/v1/search/keyword.json?query=%s&location=%f,%f&radius=%d&page=%d&apikey=%s";
	public static final String DAUM_MAPS_LOCAL_CATEGORY_SEARCH_API_FORMAT = "https://apis.daum.net/local/v1/search/category.json?code=%s&location=%f,%f&radius=%d&page=%d&apikey=%s";

	
	private static final String HEADER_NAME_X_APPID = "x-appid";
	private static final String HEADER_NAME_X_PLATFORM = "x-platform";
	private static final String HEADER_VALUE_X_PLATFORM_ANDROID = "android";
	
	OnFinishSearchListener onFinishSearchListener;
	SearchTask searchTask;
	String appId;
	
	private class SearchTask extends AsyncTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... urls) {
			String url = urls[0];
			Map<String, String> header = new HashMap<String, String>();
			header.put(HEADER_NAME_X_APPID, appId);
			header.put(HEADER_NAME_X_PLATFORM, HEADER_VALUE_X_PLATFORM_ANDROID);
			String json = fetchData(url, header);
			List<Item> itemList = parse(json);
			if (onFinishSearchListener != null) {
				if (itemList == null) {
					onFinishSearchListener.onFail();
				} else {
					onFinishSearchListener.onSuccess(itemList);
				}
			}
			return null;
		}
	}

	public void searchKeyword(Context applicationContext, String query, double latitude, double longitude, int radius, int page, String apikey, OnFinishSearchListener onFinishSearchListener) {
    	this.onFinishSearchListener = onFinishSearchListener;
    	
		if (searchTask != null) {
			searchTask.cancel(true);
			searchTask = null;
		}
		
		if (applicationContext != null) {
			appId = applicationContext.getPackageName();
		}
		String url = buildKeywordSearchApiUrlString(query, latitude, longitude, radius, page, apikey);
		searchTask = new SearchTask();
		searchTask.execute(url);
    }
	
	public void searchCategory(Context applicationContext, String categoryCode, double latitude, double longitude, int radius, int page, String apikey, OnFinishSearchListener onFinishSearchListener) {
		this.onFinishSearchListener = onFinishSearchListener;
		
		if (searchTask != null) {
			searchTask.cancel(true);
			searchTask = null;
		}
		
		if (applicationContext != null) {
			appId = applicationContext.getPackageName();
		}
		String url = buildCategorySearchApiUrlString(categoryCode, latitude, longitude, radius, page, apikey);
		searchTask = new SearchTask();
		searchTask.execute(url);
	}
    
	private String buildKeywordSearchApiUrlString(String query, double latitude, double longitude, int radius, int page, String apikey) {
    	String encodedQuery = "";
		try {
			encodedQuery = URLEncoder.encode(query, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	return String.format(DAUM_MAPS_LOCAL_KEYWORD_SEARCH_API_FORMAT, encodedQuery, latitude, longitude, radius, page, apikey);
    }
	
	private String buildCategorySearchApiUrlString(String categoryCode, double latitude, double longitude, int radius, int page, String apikey) {
		return String.format(DAUM_MAPS_LOCAL_CATEGORY_SEARCH_API_FORMAT, categoryCode, latitude, longitude, radius, page, apikey);
	}
	
	private String fetchData(String urlString, Map<String, String> header) {
		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(4000 /* milliseconds */);
			conn.setConnectTimeout(7000 /* milliseconds */);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			if (header != null) {
				for (String key : header.keySet()) {
					conn.addRequestProperty(key, header.get(key));
				}
			}
			conn.connect();
			InputStream is = conn.getInputStream();
			@SuppressWarnings("resource")
			Scanner s = new Scanner(is);
			s.useDelimiter("\\A");
			String data = s.hasNext() ? s.next() : "";
			is.close();
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
    
	private List<Item> parse(String jsonString) {
		List<Item> itemList = new ArrayList<Item>();
		try {
			JSONObject reader = new JSONObject(jsonString);
			JSONObject channel = reader.getJSONObject("channel");
			JSONArray objects = channel.getJSONArray("item");
			for (int i = 0; i < objects.length(); i++) {
				JSONObject object = objects.getJSONObject(i);
				Item item = new Item();
				item.title = object.getString("title");
				item.imageUrl = object.getString("imageUrl");
				item.address = object.getString("address");
				item.newAddress = object.getString("newAddress");
				item.zipcode = object.getString("zipcode");
				item.phone = object.getString("phone");
				item.category = object.getString("category");
				item.latitude = object.getDouble("latitude");
				item.longitude = object.getDouble("longitude");
				item.distance = object.getDouble("distance");
				item.direction = object.getString("direction");
				item.id = object.getString("id");
				item.placeUrl = object.getString("placeUrl");
				item.addressBCode = object.getString("addressBCode");
				itemList.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return itemList;
	}
	
	public void cancel() {
		if (searchTask != null) {
			searchTask.cancel(true);
			searchTask = null;
		}
	}
}
