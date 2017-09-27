package com.service.dullegil.view;

import com.service.dullegil.R;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutCompat.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class FragmentCourse extends Fragment implements OnClickListener, OnScrollChangedListener{
	
	static final int MAP_COURSE_1 = 1;
	static final int MAP_COURSE_2 = 2;
	static final int MAP_COURSE_3 = 3;
	static final int MAP_COURSE_4 = 4;
	static final int MAP_COURSE_5 = 5;
	static final int MAP_COURSE_6 = 6;
	static final int MAP_COURSE_7 = 7;
	static final int MAP_COURSE_8 = 8;
	static final int MAP_COURSE_WHOLE = 0;
	
	private Context context;
	Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
	ScrollView scrollV;
	TextView course;
	View v;
	
	private float mActionBarHeight;
	private ActionBar mActionBar;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		v = inflater.inflate(R.layout.fragment_course, container, false);
		
		this.context = container.getContext();
		
		
		final TypedArray styleAttributes =  getActivity().getTheme().obtainStyledAttributes(
				new int[] { android.R.attr.actionBarSize });
		
		mActionBarHeight = styleAttributes.getDimension(0, 0);
		styleAttributes.recycle();
		mActionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
		
		btn1 = (Button) v.findViewById(R.id.btnCourse1);
		btn2 = (Button) v.findViewById(R.id.btnCourse2);
		btn3 = (Button) v.findViewById(R.id.btnCourse3);
		btn4 = (Button) v.findViewById(R.id.btnCourse4);
		btn5 = (Button) v.findViewById(R.id.btnCourse5);
		btn6 = (Button) v.findViewById(R.id.btnCourse6);
		btn7 = (Button) v.findViewById(R.id.btnCourse7);
		btn8 = (Button) v.findViewById(R.id.btnCourse8);
		btn9 = (Button) v.findViewById(R.id.btnFullMap);
		
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		btn4.setOnClickListener(this);
		btn5.setOnClickListener(this);
		btn6.setOnClickListener(this);
		btn7.setOnClickListener(this);
		btn8.setOnClickListener(this);
		btn9.setOnClickListener(this);	
		
		((ScrollView) v.findViewById(R.id.scrollViewCourse))
		.getViewTreeObserver().addOnScrollChangedListener(this);
		
				
	return v;
}

	@Override
	public void onClick(View v) {
		
		Intent i = new Intent(context, CourseDetailActivity.class);
		
		switch (v.getId()) {
		case R.id.btnCourse1:
			i.putExtra("course", 1);
			startActivity(i);			
			break;
		case R.id.btnCourse2:
			i.putExtra("course", 2);
			startActivity(i);
			break;
		case R.id.btnCourse3:
			i.putExtra("course", 3);
			startActivity(i);
			break;
		case R.id.btnCourse4:
			i.putExtra("course", 4);
			startActivity(i);
			break;
		case R.id.btnCourse5:
			i.putExtra("course", 5);
			startActivity(i);
			break;
		case R.id.btnCourse6:
			i.putExtra("course", 6);
			startActivity(i);
			break;
		case R.id.btnCourse7:
			i.putExtra("course", 7);
			startActivity(i);
			break;
		case R.id.btnCourse8:
			i.putExtra("course", 8);
			startActivity(i);
			break;
		case R.id.btnFullMap:
			startActivity(new Intent(getActivity(), MapActivity.class));
			break;

		default:
			break;
		}
		
	}

	@Override
	public void onScrollChanged() {
		
		float y = ((ScrollView) v.findViewById(R.id.scrollViewCourse)).getScrollY();
		
		if(mActionBar != null){
			
			if(y >= mActionBarHeight && mActionBar.isShowing()){
				mActionBar.hide();
			} else if( y==0 && !mActionBar.isShowing()){
				mActionBar.show();
			}
		}
		
		
	}
}
