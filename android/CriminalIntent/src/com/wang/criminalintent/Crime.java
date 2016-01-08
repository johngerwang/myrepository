package com.wang.criminalintent;

import java.util.Date;
import java.util.UUID;

import android.text.format.DateFormat;

public class Crime {
	@Override
	public String toString() {
		return "Crime [mTitle=" + mTitle + "]";
	}

	private UUID mId;
	private String mTitle;
	private Date mDate;
	private boolean mResolved;
	
	public Crime(){
		mId = UUID.randomUUID();
		mDate = new Date();
	}

	public UUID getmId() {
		return mId;
	}

	public String getmDate() {
		CharSequence cs = DateFormat.format("EEEE, MMMM dd, yyyy h:mmaa", mDate);
		return cs.toString();
	}

	public void setmDate(Date mDate) {
		this.mDate = mDate;

	}

	public boolean isResolved() {
		return mResolved;
	}

	public void SetResolved(boolean mResolved) {
		this.mResolved = mResolved;
	}

	public void setmId(UUID mId) {
		this.mId = mId;
	}

	public String getmTitle() {
		return mTitle;
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}

}
