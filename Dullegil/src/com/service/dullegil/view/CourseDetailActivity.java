package com.service.dullegil.view;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.service.dullegil.R;
import com.service.dullegil.model.CourseDetailInfo;

public class CourseDetailActivity extends FragmentActivity implements
		OnClickListener {

	static int num_items = 6;
	ImageFragmentPagerAdapter imageFragmentPagerAdapter;
	ViewPager viewPager = null;

	private static int course = 0;
	private static String root_course;

	private static final String COURSE1 = "수락·불암산 코스";
	private static final String COURSE2 = "용마·아차산 코스";
	private static final String COURSE3 = "고덕·일자산 코스";
	private static final String COURSE4 = "대모·우면산 코스";
	private static final String COURSE5 = "관악산 코스";
	private static final String COURSE6 = "안양천 코스";
	private static final String COURSE7 = "봉산·앵봉산코스";
	private static final String COURSE8 = "북한산코스";

	static ArrayList<CourseDetailInfo> detailList;

	public static String[] image_name = {"photo", "photo", "photo", "photo"
		, "photo", "photo", "photo", "photo", "photo", "photo"};
	public static String navi[];

	Button btnMap, btnReview, btnOther, btnBack;
	TextView tvCourse;

	private static String[] detail_info = new String[] { "해당 정보가 없습니다." };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_course_detail);
		
		navi = getResources().getStringArray(R.array.nav_icon);
		Log.e("NAVI", "navi: " + navi.length);
		
		if (getIntent() != null) {
			course = getIntent().getIntExtra("course", 0);
			Log.e("INTENT", "course: " + course);

			setCourseData(course);
		}

		imageFragmentPagerAdapter = new ImageFragmentPagerAdapter(
				getSupportFragmentManager());
		viewPager = (ViewPager) findViewById(R.id.detailViewPager);
		viewPager.setAdapter(imageFragmentPagerAdapter);

		btnMap = (Button) findViewById(R.id.btnShowEachMap);
		btnReview = (Button) findViewById(R.id.btnShowReview);
		btnOther = (Button) findViewById(R.id.btnSelectOtherCourse);
		btnBack = (Button) findViewById(R.id.backButton);
		tvCourse = (TextView) findViewById(R.id.tvDetailCourse);
		
		tvCourse.setText(course + "코스 정보");

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		int width = dm.widthPixels / 3;
		btnMap.setWidth(width);
		btnReview.setWidth(width);
		btnOther.setWidth(width);

		btnMap.setOnClickListener(this);
		btnReview.setOnClickListener(this);
		btnOther.setOnClickListener(this);
		btnBack.setOnClickListener(this);

	}

	private void setCourseData(int courseNum) {

		switch (courseNum) {
		case 1:
			detail_info = getResources().getStringArray(R.array.course1);
			root_course = COURSE1;
			break;
		case 2:
			detail_info = getResources().getStringArray(R.array.course2);
			root_course = COURSE2;
			break;
		case 3:
			detail_info = getResources().getStringArray(R.array.course3);
			root_course = COURSE3;
			break;
		case 4:
			detail_info = getResources().getStringArray(R.array.course4);
			root_course = COURSE4;
			break;
		case 5:
			detail_info = getResources().getStringArray(R.array.course5);
			root_course = COURSE5;
			break;
		case 6:
			detail_info = getResources().getStringArray(R.array.course6);
			root_course = COURSE6;
			break;
		case 7:
			detail_info = getResources().getStringArray(R.array.course7);
			root_course = COURSE7;
			break;
		case 8:
			detail_info = getResources().getStringArray(R.array.course8);
			root_course = COURSE8;
			break;
		default:
			break;
		}

		setDetailDataParsing();
	}

	private void setDetailDataParsing() {
		String detail_course = "defult";
		String detail_spot = "defult";
		String detail_stamp = "defult";
		String detail_location = "defult";
		String detail_spot_descript = "defult";
		
		detailList = new ArrayList<CourseDetailInfo>();

		try {
			XmlPullParser parser = getResources().getXml(R.xml.detail_info);

			int event = XmlPullParser.START_DOCUMENT;
			while ((event = parser.next()) != XmlPullParser.END_DOCUMENT) {
				switch (event) {
				case XmlPullParser.START_TAG:

					String startTag = parser.getName();

					if (startTag.trim().equals("course")) {
						detail_course = parser.nextText();
					}
					if (startTag.trim().equals("spot")) {
						detail_spot = parser.nextText();
					} 
					if (startTag.trim().equals("stamp")) {
						detail_stamp = parser.nextText();
					} 
					if (startTag.trim().equals("location")) {
						detail_location = parser.nextText();
					} 
					if (startTag.trim().equals("spot_descript")) {
						detail_spot_descript = parser.nextText();

						if (detail_course.trim().equals(root_course)) {
							detailList.add(new CourseDetailInfo(detail_course,
									null, detail_spot, detail_stamp,
									detail_location, detail_spot_descript));
						}
					}


					break;

				default:
					break;
				}
			}
		} catch (Throwable t) {

		}

		num_items = detailList.size();
		
		System.out.println("list size: " + num_items + ", 내용: " + detailList.toString());
	}

	public static class ImageFragmentPagerAdapter extends FragmentPagerAdapter {
		public ImageFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return num_items+1;
		}

		@Override
		public Fragment getItem(int position) {
			if (position == 0) {
				return new DetailFirstFragment();
			} else {
				SwipeFragment fragment = new SwipeFragment();
				return SwipeFragment.newInstance(position);
			}
		}
	}

	public static class DetailFirstFragment extends Fragment {

		ImageView infoV, routeV, stampV, trafficV;
		// 리뷰 관련 텍스트뷰 필요
		ListView reviewV;
		Button btnStamp, btnReview;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			final View v = inflater.inflate(R.layout.fragment_detail_first,
					container, false);

			infoV = (ImageView) v.findViewById(R.id.detailCourseInfo);
			routeV = (ImageView) v.findViewById(R.id.detailCourseRoute);
			stampV = (ImageView) v.findViewById(R.id.detailCourseStamp);
			trafficV = (ImageView) v.findViewById(R.id.detailCourseTraffic);
			btnStamp = (Button) v.findViewById(R.id.btnGoStampMap);

			for (int i = 0; i < detail_info.length; i++) {
				int detailResId = getResources().getIdentifier(detail_info[i],
						"drawable", getContext().getPackageName());
				switch (i) {
				case 0:
					infoV.setImageResource(detailResId);
					break;
				case 1:
					routeV.setImageResource(detailResId);
					break;
				case 2:
					stampV.setImageResource(detailResId);
					break;
				case 3:
					trafficV.setImageResource(detailResId);
					break;
				default:
					break;
				}
			}

			btnStamp.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					v.findViewById(R.id.btnGoStampMap).setBackgroundResource(
							R.drawable.one_button_tap);

					Intent showStampMapIntent = new Intent(getActivity(),
							MapActivity.class);
					showStampMapIntent.putExtra("showStamp", course);
					startActivity(showStampMapIntent);

				}
			});

			return v;
		}

		@Override
		public void onResume() {
			super.onResume();

			btnStamp.setBackgroundResource(R.drawable.one_button_default);

		}

	}

	public static class SwipeFragment extends Fragment {

		ImageView imageView, stampImage, naviImage;
		TextView tvLocation, tvSpot, tvDesc, tvSpotNum;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View swipeView = inflater.inflate(R.layout.fragment_detail,
					container, false);
			imageView = (ImageView) swipeView.findViewById(R.id.imageView);
			naviImage = (ImageView) swipeView.findViewById(R.id.iconCourseNavi);
			tvLocation = (TextView) swipeView.findViewById(R.id.tvNaviLocation);
			tvSpot = (TextView) swipeView.findViewById(R.id.tvImageTitle);
			tvDesc = (TextView) swipeView.findViewById(R.id.tvDetailSummary);
			tvSpotNum = (TextView) swipeView.findViewById(R.id.tvTotalPhoto);
			stampImage = (ImageView) swipeView
					.findViewById(R.id.stampImageView);
			
			

			Bundle bundle = getArguments();
			int position = bundle.getInt("position") - 1;
			String imageFileName = image_name[position];
			CourseDetailInfo tempDetail = detailList.get(position);

			int imgResId = getResources().getIdentifier(imageFileName,
					"drawable", getContext().getPackageName());
			int naviResId = getResources().getIdentifier(navi[position], 
					"drawable", getContext().getPackageName());

			imageView.setImageResource(imgResId);
			naviImage.setImageResource(naviResId);
			tvLocation.setText(tempDetail.getLocation());
			tvSpot.setText(tempDetail.getSpot());
			tvDesc.setText(tempDetail.getSpot_descript());
			if (tempDetail.getStamp().trim().equals("true")) {
				stampImage.setImageResource(R.drawable.stamp);
				;
			}
			tvSpotNum.setText("총 " + num_items + "개의 주요 거점이 있습니다.");

			return swipeView;
		}

		static SwipeFragment newInstance(int position) {
			SwipeFragment swipeFragment = new SwipeFragment();
			Bundle bundle = new Bundle();
			bundle.putInt("position", position);
			swipeFragment.setArguments(bundle);
			return swipeFragment;
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btnShowEachMap:
			Intent mapIntent = new Intent(CourseDetailActivity.this,
					MapActivity.class);
			mapIntent.putExtra("course", course);
			startActivity(mapIntent);
			break;
		case R.id.btnShowReview:
			break;
		case R.id.btnSelectOtherCourse:
			onBackPressed();
			break;
		case R.id.backButton:
			onBackPressed();
			break;

		default:
			break;
		}

	}
}
