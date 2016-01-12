package com.wang.criminalintent;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

public class DatePickerFragment extends DialogFragment {

	public static final String EXTRA_DATE = "DatePickerFragment";
	
	private Date date;
	
	public Dialog onCreateDialog(Bundle saveInstanceState) {
		date = (Date)getArguments().getSerializable(EXTRA_DATE);
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		int year = ca.get(Calendar.YEAR);
		int month = ca.get(Calendar.MONDAY);
		int day = ca.get(Calendar.DAY_OF_MONTH);
		
		View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_date, null);
		DatePicker dp = (DatePicker)view.findViewById(R.id.dialog_date_picker);
		dp.init(year, month, day, new OnDateChangedListener(){
			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				date = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();
				System.out.print("onDateChanged"+ date);
				getArguments().putSerializable(EXTRA_DATE,date);
			}
			
		});
		return new AlertDialog.Builder(this.getActivity()).setView(view).setTitle(R.string.date_picker_title)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						sendResult(Activity.RESULT_OK);
					}
					
				}).create();
		//null是一个实现Dialog-Interface.OnClickListener接口的对象
		//Android有3种可用于对话框的按钮：positive按钮、negative按钮以及neutral按钮。
		//用户点击positive按钮接受对话框展现信息。如同一对话框上放置多个按钮，按钮的类型与命名决定着它们在对话框上显示的位置
	}
	
	public static DatePickerFragment newInstance(Date date){
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_DATE, date);
		DatePickerFragment dpf = new DatePickerFragment();
		dpf.setArguments(args);
		return dpf;
		
	}
	private void sendResult(int resultCode){
		if(getTargetFragment()==null){
			return;
		}
		Intent intent = new Intent();
		System.out.print("sendResult"+date);
		intent.putExtra(EXTRA_DATE, date);
		getTargetFragment().onActivityResult(this.getTargetRequestCode(), resultCode, intent);
//		处理由同一activity托管的两个fragment间的数据返回时，可借用Fragment.onActivityResult(...)方法。
		//因此，直接调用目标fragment的Fragment.onActivityResult(...)方法，
//		即可实现数据的回传。
	}

}
