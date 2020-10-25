package com.mc.phonelive.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.activity.LocationActivity;
import com.mc.phonelive.bean.AddressVO;
import com.mc.phonelive.bean.CityBean;
import com.mc.phonelive.bean.TxLocationPoiBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.utils.CommentUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * created by WWL on 2019/4/26 0026:11
 * 新增收货地址
 */
public class AddressAddActivity extends AbsActivity implements View.OnClickListener {
    @BindView(R.id.username_edit)
    EditText usernameEdit;
    @BindView(R.id.phone_edit)
    EditText phoneEdit;
    @BindView(R.id.Address_name_tv)
    TextView AddressNameTv;
    @BindView(R.id.address_content_edit)
    EditText addressContentEdit;
    @BindView(R.id.submit_address_tv)
    TextView submitAddressTv;
    @BindView(R.id.Address_name_choise_tv)
    TextView AddressNameChoiseTv;
    private TextView mTitleName;
    AddressVO.DataBean.InfoBean info = new AddressVO.DataBean.InfoBean();
    String mUid = "";
    String mToken = "";
    //    LocationInfo info = null;
    private boolean isUpData = false;//判断是否是新增地址还是编辑地址
    private String mAddressId = "";

    @Override
    protected int getLayoutId() {
        return R.layout.address_add_layout;
    }

    @Override
    protected void main() {
        setBarModel(true);
        mTitleName = findViewById(R.id.titleView);
        mTitleName.setText("添加地址");
        usernameEdit = findViewById(R.id.username_edit);
        phoneEdit = findViewById(R.id.phone_edit);
        AddressNameTv = findViewById(R.id.Address_name_tv);
        addressContentEdit = findViewById(R.id.address_content_edit);
        submitAddressTv = findViewById(R.id.submit_address_tv);
        findViewById(R.id.btn_back).setOnClickListener(this);
        boolean registered = EventBus.getDefault().isRegistered(this);
        if (!registered) {
            EventBus.getDefault().register(this);
        }

        AddressVO.DataBean.InfoBean dataBean = (AddressVO.DataBean.InfoBean) this.getIntent().getSerializableExtra("address");
        if (dataBean != null) {
            isUpData = true;
            this.info = dataBean;
            usernameEdit.setText(info.getReal_name());
            phoneEdit.setText(info.getPhone());
            AddressNameTv.setText(info.getDistrict());
            addressContentEdit.setText(info.getDetail());
            mAddressId = dataBean.getId();

        }

        mUid = AppConfig.getInstance().getUid();
        mToken = AppConfig.getInstance().getToken();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
    @OnClick({R.id.Address_name_tv, R.id.submit_address_tv,R.id.Address_name_choise_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Address_name_tv:
                Intent intent = new Intent(AddressAddActivity.this, LocationActivity.class);
                startActivityForResult(intent, 200);
                break;
            case R.id.Address_name_choise_tv:
                    choisePickerViewData();
                break;
            case R.id.submit_address_tv:
                if (DataSafeUtils.isEmpty(usernameEdit.getText().toString().trim())) {
                    Toast.makeText(this, "收货人姓名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (DataSafeUtils.isEmpty(phoneEdit.getText().toString().trim())) {
                    Toast.makeText(this, "收货人电话不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (DataSafeUtils.isEmpty(addressContentEdit.getText().toString().trim())) {
                    Toast.makeText(this, "详细地址不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!DataSafeUtils.isEmpty(info)) {
                    setAddressSubmit();
                } else {
                    Toast.makeText(this, "请选择收货地址", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void choisePickerViewData() {
        try {
            String str = CommentUtil.getAssetJson("city", AddressAddActivity.this);
            //将读出的字符串转换成JSONobject
            new org.json.JSONObject(str);
            CityBean object = com.alibaba.fastjson.JSONObject.parseObject(str, CityBean.class);
            List<CityBean.DataBean.ListBean> options1Items =new ArrayList<>();
            options1Items = object.getData().getList();
            // 条件选择器
            List<CityBean.DataBean.ListBean> finalOptions1Items = options1Items;
            OptionsPickerView pvOptions = new OptionsPickerBuilder(AddressAddActivity.this, new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                    //返回的分别是三个级别的选中位置
                    String tx = finalOptions1Items.get(options1).getName()
                            + finalOptions1Items.get(options1).getCity_list().get(option2).getName()
                            + finalOptions1Items.get(options1).getCity_list().get(option2).getArea_list().get(options3).getName();
                    AddressNameChoiseTv.setText(tx);
                }
            }).build();
            pvOptions.setPicker(options1Items);
            pvOptions.show();
//            initDatas(cityModelDataList);
            //获取JSONObject中的数组数据
        } catch (JSONException e) {
        }

//        List<CityBean.DataBean.ListBean.CityListBean> options2Items =new ArrayList<>();
//        List<CityBean.DataBean.ListBean.CityListBean.AreaListBean> options3Items =new ArrayList<>();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 200) {
//            double lat = data.getDoubleExtra(Constants.LAT, 0);
//            double lng = data.getDoubleExtra(Constants.LNG, 0);
//           String addressname= data.getStringExtra("address");
            TxLocationPoiBean location = (TxLocationPoiBean) data.getSerializableExtra("location");
            if (location != null) {
                AddressNameTv.setText(location.getTitle() + "");
                addressContentEdit.setText(location.getAddress());
//                info.setLatitude(location.getLocation().getLat()+"");
//                info.setLongitude(location.getLocation().getLng()+"");
                info.setDistrict(location.getTitle());
                info.setDetail(location.getAddress());
            }
        }
    }

    /**
     * 提交
     */
    private void setAddressSubmit() {

        HttpUtil.AddAddress(mAddressId, usernameEdit.getText().toString() + "", phoneEdit.getText().toString() + "", AddressNameTv.getText().toString(), addressContentEdit.getText().toString(), "1", isUpData, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    EventBus.getDefault().post(new EventBusModel("address_refresh"));
                    ToastUtil.show(msg);
                    AddressAddActivity.this.finish();
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEventBus(EventBusModel eventBusModel) {
        String code = eventBusModel.getCode();
        switch (code) {
            case "searchaddress"://登录或者注册成功

                break;

        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        boolean registered = EventBus.getDefault().isRegistered(this);
        if (registered) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onClick(View v) {
        this.finish();
    }


}
