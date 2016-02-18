# Activity生命周期
基本上可以分3对，onCreate对应onDestroy销毁跟创建， onStart对应onStop可见与不可见都不能操作，onResume对应onPause都可见，前者可操作后者不可操作。保存数据最好在onPause中执行。因为当对象处于不可见时，操作系统可能会因为内存紧张而回收掉该Activity，所以，尽量不要onStop在中执行保存数据的操作。
onCreate()------->onStart()-------->onResume()--------->onSaveInstanceState()---->onPause()------->onStop--------->onDestroy()。
每一个activity都经历了这样一个过程的大部分。当启动一个activity时，首先是在onCreate处进行初始化界面所需要的信息，如：界面的views，buttons，分配的引用变量等；初始化完成后就会调用onStart，此时就可以看到界面了；当用户与界面进行交互时就会调用onResume函数；当此activity因为被其他activity没有完全覆盖而进入pause状态时就调用onPause（），当被其他activity完全覆盖时就调用onStop函数。在后面的这两种情况下都会调用onSaveInstanceState方法来暂时保存被覆盖的activity的状态，在这些被覆盖的activity重新回到界面上的时候会恢复这些状态；当调用finish方法使，这个activity就被destroy了，也就从栈中移除了。

#FrameLayout,LinearLayout,RelativeLayout,TableLayout
- FrameLayout。单帧布局。即各组件重叠，后一个组件位于前一个组件之上，如果
- LinearLayout。可以是垂直或者水平布局，后定义的组件位于先定义组件之后。
- RelativeLayout。相对位置布局，可以定义相对于其他组件或者父组件的位置。
- TableLayout。表格布局。

- TableLayout。其中属性android:stretchColumns="*"，，该属性可确保表格布局的列都具有同样的宽度。android:stretchMode="columnWith":如果在列的空间分配上出现少于120dp(android:columnWidth="120dp"
    android:numColumns="auto_fit":指示GridView创建尽可能多的列，以铺满整个屏幕)的剩余空间，则stretchMode属性会要求布局(如GridView)在全部列间均分这部分剩余空间。要求GridView在全部列间均分这部分剩余空间。

#从调用Activity获取数据
- startActivityForResult(i, REQUEST_CONTACT);

# 上下文菜单
- 需要实现public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)方法，创建上下文菜单视图。只会调用一次，他只会在Menu显示之前去调用一次，之后就不会在去调用。onPrepareOptionsMenu是每次在display Menu之前，都会去调用，只要按一次Menu按鍵，就会调用一次。onPrepareOptionsMenu在3.0以后版本的设备中，在onOptionsItemSelected(MenuItem)通过Activity.invalidateOptionsMenu()方法回调onPrepareOptionsMenu(Menu)方法并刷新菜单项。
- 实现该public boolean onContextItemSelected(MenuItem item)方法，完成菜单的动作。MenuItem表示具体选中的是哪一个菜单。
- ContextMenuInfo。AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();该类表示，某一个View给上下文菜单提供的信息，如位置信息，id信息等。上下文菜单作用于View，而OptionsMenu作用于整个Activity。

#如何判断设备是否有相机
- 先获得PackageManager。PackageManager pm = getActivity().getPackageManager();
- PackageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) PackageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT))

#Surface,SurfaceView,SurfaceHolder
- Surface：代表像素集，可以理解为显存的映像。
- SurfaceView:视图，将显存的内容显示出来。
- SurfaceHolder：连接Surface和SurfaceView的一个控制器，提供各种生命周期的回调方法。实际上SurfaceHolder提供了另一个接口：SurfaceHolder.Callback。该接口监听Surface生命周期中的事件，通过该接口就可以控制Surface与其客户端(如Camera)协同工作。SurfaceHolder.Callback的三个接口方法如下。
 
 > public abstract void surfaceCreated(SurfaceHolder holder)
包含SurfaceView的视图层级结构被放到屏幕上时调用该方法。这里也是Surface与其客户端进行关联的地方。
当客户端是相机时，在该方法中调用public final void setPreviewDisplay(SurfaceHolder holder)连接Camera与Surface
 > abstract void surfaceChanged(SurfaceHolder holder, int format, int width, int height)Surface首次显示在屏幕上时调用该方法。通过传入的参数，可以知道Surface的像素格式以及它的宽度和高度。该方法内可以通知Surface的客户端，有多大的绘制区域可以使用。当客户端是相机时，使用public final void startPreview()方法来在Surface上绘制帧。 > public abstract void surfaceDestroyed(SurfaceHolder holder)SurfaceView从屏幕上移除时，Surface也随即被销毁。通过该方法，可以通知Surface的客户端停止使用Surface。
当客户端是相机时，使用public final void stopPreview()该方法用来停止在Surface上绘制帧。



#Camera.takePicture(ShutterCallback shutter,PictureCallback raw, PictureCallback jpeg)
- ShutterCallback回调方法onShutter会在相机捕获图像时调用，但此时，图像数据还未处理完成。
- 第一个PictureCallback的回调方法onPictureTake()是在原始图像数据可用时调用，通常来说，是在加工处理原始图像数据且没有存储之前。
- 第二个PictureCallback的回调方法onPictureTake(byte[] bytes ,Camera camera)是在JPEG版本的图像可用时调用。

#BitmapFactory.Options
- 将图片加载到应用中显示时使用BitmapFactory.decodeFile(String path)方法将图片转成Bitmap时，如遇到大一些的图片，会出现OOM。避免出现OOM的方式是使用BitmapFactory.Options。即使BitmapFactory.Options.inJustDecodeBounds=true，那么BitmapFactory.decodeFile(String path, Options opt)并不会真的返回一个Bitmap给你，它仅仅会把它的宽，高取回来给你，这样就不会占用太多的内存（实际上返回的是null)，也就不会那么频繁的发生OOM了。 

#Bitmap.recycle()
- Android开发文档暗示不需要调用Bitmap.recycle()方法，但实际上需要.Bitmap.recycle()方法释放了bitmap占用的原始存储空间。这也是bitmap对象最核心的部分。如果不主动调用recycle()方法释放内存，占用的内存也会被清理。但是，它是在将来某个时点在finalizer中清理，而不是在bitmap自身的垃圾回收时清理。这意味着很可能在finalizer调用之前，应用已经耗尽了内存资源。

