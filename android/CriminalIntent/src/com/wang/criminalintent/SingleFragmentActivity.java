package com.wang.criminalintent;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public abstract class SingleFragmentActivity extends Activity {

	protected abstract Fragment createFragment();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {// 处于pre创建阶段，直到onCreateView后activity才真的创建出来（但是处于停止状态)
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment);
		FragmentManager fm = this.getFragmentManager();
		// 如果fragment队列中中已经存在，则直接返回。为什么会队列中存在呢，因为该Activity会因设备的旋转或者内存被销毁
		// 而重建，重建时会调用onCreate()方法。而销毁时，FragmentManager将会保存fragment队列。
		// FragmentManager中持有该fragment的队列。
		Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
		// 为什么要创建一个新的fragment事物
		if (fragment == null) {
			fragment = createFragment();
			// FragmentTranction.资源id告知出现在activity视图的什么地方，是FragmentManager中的唯一标识符
			fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
		}

		/**
		 * FragmentManager负责调用队列中fragment的生命周期方法，onAttach(Activity)->onCreate(
		 * Bundle)->onCreateView()
		 * 在activity处于停止、暂停或运行状态下时，添加fragment会发生什么呢？此种情况下，
		 * FragmentManager立即驱使fragment快速跟上activity的步伐，直到与activity的最新状态保持同步。
		 * 例如，向处于运行状态的activity中添加fragment时，以下fragment生命周期方法会被依次调用：
		 * onAttach(Activity)、onCreate(Bundle)、
		 * onCreateView(...)、onActivityCreated(Bundle)、
		 * onStart()，以及onResume()方法。
		 */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.crime, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.crime_solved) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
