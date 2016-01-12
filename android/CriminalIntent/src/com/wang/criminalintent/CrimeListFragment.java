package com.wang.criminalintent;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class CrimeListFragment extends ListFragment {
	
	private ArrayList<Crime> mCrimes;
	
	private static final String TAG = "CrimeListFragment";
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.getActivity().setTheme(R.string.crimes_title);
		mCrimes = CrimeLab.get(getActivity()).getCrimes();
		//（android.R.layout.simple_list_item_1）是Android SDK提供的预定义布局资源
		CrimeArrayAdapter adapter = new CrimeArrayAdapter(mCrimes);
//		ListView需要显示视图对象时，会与其adapter展开会话沟通.
//		首先，通过调用adapter的getCount()方法，ListView询问数组列表中包含多少个对象。（为
//		避免出现数组越界的错误，获取对象数目信息非常重要。）
//		紧接着，ListView就调用adapter的getView(int, View, ViewGroup)方法。该方法的第一
//		个参数是ListView要查找的列表项在数组列表中的位置。
//		在getView(...)方法的内部实现里，adapter使用数组列表中指定位置的列表项创建一个视
//		图对象，并将该对象返回给ListView。ListView继而将其设置为自己的子视图，并刷新显示在
//		屏幕上。
//		默认的ArrayAdapter<T>.getView(...)实现方法依赖于toString()方法。它首先生成布
//		局视图，然后找到指定位置的Crime对象并对其调用toString()方法，最后得到字符串信息并传
//		递给TextView。
	
		setListAdapter(adapter);
	}

	private class CrimeArrayAdapter extends ArrayAdapter<Crime>{

		public CrimeArrayAdapter(ArrayList<Crime> objects) {
			//第二个参数:布局ID参数，不适用预定义布局参数时值为0
			super(getActivity(), 0, objects);
			// TODO Auto-generated constructor stub
		}
		//convertView，已存在的列表项视图，即是一个ListView，adapter可重新配置并返回它.
		public View getView(int position,View convertView,ViewGroup parent){
			//检查传入的视图对象是否是复用对象。如不是，则从定制布局里产生一个新的视图对象。
			if(convertView==null){
				convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_crime, null);
			}				
				Crime c = getItem(position);
				TextView  titleTextView = (TextView)convertView.findViewById(R.id.crime_list_titleTextView);
				titleTextView.setText(c.getmTitle());
				TextView  dateTextView = (TextView)convertView.findViewById(R.id.crime_list_dateTextView);
				dateTextView.setText(c.getmDate());
				CheckBox solvedCheckBox = (CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
				solvedCheckBox.setChecked(c.isResolved());
				return convertView;
			}
		}
	private static final int REQUEST_CRIME = 1;

	@Override
	public void onListItemClick(ListView l ,View v ,int position,long id){
		Crime c =((CrimeArrayAdapter)getListAdapter()).getItem(position);
		Intent i = new Intent(getActivity(),CrimePagerActivity.class);
		i.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getmId());
		//startActivity(i);//此处相当于调用了（后台）Activity.startActivity()方法
		startActivityForResult(i,REQUEST_CRIME);
		Log.d(TAG, c.getmTitle()+" was clicked.Id is " + id);
	}
	//为什么选择覆盖onResume()方法来刷新列表项显示，而非onStart()方法呢？当一个activity位于我们的activity之前时，
	//我们无法保证自己的activity是否会被停止。如前面的activity是透明的，则我们的activity可能只会被暂停。
	//对于此场景下暂停的activity，onStart()方法中的更新代码是不会起作用的。一般来说，要保证fragment视图得到刷新，在onResume()方法内更新代码是最安全的选择。
	@Override
	public void onResume(){
		super.onResume();
		((CrimeArrayAdapter)this.getListAdapter()).notifyDataSetChanged();
	}
	//	activity可以返回结果，fragment能够从activity中接收返回结果。
	//	但是fragment无法返回结果，Fragment有自己的startActivityForResult(...)和onActivityResult(...)方法。
	
	@Override
	public void onActivityResult(int requestCode,int resultCode,Intent intent){
		if(REQUEST_CRIME==requestCode){
			
		}
		
	}
}
