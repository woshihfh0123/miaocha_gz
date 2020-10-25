package com.mc.phonelive.activity.shop;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.adapter.shop.FoodOrderListAdapter;
import com.mc.phonelive.bean.AddressVO;
import com.mc.phonelive.bean.FoodOrderVo;
import com.mc.phonelive.bean.ScoreInfoBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpConsts;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.DialogUitl;
import com.mc.phonelive.utils.GlideUtils;
import com.mc.phonelive.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *
 * 确认兑换下单
 * 8888888
 */
public class ScoreGoodsOrderActivity extends AbsActivity {
    @BindView(R.id.order_address_name)
    TextView orderAddressName;
    @BindView(R.id.miv)
    ImageView miv;
    @BindView(R.id.tv_goods_name)
    TextView tv_goods_name;
    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.tv_nubs)
    TextView tv_nubs;
    @BindView(R.id.tv_kyjf)
    TextView tv_kyjf;
    @BindView(R.id.tv_jfz)
    TextView tv_jfz;
    @BindView(R.id.order_address_username)
    TextView orderAddressUsername;
    @BindView(R.id.order_address_userphone)
    TextView orderAddressUserphone;
    @BindView(R.id.ll_ljdh)
    LinearLayout ll_ljdh;
    @BindView(R.id.address_choise_layout)
    RelativeLayout addressChoiseLayout;
    @BindView(R.id.order_choise_address)
    TextView orderChoiseAddress;
    @BindView(R.id.order_recyclerview)
    RecyclerView orderRecyclerview;
//    @BindView(R.id.goods_num)
//    TextView goodsNum;
//    @BindView(R.id.heji)
//    TextView heji;
//    @BindView(R.id.shoppingPrise)
//    TextView shoppingPrise;
//    @BindView(R.id.settlement)
//    TextView settlement;
    FoodOrderVo.InfoBean mData = new FoodOrderVo.InfoBean();
    FoodOrderListAdapter mFoodListAdapter;//订单列表
    private String mGoodsId = "";
    private String mCartNum = "";
    private String mCartIds = "";
    private String mRemark = "";//备注
    private String mAddressId = "";
    private String mGoodsList = "";
    private String mTotalPrice = "";
    private String goodsId="";
    private String product_attrs="";
    private String goodsnum="";
    private String totalprice="";

    @Override
    protected int getLayoutId() {
        return R.layout.scoregood_order_layout;
    }

    @Override
    protected void main() {
        super.main();
        boolean registered = EventBus.getDefault().isRegistered(this);
        if (!registered) {
            EventBus.getDefault().register(this);
        }
        setBarModel(true);
        setFoodAdapter(mData.getStore());
        String []info= getIntent().getStringArrayExtra("info");
        if(Utils.isNotEmpty(info)){
            JSONObject obj = JSON.parseObject(info[0]);
            ScoreInfoBean scoreInfoBean=JSON.parseObject(obj.getString("info"), ScoreInfoBean.class);
            tv_goods_name.setText(scoreInfoBean.getTitle());

            tv_price.setText(scoreInfoBean.getPrice());
            tv_nubs.setText("x"+scoreInfoBean.getGoodsnum());
            goodsId=scoreInfoBean.getId();
            goodsnum=scoreInfoBean.getGoodsnum();
            product_attrs=scoreInfoBean.getGoods_attr();
//            totalprice=scoreInfoBean.getPrice();
            tv_kyjf.setText(scoreInfoBean.getScore());
            if(Utils.isNotEmpty(scoreInfoBean.getPrice())){

                double prs=Double.parseDouble(scoreInfoBean.getPrice());
                int numb=Integer.parseInt(scoreInfoBean.getGoodsnum());
                double conprs=prs*numb;
                tv_jfz.setText(conprs+"");
                totalprice=conprs+"";
            }


            String img=scoreInfoBean.getThumb();
            if(Utils.isNotEmpty(img)){
                GlideUtils.loadImage(mContext,img,miv);
            }

        }


//        String cartids = this.getIntent().getStringExtra("cart_ids");
//        if (!DataSafeUtils.isEmpty(cartids)) {
//            this.mCartIds = cartids;
//        }
        String goodsid = this.getIntent().getStringExtra("goods_id");
        if (!DataSafeUtils.isEmpty(goodsid)) {
            this.mGoodsId = goodsid;
            mCartNum = "1";
        }
//        initHttpData();
        getDefaultAddress();
    }

    private void getDefaultAddress() {
        HttpUtil.getAddressList(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (info.length > 0) {
                    if (info[0].length() > 0) {
                        AddressVO.DataBean.InfoBean infoBean = JSON.parseObject(info[0], AddressVO.DataBean.InfoBean.class);
                        Log.e("DDDD:",infoBean.getDistrict());
                        addressChoiseLayout.setVisibility(View.VISIBLE);
                        orderChoiseAddress.setVisibility(View.GONE);
                        orderAddressName.setText("" + infoBean.getDetail());
                        orderAddressUsername.setText("" + infoBean.getReal_name());
                        orderAddressUserphone.setText("" + infoBean.getPhone());
                        mAddressId = ""+infoBean.getId();
                    }
                } else {
                    addressChoiseLayout.setVisibility(View.GONE);
                    orderChoiseAddress.setVisibility(View.VISIBLE);

                }
            }
        });

    }


    private void setFoodAdapter(List<FoodOrderVo.InfoBean.StoreBean> shoplist) {
        mFoodListAdapter = new FoodOrderListAdapter(R.layout.order_food_item_layout, shoplist);
        orderRecyclerview.setAdapter(mFoodListAdapter);
        orderRecyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
    }


    @OnClick({R.id.address_choise_layout, R.id.address_layout,R.id.ll_ljdh})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.address_choise_layout:
            case R.id.address_layout:
                startActivity(new Intent(mContext, MyAddressListActivity.class));
                break;
            case R.id.ll_ljdh:
                setBuy();
                break;
        }
    }
    private void setBuy() {
        HttpUtil.payOrder(goodsId, product_attrs, goodsnum,mAddressId,totalprice, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                ToastUtil.show(msg);
                finish();
//                startActivity(new Intent(mContext,ScoreGoodsOrderActivity.class));
            }
        });
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
                        Intent intent = new Intent(ScoreGoodsOrderActivity.this, PayActivity.class);
                        intent.putExtra("out_trade_no", out_trade_no);
                        intent.putExtra("order_id", order_id);
                        intent.putExtra("money", mData.getStore().get(0).getStore_total());
                        startActivity(intent);
                        ScoreGoodsOrderActivity.this.finish();
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