#图片加载与卸载时刻使用什么什么周期方法
- 加载:onStart()。当Activity可见（即使不能操作）时就加载。
- 卸载:onStop()。当Activity不可见时就卸载。记得一定要卸载，不然暂用内存。

#显示图片使用什么组件
- ImageView

#abd shell am start -n packagename/.LauncherActivity
- 通过该命令能够单独启动一个Activity。但是此Activity在AndroidManifest.xml中必须设置android:export="true"

#Fragment.getString()
- 在strings.xml中定义字符串时，可以定义格式化字符串，如 <string name="crime_report">%1$s!The crime was discovered on %2$s. %3$s, and %4$s </string>，其中1$s，代表格式串，需要赋值进去。
- 使用Fragment.getString()方法可以获得strings.xml中的字符串,当需要给上述带有格式化字符串赋值时，也可以使用该方法，主要在getString()方法中根据格式串，设置对应的值就可以了。例如,getString(R.string.crime_report,dateString,solvedString,suspect)。

#显式Intent和隐式Intent
- Intent的中文意思是“意图，意向”，在Android中提供了Intent机制来协助应用间的交互与通讯，Intent负责对应用中一次操作的动作、动作涉及数据、附加数据进行描述，Android则根据此Intent的描述，负责找到对应的组件，将 Intent传递给调用的组件，并完成组件的调用。Intent不仅可用于应用程序之间，也可用于应用程序内部的Activity/Service之间的交互。因此，可以将Intent理解为不同组件之间通信的“媒介”专门提供组件互相调用的相关信息。
- 显式Intent使用方法。
该构造方法使用传入的参数来获取Intent需要的ComponentName。ComponentName由包名和类名共同组成。传入Activity和Class创建Intent时，构造方法会通过Activity类自行确定全路径包名。
		
		//方法1.
		Intent i = new Intent(Context packageContext, Class<?> cls)，
		其中Context是指上下文环境，比如在A Activity中启动B Activity时，那么Context是A Activity，Class是指B Activity.class。
		//方法2.通过包名和类名创建一个ComponentName，然后再使用下面的Intent方法创建一个显式intent：
		public Intent setComponent(ComponentName component)
		
		//方法3
		Intent i = new Intent(Intent.ACTION_MAIN);//动作可以根据实际情况设置，本例中设置为ACTION_MAIN。
		//设置Activity的包名和类名。该方法能够自动创建组件名，所以使用该方法需要的实现代码相对最少。
		i.setClassName(activityInfo.applicationInfo.packageName, activityInfo.name);
		
- 隐式intent，只需向操作系统描述清楚我们的工作意图。操作系统会去启动那些对外宣称能够胜任工作任务的activity。如果操作系统找到多个符合的activity，用户将会看到一个可选应用列表，然后就看用户如何选择了。隐式Intent的组合部分如下。
> 1. 要执行的操作。通常以Intent类中的常量来表示。例如，要访问查看某个URL，可以使用Intent.ACTION_VIEW；要发送邮件，可以使用Intent.ACTION_SEND。Intent.ACTION_DIAL，进入拨打电话界面，并设置好电话号码，等待用于拨打。Intent.ACTION_CALL，直接将电话拨打出去。> 2. 要访问数据的位置。这可能是设备以外的资源，如某个网页的URL，也可能是指向某个文件的URI，或者是指向ContentProvider中某条记录的某个内容URI（content URI）。
> 3. 操作涉及的数据类型。这指的是MIME形式的数据类型，如text/html或audio/mpeg3。如果一个intent包含某类数据的位置，那么通常可以从中推测出数据的类型。>4. 可选类别。
如果操作用于描述具体要做什么，那么类别通常用来描述我们是何时、何地或者说如何使用某个activity的。Android的android.intent.category.LAUNCHER类别表明，activity应该显示在顶级应用启动器中

例子		
	 
	 Intent i = new Intent(Intent.ACTION_SEND);//通过邮件发送
    i.setType("text/plain");//设置数据类型。
    i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());//要发送的数据。
    i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));//要发送的数据。
    i = Intent.createChooser(i, getString(R.string.send_report));//设置是否一定出现应用选择器
    startActivity(i)//打开应用选择器
    Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);//设置从联系人打
    开连接人列表。
    startActivityForResult(i, REQUEST_CONTACT);//打开联系人应用
- 如果某个设备上没有任何与目标隐式intent相匹配的activity，会出现什么情况呢？答案是，如果操作系统找不到匹配的activity，那么应用会立即崩溃。可以通过以下方式来检查是否有匹配的Activity。将intent传入PackageManager类的queryIntentActivities(...)方法中，该方法会返回一个对象列表。这些对象包含响应传入intent的activity的元数据信息。我们只需确认对象列表中至少包含一个有效项。也就是说，设备上，至少有一个activity可以响应我们的intent。
		
		PackageManager pm = getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(yourIntent,0);
		boolean isIntentSafe = activities.size()>0;
		

#获取联系人信息
- 联系人应用返回包含在intent中的URI数据给父activity时，它会添加一个Intent.FLAG_GRANT_READ_URI_PERMISSION标志。该标志向Android示意，父activity可以使用联系人数据一次。通过startActivityForResult()启动联系人应用后，返回的结果可以在onActivityResult(int requestCode, int resultCode, Intent data)方法中，使用Uri contactUri = data.getData()获得所有联系人URI。
- 设置查询字段。如设置查询字段为联系人名。
  String[] queryFields = new String[] { ContactsContract.Contacts.DISPLAY_NAME_PRIMARY };
- 根据查询字段返回查询结果。联系人信息是通过ContentProvider类来实现的，该类的实例封装了联系人数据库并提供给其他应用使用。我们可以通过一个ContentResolver访问。通过URI：ContactsContract.Contacts.CONTENT_URI可以查询到所有联系人。

       	ContentProviderCursor c = getActivity().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, queryFields, null, null, null);

