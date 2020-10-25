package com.mc.phonelive.activity.shop;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.adapter.shop.FoodOrderListAdapter;
import com.mc.phonelive.bean.AddressVO;
import com.mc.phonelive.bean.FoodOrderVo;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpConsts;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.utils.ButtonUtils;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.DialogUitl;
import com.mc.phonelive.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *
 * 确认下单888888888888888
 */
public class GoodsOrderActivity extends AbsActivity {
    @BindView(R.id.order_address_name)
    TextView orderAddressName;
    @BindView(R.id.order_address_username)
    TextView orderAddressUsername;
    @BindView(R.id.order_address_userphone)
    TextView orderAddressUserphone;
    @BindView(R.id.address_choise_layout)
    RelativeLayout addressChoiseLayout;
    @BindView(R.id.order_choise_address)
    TextView orderChoiseAddress;
    @BindView(R.id.order_recyclerview)
    RecyclerView orderRecyclerview;
    @BindView(R.id.goods_num)
    TextView goodsNum;
    @BindView(R.id.heji)
    TextView heji;
    @BindView(R.id.shoppingPrise)
    TextView shoppingPrise;
    @BindView(R.id.settlement)
    TextView settlement;
    FoodOrderVo.InfoBean mData = new FoodOrderVo.InfoBean();
    FoodOrderListAdapter mFoodListAdapter;//订单列表
    private String mGoodsId = "";
    private String mCartNum = "";
    private String mCartIds = "";
    private String mRemark = "";//备注
    private String mAddressId = "";
    private String mGoodsList = "";
    private String mTotalPrice = "";

    @Override
    protected int getLayoutId() {
        return R.layout.good_order_layout;
    }

    @Override
    protected void main() {
        super.main();
        boolean registered = EventBus.getDefault().isRegistered(this);
        if (!registered) {
            EventBus.getDefault().register(this);
        }

        setFoodAdapter(mData.getStore());


        String cartids = this.getIntent().getStringExtra("cart_ids");
        if (!DataSafeUtils.isEmpty(cartids)) {
            this.mCartIds = cartids;
        }
        String goodsid = this.getIntent().getStringExtra("goods_id");
        if (!DataSafeUtils.isEmpty(goodsid)) {
            this.mGoodsId = goodsid;
            mCartNum = "1";
        }
        initHttpData();
    }

    /**
     * 获取接口数据
     */
    private void initHttpData() {
        HttpUtil.SureOrder(mGoodsId, mCartNum, mCartIds, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                   mData= JSON.toJavaObject(obj, FoodOrderVo.InfoBean.class);
                    if (!DataSafeUtils.isEmpty(mData.getStore()) && mData.getStore().size() > 0) {
                        goodsNum.setText("共" + mData.getStore().get(0).getStore_num() + "件");
                        shoppingPrise.setText("￥" + mData.getStore().get(0).getStore_total() + "");
                        if (!DataSafeUtils.isEmpty(mData.getStore().get(0).getStore_total()))
                            mTotalPrice = mData.getStore().get(0).getStore_total();

                        if (mFoodListAdapter != null) {
                            mFoodListAdapter.setNewData(mData.getStore());
                        }
                        JSONArray json = new JSONArray();
                        org.json.JSONObject jo = new org.json.JSONObject();
                        try {
                            jo.put("store_id", mData.getStore().get(0).getStore_id());
                            jo.put("store_num", mData.getStore().get(0).getStore_num());
                            jo.put("store_freight", mData.getStore().get(0).getStore_freight());
                            jo.put("store_total", mData.getStore().get(0).getStore_total());
                            jo.put("ifcart", mData.getStore().get(0).getIfcart());
                            JSONArray goodjson = new JSONArray();
                            for (int i = 0; i < mData.getStore().get(0).getGoods().size(); i++) {
                                org.json.JSONObject jo1 = new org.json.JSONObject();
                                jo1.put("goods_id", mData.getStore().get(0).getGoods().get(i).getId());
                                jo1.put("cart_num", mData.getStore().get(0).getGoods().get(i).getCart_num());
                                goodjson.put(jo1);
                            }
                            jo.put("goodsinfo", goodjson);
                            json.put(jo);
                            Log.v("tags", "json==" + json);
                            mGoodsList = json + "";
                        } catch (JSONException e) {
                        }

                    }

                    if (!DataSafeUtils.isEmpty(mData.getAddressinfo())) {
                        addressChoiseLayout.setVisibility(View.VISIBLE);
                        orderChoiseAddress.setVisibility(View.GONE);
                        orderAddressName.setText("" + mData.getAddressinfo().getDetail());
                        orderAddressUsername.setText("" + mData.getAddressinfo().getReal_name());
                        orderAddressUserphone.setText("" + mData.getAddressinfo().getPhone());
                        mAddressId = mData.getAddressinfo().getId();
                    } else {
                        addressChoiseLayout.setVisibility(View.GONE);
                        orderChoiseAddress.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public boolean showLoadingDialog() {
                return true;
            }

            @Override
            public Dialog createLoadingDialog() {
                return DialogUitl.loadingDialog(mContext);
            }
        });
    }

    private void setFoodAdapter(List<FoodOrderVo.InfoBean.StoreBean> shoplist) {
        mFoodListAdapter = new FoodOrderListAdapter(R.layout.order_food_item_layout, shoplist);
        orderRecyclerview.setAdapter(mFoodListAdapter);
        orderRecyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
    }


