package com.service.dullegil.model;

public class CourseDetailInfo {

	private String course;
	private String course_descript;
	private String spot;
	private String stamp;
	private String location;
	private String spot_descript;
	
	public CourseDetailInfo(){
		
	}
	
	public CourseDetailInfo(String course, String course_descript, String spot,
			String stamp, String location, String spot_descript) {
		super();
		this.course = course;
		this.course_descript = course_descript;
		this.spot = spot;
		this.stamp = stamp;
		this.location = location;
		this.spot_descript = spot_descript;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public String getCourse_descript() {
		return course_descript;
	}
	public void setCourse_descript(String course_descript) {
		this.course_descript = course_descript;
	}
	public String getSpot() {
		return spot;
	}
	public void setSpot(String spot) {
		this.spot = spot;
	}
	public String getStamp() {
		return stamp;
	}
	public void setStamp(String stamp) {
		this.stamp = stamp;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getSpot_descript() {
		return spot_descript;
	}
	public void setSpot_descript(String spot_descript) {
		this.spot_descript = spot_descript;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DetailInfo [course=");
		builder.append(course);
		builder.append(", course_descript=");
		builder.append(course_descript);
		builder.append(", spot=");
		builder.append(spot);
		builder.append(", stamp=");
		builder.append(stamp);
		builder.append(", location=");
		builder.append(location);
		builder.append(", spot_descript=");
		builder.append(spot_descript);
		builder.append("]");
		return builder.toString();
	}
	
	
}