#资源别名
- 别名资源是一种指向其他资源的特殊资源。它存放在res/values/目录下，并按照约定定义在refs.xml文件中。如有两种布局定义，分别是activity_fragment.xml，用于手机的布局，activity_twopane.xml用于平板的布局。当想使用一份代码来管理时，可以在refs.xml文件中设置别名，如下。
  
  	values目录下的refs.xml文件中设置别名，意思是在手机设备中，使用activity_fragment.xml布局。
   		
   		<resources>
			<item name="activity_masterdetail" type="layout">@layout/activity_fragment</item>
		</resources>
		
   	values-sw600dp目录下的refs.xml文件中设置别名，意思是在平板设备中，使用activity_twopane.xml布局。
    	
    	<resources>
   	 		<item name="activity_masterdetail" type="layout">@layout/activity_twopane</item>
    	</resources>

-sw600dp配置修饰符表明：对任何最小尺寸为600dp或更高dp的设备，都使用该资源。对于指定平板的屏幕尺寸规格来说，这是一种非常好的做法。

#将Fragment托管Activity:在Activity的onCreate()方法
- 定义一个FrameLayout布局。假设id为fragmentContainer
- 创建FragmentManager对象。
	
		FragmentManager fm = this.getSupportFragmentManager();

- 使用FragmentManager，通过id查到到Fragment。理由:为什么会队列中存在呢，因为该Activity会因设备的旋转或者内存被销毁而重建，重建时会调用onCreate()方法。而销毁时，FragmentManager将会保存fragment队列。FragmentManager中持有该fragment的队列。

		//如果fragment队列中中已经存在，则直接返回
		Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);

- 新建Fragment，并将Fragment添加到FragmentManager的事务中。一般第2步与本步骤一起使用。如果在FragmentManager中不存在Fragment，那么新建一个Fragment。
		
		if (fragment == null) {
			fragment = new XXXXFragment();//新创建一个Fragment。
			fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
		}

# 回调
利用多态特性，可以在父类的方法执行时，调用子类的方法。该方法必须在子类中覆盖。举例说明如下：

- 概要如下：一个Activity托管这2个Fragment。其中一个Fragment是内容的列表，而另外一个Fragment是内容的明细。这2个Fragment左右相邻显示。在左侧列表Fragment中选择其中一个item时，右边明细Fragment显示详细内容。
- 回调实现。在左侧Fragment中定义一个接口，如Callback，方法为onItemSelect()，在托管Activity中实现该Callable接口，实现onItemSelected方法的内容为在该Activity的右侧显示明细Fragment。列表Fragment定义一个Callable成员变量callbacker，并在onAttach(Activity A)生命周期方法中，将该托管Activity A转换成Callable对象并赋值给callbacker，当在明细Fragment中选择某一个Item时（onListItemClick方法被调用时），调用callbacker.onItemSelect()时，就会调用到托管Activity的onItemSelect()方法，这样就能实现回调的作用。
- 那么为什么需要通过回调的方式来处理呢？因为某种层面上来讲，创建明细Fragment也好，列表Fragment也好，都应该是Activity的工作，而不应该是列表Fragment的工作。
- 实例代码。详细请参考<Android编程权威指南>P346。

public class CrimeListFragment extends ListFragment {
 
    	private Callbacks mCallbacks;

    	public interface Callbacks {
        	void onCrimeSelected(Crime crime);
    	}

    	@Override
    	public void onAttach(Activity activity) {
        	super.onAttach(activity);
        	mCallbacks = (Callbacks)activity;
    	}
    	。。。。。。。
    
   		public void onListItemClick(ListView l, View v, int position, long id) {
        	// get the Crime from the adapter
        	Crime c = ((CrimeAdapter)getListAdapter()).getItem(position);
        	mCallbacks.onCrimeSelected(c);
    	}
    }

	public class CrimeListActivity extends SingleFragmentActivity 
    	implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks {

	public void onCrimeSelected(Crime crime) {
        if (findViewById(R.id.detailFragmentContainer) == null) {
            // start an instance of CrimePagerActivity
            Intent i = new Intent(this, CrimePagerActivity.class);
            i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
            startActivityForResult(i, 0);
        } else {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment oldDetail = fm.findFragmentById(R.id.detailFragmentContainer);
            Fragment newDetail = CrimeFragment.newInstance(crime.getId());
            if (oldDetail != null) {
                ft.remove(oldDetail);
            } 
            ft.add(R.id.detailFragmentContainer, newDetail);
            ft.commit();
        }
    }


#获取能够匹配intent的activity列表
- 获得能够Activity，该Activity使用的Intent的动作为ACTION_MAIN,类别为CATEGORY_LAUNCHER的
		
		Intent startupIntent = new Intent(Intent.ACTION_MAIN);
		startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		PackageManager pm = getActivity().getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent,0)
		
#通过字符的大小写排序
- String.CASE_INSENSITIVE_ORDER.compare(String A,String B) 。CASE_INSENSITIVE_ORDER是一个CaseInsensitiveComparator。

#ResolveInfo：通过ResolveInfo可以获得Activity的信息，如
- Activity类名获取方法:ResolveInfo.activityInfo.name
- Activity包名获取方法：ResolveInfo.activityInfo.packageName
- App包名获取方法:resolve.activityInfo.applicationInfo.packageName
- icon获取获取方法：resolve.loadIcon(packageManager)
- 应用名称获取方法：resolve.loadLabel(packageManager).toString()

#ArrayAdapter
- Adapter是连接后端数据和前端显示的适配器接口，是数据和UI（View）之间一个重要的纽带。在常见的View(ListView,GridView)等地方都需要用到Adapter。DataSource<-->Adapter<-->ListView
- ArrayAdapter支持泛型操作，最为简单，只能展示一行字
- 使用步骤
		
		//STEP1.继承ArrayAdapter，需要明确泛型类型
		class CrimeAdapter extends ArrayAdapter<Crime>
		//STEP2.实现构造方法
		ArrayAdapter(Context context, @LayoutRes int resource, @IdRes int textViewResourceId)
		public CrimeAdapter(ArrayList<Crime> crimes) {
            super(getActivity(), android.R.layout.simple_list_item_1, crimes);
        }
		//实现返回当前选中的视图的方法
		public View getView(int position, View convertView, ViewGroup parent)
		//
		//通知底层数据模型有改变，刷新视图
		ArrayAdapter.notifyDataSetChanged();
		//设置adapter和视图相关联，如跟ListFragment默认的ListView视图关联。
		setListAdapter(ArrayAdapter)
