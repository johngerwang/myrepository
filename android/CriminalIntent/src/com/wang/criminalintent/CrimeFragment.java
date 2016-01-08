package com.wang.criminalintent;

import java.util.UUID;

import android.app.Fragment;
import android.os.Bundle;
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
//	附加argument bundle给fragment，需调用Fragment.setArguments(Bundle)方法。注意，该
//	任务必须在fragment创建后、添加给activity前完成。
//	为满足以上苛刻的要求，Android开发者遵循的习惯做法是：添加名为newInstance()的静态方法给Fragment类。
//	使用该方法，完成fragment实例及bundle对象的创建，然后将argument放入bundle中，最后再附加给fragment
	public static CrimeFragment newInstance(UUID crimeid){
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_CRIME_ID,crimeid);
		CrimeFragment fragment = new CrimeFragment();
		fragment.setArguments(args);
		return fragment;
	}
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//通过getActivity()方法来获取父容器即CrimeActivity，然后通过父容器的getIntent方法获得intent。
		//UUID crimeID = (UUID)getActivity().getIntent().getSerializableExtra(EXTRA_CRIME_ID);
		UUID crimeID = (UUID)this.getArguments().getSerializable(EXTRA_CRIME_ID);
		mCrime = CrimeLab.get(getActivity()).getCrime(crimeID);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState){
		//R.layout.fragment_crime = fragment_crime.xml
		//false，告知布局生存期是否将生成的视图添加给父视图，此处是通过activity代码的方式添加视图
		View v = inflater.inflate(R.layout.fragment_crime, parent,false);
		//在Activity中是通过Activity.findViewById(int)来完成的，此方法会调用View.findViewById()
		mTitleField = (EditText)v.findViewById(R.id.crime_title);
		mTitleField.setText(mCrime.getmTitle());
		mTitleField.addTextChangedListener(new TextWatcher(){
			public void onTextChanged(CharSequence c,int start,int before,int count){
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
		
		mDateButton = (Button)v.findViewById(R.id.crime_date);
		mDateButton.setText(mCrime.getmDate().toString());
		mDateButton.setEnabled(false);
		
		mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
		mSolvedCheckBox.setChecked(mCrime.isResolved());
		mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			//CheckBox是CompoundButton的子类
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mCrime.SetResolved(isChecked);
			}
		});
		return v;
	}
}
