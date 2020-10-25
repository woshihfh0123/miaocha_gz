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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.adapter.YhqSetAdapter;
import com.mc.phonelive.adapter.shop.FoodOrderListAdapter;
import com.mc.phonelive.adapter.shop.MakeOrderListAdapter;
import com.mc.phonelive.bean.AddressVO;
import com.mc.phonelive.bean.FoodOrderVo;
import com.mc.phonelive.bean.MakeOrderBean;
import com.mc.phonelive.bean.PayOrderBean;
import com.mc.phonelive.bean.YhqSelBean;
import com.mc.phonelive.bean.pay.PayResult;
import com.mc.phonelive.dialog.PayOkDialog;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpConsts;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.httpnet.PayCallback;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.DialogUitl;
import com.mc.phonelive.utils.DisplayUtils;
import com.mc.phonelive.utils.GlideUtils;
import com.mc.phonelive.utils.PayUtis;
import com.mc.phonelive.utils.ToastUtil;
import com.rey.material.app.BottomSheetDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *
 * 商品确认下单
 * 8888888
 */
public class PayGoodsOrderActivity extends AbsActivity {
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
    @BindView(R.id.tv_money)
    TextView tv_money;
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
    MakeOrderListAdapter makeOrderAdapter;
    private String mGoodsId = "";
    private String mCartNum = "";
    private String mCartIds = "";
    private String mRemark = "";//备注
    private String mAddressId = "";
    private String mGoodsList = "";
    private String mTotalPrice = "";
    private String storeId="";
    List<PayOrderBean.GoodsBean> goodsList;
    private String goodsId="";
    private String postage="";
    private String product_attrs="";
    private String goodsnum="";
    private String totalprice="";
    private String defaultBuy="";
    private TextView tv_shop_name;
    private RecyclerView rv_cart;
    private TextView tv_yhj;
    private LinearLayout ll_zfb;
    private LinearLayout ll_wx;
    private ImageView iv_wx;
    private ImageView iv_zfb;
    private String pay_type="";
    private String coupons_id="";
    private View yhq_view;
    private LinearLayout ll_selector_yhq;
    private List<YhqSelBean> yhqList;
    private String givescore="";
    private double setMoney=0;
    private TextView tv_gg;
    private String price="";

    private String isCart= "";//是否为购物车下单
    private String isMS = "" ;

    @Override
    protected int getLayoutId() {
        return R.layout.pay_order_layout;
    }

