package com.service.dullegil.view;

import java.util.ArrayList;

import com.service.dullegil.MainActivity;
import com.service.dullegil.R;
import com.service.dullegil.model.Stamp;
import com.service.dullegil.util.Stamp_DAO;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class NfcActivity extends Activity implements OnClickListener{
	
	ArrayList<Integer> stampList = new ArrayList<Integer>();
	String result;
	int stampId;
	
	Button btnStampNfc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_nfc);
		
		btnStampNfc = (Button) findViewById(R.id.btnGoStampbook);
		btnStampNfc.setOnClickListener(this);
		
		stampId = getIntent().getIntExtra("stamp_id", 0);
		
		bringnaddStampList();
		
		
	}

	private void bringnaddStampList() {
		
		if(stampId != 0){
			Stamp_DAO dao = new Stamp_DAO(this);
			dao.open();
			Cursor mCursor = dao.getAllStamp();
			//Stamp stamp;
			
			while(mCursor.moveToNext()){
				int tmpId = mCursor.getInt(mCursor.getColumnIndex("stampid"));
//				stamp = new Stamp(mCursor.getInt(mCursor.getColumnIndex("stampid")),
//						mCursor.getString(mCursor.getColumnIndex("date")));
//				stampList.add(stamp);
				stampList.add(tmpId);
			}
			Log.e("NfcActivity", "stampList: " + stampList.size());
			
			for(int	id:stampList){
				if(stampId == id){
					Log.e("NfcActivity", "존재하는 스탬프.");
					result = "false";
					dao.close();
					return;
				}
			}
			
			dao.insertStamp(stampId);
			dao.close();
			result = "true";
		}
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btnGoStampbook:
			
			btnStampNfc.setBackgroundResource(R.drawable.nfc_button_tap);
			
			Intent i = new Intent(NfcActivity.this, MainActivity.class);
			i.putExtra("stamp_result", result);
			Log.e("NfcActivity", "stList: " + stampList);
			i.putIntegerArrayListExtra("stampList", stampList);
			Log.e("NfcActivity", "stampList2: " + stampList.size());
			
			if(result.equals("true")){
				i.putExtra("stampId", stampId);
			}
			startActivity(i);
			finish();
			
			break;

		default:
			break;
		}
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		btnStampNfc.setBackgroundResource(R.drawable.nfc_button_default);
	}
}
