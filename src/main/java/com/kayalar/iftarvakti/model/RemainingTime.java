package com.kayalar.iftarvakti.model;

public class RemainingTime {

	public enum RemainigTimeType {
		IFTAR, IMSAK
	};

	RemainigTimeType type;

	int hour;
	int minute;
	int second;

	public RemainingTime(RemainigTimeType type, int hour, int minute, int second) {
		super();
		this.type = type;
		this.hour = hour;
		this.minute = minute;
		this.second = second;
	}

	public RemainigTimeType getType() {
		return type;
	}

	public void setType(RemainigTimeType type) {
		this.type = type;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}

	@Override
	public String toString() {
		String str = "";

		if (hour != 0)
			str += (hour + " saat, ");

		if (minute != 0)
			str += (minute + " dakika, ");

		if (second != 0)
			str += (second + " saniye");

		return String.format("%s için kalan süre: %s", type, str);
	}
}
