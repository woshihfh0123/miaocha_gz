package com.mc.phonelive.activity.shop;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.gson.Gson;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.adapter.shop.FoodCartAdapter;
import com.mc.phonelive.bean.FoodCartVo;
import com.mc.phonelive.dialog.CancelORderDialog;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.DialogUitl;
import com.mc.phonelive.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 购物车888888
 */
public class CartMainActivity extends AbsActivity {

    @BindView(R.id.cart_recyclerview)
    RecyclerView foodcartRecyclerview;
    @BindView(R.id.all_check_img)
    ImageView allCheckImg;
    @BindView(R.id.btn_back)
    ImageView btn_back;
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.goods_num)
    TextView goodsNum;
    @BindView(R.id.food_all_price)
    TextView foodAllPrice;
    @BindView(R.id.cart_btn)
    TextView cartBtn;
    @BindView(R.id.tv_choise_text)
    TextView tv_choise_text;
    @BindView(R.id.cart_layout)
    LinearLayout cartLayout;
    @BindView(R.id.close_tv)
    TextView closeTv;
    @BindView(R.id.bottom_layout01)
    RelativeLayout bottomLayout01;
    private FoodCartAdapter mFoodCartAdapter;
    private List<FoodCartVo.DataBean.InfoBean.CartListBean> mShopList = new ArrayList<>();//店铺列表
    private ArrayList<FoodCartVo.DataBean.InfoBean.CartListBean.GoodsBean> allSelectedGoods = new ArrayList<>();//所有选中商品列表
    //选中删除的购物车商品cartId
    private String selectedCartIds = "";
    boolean isSelectAll = false;
    private int mMinusOrAdd = 1;
    boolean isManag = false;//是否点击了管理

    @Override
    protected int getLayoutId() {
        return R.layout.cart_main_layout;
    }
    @Override
    protected boolean isStatusBarWhite() {
        return true;
    }
    @Override
    protected void main() {
        super.main();
        boolean registered = EventBus.getDefault().isRegistered(this);
        if (!registered) {
            EventBus.getDefault().register(this);
        }
        setCartListAdapter();
        initHttpData();
    }

    @Override
    protected void onResuname01() {
//        initHttpData();
    }

    /**
     * 通过接口获取购物车数据
     */
    private void initHttpData() {
        HttpUtil.getCartList(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (info.length > 0) {
                    Log.v("tags", code + "---" + msg + "---" + info[0]);
                    mShopList.clear();
                    JSONObject content = JSON.parseObject(info[0]);
                    List<FoodCartVo.DataBean.InfoBean.CartListBean> infoBean = JSON.parseArray(content.getString("cart_list"), FoodCartVo.DataBean.InfoBean.CartListBean.class);
                    if (!DataSafeUtils.isEmpty(infoBean) && infoBean.size() > 0) {
                        if (foodcartRecyclerview != null)
                            foodcartRecyclerview.setVisibility(View.VISIBLE);
                        if (cartLayout != null) cartLayout.setVisibility(View.GONE);
                        if (tv_choise_text != null) tv_choise_text.setVisibility(View.VISIBLE);
                        for (int j = 0; j < infoBean.size(); j++) {//给每个店铺添加一个是否已经选中的状态的值
                            boolean isAllCheck = true;
                            for (int i = 0; i < infoBean.get(j).getGoods().size(); i++) {
                                if (infoBean.get(j).getGoods().get(i).getIs_check().equals("0")) {
                                    isAllCheck = false;
                                }
                            }
                            infoBean.get(j).setShopcheck(isAllCheck);
                        }
                        mShopList.addAll(infoBean);
                        if (mFoodCartAdapter != null) {
                            mFoodCartAdapter.setNewData(infoBean);
                        }

                    } else {
                        if (foodcartRecyclerview != null)
                            foodcartRecyclerview.setVisibility(View.GONE);
                        if (cartLayout != null) cartLayout.setVisibility(View.VISIBLE);
                        if (tv_choise_text != null) tv_choise_text.setVisibility(View.GONE);
                        if (bottomLayout01 != null)
                            bottomLayout01.setVisibility(View.GONE);
                        if (tv_choise_text!=null) {
                            tv_choise_text.setVisibility(View.INVISIBLE);
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
                return DialogUitl.loadingDialog(CartMainActivity.this);
            }

        });
    }

    private void setCartListAdapter() {
        mFoodCartAdapter = new FoodCartAdapter(mContext, R.layout.foodcart_list_item, mShopList);
        foodcartRecyclerview.setAdapter(mFoodCartAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        foodcartRecyclerview.setLayoutManager(manager);
//        List<FoodCartVo.DataBean.InfoBean.CartListBean> nlist=new ArrayList<>();
//        for(int i=0;i<10;i++){
//            nlist.add(new FoodCartVo.DataBean.InfoBean.CartListBean());
//        }
//        mFoodCartAdapter.setNewData(nlist);

    }

    @OnClick({R.id.all_check_img, R.id.submit, R.id.cart_btn, R.id.btn_back, R.id.tv_choise_text, R.id.close_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.all_check_img:
//                if (!isSelectAll) {
//                    isSelectAll = true;
//                    allCheckImg.setImageResource(R.drawable.check_select);
//                } else {
//                    isSelectAll = false;
//                    allCheckImg.setImageResource(R.drawable.check_normal);
//                }
//                updateAllData(isSelectAll);
//                break;
            case R.id.btn_back:
                this.finish();
                break;
            case R.id.cart_btn:
                this.finish();
                break;
            case R.id.tv_choise_text://管理
                if (!isManag) {
                    tv_choise_text.setText("完成");
                    isManag = true;
                    bottomLayout01.setVisibility(View.VISIBLE);
                } else {
                    tv_choise_text.setText("管理");
                    isManag = false;
                    bottomLayout01.setVisibility(View.GONE);
                }
                break;
            case R.id.close_tv:
                if (selectedCartIds.length() <= 0) {
                    ToastUtil.show("请选择商品");
                    return;
                }
                DelGoodsData(selectedCartIds);
                break;
//            case R.id.submit:
//                if (selectedCartIds.length() <= 0) {
//                    ToastUtil.show("请选择商品");
//                    return;
//                }
//                mContext.startActivity(new Intent(mContext, GoodsOrderActivity.class));
//                break;
        }
    }

    /**
     * 修改选中状态
     */
    private void updateAllData(boolean isAllSelect) {
        if (mFoodCartAdapter != null) {
            List<FoodCartVo.DataBean.InfoBean.CartListBean> foodsListBeans = mFoodCartAdapter.getData();
            for (int i = 0; i < foodsListBeans.size(); i++) {
                if (!isAllSelect) {
                    mFoodCartAdapter.getData().get(i).setShopcheck(false);
                    for (int j = 0; j < mFoodCartAdapter.getData().get(i).getGoods().size(); j++) {
                        mFoodCartAdapter.getData().get(i).getGoods().get(j).setIs_check("0");
                    }
                } else {
                    for (int j = 0; j < mFoodCartAdapter.getData().get(i).getGoods().size(); j++) {
                        mFoodCartAdapter.getData().get(i).getGoods().get(j).setIs_check("1");
                    }
                    mFoodCartAdapter.getData().get(i).setShopcheck(true);
                }
            }
            accountSelectGoods();//这个方式，只是遍历被选中 获取商品的id
        }
    }

    /**
     * 合计选中的购物车商品
     */
    private void accountSelectGoods() {

        allSelectedGoods.clear();
        allSelectedGoods.addAll(getSelectedGoods());
        selectedCartIds = "";
        for (int i = 0; i < allSelectedGoods.size(); i++) {
            /**
             * 1.获取选中商品的cardId
             */
            FoodCartVo.DataBean.InfoBean.CartListBean.GoodsBean goodsListBean = allSelectedGoods.get(i);
            selectedCartIds = selectedCartIds + goodsListBean.getId();
            if (i != allSelectedGoods.size() - 1) {
                selectedCartIds = selectedCartIds + ",";

            }
        }
        Log.v("tags", "selectedCartIds=" + selectedCartIds + "-------------");
    }


    /**
     * 获取每次选中的商品列表
     *
     * @return
     */
    private List<FoodCartVo.DataBean.InfoBean.CartListBean.GoodsBean> getSelectedGoods() {

        List<FoodCartVo.DataBean.InfoBean.CartListBean.GoodsBean> selectGoods = new ArrayList<>();
        if (mShopList != null) {
            for (int i = 0; i < mShopList.size(); i++) {
                FoodCartVo.DataBean.InfoBean.CartListBean resultBean = mShopList.get(i);
                boolean selectedShop = resultBean.isShopcheck();
                if (selectedShop) {
                    /**
                     * 店铺被选中，其下的所有商品都选中
                     */
                    mFoodCartAdapter.getData().get(i).setShopcheck(selectedShop);
                    selectGoods.addAll(resultBean.getGoods());

                } else {
                    /**
                     * 商铺未被选中，遍历所有的商品，看哪个被选中
                     */
                    mFoodCartAdapter.getData().get(i).setShopcheck(selectedShop);
                    for (int j = 0; j < resultBean.getGoods().size(); j++) {

                        FoodCartVo.DataBean.InfoBean.CartListBean.GoodsBean goodsListBean = resultBean.getGoods().get(j);
                        String select = goodsListBean.getIs_check();
                        if (select.equals("1")) {
                            selectGoods.add(goodsListBean);
                        }
                    }
                }
                specialUpdate();
            }
        }

        Log.i("tags", new Gson().toJson(selectGoods));
        return selectGoods;
    }

    private void specialUpdate() {
        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                mFoodCartAdapter.notifyDataSetChanged();
            }
        };
        handler.post(r);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateAllSelected(EventBusModel eventBusModel) {

        String code = eventBusModel.getCode();

        switch (code) {

            case "check_all_select_state"://点击单个店铺的选择按钮
                List<FoodCartVo.DataBean.InfoBean.CartListBean.GoodsBean> foodsListBeans = (List<FoodCartVo.DataBean.InfoBean.CartListBean.GoodsBean>) eventBusModel.getObject();
                /**
                 * 检查是否全选的状态
                 */

                boolean isAllSelect = true;
                for (int i = 0; i < mShopList.size(); i++) {
                    boolean selectedShop = mShopList.get(i).isShopcheck();//
                    isAllSelect = isAllSelect && selectedShop;
                    if (!isAllSelect) {
                        break;
                    }
                }


                accountSelectGoods();
                break;
            case "check_item_select_state"://点击单个商品
                List<FoodCartVo.DataBean.InfoBean.CartListBean.GoodsBean> foodsListBeans1 = (List<FoodCartVo.DataBean.InfoBean.CartListBean.GoodsBean>) eventBusModel.getObject();
                String mCartId = (String) eventBusModel.getSecondObject();

                boolean isItemAllCheck = true;
                for (int i = 0; i < foodsListBeans1.size(); i++) {
                    String selectedShop = foodsListBeans1.get(i).getIs_check();
                    if (selectedShop.equals("0")) {
                        isItemAllCheck = false;
                    }
                }
                for (int i = 0; i < mFoodCartAdapter.getData().size(); i++) {
                    if (mFoodCartAdapter.getData().get(i).getStore_id().equals(mCartId)) {
                        if (isItemAllCheck) {
                            mFoodCartAdapter.getData().get(i).setShopcheck(true);
                        } else {
                            mFoodCartAdapter.getData().get(i).setShopcheck(false);
                        }
                    }
                }
                accountSelectGoods();
                break;

            case "cart_add"://购物车数量增加
//                showDialog();
                Log.v("tags", "add--------");
                mMinusOrAdd = 1;
                String mGoodsId = (String) eventBusModel.getObject();
                FoodCartVo.DataBean.InfoBean.CartListBean foodbean = (FoodCartVo.DataBean.InfoBean.CartListBean) eventBusModel.getSecondObject();
                AddCartInfo(mGoodsId, foodbean);
                break;
            case "cart_minus"://购物车数量减少
                mMinusOrAdd = 2;
                mGoodsId = (String) eventBusModel.getObject();
                foodbean = (FoodCartVo.DataBean.InfoBean.CartListBean) eventBusModel.getSecondObject();
                AddCartInfo(mGoodsId, foodbean);
                break;
        }

    }


    /**
     * 加减购物车
     *
     * @param listbean 店铺信息
     * @param goodid
     */
    private void AddCartInfo(String goodid, FoodCartVo.DataBean.InfoBean.CartListBean listbean) {
        for (int i = 0; i < mFoodCartAdapter.getData().size(); i++) {
            for (int j = 0; j < mFoodCartAdapter.getData().get(i).getGoods().size(); j++) {
                if (mFoodCartAdapter.getData().get(i).getGoods().get(j).getGoods_id().equals(goodid)) {
                    if (mMinusOrAdd == 1) {//如果是添加数量
                        mFoodCartAdapter.getData().get(i).getGoods().get(j).setCart_num((Integer.parseInt(mFoodCartAdapter.getData().get(i).getGoods().get(j).getCart_num()) + 1) + "");
                    } else if (mMinusOrAdd == 2) {//如果是减少数量
                        mFoodCartAdapter.getData().get(i).getGoods().get(j).setCart_num((Integer.parseInt(mFoodCartAdapter.getData().get(i).getGoods().get(j).getCart_num()) - 1) + "");
                    }
                    addGoodsToCart(mFoodCartAdapter.getData().get(i).getGoods().get(j).getId(), mFoodCartAdapter.getData().get(i).getGoods().get(j).getCart_num());
                    break;
                }
            }
        }

    }


    /**
     * 购物车数量增加减少
     *
     * @param id
     */
    private void addGoodsToCart(String id, String goodsnum) {
        Log.v("tags", "goodsnum=" + goodsnum);
        HttpUtil.setCartGoodsNum(id, goodsnum, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                Log.v("tags", code + "------" + msg);
                if (code == 0) {
                    initHttpData();
                } else {
                    ToastUtil.show(msg);
                }
            }

            @Override
            public boolean showLoadingDialog() {
                return true;
            }

            @Override
            public Dialog createLoadingDialog() {
                return DialogUitl.loadingDialog(CartMainActivity.this);
            }
        });
    }

    private void DelGoodsData(String selectedidlist) {
        CancelORderDialog oRderDialog = new CancelORderDialog(this, "删除商品", "确认删除商品吗？") {
            @Override
            public void doConfirmUp() {
                getDelGoods(selectedidlist);

            }
        };
        oRderDialog.show();

    }

    /**
     * 商品删除
     *
     * @param selectedidlist
     */
    private void getDelGoods(String selectedidlist) {
        Log.v("tags", "goodsnum=" + selectedidlist);
        HttpUtil.DelCart(selectedidlist, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                Log.v("tags", code + "------" + msg);
                ToastUtil.show(msg);
                if (code == 0) {
                    initHttpData();
                }
            }

            @Override
            public boolean showLoadingDialog() {
                return true;
            }

            @Override
            public Dialog createLoadingDialog() {
                return DialogUitl.loadingDialog(CartMainActivity.this);
            }
        });
    }

    @Override
    protected void onDestroy01() {
        boolean isRegist = EventBus.getDefault().isRegistered(this);
        if (isRegist) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


}
