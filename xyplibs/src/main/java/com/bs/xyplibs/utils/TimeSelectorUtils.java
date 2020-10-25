package com.bs.xyplibs.utils;

import android.content.Context;
import android.view.View;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2018/8/21.
 */

public class TimeSelectorUtils {

    private   static  String time=DateUtils.getCurrentDate();
    public static String getTime(){

        long time=System.currentTimeMillis();//获取系统时间的10位的时间戳
        String  str=String.valueOf(time);

        return str;

    }
    public  static String showTime(Context context) {
        TimePickerView pvTime = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                time = DateUtils.ConverToString(date);

            }
        }).build();
        pvTime.show();
        return time;
    }
    public static  String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }
}