- 列表的显示需要三个元素：a．ListVeiw 用来展示列表的View。b．适配器 用来把数据映射到ListView上的中介。c．数据    具体的将被映射的字符串，图片，或者基本组件。	


#Task任务
- 在每一个运行的应用中，Android都使用任务来跟踪用户的状态。任务是用户比较关心的activity栈。栈底部的activity通常称为基activity。栈顶的activity是用户可以看到的activity。用户点击后退键时，栈顶activity会弹出栈外。如果当前屏幕上显示的是基activity，点击后退键，系统将退回主屏幕。不影响各个任务的状态，任务管理器可以让我们在任务间切换。例如，如果启动进入联系人应用，然后切换到Twitter应用查看信息，这时，我们将启动两个任务。如果再切换回联系人应用，我们在两项任务中所处的状态位置会被保存下来。默认情况下，新activity都在当前任务中启动。假设在A应用中，无论什么时候启动新activity，它都会被添加到当前任务中。即使要启动的activity不属于A应用，它同样也是在当前任务中启动。在当前任务中启动activity的好处是，用户可以在任务内而不是在应用层级间导航返回。
- 简单理解TASK意思是，如果多个Activity在同一个TASK中，但是不在同一个应用中，那么长按HOME键后出现的任务管理中，只会出现一个应用。而如果处于不同的TASK中，则会出现多个应用的图标。
- 通过下面的代码可以创建一个新的任务。但是FLAG_ACTIVITY_NEW_TASK标志控制着每个activity仅创建一个任务，即如果A应用中启动了B应用的某一个Activity，即使A任务再一次启动B应用的某一个Activity，在任务管理器中也只是存在一个B应用。

		Intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)。
		startActivity(Intent)；	


#隐藏标题项
- 在onCreate()方法中，调用requestWindowFeature(Window.FEATURE_ON_TITLE);

