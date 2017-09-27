package com.service.dullegil.util;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Stamp_DAO {

	private static final String TAG = "Stamp_DAO";
	private static final String DATABASE_NAME = "data";
	private static final String DATABASE_TABLE = "stampDb";
	private static final int DATABASE_VERSION = 1;
	
	private static final String KEY_STAMP = "stampid";
	
	private static final String DATABASE_CREATE = "CREATE TABLE stampDb "
			+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "stampid INTEGER NOT NULL, " + "date TEXT NOT NULL);" ;
	
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	private Context mCtx;
	
	public Stamp_DAO(Context c) {
		mCtx = c;
	}
	
	public Stamp_DAO open(){
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}
	
	public Stamp_DAO openForRead(){
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getReadableDatabase();
		return this;
	}
	
	public void close(){
		mDbHelper.close();
		mDb.close();
	}
	
	public boolean insertStamp(int stampId){
		ContentValues values = new ContentValues();
		Date date = new Date();
		values.put("stampid", stampId);
		values.put("date", date.toString());
		return mDb.insert(DATABASE_TABLE, null, values) > 0;
	}
	
	public boolean deleteStamp(int stampId){
		return mDb.delete(DATABASE_TABLE, KEY_STAMP + "=" + stampId, null) > 0;
	}
	
	public Cursor getAllStamp(){
		return mDb.query(DATABASE_TABLE, null, null, null, null, null, KEY_STAMP);
	}
	
	public Cursor getStamp(int stampId){
		return mDb.query(DATABASE_TABLE, null, KEY_STAMP + "=?", 
				new String[] {String.valueOf(stampId)}, null, null, null);
	}
	
	
	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
			Log.e(TAG, "DB table is created!!!!!!!");
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
			
		}
		
	}
}
