package com.service.dullegil.navigation;


import com.service.dullegil.R;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NotificationCall extends Service{
	boolean flag;
	NotificationManager notiM;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				while(flag){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		t.start();
		
		return START_STICKY;
	}
	
	public void onCreate() {
		super.onCreate();
		flag=true;
		notiM=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		displayNotificationMessage("경로를 이탈하였습니다.");
	};
	
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	private void displayNotificationMessage(String msg){
		NotificationCompat.Builder builder= new NotificationCompat.Builder(getApplicationContext());
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setTicker(msg);
		builder.setContentTitle("경로이탈");
		builder.setContentText(msg);
		//builder.setVibrate(new long[]{0,1500});
		builder.setVibrate(new long[]{0,300});
		notiM.notify(1, builder.build());
	}

}