#LaunchMode
- 任务和Activity。Activity：一个activity就是一个用户界面，可以连续启动很多activity以提高用户体验。当然这个数量是有限的，这要根据手机的硬件配置来决定。所有打开的activity都是存储在栈中的，所以如果打开的activity过多，会消占去很多的内存和处理器资源，严重时会导致当前应用无法启动或者系统崩溃。简单的的说，任务就是用户所体验到的“应用程序”。它是一组相关的activity，分配到 一个栈中。栈中的根activity，是任务的开始，一般来说，它是用户组应用程序加载器中选择的activity。在栈顶的activity正是当前 正在运行的——集中处理用户动作的那个。当一个activity启动了另外一个，这个新的activity将压入栈中，它将成为正在运行中的 activity。前一个activity保留在栈中。当用户按下后退按键，当前的这个activity将中栈中弹出，而前面的那个activity恢复 成运行中状态。activity是和任务紧密联系的。因为在android系统中，为了保持用户体验，用户想做某件事情是以任务的结构作为逻辑，以应用的形式来表现的。而一个应用又可以包含很多的activity，所以就涉及到activity和task的关系问题。note：一个task由很多的activity组成，task也可以多个存在（可以理解为任务管理器中的可以看到的应用），有前台和后台之分。
- Standard(标准模式，默认的)。这种模式下，当Intent发送的时候，Activity总是被创建一个新的出来单独工作。想象一下，如果有发送10个撰写邮件的Intent，那么将有10个不同的Activity启动。
- singleTop.在自身task中启动activity。如果task中activity已存在，则清除回退栈中该activity上的任何activity，然后路由新intent给现有activity.它的表现几乎和standard模式一模一样，一个singleTop Activity 的实例可以无限多，唯一的区别是如果在当前（注意是当前任务）任务的栈顶已经有一个相同类型的Activity实例，Intent不会再创建一个Activity，而是通过onNewIntent()被发送到现有的Activity。这种启动模式的用例之一就是搜索功能。假设我们创建了一个搜索框，点击搜索的时候将导航到一个显示搜索结果列表的SearchActivity中，为了更好的用户体验，这个搜索框一般也会被放到SearchActivity中，这样用户想要再次搜索就不需要按返回键。想像一下，如果每次显示搜索结果的时候我们都启动一个新的activity，10次搜索10个activity，那样当我们想返回最初的那个activity的时候需要按10次返回。在singleTop模式下我们需要同时在onCreate() 和 onNewIntent()中处理发来的intent，以满足不同情况。
- singleTask.在自身task中启动activity。该activity是task中唯一的activity。如果任何其他activity从该task中启动，它们都各自启动到自己的task中。如果task中activity已存在，则直接路由新intent给现有activity。这种模式和standard以及singleTop有很大不同。singleTask模式的Activity只允许在系统中有一个实例。如果系统中已经有了一个实例，持有这个实例的任务将移动到栈顶部，同时intent将被通过onNewIntent()发送。如果没有，则会创建一个新的Activity并置放在合适（此处合适有2层意思，同一个应用和不同应用）的任务中。在同一个应用中的情况：如果系统中还没有singleTask的Activity，会新创建一个，并放在同一任务的栈顶。但是如果已经存在，singleTask Activity上面的所有Activity将以合适的方式自动销毁，让我们想要显示的Activity处于栈顶。同时Intent也会通过onNewIntent()方法发送到这个singleTask Activity；和其他应用一起工作的情况：一旦intent是从另外的应用发送过来，并且系统中也没有任何Activity的实例，则会创建一个新的任务，并且新的Activity被实例化放入该任务（或者说Activity栈顶)，假设已经有了一个Activity的实例，不管它是在哪个任务中，该任务中的Activity将被移到调用放的任务的Activity栈顶端。
这种模式的应用案例有邮件客户端的收件箱，这些Activity一般不会设计成拥有多个实例，singleTask可以满足。但是在使用这种模式的时候必须要明智，因为有些Activity会在用户不知情的情况下被销毁（因为为了使singleTask Activity变成栈顶元素，必须要销毁原来处于singleTask Activity之上的元素）。
- singleInstance
	跟singleTask一样，唯一的区别是持有该Activity的任务(Activity栈）只有Activity一个实例，即此singleInstance，无其他Activity。如果在应用A中启动了应用B中的singleInstance的Activity，当前显示singleInstance Activity后，如果按home键返回桌面后，再次进入应用A，不会显示应用B的singleInstance Activity。因为singleInstance Activity位于一个独立的任务中，该任务的Activity栈中只有一个singleInstance Activity。

#xml中消除重复代码
- 使用include标签，可以导入布局定义xml文件。include标签的目的是，将共通的布局定义提取到一个问题件中，然后通过include包含进来，这样减少代码量，而且可以减少修改量。
- 可以将共通的式样定义，提取到styles.xml中，然后在布局layout文件中，通过style="@style/xxxx"来引用。

# XML Drawable & 9-Patches
- XML Drawable。与像素无关。只需要放在drawable目录下即可。如下创建一个按钮的背景式样(drawable/button_shape_normal.xml)，该XML文件定义了一个圆角矩形。corner元素指定了圆角矩形的圆角半径，而gradient元素则指定了色彩渐变的方向以及起始颜色，其他可使用shape创建的各种图形可参考http://developer.android.com/guide/topics/resources/drawable-resource.html。然后在通过android:background = “@drawable/button_shape_normal”就可以引用到该式样了。
			<?xml version="1.0" encoding="utf-8"?>
			<shape xmlns:android="http://schemas.android.com/apk/res/android"
			  android:shape="rectangle" >
			  <corners android:radius="3dp" />
			  <gradient
			    android:angle="90"
			    android:endColor="#cccccc"
			    android:startColor="#acacac" />
			</shape>
			
- state list drawable。可根据关联View的不同状态显示不同的drawable(drawable/button-shape.xml），注意，需要有一个selector元素。下例子表示，当非按下状态时@drawable/button_shape_normal显示按钮式样，按下时按照button_shape_pressed显示式样。

			<?xml version="1.0" encoding="utf-8"?>
			<selector xmlns:android="http://schemas.android.com/apk/res/android">
			  <item android:drawable="@drawable/button_shape_normal" 
			   android:state_pressed="false"/>
			  <item android:drawable="@drawable/button_shape_pressed"
			    android:state_pressed="true"/>
			</selector>
- layer-list& inset
- 9-patch。9-patch可将图像分成3×3的网格，即由9部分或9 patch组成的网格。网格角落的patch不会被缩放，边缘部分的4个patch只按一个维度缩放，而中间部分则同时按两个维度缩放。9-patch图像和普通的png图像基本相同，但以下两点除外：9-patch图像文件名是以.9.png结尾的，图像边缘具有一个像素宽度的边框，用以指定9-patch图像的中间位置。边框像素绘制为黑线，以表明中间位置，边缘部分则用透明色表示。可使用任何图形编辑工具来创建一张9-patch图像，但使用Android SDK中自带的draw9patch工具要更方便些。不要让9-patch图像与其他图像发生冲突。例如，如同时存在名为window.9.png和window.png的两张图像，应用编译会失败。


#从网络获取数据(HTTP)
- 从网络上获得数据的实例代码如下。
 		
	 		URL url = new URL(urlSpec);
	        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
	
	        try {
	            ByteArrayOutputStream out = new ByteArrayOutputStream();
	            InputStream in = connection.getInputStream();//此时才会真正的去连接服务器端
	            
	            //所以只有在此处才能去判断HTTP的返回代码
	            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
	                return null;
	            }
	
	            int bytesRead = 0;
	            byte[] buffer = new byte[1024];
	            while ((bytesRead = in.read(buffer)) > 0) {//每次读取并写入1024个字节。
	                out.write(buffer, 0, bytesRead);
	            }
	            out.close();
	            return out.toByteArray();
	        } finally {
	            connection.disconnect();
	        }
- 获取访问网络的权限。在AndroidManifest.xml按如下设置。

		<uses-permission android:name="android.permission.INTERNET" />

- AsyncTask创建后台线程。实现下面doInBackground方法。网络访问比较耗时，不能在主线程中访问，否则会抛出出NetworkOnMainThreadException，需要在其他子线程中访问。
 主线程：它运行着所有用于更新UI的代码，其中包括响应activity的启动、按钮的点击等不同UI相关事件的代码。（由于响应的事件基本都与用户界面相关，主线程有时也叫做UI线程。）
 
		 protected Result doInBackground(Void... params) 
		 
AsyncTask提供有另一个可覆盖的onPostExecute(...)方法。onPostExecute(...)方法在doInBackground(...)方法执行完毕后才会运行，而且它是在主线程而非后台线程上运行的。因此，在该方法中更新UI比较安全。不要在非主线程中更新UI。
- Uri.Builder。使用Uri.Builder构建完整的网站API请求URL。便利类Uri.Builder可创建正确转义的参数化URL。Uri.Builder.appendQueryParameter(String,String)可自动转义查询字符串。

			 String url = Uri.parse(ENDPOINT).buildUpon()
			                    .appendQueryParameter("method", METHOD_GET_RECENT)
			                    .appendQueryParameter("api_key", API_KEY)
			                    .appendQueryParameter(PARAM_EXTRAS, EXTRA_SMALL_URL)
			                    .build().toString();
			
- XmlPullParser。通过该类可以解析XML文件。当通过请求获得了网站上的数据后，就可以通过该类来解析获得具体我们需要的数据了。

			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xmlString));//需要把被解析的数据设置到parser中。
            
            int eventType = parser.next();

        	while (eventType != XmlPullParser.END_DOCUMENT) {
            		if (eventType == XmlPullParser.START_TAG &&
               	 	XML_PHOTO.equals(parser.getName())) {
                		String id = parser.getAttributeValue(null, "id");
                		String caption = parser.getAttributeValue(null, "title");
                		String smallUrl = parser.getAttributeValue(null, EXTRA_SMALL_URL);
				}
            eventType = parser.next();
        }

	#GridView
	- 因为GridView无方便的GridFragment类可用，所以我们必须自己编码实现adapter的管理。得根据当前模型数据的不同状态，可对应设置GridView的adapter(GridView.setAdapter(。。。。))。我们应在onCreateView(...)方法中调用该方法，这样每次因设备旋转重新生成GridView视图时，可重新为其配置对应的adapter。另外，每次模型层对象发生改变时，也应该及时再次设置GridView的adapter来更新UI，如果是AsyncTask获取网站数据后，可以在AsyncTask.onPostExecute方法中再次调用设置adapter。

	#Looper,Handler,Message,HandlerThread
	- Android系统中，线程使用的收件箱叫做消息队列（message queue）。使用消息队列的线程叫做消息循环（message loop）。消息循环会不断循环(只要创建Handler，实现其适合的方法即可，应该是一个while(true)的运行的线程)检查队列上是否有新消息。消息循环由一个线程和一个looper组成。Looper对象管理着线程的消息队列。主线程也是一个消息循环，因此具有一个looper。主线程的所有工作都是由其looper完成的。looper不断从消息队列中抓取消息，然后完成消息指定的任务。
	- Looper。与MessageQueue和Handler关联。一个Looper可以有多个Handler。
	- Message。Message.sendToTarget()设置消息与Handler的关联，并将消息设置到Looper关联的MessageQueue队列的末尾。
	- Handler。完成实际的消息处理工作。Handler.post(Runnable r):使用r线程来处理主(UI)线程的更新。也可以通过Handler.sendMessage()发送一个消息给UI线程，让UI线程根据消息类型去执行更新UI。需要实现Handler.handleMessage方法来处理消息。Handler.obtainMessage()方法创建一个新的message（从message池中获取一个新的实例)。
	- MessageQueue。管理消息。
	- HandlerThread。是一个线程，拥有Looper实例,一般可以在非主线程中配合Handler，Looper,Message用来处理对耗时操作(如下载等)。Handy class for starting a new thread that has a looper. The looper can then be used to create handler classes. Note that start() must still be called.

