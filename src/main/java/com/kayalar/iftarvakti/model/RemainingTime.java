package com.kayalar.iftarvakti.model;

import java.time.ZonedDateTime;

public class RemainingTime {

	private FastEnum fastEnum;
	private ZonedDateTime time;
	private int remainingDurationInSec;

	public RemainingTime(FastEnum fastEnum, ZonedDateTime time, int remainingDurationInSec) {
		this.fastEnum = fastEnum;
		this.time = time;
		this.remainingDurationInSec = remainingDurationInSec;
	}

	public FastEnum getFastEnum() {
		return fastEnum;
	}

	public ZonedDateTime getTime() {
		return time;
	}

	public int getRemainingDurationInSec() {
		return remainingDurationInSec;
	}
}
