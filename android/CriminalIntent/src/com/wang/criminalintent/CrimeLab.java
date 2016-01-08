package com.wang.criminalintent;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;

public class CrimeLab {
	
	private static CrimeLab sCrimeLab;
	private Context mAppContext;
	private ArrayList<Crime> mCrimes;
	
	
	public CrimeLab(Context mAppContext) {
		this.mAppContext = mAppContext;
		mCrimes = new ArrayList<Crime>();
		for(int i=0;i<100;i++){
			Crime c = new Crime();
			c.setmTitle("Crime#" +i);
			c.SetResolved(i%2==0);
			mCrimes.add(c);
		}
	}
	
	public ArrayList<Crime> getCrimes(){
		return mCrimes;
	}
	
	public Crime getCrime(UUID id){
		for(Crime c:mCrimes){
			if(c.getmId().equals(id)){
				return c;
			}
		}
		return null;
	}
//	CrimeLab类的构造方法需要一个Context参数。这在Android开发里很常见，使用Context
//	参数，单例可完成启动activity、获取项目资源，查找应用的私有存储空间等任务。
//	注意，在get(Context)方法里，我们并没有直接将Context参数传给构造方法。该Context
//	可能是一个Activity，也可能是另一个Context对象，如Service。在应用的整个生命周期里，
//	我们无法保证只要CrimeLab需要用到Context，Context就一定会存在。
//	因此，为保证单例总是有Context可以使用，可调用getApplicationContext()方法，将
//	不确定是否存在的Context替换成application context。application context是针对应用的全局性
//	Context。任何时候，只要是应用层面的单例，就应该一直使用application context。
	public static CrimeLab get(Context c){
		if(sCrimeLab==null){
			sCrimeLab = new CrimeLab(c.getApplicationContext());
		}
		return sCrimeLab;
	}
	
	

}
