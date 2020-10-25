package com.mc.phonelive.activity.shop;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bspopupwindow.model.TreeVO;
import com.bspopupwindow.view.BSPopupWindowsTitle;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.adapter.JfmxAdapter;
import com.mc.phonelive.bean.ScoreItemBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *提现明细888888
 *
 *
 */

public class DianPuTiXianListActivity extends AbsActivity {

    private RecyclerView mRv;
    private JfmxAdapter mAdapter;
    private View lines;
    private LinearLayout ll_xz;
    private TextView tv_date;
    private BSPopupWindowsTitle mBsPopupWindowsTitle;
    private SmartRefreshLayout srl;
    private TextView tv_noinfo;
    private int page=1;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_score_list;
    }

    @Override
    protected void main() {
        setTitle("提现记录");
        setBarModel(true);
        mRv = (RecyclerView)findViewById(R.id.rv);
        lines = findViewById(R.id.lines);
        tv_noinfo = findViewById(R.id.tv_noinfo);
        srl = findViewById(R.id.srl);
        ll_xz = findViewById(R.id.ll_xz);
        tv_date = findViewById(R.id.tv_date);
        mAdapter=new JfmxAdapter();
        mRv.setLayoutManager(Utils.getLvManager(mContext));
        mRv.setAdapter(mAdapter);
        int crm=DateUtils.getCurrentMonth()+1;
        String lsm="";
        if(crm<10){
            lsm="0"+crm;
        }else{
            lsm=crm+"";
        }
        String lastDate=DateUtils.getCurrentYear()+"-"+lsm;
        setInfo(lastDate);
        tv_date.setText(lastDate);
        ll_xz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTime();
            }
        });
        srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(1000);
                setInfo(tv_date.getText().toString());
            }
        });
    }
    BSPopupWindowsTitle.TreeCallBack callback = new BSPopupWindowsTitle.TreeCallBack() {

        @Override
        public void callback(TreeVO vo) {
            if (vo.getLevel() == 1) {
                // 审批一级菜单
                if (com.bspopupwindow.utils.Utils.isNotEmpty(vo) && com.bspopupwindow.utils.Utils.isNotEmpty(vo.getId())) {
//                    mCateId = vo.getId() + "";
                    if (vo.getId() == 0) {//全部
//                        mTypeTv01.setText("全部");
//                        mTypeTv01.setTextColor(mContext.getResources().getColor(R.color.textColor));
//                        mTypeImg01.setImageResource(R.mipmap.b_icon);
                    } else {
//                        mTypeTv01.setText(vo.getName());
//                        mTypeTv01.setTextColor(mContext.getResources().getColor(R.color.redlive));
//                        mTypeImg01.setImageResource(R.mipmap.b_icon_choise);
                    }

                }
            }
        }
    };
    private void showTime( ) {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2015,1,1);
        Calendar entDate = Calendar.getInstance();
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        SimpleDateFormat formatter_year = new SimpleDateFormat("yyyy ");
        String year_str = formatter_year.format(curDate);
        int year_int = (int) Double.parseDouble(year_str);


        SimpleDateFormat formatter_mouth = new SimpleDateFormat("MM ");
        String mouth_str = formatter_mouth.format(curDate);
        int mouth_int = (int) Double.parseDouble(mouth_str);
//        DateUtils.getCurrentYear()
        entDate.set(DateUtils.getCurrentYear(),DateUtils.getCurrentMonth(),DateUtils.getCurrentDay());
        new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String tm= DateUtils.ConverToTimeYH(date);
                String []strSp=tm.split(":");
                tv_date.setText(tm);
                setInfo(tm);
            }
        }).setDate(entDate).setRangDate(startDate,entDate).setType(new boolean[]{true,true,false,false,false,false}).build().show();


    }

    private void setInfo(String tm) {
        HttpUtil.getListTiXian("1",tm,page,new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    String num=obj.getString("integra");
                    ArrayList<ScoreItemBean> nlist = (ArrayList<ScoreItemBean>) JSON.parseArray(obj.getString("list"), ScoreItemBean.class);
                    if(Utils.isNotEmpty(nlist)){
                        mAdapter.setNewData(nlist);
                    }else{
                        mAdapter.setNewData(null);
                    }
                }
            }
            @Override
            public void onFinish() {
                super.onFinish();
                if(Utils.isNotEmpty(mAdapter)&&Utils.isNotEmpty(mAdapter.getData())){
                    tv_noinfo.setVisibility(View.GONE);
                }else{
                    tv_noinfo.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    private ArrayList<TreeVO> initPopDowns() {
        ArrayList<TreeVO> list = new ArrayList<TreeVO>();
        TreeVO allTreeVo = new TreeVO();
        allTreeVo.setId(0);
        allTreeVo.setParentId(0);
        allTreeVo.setName("全部");
        allTreeVo.setLevel(1);
        allTreeVo.setHaschild(false);
        allTreeVo.setDepartmentid("-1");
        allTreeVo.setDname("全部");
        list.add(allTreeVo);
//        if (com.bspopupwindow.utils.Utils.isNotEmpty(mTypelist)) {
//            for (int i = 0; i < mTypelist.size(); i++) {
//                TreeVO parentVo = new TreeVO();
//                parentVo.setId(Integer.parseInt(mTypelist.get(i).getId()));
//                parentVo.setParentId(0);
//                parentVo.setName(mTypelist.get(i).getCate_name());
//                parentVo.setParentSerachId(i + "");
//                parentVo.setLevel(1);
//                list.add(parentVo);
//                parentVo.setHaschild(false);
//
//            }
//        }
            for (int i = 1; i < 13; i++) {
                TreeVO parentVo = new TreeVO();
                parentVo.setId(i);
                parentVo.setParentId(0);
                if(i<10){
                    parentVo.setName("2018-0"+i);
                }else{
                    parentVo.setName("2018-"+i);
                }

                parentVo.setParentSerachId(i + "");
                parentVo.setLevel(1);
                list.add(parentVo);
                parentVo.setHaschild(false);

        }

        return list;
    }
}