    @Override
    protected void main() {
        super.main();
        boolean registered = EventBus.getDefault().isRegistered(this);
        if (!registered) {
            EventBus.getDefault().register(this);
        }
        setBarModel(true);
        tv_shop_name=findViewById(R.id.tv_shop_name);
        yhq_view=findViewById(R.id.yhq_view);
        ll_wx=findViewById(R.id.ll_wx);
        tv_gg=findViewById(R.id.tv_gg);
        tv_yhj=findViewById(R.id.tv_yhj);
        ll_selector_yhq=findViewById(R.id.ll_selector_yhq);
        ll_zfb=findViewById(R.id.ll_zfb);
        iv_wx=findViewById(R.id.iv_wx);
        iv_zfb=findViewById(R.id.iv_zfb);
        setFoodAdapter(mData.getStore());
        String goodsid = this.getIntent().getStringExtra("goods_id");
        String isMMS = this.getIntent().getStringExtra("isMs");

        if (!DataSafeUtils.isEmpty(isMMS)) {
            isMS = isMMS;
        }
        if (!DataSafeUtils.isEmpty(goodsid)) {
            this.mGoodsId = goodsid;
            mCartNum = "1";

            mGoodsId=getIntent().getStringExtra("goods_id");
            product_attrs=getIntent().getStringExtra("product_attrs");
            defaultBuy=getIntent().getStringExtra("goods_num");
            tv_gg.setText(product_attrs+"");
            isCart = "";
        }else{
            mCartIds = this.getIntent().getStringExtra("cart_ids");
            isCart = "1";
        }
//        initHttpData();
        getDefaultAddress();



        getInfo();
        ll_zfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_zfb.setImageResource(R.drawable.check_y);
                iv_wx.setImageResource(R.drawable.check_u);
                pay_type="1";
            }
        });
        ll_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_zfb.setImageResource(R.drawable.check_u);
                iv_wx.setImageResource(R.drawable.check_y);
                pay_type="2";
            }
        });
        ll_selector_yhq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectorPop();
            }
        });
    }

    private void showSelectorPop() {//3规格，2，添加仓库，0，加入采购单 1，立即下单
        BottomSheetDialog mDialog = new BottomSheetDialog(mContext);
        View dialogView = View.inflate(mContext,R.layout.selector_yhq, null);
        TextView tv_ljxd=dialogView.findViewById(R.id.tv_ljxd);
        int popHeight = DisplayUtils.getScreenHeight(mContext) * 3 / 4;
        RecyclerView rv=dialogView.findViewById(R.id.rv);
        YhqSetAdapter tpAdapter=new YhqSetAdapter();
        rv.setLayoutManager(Utils.getLvManager(mContext));
        rv.setAdapter(tpAdapter);
        if(Utils.isNotEmpty(yhqList)){
            tpAdapter.setNewData(yhqList);
        }
        tpAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view.getId()==R.id.tv_lq){
                    coupons_id=tpAdapter.getData().get(position).getId();
                    tv_yhj.setText(tpAdapter.getData().get(position).getName());
                    mDialog.dismiss();
                    double jqmoney=Double.parseDouble(tpAdapter.getData().get(position).getPrice());
                    DecimalFormat df = new DecimalFormat("#0.00");
                    double zzmoney=setMoney-jqmoney;
                    tv_money.setText(String.format("%.2f", zzmoney)+"");
//                    tpAdapter.getData().get(position).setIs_get("1");
//                    tpAdapter.notifyItemChanged(position);
//                    lqYhq(tpAdapter.getData().get(position).getId());
                }
            }
        });
        mDialog.contentView(dialogView)
                .heightParam(popHeight)
                .inDuration(500)
                .cancelable(true)
                .show();
        tv_ljxd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//判断事件//3规格，2，添加仓库，0，加入采购单 1，立即下单
                mDialog.dismiss();

            }
        });
    }

    private void lqYhq(String yid) {
        HttpUtil.getYhqLq(yid, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                Log.v("tags", Arrays.toString(info) + "-----info");

            }

            @Override
            public boolean showLoadingDialog() {
                return true;
            }

            @Override
            public Dialog createLoadingDialog() {
                return DialogUitl.loadingDialog(PayGoodsOrderActivity.this);
            }
        });
    }
    /**
     *   intent.putExtra("goods_id",mGoodsId);
     *         intent.putExtra("goods_num",defaultBuy+"");
     *         intent.putExtra("product_attrs",product_attrs);
     * @return
     */

    private void getInfo() {
        if (!DataSafeUtils.isEmpty(mCartIds)){
            HttpUtil.setCartPayOrder(mCartIds, new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    if(Utils.isNotEmpty(info)){
                        JSONObject obj = JSON.parseObject(info[0]);
                        List<PayOrderBean> storeInfo=JSON.parseArray(obj.getString("store"), PayOrderBean.class);
//                        yhqList=JSON.parseArray(obj.getString("list"), YhqSelBean.class);
                        yhqList =  storeInfo.get(0).getList();
                        if(Utils.isNotEmpty(yhqList)){
                            ll_selector_yhq.setVisibility(View.VISIBLE);
                            yhq_view.setVisibility(View.VISIBLE);

                        }else{
                            ll_selector_yhq.setVisibility(View.GONE);
                            yhq_view.setVisibility(View.INVISIBLE);
                        }
                        product_attrs=obj.getString("goods_attr");
                        tv_nubs.setText("x"+defaultBuy);
                        if(Utils.isNotEmpty(storeInfo.get(0).getGoods())){
                            PayOrderBean.GoodsBean scoreInfoBean = storeInfo.get(0).getGoods().get(0);
                            tv_goods_name.setText(scoreInfoBean.getGoods_title());
                            price = scoreInfoBean.getPrice();
                            tv_price.setText("￥"+scoreInfoBean.getPrice());

                            tv_shop_name.setText(storeInfo.get(0).getStore_name());
                            goodsId=scoreInfoBean.getId();
                            storeId = scoreInfoBean.getStore_id();
                            goodsList = new ArrayList<>();
                            goodsList.addAll(storeInfo.get(0).getGoods());
                            postage = storeInfo.get(0).getStore_freight();
                            goodsnum=scoreInfoBean.getCart_num();
                            if(Utils.isNotEmpty(scoreInfoBean.getGoods_image_url())){
                                GlideUtils.loadImage(mContext,scoreInfoBean.getGoods_image_url(),miv);
                            }else{
                                miv.setImageResource(R.drawable.default_img);
                            }
                            tv_jfz.setText("共"+storeInfo.get(0).getStore_num()+"项，实付：￥");
                            if(Utils.isNotEmpty(storeInfo.get(0).getStore_total())){
                                setMoney=Double.parseDouble(storeInfo.get(0).getStore_total());
                            }
                            if(Utils.isNotEmpty(storeInfo.get(0).getGoods())){
                                double dj=Double.parseDouble(storeInfo.get(0).getGoods().get(0).getPrice());
                                int nubs=Integer.parseInt(storeInfo.get(0).getGoods().get(0).getCart_num());
                                double money=dj*nubs;
                                tv_money.setText(money+"");
                            }
                            setMakeOrderAdapter(storeInfo);
//            totalprice=scoreInfoBean.getPrice();
//                        tv_kyjf.setText(scoreInfoBean.getScore());
//                        if(Utils.isNotEmpty(scoreInfoBean.getPrice())){
//
//                            double prs=Double.parseDouble(scoreInfoBean.getPrice());
//                            int numb=Integer.parseInt(scoreInfoBean.getGoodsnum());
//                            double conprs=prs*numb;
//                            tv_jfz.setText(conprs+"");
//                            totalprice=conprs+"";
//                        }
                        }


                    }

                }
            });
        }else{
            HttpUtil.setPayOrder(mGoodsId, product_attrs, defaultBuy, isMS,new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    if(Utils.isNotEmpty(info)){
                        JSONObject obj = JSON.parseObject(info[0]);
                        List<PayOrderBean> storeInfo=JSON.parseArray(obj.getString("store"), PayOrderBean.class);
//                        yhqList=JSON.parseArray(obj.getString("list"), YhqSelBean.class);
                        yhqList =  storeInfo.get(0).getList();
                        if(Utils.isNotEmpty(yhqList)){
                            ll_selector_yhq.setVisibility(View.VISIBLE);
                            yhq_view.setVisibility(View.VISIBLE);
                        }else{
                            ll_selector_yhq.setVisibility(View.GONE);
                            yhq_view.setVisibility(View.INVISIBLE);
                        }
                        product_attrs=obj.getString("goods_attr");
                        tv_nubs.setText("x"+defaultBuy);
                        if(Utils.isNotEmpty(storeInfo.get(0).getGoods())){
                            PayOrderBean.GoodsBean scoreInfoBean = storeInfo.get(0).getGoods().get(0);
                            tv_goods_name.setText(scoreInfoBean.getGoods_title());
                            price = scoreInfoBean.getPrice();
                            tv_price.setText("￥"+scoreInfoBean.getPrice());

                            tv_shop_name.setText(storeInfo.get(0).getStore_name());
                            storeId = scoreInfoBean.getStore_id();
                            goodsList = new ArrayList<>();
                            goodsList.addAll(storeInfo.get(0).getGoods());
                            goodsId=scoreInfoBean.getId();
                            postage = storeInfo.get(0).getStore_freight();
                            goodsnum=scoreInfoBean.getCart_num();
                            if(Utils.isNotEmpty(scoreInfoBean.getGoods_image_url())){
                                GlideUtils.loadImage(mContext,scoreInfoBean.getGoods_image_url(),miv);
                            }else{
                                miv.setImageResource(R.drawable.default_img);
                            }
                            tv_jfz.setText("共"+storeInfo.get(0).getStore_num()+"项，实付：￥");
                            if(Utils.isNotEmpty(storeInfo.get(0).getStore_total())){
                                setMoney=Double.parseDouble(storeInfo.get(0).getStore_total());
                            }
                            if(Utils.isNotEmpty(storeInfo.get(0).getGoods())){
                                double dj=Double.parseDouble(storeInfo.get(0).getGoods().get(0).getPrice());
                                int nubs=Integer.parseInt(storeInfo.get(0).getGoods().get(0).getCart_num());
                                double money=dj*nubs;
                                tv_money.setText(money+"");
                            }
                            setMakeOrderAdapter(storeInfo);
//            totalprice=scoreInfoBean.getPrice();
//                        tv_kyjf.setText(scoreInfoBean.getScore());
//                        if(Utils.isNotEmpty(scoreInfoBean.getPrice())){
//
//                            double prs=Double.parseDouble(scoreInfoBean.getPrice());
//                            int numb=Integer.parseInt(scoreInfoBean.getGoodsnum());
//                            double conprs=prs*numb;
//                            tv_jfz.setText(conprs+"");
//                            totalprice=conprs+"";
//                        }
                        }


                    }
                }
            });
        }

    }
    public void pay(String mOrderNo) {
        Log.e("NNNNNNN:",mOrderNo);
        Log.e("NNNNNNNNNN:",pay_type);
        HttpUtil.OrderGetPay(mOrderNo, pay_type, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (info.length>0){
                    if(Utils.isNotEmpty(price)&&price.equals("0")){
                        ToastUtil.show(msg);
                        finish();
                    }else{
                        if ("2".equals(pay_type)) {
                            JSONObject obj = JSON.parseObject(info[0]);
//                        String json = obj.getString("payOrder");
                            JSONObject pod=obj.getJSONObject("payOrder");
                            Gson gson = new Gson();
                            final PayResult vo = gson.fromJson(pod.toJSONString(), PayResult.class);
                            Log.e("PPPP:",Utils.isNotEmpty(vo.getAppid())?vo.getAppid():"null");
                            PayUtis.weiXinPay(PayGoodsOrderActivity.this, vo);
                        } else if (pay_type.equals("1")) {
                            JSONObject obj = JSON.parseObject(info[0]);
                            String payInfo = obj.getString("payOrder");
                            PayUtis.zhiFuBaoPay(PayGoodsOrderActivity.this, payInfo, new PayCallback() {
                                @Override
                                public void payResult(int type) {
                                    payFinish(type);
                                }
                            });
                        }
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

    private void payFinish(int type) {
        Log.e("PPP:",type+"");
//        startActivity(new Intent(mContext, MyOrderActivity.class));
//        finish();
        if(type==1){
            if(Utils.isNotEmpty(givescore)){
                new PayOkDialog(mContext,"+"+givescore,"").show();
            }
        }
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
//        mFoodListAdapter = new FoodOrderListAdapter(R.layout.order_food_item_layout, shoplist);
//        orderRecyclerview.setAdapter(mFoodListAdapter);
//        orderRecyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
    }

    private void setMakeOrderAdapter(List<PayOrderBean> shoplist) {
//        mFoodListAdapter = new FoodOrderListAdapter(R.layout.order_food_item_layout, shoplist);
//        orderRecyclerview.setAdapter(mFoodListAdapter);
//        orderRecyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        makeOrderAdapter = new MakeOrderListAdapter(R.layout.order_food_item_layout,shoplist);
        orderRecyclerview.setAdapter(makeOrderAdapter);
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
        if(Utils.isEmpty(pay_type)){
            ToastUtil.show("请先选择支付方式");
            return;
        }

        MakeOrderBean bean = new MakeOrderBean();
        bean.setStore_id(storeId);
        bean.setTotal(totalprice);
        bean.setStore_num(goodsnum);
        bean.setIfcart(isCart);
        bean.setStore_freight(postage);
        bean.setRemark(" ");
        List<MakeOrderBean.GoodsinfoBean> list = new ArrayList<>();
        for(int i=0;i<goodsList.size();i++){
            MakeOrderBean.GoodsinfoBean goodbean = new MakeOrderBean.GoodsinfoBean();
            goodbean.setGoods_attr(goodsList.get(i).getGoods_attr());
            goodbean.setCart_num(goodsList.get(i).getCart_num());
            goodbean.setGoods_id(goodsList.get(i).getId());

            list.add(goodbean);
        }
        bean.setGoodsinfo(list);

        GsonBuilder builder=new GsonBuilder();
        Gson gson=builder.create();

        //2.将list转为json(需要使用数组JSONArray)
        List<MakeOrderBean> peopleList = new ArrayList<MakeOrderBean>();
        peopleList.add(bean);
       String jsonData =  gson.toJson(peopleList);
        HttpUtil.payGoodsBuy(pay_type,jsonData,isCart,coupons_id,goodsId, product_attrs, goodsnum,mAddressId,totalprice, isMS,new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                JSONObject obj = JSON.parseObject(info[0]);
                givescore=obj.getString("givescore");
                pay(obj.getString("out_trade_no"));
            }
        });
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusModel event) {
        String code = event.getCode();
        switch (code) {
            case "payment_success":
                Log.e("tttttttttt","支付成功");
                //支付成功
                payFinish(1);
                break;
            case "payment_fail":
                Log.e("ffffffffffff","支付失败");
                //失败
                payFinish(2);
                break;
            case "send_info_from_pay":
              finish();
                break;
        }

    }

    /**
     * 创建订单
     */
    private void setLement() {
        mRemark = mFoodListAdapter.getData().get(0).getRemark();
        Log.v("tags", "beizhu=" + mRemark);
        HttpUtil.OrderCreate(mAddressId, mGoodsList, mRemark,isMS, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    String out_trade_no = obj.getString("out_trade_no");
                    String order_id = obj.getString("order_id");
                    Log.v("tags", "out_trade_no=" + out_trade_no);
                    if (code == 0) {
                        Intent intent = new Intent(PayGoodsOrderActivity.this, PayActivity.class);
                        intent.putExtra("out_trade_no", out_trade_no);
                        intent.putExtra("order_id", order_id);
                        intent.putExtra("money", mData.getStore().get(0).getStore_total());
                        startActivity(intent);
                        PayGoodsOrderActivity.this.finish();
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
