package com.wang.criminalintent;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
//不使用DialogFragment，也可显示AlertDialog视图，但Android开发原则不推荐这种做法。使用
//FragmentManager管理对话框，可使用更多配置选项来显示对话框。
//另外，如果设备发生旋转，独立配置使用的AlertDialog会在旋转后消失，而配置封装在
//fragment中的AlertDialog则不会有此问题。
public class CrimePagerActivity extends FragmentActivity {
	// FragmentManager要求任何用作fragment容器的视图都必须具有资源ID。ViewPager是一个fragment容器，因此，必须赋予其资源ID。
	// 以代码的方式创建视图，应完成以下任务项：
	//  为ViewPager创建资源ID；
	//  创建ViewPager实例并赋值给mViewPager；
	//  赋值资源ID给ViewPager，并对其进行配置；
	//  设置ViewPager为activity的内容视图。
	private ViewPager mViewPager;//是支持库的类，并不是标准的ViewPager
	private ArrayList<Crime> mCrimes;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		this.setContentView(mViewPager);
		
		mCrimes = CrimeLab.get(this).getCrimes();
		FragmentManager fm = this.getSupportFragmentManager();
		//ViewPager是v4支持包中的类，所以跟它配套的类也只能是v4支持包中的内容
		//FragmentStatePagerAdapter是我们的代理，负责管理与ViewPager的对话并协同工作。
		//代理究竟做了哪些工作呢？简单来说，就是将返回的fragment添加给托管activity，并帮助Viewpager找到fragment的视图并一一对应。
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm){
//			使用FragmentStatePagerAdapter会销毁掉不需要的fragment。事务提交后，可将fragment
//			从activity的FragmentManager中彻底移除。FragmentStatePagerAdapter类名中的“state”表
//			明：在销毁fragment时，它会将其onSaveInstanceState(Bundle)方法中的Bundle信息保存下
//			来。用户切换回原来的页面后，保存的实例状态可用于恢复生成新的fragment
			
//			选择哪一种adapter取决于应用的要求。通常来说，使用FragmentStatePagerAdapter更节
//			省内存。CriminalIntent应用需显示大量crime记录，每份记录最终还会包含图片。在内存中保存所
//			有信息显然不合适，因此，我们选择使用FragmentStatePagerAdapter。
//			另一方面，如果用户界面只需要少量固定的fragment，则FragmentPagerAdapter是个安全
//			且合适的选择。最常见的例子为分页显示用户界面。例如，某些应用的明细视图所含内容较多，
//			通常需分两页显示，这时就可以将这些明细信息分拆开来，以多页面的形式展显。显然，为用户
//			界面添加支持滑动切换的ViewPager，可增强应用的触摸体验。而且，将fragment保存在内存中，
//			可使控制层的代码更易于管理。对于这种类型的用户界面，每个activity通常只有两三个fragment，
//			内存不足的风险基本不太可能发生。
			@Override
			public int getCount(){
				return mCrimes.size();
			}
			//首先获取了数据集中指定位置的Crime实例，然后利用该Crime实例的ID创建并返回一个有效配置的CrimeFragment
			public Fragment getItem(int pos){
				Crime crime = mCrimes.get(pos);
				CrimeFragment crimeFragment = CrimeFragment.newInstance(crime.getmId());
				return crimeFragment;
			}
		});
		//可通过调用setOffscreenPageLimit(int)方法，定制预加载相邻页面的数目。
		//ViewPager默认只显示PageAdapter中的第一个列表项。可设置ViewPager当前要显示的列表项为Crime数组中指定位置的列表项，从而实现所选列表项的正确显示。
		mViewPager.setOffscreenPageLimit(3);
		
		UUID id = (UUID) this.getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
		for(int i=0;i<mCrimes.size();i++){
		if(mCrimes.get(i).getmId().equals(id)){
				mViewPager.setCurrentItem(i);//根据CrimeListFragment传递过来的id号来显示CrimeFragment
				break;
			}
		}
		
		//setOnPageChangeListener(过时了)->addOnPageChangeListener被替代了
		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				Crime mCrime = mCrimes.get(arg0);
				setTitle(mCrime.getmTitle());
			}
			//onPageScrolled(...)方法可告知我们页面将会滑向哪里		
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {				
			}
			//onPageScrollStateChanged(...)方法可告知我们当前页面所处的行为状态，如正在被用户滑动、页面滑动入位到完全静止以及页面切换完成后的闲置状态。
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}
	

}