#搜索
- 添加菜单项。menu目录下，添加item。
- 实现和调用Activity.onSearchRequested()。
- 配置可搜索的Activity。res/xml目录中新建searchable.xml文件，元素为searchable。
- 设置Android的SearchManager，activity能够处理搜索任务，并同时将搜索配置信息提供给它。SearchManager属于操作系统级别的服务，负责展现搜索对话框并管理搜索相关的交互。具体配位AndroidManifest.xml
		
		//当该Activity为栈顶时，使用该Activity，不会新建。
		android:launchMode = "singleTop"
     
        <intent-filter>
        	<action adnroid:name="android.intent.action.SEARCH"/>
        </intent-filter>
        //android:resource获取id，android:value获取定义的值
        <meta-data android:name="android.app.searchable" android:resource="@xml/searchable"/>
 - String query = intent.getStringExtra(SearchManager.QUERY)获取搜索框中的字符串。
 - SearchView。Honeycomb新增加了一个SearchView类。SearchView属于操作视图（action view），可内置在操作栏里。因此，不像对话框那样叠置在activity上，SearchView支持将搜索界面内嵌在activity的操作栏里在menu/item标签内，定义android:actionViewClass = "android.widget.SearchView"。在菜单XML中指定操作视图，相当于告诉Android“不要在操作栏对此菜单项使用常规视图部件，请使用指定的视图类。实例代码如下。
 
 				//通过搜索MenuItem的资源ID找到它， 然后调用getActionView()方法获得它的操作视图。
	 		MenuItem searchItem = menu.findItem(R.id.menu_item_search);
	        SearchView searchView = (SearchView)searchItem.getActionView();
	            // get the data from our searchable.xml as a SearchableInfo
	            //通过SearchManager取得搜索配置信息。SearchManager是负责处理所有搜索相关事宜的系统服务。
	            //有关搜索的全部信息，包括应该接收intent的activity名称以及所有searchable.xml中的信息，
	            //都存储在了SearchableInfo对象中。调用getSearchableInfo(ComponentName)方法可获得该对象。
	            SearchManager searchManager = (SearchManager)getActivity()
	                .getSystemService(Context.SEARCH_SERVICE);
	            ComponentName name = getActivity().getComponentName();
	            SearchableInfo searchInfo = searchManager.getSearchableInfo(name);
	            //调用setSearchableInfo(SearchableInfo)方法将相关信息通知给SearchView。
	            searchView.setSearchableInfo(searchInfo);
 
 取得SearchableInfo对象后，
#轻量级数据的持久化SharedPreferences
 - 要获得特定的SharedPreferences实例，可使用Context.getSharedPreferences (String，int)方法。
 - PreferenceManager.getDefaultSharedPreferences(Context)。在实际开发中，由于shared preferences共享于整个应用，我们通常并不太关心获取的特定实例是什么。所以我们最好使该方法，该方法会返回具有私有权限与默认名称的实例。
 - 写数据。通过调用SharedPreferences.edit()方法，可获取一个SharedPreferences.Editor 实例。它就是我们在SharedPreferences 中保存查询信息要使用的类.与FragmentTransaction的使用类似，利用SharedPreferences.Editor，我们可将一组数据操作放入一个事务中。如有一批数据更新操作，我们可在一个事务中批量进行数据存储写入操作。完成所有数据的变更准备后，调用SharedPreferences.Editor的commit()方法最终写入数据。这样，该SharedPreferences文件的其他用户即可看到写入的数据。
 
			 PreferenceManager.getDefaultSharedPreferences(this).edit()
			.putString(FlickrFetchr.PREF_SEARCH_QUERY, query)
			.commit();
                
 - 读数据。只需调用对应写入数据类型的方法即可，如SharedPreferences.getString(...)方法或者SharedPreferences.getInt(...)方法等。
        
#Service
服务就像Android应用的后台。用户无需关心后台发生的一切。即使前台(Activity)关闭，activity长时间停止运行，后台服务依然可以持续不断地执行工作任务。
-服务的生命周期。onCreate：在创建时调用。onStartCommand。startService方法启动服务时调用一次。onDestory，服务不需要时调用，通常是服务停止后。
- IntentService。该服务能够响应intent，该服务也是Context。使用该服务需要继承IntentService，需要实现onHandleIntent(Intent intent)接口方法，该接口方法能够响应Intent，服务的intent又称作命令（command）。每一条命令都要求服务完成某项具体的任务。根据服务的种类不同，服务执行命令的方式也不尽相同。IntentService执行Intent时，如果有收到多个Intent时，会把Intent放到命令队列中，IntentService逐个执行命令队列里的命令。接收到首个命令时，IntentService即完成启动，并触发一个后台线程，然后将命令放入队列。随后，IntentService继续按顺序执行每一条命令，并同时为每一条命令在后台线程上调用onHandleIntent(Intent)方法。新进命令总是放置在队列尾部。最后，执行完队列中全部命令后，服务也随即停止并被销毁。
- 使用服务必须在AndroidManifest.xml中声明。如下。

				<service android:name=".XXXXService"/>

