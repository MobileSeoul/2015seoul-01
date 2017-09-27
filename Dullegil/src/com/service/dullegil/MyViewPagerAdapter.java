package com.service.dullegil;

import com.service.dullegil.view.FragmentCourse;
import com.service.dullegil.view.FragmentStamp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MyViewPagerAdapter extends FragmentStatePagerAdapter {

	/*
	 * 이 클래스의 부모생성자 호출시 인수로 반드시 FragmentManager객체를 넘겨야한다.
	 * 이 객체는 Activity에서만 만들수 있고, 여기서사용중인 Fragment가 v4이기 때문에
	 * Activity중에서도 ActionBarActivity에서 얻어와야한다.
	 */
	
	Fragment[] fragments = new Fragment[2];
	
	public MyViewPagerAdapter(FragmentManager fm) {
		super(fm);
		fragments[0] = new FragmentCourse();
		fragments[1] = new FragmentStamp();
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragments[arg0];
	}

	
	@Override
	public int getCount() {
		return fragments.length;
	}

}
