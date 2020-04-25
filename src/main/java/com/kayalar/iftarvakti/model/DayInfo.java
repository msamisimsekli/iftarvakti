package com.kayalar.iftarvakti.model;

public class DayInfo {

	private int day;
	private int month;
	private int year;

	private int imsakHour;
	private int sabahHour;
	private int ogleHour;
	private int ikindiHour;
	private int aksamHour;
	private int yatsiHour;

	private int imsakMinute;
	private int sabahMinute;
	private int ogleMinute;
	private int ikindiMinute;
	private int aksamMinute;
	private int yatsiMinute;

	public DayInfo(int day, int month, int year, int imsakHour, int sabahHour, int ogleHour, int ikindiHour,
			int aksamHour, int yatsiHour, int imsakMinute, int sabahMinute, int ogleMinute, int ikindiMinute,
			int aksamMinute, int yatsiMinute) {
		this.day = day;
		this.month = month;
		this.year = year;
		this.imsakHour = imsakHour;
		this.sabahHour = sabahHour;
		this.ogleHour = ogleHour;
		this.ikindiHour = ikindiHour;
		this.aksamHour = aksamHour;
		this.yatsiHour = yatsiHour;
		this.imsakMinute = imsakMinute;
		this.sabahMinute = sabahMinute;
		this.ogleMinute = ogleMinute;
		this.ikindiMinute = ikindiMinute;
		this.aksamMinute = aksamMinute;
		this.yatsiMinute = yatsiMinute;
	}

	public int getImsakHour() {
		return imsakHour;
	}

	public void setImsakHour(int imsakHour) {
		this.imsakHour = imsakHour;
	}

	public int getSabahHour() {
		return sabahHour;
	}

	public void setSabahHour(int sabahHour) {
		this.sabahHour = sabahHour;
	}

	public int getOgleHour() {
		return ogleHour;
	}

	public void setOgleHour(int ogleHour) {
		this.ogleHour = ogleHour;
	}

	public int getIkindiHour() {
		return ikindiHour;
	}

	public void setIkindiHour(int ikindiHour) {
		this.ikindiHour = ikindiHour;
	}

	public int getAksamHour() {
		return aksamHour;
	}

	public void setAksamHour(int aksamHour) {
		this.aksamHour = aksamHour;
	}

	public int getYatsiHour() {
		return yatsiHour;
	}

	public void setYatsiHour(int yatsiHour) {
		this.yatsiHour = yatsiHour;
	}

	public int getImsakMinute() {
		return imsakMinute;
	}

	public void setImsakMinute(int imsakMinute) {
		this.imsakMinute = imsakMinute;
	}

	public int getSabahMinute() {
		return sabahMinute;
	}

	public void setSabahMinute(int sabahMinute) {
		this.sabahMinute = sabahMinute;
	}

	public int getOgleMinute() {
		return ogleMinute;
	}

	public void setOgleMinute(int ogleMinute) {
		this.ogleMinute = ogleMinute;
	}

	public int getIkindiMinute() {
		return ikindiMinute;
	}

	public void setIkindiMinute(int ikindiMinute) {
		this.ikindiMinute = ikindiMinute;
	}

	public int getAksamMinute() {
		return aksamMinute;
	}

	public void setAksamMinute(int aksamMinute) {
		this.aksamMinute = aksamMinute;
	}

	public int getYatsiMinute() {
		return yatsiMinute;
	}

	public void setYatsiMinute(int yatsiMinute) {
		this.yatsiMinute = yatsiMinute;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	@Override
	public String toString() {
		return "DayInfo [day=" + day + ", month=" + month + ", year=" + year + ", imsakHour=" + imsakHour
				+ ", sabahHour=" + sabahHour + ", ogleHour=" + ogleHour + ", ikindiHour=" + ikindiHour + ", aksamHour="
				+ aksamHour + ", yatsiHour=" + yatsiHour + ", imsakMinute=" + imsakMinute + ", sabahMinute="
				+ sabahMinute + ", ogleMinute=" + ogleMinute + ", ikindiMinute=" + ikindiMinute + ", aksamMinute="
				+ aksamMinute + ", yatsiMinute=" + yatsiMinute + "]";
	}
}
