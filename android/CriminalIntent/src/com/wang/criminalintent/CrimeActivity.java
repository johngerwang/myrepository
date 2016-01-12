package com.wang.criminalintent;

import java.util.UUID;

import android.support.v4.app.Fragment;

public class CrimeActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		UUID crimeid = (UUID) getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
		CrimeFragment crimeFragment = CrimeFragment.newInstance(crimeid);
		return crimeFragment;

	}

}
