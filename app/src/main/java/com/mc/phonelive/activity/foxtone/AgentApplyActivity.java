package com.mc.phonelive.activity.foxtone;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.bean.foxtone.CityBean01;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 申请城市代理
 */
public class AgentApplyActivity extends AbsActivity {
    @BindView(R.id.btn_2)
    RadioButton btn2;
    @BindView(R.id.btn_3)
    RadioButton btn3;
    @BindView(R.id.address_name)
    TextView addressName;
    @BindView(R.id.apply_submit)
    TextView applySubmit;
    private String mType="2";
    List<CityBean01.InfoBean> mList = new ArrayList<>();
    private ArrayList<String> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private String mProvinceid = "";//省id
    private String mCityId = "";//城市id
    private String mDistrictId = "";//区id
    @Override
    protected int getLayoutId() {
        return R.layout.agentapply_view;
    }

    @Override
    protected void main() {

        initHttpData();
    }

    private void initHttpData() {
        HttpUtil.getUserCity(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                mList = JSON.parseArray(Arrays.toString(info), CityBean01.InfoBean.class);
                if (mList!=null && mList.size()>0){
                    choisePickerViewData(mList);
                }
            }
        });
    }

    private void choisePickerViewData(List<CityBean01.InfoBean> lists) {

        for (int i = 0; i < lists.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）
            for (int c = 0; c < lists.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = lists.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市
                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表
                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (lists.get(i).getCityList().get(c).getAreaList() == null
                        || lists.get(i).getCityList().get(c).getAreaList().size() == 0) {
                    City_AreaList.add("");
                } else {
                    for (int k = 0; k < lists.get(i).getCityList().get(c).getAreaList().size(); k++) {
                        City_AreaList.add(lists.get(i).getCityList().get(c).getAreaList().get(k).getName());
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }
            options2Items.add(CityList);
            options3Items.add(Province_AreaList);
            options1Items.add(lists.get(i).getName());
        }




    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({ R.id.btn_2, R.id.btn_3, R.id.address_name, R.id.apply_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_2:
                btn2.setChecked(true);
                mType="1";
                mProvinceid="";
                mCityId="";
                mDistrictId="";
                addressName.setText("");
                break;
            case R.id.btn_3:
                btn3.setChecked(true);
                mType="2";
                mProvinceid="";
                mCityId="";
                mDistrictId="";
                addressName.setText("");
                break;
            case R.id.address_name:
                if (mType.equals("0")){
                    OptionsPickerProvince();
                }else if (mType.equals("1")){
                    OptionsPickerCity();
                }else if (mType.equals("2")){
                    OptionsPickerArea();
                }
                break;
            case R.id.apply_submit:
                if (DataSafeUtils.isEmpty(mType)){
                    ToastUtil.show("请选择区域");
                    return;
                }
                if (DataSafeUtils.isEmpty(mProvinceid)){
                    ToastUtil.show("请选择地区");
                    return;
                }
                AgentSubmit();
                break;
        }
    }

    private void AgentSubmit() {
        HttpUtil.getAgentApply(mType, mProvinceid, mCityId, mDistrictId, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                ToastUtil.show(msg);
                if (code==0){
                    AgentApplyActivity.this.finish();
                }
            }
        });
    }

    /**
     * 省代理选择
     */
    private void OptionsPickerProvince() {

        OptionsPickerView pvOptions = new OptionsPickerBuilder(AgentApplyActivity.this, new OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = mList.get(options1).getName();
                mProvinceid = mList.get(options1).getCode();
                addressName.setText(tx);
            }
        }).setCancelColor(getResources().getColor(R.color.textColor2)).setSubmitColor(getResources().getColor(R.color.redlive))
                .setDecorView(getWindow().getDecorView().findViewById(android.R.id.content)).build();
        pvOptions.setPicker(options1Items);
        pvOptions.show();

    }
    /**
     * 市代理选择
     */
    private void OptionsPickerCity() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(AgentApplyActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = mList.get(options1).getName()
                        + mList.get(options1).getCityList().get(option2).getName();
                mProvinceid = mList.get(options1).getCode();
                mCityId = mList.get(options1).getCityList().get(option2).getCode();
                addressName.setText(tx);
            }
        }).setCancelColor(getResources().getColor(R.color.textColor2)).setSubmitColor(getResources().getColor(R.color.redlive))
                .setDecorView(getWindow().getDecorView().findViewById(android.R.id.content)).build();
        pvOptions.setPicker(options1Items, options2Items);
        pvOptions.show();

    }
    /**
     * 区代理选择
     */
    private void OptionsPickerArea() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(AgentApplyActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = mList.get(options1).getName()
                        + mList.get(options1).getCityList().get(option2).getName()
                        + mList.get(options1).getCityList().get(option2).getAreaList().get(options3).getName();
                mProvinceid = mList.get(options1).getCode();
                mCityId = mList.get(options1).getCityList().get(option2).getCode();
                mDistrictId = mList.get(options1).getCityList().get(option2).getAreaList().get(options3).getCode();
                addressName.setText(tx);
            }
        }).setCancelColor(getResources().getColor(R.color.textColor2)).setSubmitColor(getResources().getColor(R.color.redlive))
                .setDecorView(getWindow().getDecorView().findViewById(android.R.id.content)).build();
        pvOptions.setPicker(options1Items, options2Items, options3Items);
        pvOptions.show();

    }
}