- 启动Service。

				Intent i = new Intent(getActivity(),XXXXService.class);
				getActivity().startService(i);

#网络连接
- 首先要获取ConnectivityManager。
				
				ConnectivityManager cm  = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)

- 在Android旧版本系统中，getBackgroundDataSetting()返回是否允许网络数据。如果返回结果为false，表示不允许使用后台数据，否则表示允许。
- 在Android 4.0（Ice Cream Sandwich）中，后台数据设置直接会禁用网络。getActiveNetworkInfo()如果返回为空，则网络不可用。如果要使用该方法，必须在AndroidManifest.xml中设置权限。

		<uses-permission android:name ="android.permission.ACCESS_NETWORK_STATE">

#PendingIntent。
延迟的intent，主要用来在某个事件完成后执行特定的Action。PendingIntent包含了Intent及Context，所以就算Intent所属程序结束，PendingIntent依然有效，可以在其他程序中使用。它的执行不是立刻的，而Intent的执行立刻的。PendingIntent执行的操作实质上是参数传进来的Intent的操作，但是使用pendingIntent的目的在于它所包含的Intent的操作的执行是需要满足某些条件的。PendingIntent就是一个可以在满足一定条件下执行的Intent，它相比于Intent的优势在于自己携带有Context对象，这样他就不必依赖于某个activity才可以存在。
- getActivity(Context, int, Intent, int)。跳转到一个activity组件
- getBroadcast(Context, int, Intent, int)。打开一个广播组件
- getService(Context, int, Intent, int) 。打开一个服务组件。调用该方法获取PendingIntent时，我们告诉操作系统：“请记住，我需要使用startService(Intent)方法发送这个intent。”

#AlarmManager,AlarmManager是可以发送Intent的系统服务。
		//获取系统服务之AlarmManager
		AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE)
 		//按照设定的POLL_INTERVAL重复使用pi(PendingIntent)启动服务。
 		am.setRepeating(AlarmManager.RTC,System.currentTimeMills(),POLL_INTERVAL,pi)
		//取消服务
	 	am.cancel(pi)
 		
#NotificationManager。通知管理器--系统服务。
- NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)
- 使用PendingIntent显示通知和打开应用(Activity)
			
			Resources r = getResource();//获取资源管理实例
			PendingIntent pi  = PendingIntent.getActivity(this，0，new Intent(this,XXXXActivity.class),0)
			Noficaiton note = new //生成通知实例，并设置通知的图片，标题等。NoficationCompat.Builder(this).setTicker(r.getString(R.string.xxx).
			setSmallIncon(android.R.drawable.xxx).setContentTitle(r.getString(R.string.xxx).
			setContentIntent(pi).setAutoCancel(true).build();//使用setAutoCancel(true)设置方法，用户点击Notification消息后，可将该消息从消息抽屉中删除。
			NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)；
			mNotificationManager.notify(0,notification);//传入的整数参数是通知消息的标识符，在整个应用中该值应该是唯一的

#Broadcast Intent
- 继承android.content.BroadcastReceiver。
- 实现onReceive(Context context,Intent intent)接口方法，实现当收到Broadcast Intent时的动作。onReceive(Context,Intent)方法刚运行完，receiver就不存在了。onReceive(Context,Intent)方法同样运行在主线程上，因此不能在该方法内做一些耗时的重度任务，如网络连接或数据的永久存储等。当系统或应用发出广播时，将会扫描系统中的所有广播接收者，通过action匹配将广播发送给相应的接收者，接收者收到广播后将会产生一个广播接收者的实例，执行其中的onReceiver()这个方法；特别需要注意的是这个实例的生命周期只有10秒，如果10秒内没执行结束onReceiver()，系统将会报错。另外在onReceiver()执行完毕之后，该实例将会被销毁，所以不要在onReceiver()中执行耗时操作，也不会在里面创建子线程处理业务（因为可能子线程没处理完，接收者就被回收了）；正确的处理方法就是通过intent调用activity或者service处理业务。
- 静态注册。配置Broadcast Receiver。AndroidManifest.xml文件的配置如下。

		<uses-permission android:name = "android.permission.RECEIVE_BOOT_COMPLETED" />
		<receiver android:name="xxxxxReceiver">
			<intent-filter>
				//当系统启动完后，会发送该Intent。
				<action android:name="android.intent.action.BOOT_COMPLETED">		</intent-filter>
		</receiver>
在配置文件中完成声明后，即使应用当前并未运行，只要有匹配的broadcast intent的发来，broadcast receiver就会接收。一收到intent，broadcast receiver的onReceive(Context,Intent)方法即开始运行，然后broadcast receiver就会被销毁。
- 动态注册。如下实例代码。

		IntentFilter filter = new IntentFilter("com.net168.testBcr.intent.mybroadcastreceiver");
		MyBroadcastReceiver receiver = new MyBroadcastReceiver();
		//动态注册广播接收者
		registerReceiver(receiver, filter);
注意，在不需要的时候，必须取消广播接受者的动态注册。
			
			unregisterReceiver(receiver);
注册与取消注册，一般来说，都是onResume()/onPause()，onActivityCreated(...)/onActivityDestroyed()

- 静态注册的广播接收者一经安装就常驻在系统之中，不需要重新启动唤醒接收者；动态注册的广播接收者随着应用的生命周期，由registerReceiver开始监听，由unregisterReceiver撤销监听，另外需要注意的一点就是如果应用退出后，没有撤销已经注册的接收者应用应用将会报错。当广播接收者通过intent启动一个activity或者service时，如果intent中无法匹配到相应的组件。动态注册的广播接收者将会导致应用报错，而静态注册的广播接收者将不会有任何报错，因为自从应用安装完成后，广播接收者跟应用已经脱离了关系。
- 普通广播Intent。普通广播是完全异步的，可以在同一时刻（逻辑上）被所有接收者接收到，消息传递的效率比较高，并且无法中断广播的传播。
			Intent intent = new Intent();
			intent.setAction("com.net168.testBcr.intent.mybroadcastreceiver");
			intent.putExtra("data", "hello");
			//发送普通广播
			sendBroadcast(intent);