    @OnClick({R.id.address_choise_layout, R.id.address_layout, R.id.settlement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.address_choise_layout:
            case R.id.address_layout:
                startActivity(new Intent(mContext, MyAddressListActivity.class));
                break;
            case R.id.settlement:
                if (!ButtonUtils.isFastDoubleClick02(R.id.settlement)) {
                    setLement();
                }
                break;
        }
    }


    /**
     * 创建订单
     */
    private void setLement() {
        mRemark = mFoodListAdapter.getData().get(0).getRemark();
        Log.v("tags", "beizhu=" + mRemark);
        HttpUtil.OrderCreate(mAddressId, mGoodsList, mRemark,"", new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    String out_trade_no = obj.getString("out_trade_no");
                    String order_id = obj.getString("order_id");
                    Log.v("tags", "out_trade_no=" + out_trade_no);
                    if (code == 0) {
                        Intent intent = new Intent(GoodsOrderActivity.this, PayActivity.class);
                        intent.putExtra("out_trade_no", out_trade_no);
                        intent.putExtra("order_id", order_id);
                        intent.putExtra("money", mData.getStore().get(0).getStore_total());
                        startActivity(intent);
                        GoodsOrderActivity.this.finish();
                    }
                }else{
                    ToastUtil.show(msg);
                }
            }

            @Override
            public boolean showLoadingDialog() {
                return true;
            }

            @Override
            public Dialog createLoadingDialog() {
                return DialogUitl.loadingDialog(mContext);
            }
        });
//        showDialog();
//        HashMap<String, String> map = new HashMap<>();
//        map.put("uid", mUid + "");
//        map.put("token", mToken + "");
//        map.put("orderKey", mData.getOrderKey() + "");
//        map.put("addressId", mAddressId + "");
//        map.put("couponId", mCouponId + "");
//        if (mOrderType.equals("2") && !DataSafeUtils.isEmpty(mGroupId)) {
//            map.put("combinationId", mGroupId + "");
//        } else if (mOrderType.equals("0") && !DataSafeUtils.isEmpty(mSpikeId)) {
//            map.put("seckill_id", mSpikeId + "");
//        }
//        map.put("mark", beizhuEdit.getText().toString().trim() + "");
//        map.put("flag", mFlag);
//        map.put("pinkId", mPinkId + "");//
//        if (mFlag.equals("3")) {//新零售批发
//            map.put("pifa_type", "1");//(1:商品代售)
//        }
//        if (mFlag.equals("2")) {//新零售购买
//            map.put("payType", pay_type + "");//支付方式(1:用购物券  2:在线支付--用钱)
//        }
//        HttpUtils.POST(ConstantUrl.ORDERCREATE, map, FoodOrderCreateVO.class, new Callback<FoodOrderCreateVO>() {
//            @Override
//            public void onStart() {
//
//            }
//
//            @Override
//            public void onSucceed(String json, String code, final FoodOrderCreateVO createVO) {
//                mOrderId = createVO.getData().getOut_trade_no();
//                OrderCrateVO crateVO = new OrderCrateVO();
//                OrderCrateVO.DataBean data = new OrderCrateVO.DataBean();
//                data.setOut_trade_no(createVO.getData().getOut_trade_no());
//                data.setOrder_id(createVO.getData().getOrder_id());
//                data.setmType("1");
//                crateVO.setData(data);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("order", crateVO);
//
//        Intent intent1 = null;
//        intent1 = new Intent(GoodsOrderActivity.this, PayActivity.class);
//        startActivity(intent1);
//        removeActivity(FoodOrderActivity.this);
//    }
//            }
//
//            @Override
//            public void onOtherStatus(String json, String code) {
//
//            }
//
//            @Override
//            public void onFailed(Throwable e) {
//                dismissDialog();
//            }
//
//            @Override
//            public void onFinish() {
//                dismissDialog();
//            }
//        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateAllSelected(EventBusModel eventBusModel) {

        String code = eventBusModel.getCode();

        switch (code) {
            case "remark"://备注信息
                String remark = (String) eventBusModel.getObject();
                String shopId = (String) eventBusModel.getSecondObject();
                for (int i = 0; i < mFoodListAdapter.getData().size(); i++) {
                    if (mFoodListAdapter.getData().get(i).getStore_id().equals(shopId)) {
                        mFoodListAdapter.getData().get(i).setRemark(remark);
                    }
                }
                break;
            case "FoodOrder_refresh":
                AddressVO.DataBean.InfoBean addressData = (AddressVO.DataBean.InfoBean) eventBusModel.getObject();
                addressChoiseLayout.setVisibility(View.VISIBLE);
                orderChoiseAddress.setVisibility(View.GONE);
                orderAddressName.setText("" + addressData.getDetail());
                orderAddressUsername.setText("" + addressData.getReal_name());
                orderAddressUserphone.setText("" + addressData.getPhone());
                mAddressId = addressData.getId();
                break;
        }

    }

    @Override
    protected void onDestroy01() {
        boolean isRegist = EventBus.getDefault().isRegistered(this);
        if (isRegist) {
            EventBus.getDefault().unregister(this);
        }
        HttpUtil.cancel(HttpConsts.ORDERSUREORDER);
    }
}
