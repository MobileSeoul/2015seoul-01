package com.service.dullegil.model;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class Stamp implements Parcelable{
    private int checking;
    private Date date;
    private int stamp_id;
    private int user_id;
    private NFCTag tag;
    
    private double lat;
    private double lon;
    private String name;
    
    private String dateString;
    
    public Stamp(int id, String date){
    	stamp_id = id;
    	setDateString(date);
    }
    
	public Stamp(int checking, Date date, int stamp_id, int user_id, NFCTag tag) {
        this.checking = checking;
        this.date = date;
        this.stamp_id = stamp_id;
        this.user_id = user_id;
        this.tag = tag;
    }
    
    public Stamp(String name, double lat, double lon){
    	this.name=name;
    	this.lat= lat;
    	this.lon = lon;
    }
    
    public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



    public int getChecking() {
        return checking;
    }

    public void setChecking(int checking) {
        this.checking = checking;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getStamp_id() {
        return stamp_id;
    }

    public void setStamp_id(int stamp_id) {
        this.stamp_id = stamp_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public NFCTag getTag() {
        return tag;
    }

    public void setTag(NFCTag tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "Stamp{" +
                "checking=" + checking +
                ", date=" + date +
                ", stamp_id=" + stamp_id +
                ", user_id=" + user_id +
                ", tag=" + tag +
                '}';
    }

	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}
}
