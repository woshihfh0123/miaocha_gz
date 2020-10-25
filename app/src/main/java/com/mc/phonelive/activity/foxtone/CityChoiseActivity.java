package com.mc.phonelive.activity.foxtone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.bean.CityBean;
import com.mc.phonelive.bean.UserBean;
import com.mc.phonelive.bean.foxtone.CityBean01;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.CommentUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.ToastUtil;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 选择地区
 */
public class CityChoiseActivity extends AbsActivity {
    @BindView(R.id.choise_province)
    TextView choiseProvince;
    @BindView(R.id.choise_city)
    TextView choiseCity;
    @BindView(R.id.choise_area)
    TextView choiseArea;
    @BindView(R.id.choise_submit)
    TextView choiseSubmit;

    private String mType="";
    List<CityBean01.InfoBean> mList = new ArrayList<>();
    private ArrayList<String> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private String mProvinceid = "";//省id
    private String mCityId = "";//城市id
    private String mDistrictId = "";//区id
    private String mAreaName="";//地区名称
    @Override
    protected int getLayoutId() {
        return R.layout.choise_city_layout;
    }

    @Override
    protected void main() {
        setTitle("选择地区");
        mAreaName=getIntent().getStringExtra(Constants.CITYNAME);
        choiseArea.setText(mAreaName);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.choise_province, R.id.choise_city, R.id.choise_area, R.id.choise_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.choise_province:
                OptionsPickerProvince();
                break;
            case R.id.choise_city:
                OptionsPickerCity();
                break;
            case R.id.choise_area:
//                OptionsPickerArea();
                choisePickerViewData();
                break;
            case R.id.choise_submit:
                if (DataSafeUtils.isEmpty(mDistrictId)){
                    ToastUtil.show("请选择地区");
                    return;
                }
                AgentSubmit();
                break;
        }
    }


    /**
     * 提交城市
     */
    private void AgentSubmit() {
        HttpUtil.updateFields("{\"city\":\"" + mAreaName + "\"}", new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    if (info.length > 0) {
                        JSONObject obj = JSON.parseObject(info[0]);
                        ToastUtil.show(obj.getString("msg"));
                        UserBean u = AppConfig.getInstance().getUserBean();
                        if (u != null) {
                            u.setCity(mAreaName);
                        }
                        Intent intent = getIntent();
                        intent.putExtra(Constants.CITYNAME, mAreaName);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                } else {
                    ToastUtil.show(msg);
                }
            }
        });