- 有序广播：发送有序广播后，广播接收者将按预先声明的优先级依次接收Broadcast。优先级高的优先接收到广播，而在其onReceiver()执行过程中，广播不会传播到下一个接收者，此时当前的广播接收者可以终止广播继续向下传播，也可以将intent中的数据进行修改设置，然后将其传播到下一个广播接收者。

			sendOrderedBroadcast(intent, null);
			public void onReceive(Context arg0, Intent intent) {
			　　//获取上一个广播的bundle数据
			　　//false：不创建新的Bundle，直接返回上一个广播接受者生产的Bundle，
			　　//如果上一个广播接受者未生成Bundle则返回空。true：前一个广播没有结果时创建新的Bundle；
			　　Bundle bundle = getResultExtras(true);			　　bundle.putString("key", "168168");
			　　//将bundle数据放入广播中传给下一个广播接收者
			　　setResultExtras(bundle);
			　　//终止广播传给下一个广播接收者
			　　abortBroadcast();
			}
在有序广播中，我们可以在前一个广播接收者将处理好的数据传送给后面的广播接收者，也可以调用abortBroadcast()来终结广播的传播。

- 静态广播接收的处理器是由PackageManagerService负责，当手机启动或者新安装了应用的时候，PackageManagerService会扫描手机中所有已安装的APP应用，将AndroidManifest.xml中有关注册广播的信息解析出来，存储至一个全局静态变量当中。需要注意的是：1、PackageManagerService扫描目录的顺序如下：system/framework  ->  system/app  ->  vendor/app  ->  data/app  -> drm/app-private.2、当处于同一目录下时：按照file.list()的返回顺序。
- 动态广播接收的处理器是由ActivityManagerService负责，当APP的服务或者进程起来之后，执行了注册广播接收的代码逻辑，即进行加载，最后会存储在一个另外的全局静态变量中。需要注意的是：1、 这个并非是一成不变的，当程序被杀死之后，已注册的动态广播接收器也会被移出全局变量，直到下次程序启动，再进行动态广播的注册，当然这里面的顺序也已经变更了一次。2、这里也并没完整的进行广播的排序，只记录的注册的先后顺序，并未有结合优先级的处理。
- 广播发出的时候，广播接收者接收的顺序为：如果广播为有序广播，那么会将动态广播处理器和静态广播处理器合并在一起处理广播的消息，最终确定广播接收的顺序：1、优先级高的先接收 2、同优先级的动静态广播接收器，动态优先于静态 3、同优先级的动态广播接收器或者同优先级的静态广播接收器；分静态：先扫描的大于后扫描的，动态：先注册的大于后注册的。
- 广播发出的时候，广播接收者接收的顺序为：当广播为普通广播时，有如下的接收顺序： 1、无视优先级，动态广播接收器优先于静态广播接收器 2、同优先级的动态广播接收器或者同优先级的静态广播接收器；分静态：先扫描的大于后扫描的，动态：先注册的大于后注册的。

#WebView打开网页
- 在布局文件中定义webview
- onCreateView方法中，实例化WebView。（WebView)v.findViewById(R.id.webView);
- WebView.getSettings().setJavaScriptEnabled(true).启用JavaScript。JavaScript默认是禁用的。虽然不一定总是需要启用它，但Flickr网站需要。启用JavaScript后，Android Lint会提示警告信息（担心跨网站的脚本攻击），因此需禁止Lint的警告。@SuppressLint("setJavaScriptEnabled")
- WebView.setWebViewClient(new WebViewClient() {public boolean shouldOVerrideUrlLoading(WebView view,String url){ return false;}。配置WebViewClient。WebViewClient是响应渲染(网页显示)事件接口。通过提供自己实现的WebViewClient，可响应各种渲染事件。必须覆盖它的shouldOverrideUrlLoading(WebView,String)默认方法。当有新的URL加载到WebView（譬说点击某个链接），该方法会决定下一步的行动。如返回true值，意即“不要处理这个URL，我自己来，即使用应用中的WebView打开。”如返回false值，使用系统的浏览器显示网页内容。
- WebView.loadurl()
- WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度。

		WebView.setWebChromeClien(new WebChromeClient(){
			public void onProgressChanged(WebView webView,int progress){
			....
			}
			public void onReceivedTitle(WebView webView,String title){
			.....
			}
			}

#定制自己的视图
- 选择超类。对于简单定制视图而言，View是一个空白画布，因此是最常见的选择。而对于聚合定制视图，我们应选择合适的布局类。
- 继承选定的超类，并至少覆盖一个超类构造方法。或者创建自己的构造方法，并在其中调用超类的构造方法。覆盖其他关键方法，以定制视图行为。
- 覆盖其他关键方法，以定制视图行为。

				//Touch事件的回调接口，EVENT.getX()，EVENT.getY()可以得到当前所触摸的坐标。
				public boolean onTouchEvent(MotionEvent event)
				
	Event有如下事件。
	EVENT| 描述
	------------ | -------------
	ACTION_DOWN |用户手指触摸到屏幕	ACTION_MOVE |用户在屏幕上移动手指	ACTION_UP |用户手指离开屏幕	ACTION_CANCEL |父视图拦截了触摸事件
	
			//绘制的回调接口
			protected void onDraw(Canvas canvas)
Canvas：画布。Canvas类具有我们需要的所有绘制操作。其方法可决定绘制的位置及图形，例如线条、圆形、字词、矩形等。
Paint类决定如何进行绘制操作。其方法可指定绘制图形的特征，例如是否填充图形、使用什么字体绘制、线条是什么颜色等。

- 定义布局文件(xml)，根元素需要定制视图类的类全名。我们必须使用BoxDrawingView的全路径类名，这样布局inflater才能够找到它。布局inflater解析布局XML文件，并按视图定义创建View实例。如果元素名不是全路径类名，布局inflater会转而在android.view和android.widget包中寻找目标。如果目标视图类放置在其他包中，布局inflater将无法找到目标并最终导致应用崩溃。因此，对于android.view和android.widget包以外的定制视图类，必须指定它们的全路径类名。
		
		<com.bignerdranch.android.draganddraw.BoxDrawingView
		    xmlns:android="http://schemas.android.com/apk/res/android"
		    android:layout_width="match_parent"
		    android:layout_height="match_parent"
		    />
		  
- View.invalidate()会使视图重新绘制自己。


