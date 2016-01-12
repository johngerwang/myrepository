package com.wang.criminalintent;

import java.util.Date;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class CrimeFragment extends Fragment {

	private Crime mCrime;
	private EditText mTitleField;
	private Button mDateButton;
	private CheckBox mSolvedCheckBox;
	public static final String EXTRA_CRIME_ID = "CrimeFragment.EXTRA_CRIME_ID";
	public static final int REQUEST_DATE = 0;

	// 附加argument
	// bundle给fragment，需调用Fragment.setArguments(Bundle)方法。注意，该任务必须在fragment创建后、添加给activity前完成。
	// 为满足以上苛刻的要求，Android开发者遵循的习惯做法是：添加名为newInstance()的静态方法给Fragment类。
	// 使用该方法，完成fragment实例及bundle对象的创建，然后将argument放入bundle中，最后再附加给fragment
	public static CrimeFragment newInstance(UUID crimeid) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_CRIME_ID, crimeid);
		CrimeFragment fragment = new CrimeFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 通过getActivity()方法来获取父容器即CrimeActivity，然后通过父容器的getIntent方法获得intent。
		// UUID crimeID =
		// (UUID)getActivity().getIntent().getSerializableExtra(EXTRA_CRIME_ID);
		UUID crimeID = (UUID) this.getArguments().getSerializable(EXTRA_CRIME_ID);
		mCrime = CrimeLab.get(getActivity()).getCrime(crimeID);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		// R.layout.fragment_crime = fragment_crime.xml
		// false，告知布局生存期是否将生成的视图添加给父视图，此处是通过activity代码的方式添加视图
		View v = inflater.inflate(R.layout.fragment_crime, parent, false);
		// 在Activity中是通过Activity.findViewById(int)来完成的，此方法会调用View.findViewById()
		mTitleField = (EditText) v.findViewById(R.id.crime_title);
		mTitleField.setText(mCrime.getmTitle());
		mTitleField.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence c, int start, int before, int count) {
				mCrime.setmTitle(c.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});

		mDateButton = (Button) v.findViewById(R.id.crime_date);
		mDateButton.setText(mCrime.getmDate().toString());
		// mDateButton.setEnabled(false);
		mDateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				DatePickerFragment dpf = DatePickerFragment.newInstance(mCrime.getDate());
				// 对于activity的数据回传，我们调用startActivityForResult(...)方法，ActivityManager
				// 负责跟踪记录父activity与子activity间的关系。当子activity回传数据后被销毁了，ActivityManager
				// 知道接收返回数据的应为哪一个activity。
				// 目标fragment以及请求代码由FragmentManager负责跟踪记录，我们可调用fragment
				// （设置目标fragment的fragment）的getTargetFragment()和getTargetRequestCode()方法获取它们。
				dpf.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
				dpf.show(fm, "date");
			}
		});

		mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
		mSolvedCheckBox.setChecked(mCrime.isResolved());
		mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			// CheckBox是CompoundButton的子类
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mCrime.SetResolved(isChecked);
			}
		});
		return v;
	}

//	Activity.onActivityResult(...)方法是ActivityManager在子activity销毁后调用的父
//	activity方法。处理activity间的数据返回时，无需亲自动手，ActivityManager会自动调用Activity.
//	onActivityResult(...)方法。父activity接收到Activity.onActivityResult(...)方法的调用
//	后，其FragmentManager会调用对应fragment的Fragment.onActivityResult(...)方法。
//	处理由同一activity托管的两个fragment间的数据返回时，可借用Fragment.onActivity
//	Result(...)方法。因此，直接调用目标fragment的Fragment.onActivityResult(...)方法，即可实现数据的回传
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode != Activity.RESULT_OK)
			return;
		if (requestCode == REQUEST_DATE) {
			Date date = (Date) intent.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			System.out.print("onActivityResult"+date);
			mCrime.setmDate(date);
			mDateButton.setText(mCrime.getmDate().toString());
		}
	}

}