//        HttpUtil.setUserArea( mProvinceid, mCityId, mDistrictId, new HttpCallback() {
//            @Override
//            public void onSuccess(int code, String msg, String[] info) {
//                ToastUtil.show(msg);
//                if (code==0){
//                    UserBean u = AppConfig.getInstance().getUserBean();
//                    if (u != null) {
//                        u.setArea(mAreaName);
//                    }
//                    Intent intent = getIntent();
//                    intent.putExtra(Constants.CITYNAME, mAreaName);
//                    setResult(RESULT_OK, intent);
//                    finish();
//                }
//            }
//        });
    }

    private void choisePickerViewData() {

        try {
            String str = CommentUtil.getAssetJson("city", CityChoiseActivity.this);
            //将读出的字符串转换成JSONobject
            new org.json.JSONObject(str);
            CityBean object = JSONObject.parseObject(str, CityBean.class);
            List<CityBean.DataBean.ListBean> options1 = new ArrayList<>();
            options1 = object.getData().getList();
            // 条件选择器
            List<CityBean.DataBean.ListBean> finalOptions1Items = options1;

            for (int i = 0; i < finalOptions1Items.size(); i++) {//遍历省份
                ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
                ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）
                for (int c = 0; c < finalOptions1Items.get(i).getCity_list().size(); c++) {//遍历该省份的所有城市
                    String CityName = finalOptions1Items.get(i).getCity_list().get(c).getName();
                    CityList.add(CityName);//添加城市
                    ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表
                    //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                    if (finalOptions1Items.get(i).getCity_list().get(c).getArea_list() == null
                            || finalOptions1Items.get(i).getCity_list().get(c).getArea_list().size() == 0) {
                        City_AreaList.add("");
                    } else {
                        for (int k = 0; k < finalOptions1Items.get(i).getCity_list().get(c).getArea_list().size(); k++) {
                            City_AreaList.add(finalOptions1Items.get(i).getCity_list().get(c).getArea_list().get(k).getName());
                        }
                    }
                    Province_AreaList.add(City_AreaList);//添加该省所有地区数据
                }
                options2Items.add(CityList);
                options3Items.add(Province_AreaList);
                options1Items.add(finalOptions1Items.get(i).getName());
            }


            OptionsPickerView pvOptions = new OptionsPickerBuilder(CityChoiseActivity.this, new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2, int options3, View v) {
                    //返回的分别是三个级别的选中位置
                    String tx = finalOptions1Items.get(options1).getName()
                            + finalOptions1Items.get(options1).getCity_list().get(option2).getName()
                            + finalOptions1Items.get(options1).getCity_list().get(option2).getArea_list().get(options3).getName();
                    mProvinceid = finalOptions1Items.get(options1).getCode();
                    mCityId = finalOptions1Items.get(options1).getCity_list().get(option2).getCode();
                    mDistrictId = finalOptions1Items.get(options1).getCity_list().get(option2).getArea_list().get(options3).getCode();
                    Log.v("tags","mProvinceid="+mProvinceid+"---------mCityId="+mCityId+"---------mDistrictId="+mDistrictId);
                    choiseArea.setText(tx);
                    mAreaName = finalOptions1Items.get(options1).getCity_list().get(option2).getArea_list().get(options3).getName();
                }
            }).setCancelColor(getResources().getColor(R.color.textColor2)).setSubmitColor(getResources().getColor(R.color.redlive))
                    .setDecorView(getWindow().getDecorView().findViewById(android.R.id.content)).build();
            pvOptions.setPicker(options1Items, options2Items, options3Items);
            pvOptions.show();
            //获取JSONObject中的数组数据
        } catch (JSONException e) {
        }


    }

    /**
     * 省代理选择
     */
    private void OptionsPickerProvince() {

        OptionsPickerView pvOptions = new OptionsPickerBuilder(CityChoiseActivity.this, new OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = mList.get(options1).getName();
                mProvinceid = mList.get(options1).getCode();
                choiseProvince.setText(tx);
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
        OptionsPickerView pvOptions = new OptionsPickerBuilder(CityChoiseActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = mList.get(options1).getName()
                        + mList.get(options1).getCityList().get(option2).getName();
                mProvinceid = mList.get(options1).getCode();
//                mCityId = mList.get(options1).getCityList().get(option2).getCode();
                choiseCity.setText(tx);
            }
        }).setCancelColor(getResources().getColor(R.color.textColor2)).setSubmitColor(getResources().getColor(R.color.redlive))
                .setDecorView(getWindow().getDecorView().findViewById(android.R.id.content)).build();
        pvOptions.setPicker(options1Items, options2Items);
        pvOptions.show();

    }
    /**
     * 区选择
     */
    private void OptionsPickerArea() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(CityChoiseActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = mList.get(options1).getName()
                        + mList.get(options1).getCityList().get(option2).getName()
                        + mList.get(options1).getCityList().get(option2).getAreaList().get(options3).getName();
                mProvinceid = mList.get(options1).getCode();
                mCityId = mList.get(options1).getCityList().get(option2).getCode();
                mDistrictId = mList.get(options1).getCityList().get(option2).getAreaList().get(options3).getCode();
                choiseArea.setText(tx);
            }
        }).setCancelColor(getResources().getColor(R.color.textColor2)).setSubmitColor(getResources().getColor(R.color.redlive))
                .setDecorView(getWindow().getDecorView().findViewById(android.R.id.content)).build();
        pvOptions.setPicker(options1Items, options2Items, options3Items);
        pvOptions.show();

    }

}
