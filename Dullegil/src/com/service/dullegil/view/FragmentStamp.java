package com.service.dullegil.view;

import java.util.ArrayList;

import com.service.dullegil.R;
import com.service.dullegil.model.Stamp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class FragmentStamp extends Fragment {

	DisplayMetrics mMetrics;
	
	String[] stampName = new String[]{"Stamp is empty."};
	int[] stampLocation = {10,11,12,13,20,21,22,23,30,31,32,33,34,40,41,42,43,
			50,51,52,53,60,61,62,63,70,71,72,73,80,81,82,83,84,85,86};

	ArrayList<Integer> stampList = new ArrayList<Integer>();
	
	View v;
	Context mCtx;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
		mCtx = activity.getApplicationContext();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		stampName = getResources().getStringArray(R.array.stamp);
		
		stampList = getActivity().getIntent().getIntegerArrayListExtra("stampList");

		v = inflater.inflate(R.layout.fragment_stamp, container, false);
		
		GridView gridView = (GridView) v.findViewById(R.id.stampGridView);
		gridView.setAdapter(new ImageAdapter(mCtx));
		

		return v;
	}
	
	public class ImageAdapter extends BaseAdapter {
		
		private Context mContext;
		
		public ImageAdapter(Context c) {
			mContext = c;
		}

		@Override
		public int getCount() {
			return stampName.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			int rowWidth = (mMetrics.widthPixels) / 2;
			int imageResId = getResources().getIdentifier(stampName[position], "drawable", mContext.getPackageName());	
			
			ImageView imageView;
			if(convertView == null){
				imageView = new ImageView(mContext);
				imageView.setLayoutParams(new GridView.LayoutParams(rowWidth, rowWidth));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setId(stampLocation[position]);
			} else {
				imageView = (ImageView) convertView;
			}
			
			if(stampList != null){
				for(int id:stampList){
					Log.e("FragmentStamp", "s.getStampId: " + id);
					if(id == stampLocation[position]){
						Log.e("FragmentStamp", "스탬프리스트에 담겨서 확인까지 확인됨");
						imageResId = getResources().getIdentifier("stamp_stamp", "drawable", mContext.getPackageName());
					}
				}
			}

			Log.e("FragmentStamp", "position(stampLo): " + stampLocation[position]);
			imageView.setImageResource(imageResId);
			
			return imageView;
		}
		
	}

}
