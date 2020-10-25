package com.bs.xyplibs.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;


import com.bs.xyplibs.R;

import java.util.Calendar;

public class DateTimePickerNew extends Activity {
	private LayoutInflater inflater = null;
	private WheelView year;
	private WheelView month;
	private WheelView day;
	private WheelView hour;
	private WheelView mins;
	private LinearLayout date_time_picker_new_date_ll,
			date_time_picker_new_time_ll;
	private Button bt, cancel;
	private String ReceiveShow = "";
	private int show = -1;
	private String flag = "";
	private int minYear = 2016;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.date_time_picker_new);
		setFinishOnTouchOutside(true);
		InitLayoutComponent();// 初始化组件
		ReceiveShow();// 接收显示
		getDataPick();// 设置数据

		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String m1 = "", d1 = "";
				int m = month.getCurrentItem() + 1;
				int d = day.getCurrentItem() + 1;
				if (m < 10) {
					m1 = "0" + m;
				} else {
					m1 = "" + m;
				}
				if (d < 10) {
					d1 = "0" + d;
				} else {
					d1 = "" + d;
				}
				String date = year.getCurrentItem() + minYear+"-"+m1;
//				String date = (year.getCurrentItem() + minYear) + "-" + (m1)
//						+ "-" + (d1);
//				String time = hour.getCurrentItem() + ":"
//						+ mins.getCurrentItem();

				Intent intent = new Intent("String_year_month");
//				intent.putExtra("flag", flag);
				intent.putExtra("DateTime", date);
				sendBroadcast(intent);
				finish();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void InitLayoutComponent() {
		bt = (Button) findViewById(R.id.set);
		cancel = (Button) findViewById(R.id.cancel);
		date_time_picker_new_date_ll = (LinearLayout) findViewById(R.id.date_time_picker_new_date_ll);
		date_time_picker_new_time_ll = (LinearLayout) findViewById(R.id.date_time_picker_new_time_ll);
	}

	// 接收显示
	private void ReceiveShow() {
		Intent intent = getIntent();
		flag = intent.getStringExtra("flag");
		ReceiveShow = intent.getStringExtra("show");
		if (ReceiveShow != null && ReceiveShow.equals("date")) {
			date_time_picker_new_time_ll.setVisibility(View.GONE);
		} else if (ReceiveShow != null && ReceiveShow.equals("time")) {
			date_time_picker_new_date_ll.setVisibility(View.GONE);
		} else {

		}
	}

	/**
	 * 
	 * @return
	 */
	private void getDataPick() {
		Calendar c = Calendar.getInstance();
		int curYear = c.get(Calendar.YEAR);
		int curMonth = c.get(Calendar.MONTH) + 1;// 通过Calendar算出的月数要+1
		int curDate = c.get(Calendar.DATE);

		// 日期
		year = (WheelView) findViewById(R.id.year);
		// year.setAdapter(new NumericWheelAdapter(1950, curYear));
		minYear = curYear - 5;
		year.setAdapter(new NumericWheelAdapter(minYear, 2099));
//		year.setLabel(getResources().getString(R.string.date_nian));
		year.setLabel("年");
		year.setCyclic(true);
		year.addScrollingListener(scrollListener);

		month = (WheelView) findViewById(R.id.month);
		month.setAdapter(new NumericWheelAdapter(1, 12));
//		month.setLabel(getResources().getString(R.string.date_yue));
		month.setLabel("月");
		month.setCyclic(true);
		month.addScrollingListener(scrollListener);

		day = (WheelView) findViewById(R.id.day);
		initDay(curYear, curMonth);
//		day.setLabel(getResources().getString(R.string.date_ri));
		day.setLabel("日");
		day.setCyclic(true);

		year.setCurrentItem(curYear - minYear);
		month.setCurrentItem(curMonth - 1);
		day.setCurrentItem(curDate - 1);

		// 时间
		hour = (WheelView) findViewById(R.id.hour);
		hour.setAdapter(new NumericWheelAdapter(0, 23));
		hour.setLabel("时");
		hour.setCyclic(true);
		mins = (WheelView) findViewById(R.id.mins);
		mins.setAdapter(new NumericWheelAdapter(0, 59));
		mins.setLabel("分");
		mins.setCyclic(true);

		hour.setCurrentItem(8);
		mins.setCurrentItem(30);

	}

	OnWheelScrollListener scrollListener = new OnWheelScrollListener() {

		@Override
		public void onScrollingStarted(WheelView wheel) {

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
			// TODO Auto-generated method stub
			int n_year = year.getCurrentItem() + 1950;
			int n_month = month.getCurrentItem() + 1;
			initDay(n_year, n_month);
		}
	};

	/**
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	private int getDay(int year, int month) {
		int day = 30;
		boolean flag = false;
		switch (year % 4) {
		case 0:
			flag = true;
			break;
		default:
			flag = false;
			break;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			day = flag ? 29 : 28;
			break;
		default:
			day = 30;
			break;
		}
		return day;
	}

	/**
	 */
	private void initDay(int arg1, int arg2) {
		day.setAdapter(new NumericWheelAdapter(1, getDay(arg1, arg2), "%02d"));
	}
}
